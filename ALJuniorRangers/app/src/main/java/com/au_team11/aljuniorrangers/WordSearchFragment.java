package com.au_team11.aljuniorrangers;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import android.util.Log;
import java.util.Random;

import android.view.View.OnTouchListener;

import android.view.MenuItem;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class WordSearchFragment extends Fragment {

    //private Activity fActivity;
    //private LinearLayout lLayout;
    private GridView gridView;
    //private GridView wordBank;
    private static Context context;
    private ArrayList<String> filenames = new ArrayList<String>();
    public String WORDSEARCH_DATA = "wordsearch_data.json";

    String wordDataJSON;
    String wordPuzzleName;
    String wordPuzzleJSONString;
    JSONArray wordBankJSONArray;
    JSONObject json;
    String[] wordBankArray = new String[100]; //TODO:Overhead needs to be addressed
    String[] wordInfoArray = new String[100];
    String[] wordPuzzleArray = new String[200];

    ArrayList<TextView> wordBankLL;

    int numColumns;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
        //adding filenames of JSON files in Assets
        filenames.add("wordsearch_data.json");
        filenames.add("wordsearch_data1.json");
        filenames.add("wordsearch_data2.json");
        filenames.add("wordsearch_data3.json");
        //Creating random number generation
        Random rGen = new Random();
        //Randomized seed that chooses location in filenames ArrayList
        int seed = rGen.nextInt(4);
        //Sets the JSON file from which to load the Word Search
        WORDSEARCH_DATA = filenames.get(seed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.content_grid_view, container, false);
        //wordBank = (GridView) view.findViewById(R.id.wordbank);
        gridView = (GridView) view.findViewById(R.id.gridview);

        /*
        fActivity = (Activity) super.getActivity();
        lLayout = (LinearLayout) inflater.inflate(R.layout.content_grid_view, container, false);

        android.widget.GridView gridView = (android.widget.GridView) fActivity.findViewById(R.id.gridview);
        */


        //Create WordBank Adapter from Array List Adapter
        //ArrayAdapter<TextView> wordBankAdapter = new ArrayAdapter<TextView>(context,
        //        android.R.layout.simple_list_item_1, gridViewAdapter.getWords());

        //Populate the Local Word Bank String Array
            //Read data from main menu data file
        wordDataJSON = loadJSONFromAsset(WORDSEARCH_DATA);
        try{
            json = new JSONObject(wordDataJSON);
            wordPuzzleName = json.getString("name");
            wordPuzzleJSONString = json.getString("puzzle");
            numColumns = json.getInt("columns");
            wordBankJSONArray = json.getJSONArray("wordbank");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
        //iterate through elements in wordBankJSONArray, adding words to wordBankArray
        for (int i = 0; i < wordBankJSONArray.length(); i++)
            wordBankArray[i] = wordBankJSONArray.getJSONObject(i).getString("word");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            //iterate through elements in wordBankJSONArray, adding words to wordBankArray
            for (int i = 0; i < wordBankJSONArray.length(); i++)
                wordInfoArray[i] = wordBankJSONArray.getJSONObject(i).getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //values = json.getJSONArray(WORDSEARCH_DATA);
        //values = wordBankJSONArray = wordDataJSON.getJSONArray(0);

        for (int i = 0; i < wordPuzzleJSONString.length(); i++) {
            wordPuzzleArray[i] = Character.toString(wordPuzzleJSONString.charAt(i));
        }



        //add textviews of words to the linearlayout word bank
        wordBankLL = new ArrayList<TextView>();
        LinearLayout leftColumn = (LinearLayout) view.findViewById(R.id.leftColumn);
        LinearLayout rightColumn = (LinearLayout) view.findViewById(R.id.rightColumn);
        for (int i = 0; i < wordBankArray.length; i++) {
            if (i%2 == 0) {
                TextView temp = new TextView(context);
                temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                temp.setText(wordBankArray[i]);
                //Uses the embedded error message to store the hint, could have used setHint but it
                //changed the size of the view when added to the list
                temp.setError(wordInfoArray[i], null);
                temp.setTextColor(Color.BLACK);
                temp.setTextSize(24);
                temp.setTypeface(Typeface.DEFAULT_BOLD);

                temp.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        //TODO: Handle touches to show animation, another view, or pop up screen
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            TextView tv = (TextView) v;
                            //Creating the instance of PopupMenu
                            //PopupMenu popup = new PopupMenu(ctx, tv);
                            //Inflating the Popup using xml file
                            //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                            //TextView popupText = (TextView) popupView.findViewById(R.id.popuptv);

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

                            return true;

                        }

                        return false;
                    }
                });
                wordBankLL.add(i, temp);
                leftColumn.addView(temp);
            }
            else {
                TextView temp = new TextView(context);
                temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                temp.setText(wordBankArray[i]);
                //Uses the embedded error message to store the hint, could have used setHint but it
                //changed the size of the view when added to the list
                temp.setError(wordInfoArray[i], null);
                temp.setTextColor(Color.BLACK);
                temp.setTextSize(24);
                temp.setTypeface(Typeface.DEFAULT_BOLD);

                temp.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        //TODO: Handle touches to show animation, another view, or pop up screen
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

                            return true;

                        }

                        return false;
                    }
                });
                wordBankLL.add(i, temp);
                rightColumn.addView(temp);
            }
        }




        //Create GridView Adapter
        TextAdapter gridViewAdapter = new TextAdapter(super.getActivity(), wordBankArray, wordPuzzleArray);
        //Create WordBank Adapter from TextAdapter2
        TextAdapter2 wordBankAdapter = new TextAdapter2(super.getActivity(), wordBankArray, wordInfoArray);
        //Toast.makeText(context, "WordBank: " + gridViewAdapter.getWords(), Toast.LENGTH_SHORT).show();

        //Populates word bank array list of textviews displayed in linear layout
        gridViewAdapter.setWordBankLL(wordBankLL);

        //Populate the Word Bank in the adapter - REDUNDANT NOW THAT IT IS PASSED IN CONSTRUCTOR ABOVE
        wordBankAdapter.setWordBank(wordBankArray);

        //Link Word Bank and Word Search(gridView) Adapters
        wordBankAdapter.setWordSearchAdapter(gridViewAdapter);
        gridViewAdapter.setWordBankAdapter(wordBankAdapter);

        //Set GridView Adapter
        gridView.setAdapter(gridViewAdapter);
        //Set Word Bank Adapter to Gridview wordbank
        //wordBank.setAdapter(wordBankAdapter);

        //wordBank.setColumnWidth(wordBank.getWidth() / 2);

        /*
        gridView.setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });
        */

        //Set GridView Touch Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("TestingClickPosition: ", Integer.toString(position));
                Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //Set WordBank Touch Listener
        /*
        wordBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                TextView popupText = (TextView) popupView.findViewById(R.id.popuptv);
                popupText.setText(wordInfoArray[position]);

                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.MATCH_PARENT);

                popupWindow.setSplitTouchEnabled(false);
                popupWindow.setFocusable(true);
                Button disButton = (Button)popupView.findViewById(R.id.disButton);
                disButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(popupText, 0, 0, 0); //showing popup menu

            }
        }); //closing the setOnClickListener method
        */





        //lLayout.findViewById(gridView.getId());
        //return lLayout;
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
    }


    //credit goes to GrlsHu on StackOverflow
    //returns a json string from the asset file
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }




    /*
    The specified word is checked to see if it matches a word from the word bank array. If it does, then it's crossed out.
     */
    public boolean guess(String word)
    {
        for(String w : wordBankArray)
        {
            if (w.equalsIgnoreCase(word))
            {
                //wordsLeft--;
                // cross word off bank
                crossOut(word);
                //checkWin();
                return true;
            }
        }
        return false;
    }

    public void crossOut(String word)
    {
        for (TextView tv : wordBankLL) {
            if (tv.getText().equals(word))
            {
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //tv.setTextColor(Color.RED);
                break;
            }
        }
    }

}
