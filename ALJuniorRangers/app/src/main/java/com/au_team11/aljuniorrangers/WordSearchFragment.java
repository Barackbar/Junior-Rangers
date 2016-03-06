package com.au_team11.aljuniorrangers;

import android.os.Bundle;
import android.app.Fragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import android.view.MenuItem;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WordSearchFragment extends Fragment {

    //private Activity fActivity;
    //private LinearLayout lLayout;
    private GridView gridView;
    private GridView wordBank;
    private static Context context;

    public static final String WORDSEARCH_DATA = "wordsearch_data.json";

    String wordDataJSON;
    String wordPuzzleName;
    String wordPuzzleJSONString;
    JSONArray wordBankJSONArray;
    JSONObject json;
    String[] wordBankArray = new String[100]; //TODO:Overhead needs to be addressed
    String[] wordInfoArray = new String[100];
    String[] wordBankPuzzle = new String[100];
    int numColumns;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.content_grid_view, container, false);
        wordBank = (GridView) view.findViewById(R.id.wordbank);
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

        //Create GridView Adapter
        TextAdapter gridViewAdapter = new TextAdapter(super.getActivity());
        //Set GridView Adapter
        gridView.setAdapter(gridViewAdapter);
        //Create WordBank Adapter from TextAdapter2
        TextAdapter2 wordBankAdapter = new TextAdapter2(super.getActivity(), wordBankArray, wordInfoArray);
        //Toast.makeText(context, "WordBank: " + gridViewAdapter.getWords(), Toast.LENGTH_SHORT).show();
        //Populate the Word Bank in the adapter
        //wordBankAdapter.setWordBank(wordBankArray);
        //Set Word Bank Adapter to Gridview wordbank
        wordBank.setAdapter(wordBankAdapter);
        //Set GridView Touch Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(WordSearchFragment.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
            }
        });

        //Set WordBank Touch Listener
        wordBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                /*
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
                */
            }
        }); //closing the setOnClickListener method

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

}
