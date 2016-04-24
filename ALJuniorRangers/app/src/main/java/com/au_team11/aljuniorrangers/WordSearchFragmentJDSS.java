package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.graphics.drawable.ColorDrawable;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

/**
 * Created by JDSS on 4/6/16.
 */
public class WordSearchFragmentJDSS extends Fragment {

    public static final int NUM_COLUMNS_BOARD = 8;
    public static final int NUM_COLUMNS_BANK = 2;

    //these colors are in RGB mode
    public static final int BACKGROUND_COLOR = Color.parseColor("#faebd7"); //OLD = Color.rgb(255, 255, 255);
    public static final int SELECTION_COLOR = Color.rgb(187,255,255);
    public static final int FOUND_COLOR = Color.parseColor("#006400"); //OLD = Color.rgb(153,51,0);
    public static final int DOUBLE_FOUND_COLOR = Color.RED;

    Context context;
    View view;

    String fileName;

    WordSearchJDSS wordSearchJDSS;
    ArrayList<TextView> puzzleTVs;
    ArrayList<TextView> wordBankTVs;
    ArrayList<String> wordBankInfoStrings;

    //the currently selected indices on the board
    ArrayList<Integer> currentSelection;

    int wordsLeft;
    int totalWords;

    LinearLayout boardCol0;
    LinearLayout boardCol1;
    LinearLayout boardCol2;
    LinearLayout boardCol3;
    LinearLayout boardCol4;
    LinearLayout boardCol5;
    LinearLayout boardCol6;
    LinearLayout boardCol7;

    LinearLayout bankCol0;
    LinearLayout bankCol1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //check if view already exists
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        currentSelection = new ArrayList<Integer>();

        //inflate view from layout
        view = inflater.inflate(R.layout.wordsearch_layout_jdss, container, false);

        boardCol0 = (LinearLayout) view.findViewById(R.id.boardCol0);
        boardCol1 = (LinearLayout) view.findViewById(R.id.boardCol1);
        boardCol2 = (LinearLayout) view.findViewById(R.id.boardCol2);
        boardCol3 = (LinearLayout) view.findViewById(R.id.boardCol3);
        boardCol4 = (LinearLayout) view.findViewById(R.id.boardCol4);
        boardCol5 = (LinearLayout) view.findViewById(R.id.boardCol5);
        boardCol6 = (LinearLayout) view.findViewById(R.id.boardCol6);
        boardCol7 = (LinearLayout) view.findViewById(R.id.boardCol7);

        bankCol0 = (LinearLayout) view.findViewById(R.id.bankCol0);
        bankCol1 = (LinearLayout) view.findViewById(R.id.bankCol1);

        fileName = getArguments().getString(getResources().getString(R.string.AssetBundleKey));

        wordSearchJDSS = new WordSearchJDSS(context, fileName, NUM_COLUMNS_BOARD);

        puzzleTVs = wordSearchJDSS.getPuzzleTVs();
        wordBankTVs = wordSearchJDSS.getWordBankTVs();
        wordBankInfoStrings = wordSearchJDSS.getWordBankInfoStrings();

        totalWords = wordBankTVs.size();
        wordsLeft = totalWords;

