package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by nopenshaw on 3/2/16.
 */
public class WordSearchJDSS {

    Context context;

    int numColumns;
    String JSONFileName;
    ArrayList<TextView> puzzleTVs;
    ArrayList<TextView> wordBankTVs;
    ArrayList<String> wordBankInfoStrings;

    public WordSearchJDSS(Context newContext, String newJSONFileName, int newNumColumns){

        context = newContext;

        JSONFileName = newJSONFileName;

        puzzleTVs = new ArrayList<TextView>();
        wordBankTVs = new ArrayList<TextView>();
        wordBankInfoStrings = new ArrayList<String>();


        generateWordSearch(JSONFileName);

        numColumns = newNumColumns;
    }

    //returns true if the indices form a straight line, returns false if not straight or if indices out of order
    public Boolean isInLine(ArrayList<Integer> indices) {
        //amount of movement rightward
        int right = 0;
        //amount of movement downward
        int down = 0;

        //if only 1 index, implicitly in a straight line
        if (indices.size() < 2 ) {
            return true;
        }

        //check first two indices
        right = (indices.get(1).intValue() % numColumns) - (indices.get(0).intValue() % numColumns);
        down = (indices.get(1).intValue() / numColumns) - (indices.get(0).intValue() / numColumns);
        Log.i("WS", "r,d: " + right  + "," + down);
        //if the indices move more than 1 place, possible to go straight up/down or left/right (down or right == 0)
        if (Math.abs(right) > 1 || Math.abs(down) > 1) {
            return false;
        }

        //check that all other indices follow the same pattern
        for (int i = 2; i < indices.size(); i++) {
            Log.i("WS", "i.g(i-1): " + indices.get(i-1).intValue());
            Log.i("WS", "i.g(i): " + indices.get(i).intValue());
            if ((((indices.get(i).intValue() % numColumns)   -   (indices.get(i-1).intValue() % numColumns))  != right) ||
                (((indices.get(i).intValue() / numColumns)      -   (indices.get(i-1).intValue() / numColumns))     != down)) {
                return false;
            }
        }

        //if it hasn't returned false yet, it's in a straight line
        return true;
    }

    //Returns the textview in the wordbank that the indices spells out, returns null if not in word bank
    public TextView findWordTextViewInPuzzle(ArrayList<Integer> indices) {
        String potentialWord = "";
        //for every board index in the indices
        for (int i = 0; i < indices.size(); i++) {
            //build the word from the board textviews
            potentialWord += puzzleTVs.get(indices.get(i)).getText().toString();
        }

        //find the wordbank textview, will return null if not in wordbank
        return findWordTextViewInWordBank(potentialWord);
    }

    //Returns the index of the word in the wordbank that the indices spells out, returns -1 if not in word bank
    public int findWordIndexInPuzzle(ArrayList<Integer> indices) {
        String potentialWord = "";
        //for every board index in the indices
        for (int i = 0; i < indices.size(); i++) {
            //build the word from the board textviews
            potentialWord += puzzleTVs.get(indices.get(i)).getText().toString();
        }

        for (int i = 0; i < wordBankTVs.size(); i++) {
            //if the potential word equals the word in the word bank
            if (potentialWord.equalsIgnoreCase(wordBankTVs.get(i).getText().toString())) {
                //return the index of the word in the word bank
                return i;
            }
        }
        //if it is not found, return -1
        return -1;
    }

    //returns the word bank textview for the word provided, null if word not in wordbank
    public TextView findWordTextViewInWordBank(String word) {
        for (int i = 0; i < wordBankTVs.size(); i++) {
            if (word.equalsIgnoreCase(wordBankTVs.get(i).getText().toString())) {
                return wordBankTVs.get(i);
            }
        }
        return null;
    }

    //returns the word bank textview for the word provided, null if word not in wordbank
    public int findWordIndexInWordBank(String word) {
        for (int i = 0; i < wordBankTVs.size(); i++) {
            if (word.equalsIgnoreCase(wordBankTVs.get(i).getText().toString())) {
                return i;
            }
        }
        return -1;
    }

    public TextView getWordInWordBank(int index) {
        if (index > -1 && index < wordBankTVs.size()) {
            return wordBankTVs.get(index);
        }
        else {
            return null;
        }
    }

    public ArrayList<TextView> getPuzzleTVs() {
        return puzzleTVs;
    }

    public ArrayList<TextView> getWordBankTVs() { return wordBankTVs; }

    public ArrayList<String> getWordBankInfoStrings() {
        return wordBankInfoStrings;
    }


    public void setWordBankInfoStrings(ArrayList<String> wordBankInfoStrings) {
        this.wordBankInfoStrings = wordBankInfoStrings;
    }

    //credit goes to GrlsHu on StackOverflow
    //returns a json string from the asset file
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void generateWordSearch(String jsonFileName) {

        //get the json data into JSON Object form
        try {
            JSONObject rootObject = new JSONObject(loadJSONFromAsset(jsonFileName));
            String puzzle = rootObject.getString("puzzle");

            //build the puzzle textviews from the json data
            for (int i = 0; i < puzzle.length(); i++) {
                //create new textview
                TextView newTextView = new TextView(context);
                //set the new textview's text to the current letter
                newTextView.setText(puzzle.substring(i, i+1));
                //add the new textview to the arraylist
                this.puzzleTVs.add(newTextView);
            }

            //build the wordbank and wordbank info from the json data
            JSONArray bankArray = rootObject.getJSONArray("wordbank");
            for (int i = 0; i < bankArray.length(); i++) {
                //get the current JSON Object
                JSONObject currentWord = bankArray.getJSONObject(i);
                //create new textview
                TextView newTextView = new TextView(context);
                //set the new textview's text to the current word
                newTextView.setText(currentWord.getString("word"));
                //add the new textview to the arraylist
                this.wordBankTVs.add(newTextView);
                //add the current word's info to the info arraylist
                this.wordBankInfoStrings.add(currentWord.getString("info"));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    //returns the wordbank words as an arraylist of strings
    public ArrayList<String> getBankWords(String data) {
        ArrayList bankArrayList = new ArrayList<String>();
        try {
            //get each word from the JSON array
            JSONArray jsonArray = new JSONObject(data).getJSONArray("wordbank");
            for (int i = 0; i < jsonArray.length(); i++) {
                bankArrayList.add(jsonArray.getJSONObject(i).getString("word"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bankArrayList;
    }

    //returns the info on each word as an arraylist of strings
}
