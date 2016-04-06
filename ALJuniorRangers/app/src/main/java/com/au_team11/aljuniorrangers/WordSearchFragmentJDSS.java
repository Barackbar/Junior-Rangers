package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by JDSS on 4/6/16.
 */
public class WordSearchFragmentJDSS extends Fragment {

    public static final int NUM_COLUMNS_BOARD = 8;
    public static final int NUM_COLUMNS_BANK = 2;

    Context context;
    View view;

    WordSearch wordSearch;
    ArrayList<TextView> puzzleTVs;
    ArrayList<TextView> wordBankTVs;

    //the currently selected indices on the board
    ArrayList<Integer> currentSelection;

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

        //do other stuff
        ArrayList<String> board = new ArrayList<String>();
        String letters = "abcdefghijklmnopqrstuvwxyzabcdeffjdksl;ahgdjkls;akjfdls;aghdjskalshrekoul;anfjdkasl;hfjdkpsarueiopwafh";
        for (int i = 1; i < letters.length(); i++) {
            board.add(letters.substring(i, i+1));
        }

        ArrayList<String> words = new ArrayList<String>();
        for (int i = 0; i < letters.length()/2; i++) {
            words.add(letters.substring(i, i+2));
        }

        wordSearch = new WordSearch(context, board, words, NUM_COLUMNS_BOARD);

        puzzleTVs = wordSearch.getPuzzleTVs();
        wordBankTVs = wordSearch.getWordBankTVs();

        //add textviews to the board
        for (int i = 0; i < puzzleTVs.size(); i++) {
            final TextView currentTextView = puzzleTVs.get(i);
            currentTextView.setTextColor(Color.BLACK);
            currentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            currentTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            currentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < puzzleTVs.size(); i++) {
                        //if referencing the same object
                        if (puzzleTVs.get(i) == currentTextView) {
                            currentSelection.add(new Integer(i));
                            Log.i("WSFJDSS", "index: " + i);
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
}