        //add textviews to the board
        for (int i = 0; i < puzzleTVs.size(); i++) {
            final TextView currentTextView = puzzleTVs.get(i);
            currentTextView.setTextColor(Color.BLACK);
            currentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            currentTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            currentTextView.setBackgroundColor(BACKGROUND_COLOR);
            currentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < puzzleTVs.size(); i++) {
                        //if referencing the same object
                        if (puzzleTVs.get(i) == currentTextView) {
                            //if this textview is already selected
                            if (indexIsInSelection(i, currentSelection)) {
                                //TODO: keep this code in if we want to have backwards letter deselection
                                //if this textview is the same as the last one selected
                                if (i == currentSelection.get(currentSelection.size() - 1).intValue()) {
                                    //remove background coloring
                                    puzzleTVs.get(i).setBackgroundColor(BACKGROUND_COLOR);
                                    //remove last index from currentSelection
                                    currentSelection.remove(currentSelection.size() - 1);
                                }
                            }
                            //else
                            else {
                                //add this index to the current selection list
                                currentSelection.add(new Integer(i));
                                //if this textview continues the line set by the current selection
                                if (wordSearchJDSS.isInLine(currentSelection)) {
                                    //change color to selection color
                                    currentTextView.setBackgroundColor(SELECTION_COLOR);
                                    //if this current selection forms a word
                                    int currentWordBankWordIndex = wordSearchJDSS.findWordIndexInPuzzle(currentSelection);
                                    if (currentWordBankWordIndex > -1) {
                                        //get the word from the list of wordbank textviews
                                        TextView currentWordBankWord = wordBankTVs.get(currentWordBankWordIndex);
                                        if (currentWordBankWord != null) {
                                            //cross out word
                                            currentWordBankWord.setPaintFlags(currentWordBankWord.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                            //change color of letters in current selection to found word color
                                            for (int j = 0; j < currentSelection.size(); j++) {
                                                //If the letter is a solution to another word
                                                if(puzzleTVs.get(currentSelection.get(j).intValue()).getCurrentTextColor() == FOUND_COLOR)
                                                {
                                                    puzzleTVs.get(currentSelection.get(j).intValue()).setTextColor(DOUBLE_FOUND_COLOR);
                                                }
                                                else {
                                                    puzzleTVs.get(currentSelection.get(j).intValue()).setTextColor(FOUND_COLOR);
                                                }
                                                puzzleTVs.get(currentSelection.get(j).intValue()).setBackgroundColor(BACKGROUND_COLOR);
                                                puzzleTVs.get(currentSelection.get(j).intValue()).setTypeface(Typeface.DEFAULT_BOLD);
                                            }
                                            //TODO: whatever else needs to be done when a word is found should be done here
                                            Toast.makeText(context, "Guess Found! Crossing out word!", Toast.LENGTH_SHORT).show();
                                            wordsLeft--;

                                            //Checks to see if there are 0 wordsLeft.
                                            checkWin();

                                            /*Toast toast = Toast.makeText(
                                                    context,
                                                    wordBankInfoStrings.get(currentWordBankWordIndex),
                                                    Toast.LENGTH_SHORT);
                                            toast.show();*/



                                            //reset current selection
                                            currentSelection = new ArrayList<Integer>();
                                        }
                                    }
                                }
                                //else the new selection does not make a line
                                else {
                                    //TODO: put code here if we want to delete the entire selection when the user makes a non-line selection
                                    //remove it from the current selection
                                    //currentSelection.remove(currentSelection.size() - 1);

                                    //Iterate through the TextViews in the Current Selection and
                                    //change colors back to what they were before.
                                    for (int i2 = 0; i2 < currentSelection.size(); i2++) {
                                        TextView temp = puzzleTVs.get(currentSelection.get(i2).intValue());
                                        //ColorDrawable cd = (ColorDrawable) temp.getBackground();
                                        //int colorCode = cd.getColor();
                                        if(temp.getCurrentTextColor() == FOUND_COLOR) {
                                            temp.setBackgroundColor(BACKGROUND_COLOR);
                                        } else {
                                            temp.setBackgroundColor(BACKGROUND_COLOR);
                                        }
                                    }
                                    //Clear current selection
                                    currentSelection = new ArrayList<Integer>();
                                }
                            }
                            //break from loop
                            i = puzzleTVs.size();
                        }
                    }
                }
            });

            if (i % NUM_COLUMNS_BOARD == 0) {
                boardCol0.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 1) {
                boardCol1.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 2) {
                boardCol2.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 3) {
                boardCol3.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 4) {
                boardCol4.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 5) {
                boardCol5.addView(currentTextView);
            }
            else if (i % NUM_COLUMNS_BOARD == 6) {
                boardCol6.addView(currentTextView);
            }
            else { //if i % NUM_COLUMNS_BOARD == 7
                boardCol7.addView(currentTextView);
            }
        }

        //add textviews to the wordbank
        for (int i = 0; i < wordBankTVs.size(); i++) {
            TextView currentTextView = wordBankTVs.get(i);
            currentTextView.setTextColor(Color.BLACK);
            currentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            currentTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            //Checks if there is an info string for the word, and sets it as error field if so
            //Grabbed later for pop up info through getError()
            if(i < wordBankInfoStrings.size()) {
                currentTextView.setError(wordBankInfoStrings.get(i), null);
            }
            currentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    //Creating the instance of PopupMenu
                    //PopupMenu popup = new PopupMenu(ctx, tv);
                    //Inflating the Popup using xml file
                    //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                    TextView popupText = (TextView) popupView.findViewById(R.id.popuptv);

                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            RadioGroup.LayoutParams.MATCH_PARENT,
                            RadioGroup.LayoutParams.MATCH_PARENT);

                    popupWindow.setSplitTouchEnabled(false);
                    popupWindow.setFocusable(true);

                    TextView word = (TextView) popupView.findViewById(R.id.word);
                    word.setText(tv.getText());

                    TextView wordInfo = (TextView) popupView.findViewById(R.id.popuptv);
                    wordInfo.setText(tv.getError());

                    Button disButton = (Button) popupView.findViewById(R.id.disButton);
                    disButton.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    Toast.makeText(context, "You Clicked : " + tv.getText(), Toast.LENGTH_SHORT).show();

                    //registering popup with OnMenuItemClickListener
                    //popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    //    public boolean onMenuItemClick(MenuItem item) {
                    //        return true;
                    //    }
                    //});

                    popupWindow.showAtLocation(tv, Gravity.FILL, 10, 10); //showing popup menu

                }
            });
            if (i % NUM_COLUMNS_BANK == 0) {
                bankCol0.addView(currentTextView);
            }
            else { //if i % NUM_COLUMNS_BANK == 1
                bankCol1.addView(currentTextView);
            }
        }

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }*/

    public static Boolean indexIsInSelection(int index, ArrayList<Integer> indexSelection) {
        for (int i = 0; i < indexSelection.size(); i++) {
            if (index == indexSelection.get(i).intValue()) {
                return true;
            }
        }
        return false;
    }

    public void checkWin()
    {
        if(wordsLeft == 0) {
            System.out.println("Congratulations, you've won the word search!");
            Toast.makeText(context, "Congratulations, you've won the word search!", Toast.LENGTH_SHORT).show();

            //Creating the AlertDialog.Builder and attaching it to the MainActivity
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Congratulations! Puzzle Complete!");
            builder.setMessage("Would you like to review your puzzle or select a new activity? " +
                    "(Press the back button at the bottom of the device to return while reviewing.)");

            //Creating New Activity button and setting listener
            builder.setPositiveButton("New Activity", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dialog.dismiss();
                    getActivity().onBackPressed();
                }

            });
            //Creating Review Puzzle button and setting listener
            builder.setNegativeButton("Review Puzzle", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            builder.show();

            String filename;
            SharedPreferences.Editor editor;

            editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            fileName = getArguments().getString(getResources().getString(R.string.AssetBundleKey));
            editor.putBoolean(fileName, true);
            editor.commit();

        }
        else {
            System.out.println("Doing great! You've got " + wordsLeft + " words left.");
            Toast.makeText(context, "Doing great! You've got " + wordsLeft + " words left.", Toast.LENGTH_SHORT).show();
        }
    }



}
