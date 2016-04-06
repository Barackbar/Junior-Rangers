package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by nopenshaw on 3/2/16.
 */
public class WordSearch {

    Context context;

    int numColumns;
    int numRows;
    ArrayList<TextView> puzzleTVs;
    ArrayList<TextView> wordBankTVs;

    public WordSearch(Context newContext, ArrayList<String> newPuzzle, ArrayList<String> newWordBank, int newNumColumns){

        context = newContext;

        puzzleTVs = createTextViews(newContext, newPuzzle);

        wordBankTVs = createTextViews(newContext, newWordBank);

        numColumns = newNumColumns;
        numRows = puzzleTVs.size() / numColumns;
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
        down = (indices.get(1).intValue() / numRows) - (indices.get(0).intValue() / numRows);
        //if the indices move more than 1 place, possible to go straight up/down or left/right (down or right == 0)
        if (Math.abs(right) > 1 || Math.abs(down) > 1) {
            return false;
        }

        //check that all other indices follow the same pattern
        for (int i = 1; i < (indices.size() - 1); i++) {
            if ((((indices.get(1).intValue() % numColumns)   -   (indices.get(0).intValue() % numColumns))  != right) ||
                (((indices.get(1).intValue() / numRows)      -   (indices.get(0).intValue() / numRows))     != down)) {
                return false;
            }
        }

        //if it hasn't returned false yet, it's in a straight line
        return true;
    }

    //Returns the textview in the wordbank that the indices spells out, returns null if not in word bank
    public TextView findWordInPuzzle(int[] indices) {
        String potentialWord = "";
        //for every board index in the indices
        for (int i = 0; i < indices.length; i++) {
            //build the word from the board textviews
            potentialWord += puzzleTVs.get(indices[i]).getText().toString();
        }

        //find the wordbank textview, will return null if not in wordbank
        return findWordInWordBank(potentialWord);
    }

    //returns the word bank textview for the word provided, null if word not in wordbank
    public TextView findWordInWordBank(String word) {
        for (int i = 0; i < wordBankTVs.size(); i++) {
            if (word.equals(wordBankTVs.get(i).getText().toString())) {
                return wordBankTVs.get(i);
            }
        }
        return null;
    }

    public TextView getWordInWordBank(int index) {
        if (index > -1 && index < wordBankTVs.size()) {
            return wordBankTVs.get(index);
        }
        else {
            return null;
        }
    }

    public ArrayList<TextView> createTextViews(Context context, ArrayList<String> strings) {
        ArrayList<TextView> newTextViews = new ArrayList<TextView>();
        for (int i = 0; i < strings.size(); i++) {

            TextView newTextView = new TextView(context);

            newTextView.setText(strings.get(i));

            //set the onclicklistener in the wordsearchfragment

            newTextViews.add(newTextView);
        }
        return newTextViews;
    }

    public ArrayList<TextView> getPuzzleTVs() {
        return puzzleTVs;
    }

    public ArrayList<TextView> getWordBankTVs() {
        return wordBankTVs;
    }
}
