package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.util.Log;
import java.lang.Object;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import android.widget.Toast;

/**
 * Text Adapter class for the Grid
 * Created by Zac on 1/31/2016.
 */
public class TextAdapter extends BaseAdapter {
    private String[] words;
    //= {"TREE", "MAPLE", "LEAF", "CHEWACLA"};

    private String[] xmlwords;
    private ArrayList<TextView> wordBankLL = new ArrayList<TextView>();
    private ArrayList<TextView> puzzleTV = new ArrayList<TextView>();
    private int wordsLeft;
    private int totalWords;
    private ArrayList<TextView> currGuess = new ArrayList<TextView>();
    private Context ctx;
    private Direction dir = null;
    private Direction currentDir = null;
    private String[] letters;
    /*
     = {
            "T", "M", "C", "D", "E", "F",
            "R", "B", "A", "D", "E", "F",
            "E", "B", "C", "P", "E", "F",
            "E", "B", "C", "D", "L", "F",
            "A", "C", "C", "D", "E", "E",
            "A", "H", "C", "D", "E", "F",
            "A", "E", "C", "D", "E", "F",
            "A", "W", "C", "D", "E", "F",
            "A", "A", "C", "D", "E", "F",
            "A", "C", "C", "D", "E", "F",
            "A", "L", "L", "E", "A", "F",
            "A", "A", "C", "D", "E", "F"

    };*/


    // row number: index / 6
    // column number: index % 6

    //private TextView[] grid = new TextView[letters.length];
    //private TextView[] wordBanktv = new TextView[words.length];

    // wordBank added to connect the two
    private TextAdapter2 wordBank;
    //private String[] wordBank;

    public int DARKGREEN = Color.parseColor("#006400");

    public TextAdapter(Context contextIn, String[] wordBankIn, String[] lettersIn) {

        ctx = contextIn;
        xmlwords = ctx.getResources().getStringArray(R.array.word_bank_1);
        //words = new String[wordBankIn.length];
        //for(int i = 0; i < wordBankIn.length; i++)
            //words[i] = wordBankIn[i];
        totalWords = 0;
        for(int i = 0; i < wordBankIn.length; i++)
        {
            if(wordBankIn[i] != null)
                totalWords++;
        }
        words = new String[totalWords];
        for (int i= 0; i < totalWords; i++)
            words[i] = wordBankIn[i];
        wordsLeft = totalWords;
        //wordsLeft = words.length;
        letters = new String[60];
        for(int i = 0; i < 60; i++)
            letters[i] = lettersIn[i];
    }

    public int getCount() {
        return letters.length;
    }

    /*Old Function to test ArrayAdapter - could be deleted, maybe moved to TextAdapter2
    *
    //Returns formatted Word Bank items
    public ArrayList<TextView> getWords() {
        //Initialize
        ArrayList<TextView> fwords = new ArrayList<TextView>(words.length);
        //Populate
        for (String s : xmlwords)
        {
            TextView temp = new TextView(ctx);
            temp.setText(s);
            fwords.add(temp);
            Log.d("Test1: ", temp.getText().toString());
        }
        //For each TextView in Array
        for (TextView v : fwords) {
            //Format Word Bank words here
            v.setTextColor(Color.RED);
        }
        for (int i = 0; i < words.length; i++) {
            Log.d("Test2: ", fwords.get(i).getText().toString());

        }
        return fwords;
    }
    *
    * */

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(ctx);
            tv.setLayoutParams(new GridView.LayoutParams(50, 50));
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tv.setOnTouchListener(new TextTouchListener());
            //puzzleTV.add(position, tv);
            //convertView = tv;
            //convertView.setTag(tv);
        } else {
            tv = (TextView) convertView;
            //tv = puzzleTV.get(position);
            //tv.setTextColor(((TextView) convertView).getCurrentTextColor());
            //tv.setText(letters[position]);
            //convertView = tv;
            //tv = (TextView) convertView;
            //tv = (TextView) convertView.getTag();
        }

        tv.setText(letters[position]);



        return tv;
    }


    public void setWordBankAdapter(TextAdapter2 wb)
    {
        wordBank = wb;
    }

    public void setWordBankLL(ArrayList<TextView> wb)
    {
        wordBankLL = wb;
    }


    /**
     * Checks to see if current highlighted letters make a
     * valid word.
     */
    public void checkGuess()
    {
        /*Quick way to correctly update wordsLeft
        int count = 0;
        for (String s:words) {
            System.out.println(s);
            count++;
        }
        wordsLeft = count;
        */

        // Check that guess is of a valid length
        if (currGuess.size() > 1)
        {
            String guess = "";
            // Pull word from TextViews
            for (TextView tv : currGuess)
                guess += tv.getText();
            // Compare guess to each word to see if there's a match
            for (int i = 0; i < words.length; i++) {
                String s = words[i];
                //check to see if s is null before proceeding
                if (s != null) {
                    // If there is a match
                    if (s.equals(guess)) {
                        // TODO: Print out that user found word
                        Toast.makeText(ctx, "Guess Found! Crossing out word!", Toast.LENGTH_SHORT).show();
                        crossOut(guess);
                        //wordBank.crossOut(guess);
                        // Set letters so that they are turned dark green
                        for (TextView tv : currGuess) {
                            tv.setTextColor(DARKGREEN);
                            tv.setTypeface(Typeface.DEFAULT_BOLD);
                            //tv.setHighlightColor(Color.YELLOW);
                            //tv.setOnTouchListener(null);
                        }

                        // Empty guess list and set direction to null
                        currGuess = new ArrayList<TextView>();
                        dir = null;

                        // Remove word from word bank
                        words[i] = words[words.length - 1];
                        words = Arrays.copyOf(words, words.length - 1);
                        wordsLeft--;

                        // See if the user finished
                        checkWin();
                    }
                }
            }
        }
    }


    public void checkWin()
    {
        if(wordsLeft == 0) {
            System.out.println("Congratulations, you've won the word search!");
            Toast.makeText(ctx, "Congratulations, you've won the word search!", Toast.LENGTH_SHORT).show();
        }
        else {
            System.out.println("Doing great! You've got " + wordsLeft + " words left.");
            Toast.makeText(ctx, "Doing great! You've got " + wordsLeft + " words left.", Toast.LENGTH_SHORT).show();
        }
    }


    //Testestig a new way to implement grid touch logic
    private class TextTouchListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // If a letter is touched
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                // Record it
                TextView tv = (TextView) v;


                //Touch is handled based on the letter's color.
                //If it's black/green: if it follows the current direction, the color is changed
                //to blue/red.
                //If it's blue/red: if it was the last letter guessed, the color is changed to
                //black/green.

                switch(tv.getCurrentTextColor())
                {
                    // if the letter hasn't already been selected
                    case 0xFF006400:
                    case Color.BLACK:
                        // if second letter
                        if (currGuess.size() == 1)
                        {
                            TextView temp = currGuess.get(currGuess.size() - 1);

                            int xSpacing = temp.getLeft() - tv.getLeft();
                            //xScaling is the dp units between vertically adjacent textviews
                            //changes with the verticalSpacing XML attribute + 100
                            int xScaling = 128;
                            int ySpacing = temp.getTop() - tv.getTop();
                            //yScaling is the dp units between horizontally adjacent textviews
                            //changes with the horizontalSpacing XML attribute + 100 + GridView Params
                            int yScaling = 82;


                            //Toast.makeText(ctx, "XSpacing: " + xSpacing + "/nYSpacing: " + ySpacing, Toast.LENGTH_SHORT).show();
                            System.out.println("XSpacing: " + xSpacing + "/nYSpacing: " + ySpacing);

                            // set direction
                            if (xSpacing == 0 && ySpacing == 1*yScaling)
                                dir = Direction.north;
                            else if (xSpacing == 0 && ySpacing == -1*yScaling)
                                dir = Direction.south;
                            else if (xSpacing == 1*xScaling && ySpacing == 0)
                                dir = Direction.east;
                            else if (xSpacing == -1*xScaling && ySpacing == 0)
                                dir = Direction.west;
                            else if (xSpacing == 1*xScaling && ySpacing == -1*yScaling)
                                dir = Direction.southeast;
                            else if (xSpacing == 1*xScaling && ySpacing == 1*yScaling)
                                dir = Direction.northeast;
                            else if (xSpacing == -1*xScaling && ySpacing == -1*yScaling)
                                dir = Direction.southwest;
                            else if (xSpacing == -1*xScaling && ySpacing == 1*yScaling)
                                dir = Direction.northwest;
                            else
                                dir = null;

                            if(dir == null) {
                                for (TextView tvguess : currGuess) {
                                    if(tvguess.getCurrentTextColor() == Color.RED) {
                                        tvguess.setTextColor(DARKGREEN);
                                    }
                                    else {
                                        tvguess.setTextColor(Color.BLACK);
                                    }
                                }
                                currGuess.clear();
                                break;
                            }
                            else
                            {
                                if (tv.getCurrentTextColor() == Color.BLACK)
                                    tv.setTextColor(Color.BLUE);
                                else
                                    tv.setTextColor(Color.RED);

                                currGuess.add(tv);
                            }
                        }
                        // if third or later letter
                        else if (currGuess.size() >= 2)
                        {
                            TextView temp = currGuess.get(currGuess.size() - 1);

                            int xSpacing = temp.getLeft() - tv.getLeft();
                            //xScaling is the dp units between vertically adjacent textviews
                            //changes with the verticalSpacing XML attribute + 100
                            int xScaling = 128;
                            int ySpacing = temp.getTop() - tv.getTop();
                            //yScaling is the dp units between horizontally adjacent textviews
                            //changes with the horizontalSpacing XML attribute + 100
                            int yScaling = 82;

                            //Toast.makeText(ctx, "XSpacing: " + xSpacing + "/nYSpacing: " + ySpacing, Toast.LENGTH_SHORT).show();
                            System.out.println("XSpacing: " + xSpacing + "/nYSpacing: " + ySpacing);

                            //Check this direction and compare to expected direction
                            if (xSpacing == 0 && ySpacing == 1*yScaling)
                                currentDir = Direction.north;
                            else if (xSpacing == 0 && ySpacing == -1*yScaling)
                                currentDir = Direction.south;
                            else if (xSpacing == 1*xScaling && ySpacing == 0)
                                currentDir = Direction.east;
                            else if (xSpacing == -1*xScaling && ySpacing == 0)
                                currentDir = Direction.west;
                            else if (xSpacing == 1*xScaling && ySpacing == -1*yScaling)
                                currentDir = Direction.southeast;
                            else if (xSpacing == 1*xScaling && ySpacing == 1*yScaling)
                                currentDir = Direction.northeast;
                            else if (xSpacing == -1*xScaling && ySpacing == -1*yScaling)
                                currentDir = Direction.southwest;
                            else if (xSpacing == -1*xScaling && ySpacing == 1*yScaling)
                                currentDir = Direction.northwest;
                            else
                                currentDir = null;

                            // check number is in same direction
                            if (currentDir == dir && currentDir != null)
                            {
                                if (tv.getCurrentTextColor() == Color.BLACK)
                                    tv.setTextColor(Color.BLUE);
                                else
                                    tv.setTextColor(Color.RED);
                                currGuess.add(tv);
                                checkGuess();
                            }
                            else
                            {
                                for (TextView tvguess : currGuess) {
                                    if(tvguess.getCurrentTextColor() == Color.RED) {
                                        tvguess.setTextColor(DARKGREEN);
                                    }
                                    else {
                                        tvguess.setTextColor(Color.BLACK);
                                    }
                                }
                                currGuess.clear();
                                break;
                            }
                        }
                        // if first letter
                        else
                        {
                            if (tv.getCurrentTextColor() == Color.BLACK)
                                tv.setTextColor(Color.BLUE);
                            else
                                tv.setTextColor(Color.RED);
                            currGuess.add(tv);
                            checkGuess();
                        }
                        break;
                    // if the letter has already been selected
                    case Color.BLUE:
                    case Color.RED:
                        TextView temp = currGuess.get(currGuess.size() - 1);
                        if (tv.getLeft() == temp.getLeft() &&
                                tv.getTop() == temp.getTop())
                        {
                            if (tv.getCurrentTextColor() == Color.BLUE)
                                tv.setTextColor(Color.BLACK);
                            else
                                tv.setTextColor(DARKGREEN);
                            currGuess.remove(currGuess.size() - 1);
                        }

                        // changed from "== 0" to "== 1"
                        if (currGuess.size() == 1)
                            dir = null;
                        break;
                }
            }
            return true;
        }
    }

    /**OLD TOUCH LISTENER
     * Touch listener which allows a letter to be pressed.
     * When pressed, the letter will toggle between black and blue.

    private class TextTouchListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // If a letter is touched
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                // Record it
                TextView tv = (TextView) v;


                //Touch is handled based on the letter's color.
                //If it's black/green: if it follows the current direction, the color is changed
                //to blue/red.
                //If it's blue/red: if it was the last letter guessed, the color is changed to
                //black/green.

                switch(tv.getCurrentTextColor())
                {
                    // if the letter hasn't already been selected
                    case Color.GREEN:
                    case Color.BLACK:
                        // if second letter
                        if (currGuess.size() == 1)
                        {
                            TextView temp = currGuess.get(currGuess.size() - 1);

                            int xSpacing = Math.abs(temp.getLeft() - tv.getLeft());
                            int ySpacing = Math.abs(temp.getTop() - tv.getTop());

                            // set direction
                            if (xSpacing == 0)
                                dir = Direction.vertical;
                            else if (ySpacing == 0)
                                dir = Direction.horizontal;
                            else
                                dir = Direction.diagonal;

                            if (tv.getCurrentTextColor() == Color.BLACK)
                                tv.setTextColor(Color.BLUE);
                            else
                                tv.setTextColor(Color.RED);
                            currGuess.add(tv);
                        }
                        // if third or later letter
                        else if (currGuess.size() >= 2)
                        {
                            TextView temp = currGuess.get(currGuess.size() - 1);

                            int xSpacing = Math.abs(temp.getLeft() - tv.getLeft());
                            int ySpacing = Math.abs(temp.getTop() - tv.getTop());

                            // check number is in same direction
                            // This doesn't work if in same direction if >1 letter away. Fix it.
                            if ((xSpacing == 0 && dir == Direction.vertical)
                                    || (ySpacing == 0 && dir == Direction.horizontal)
                                    || (xSpacing > 0 && ySpacing > 0 && dir == Direction.diagonal))
                            {
                                if (tv.getCurrentTextColor() == Color.BLACK)
                                    tv.setTextColor(Color.BLUE);
                                else
                                    tv.setTextColor(Color.RED);
                                currGuess.add(tv);
                                checkGuess();
                            }
                        }
                        // if first letter
                        else
                        {
                            if (tv.getCurrentTextColor() == Color.BLACK)
                                tv.setTextColor(Color.BLUE);
                            else
                                tv.setTextColor(Color.RED);
                            currGuess.add(tv);
                            checkGuess();
                        }
                        break;
                    // if the letter has already been selected
                    case Color.BLUE:
                    case Color.RED:
                        TextView temp = currGuess.get(currGuess.size() - 1);
                        if (tv.getLeft() == temp.getLeft() &&
                                tv.getTop() == temp.getTop())
                        {
                            if (tv.getCurrentTextColor() == Color.BLUE)
                                tv.setTextColor(Color.BLACK);
                            else
                                tv.setTextColor(Color.GREEN);
                            currGuess.remove(currGuess.size() - 1);
                        }

                        // changed from "== 0" to "== 1"
                        if (currGuess.size() == 1)
                            dir = null;
                        break;
                }
            }
            return true;
        }
    }
    */

    /*
    The specified word is checked to see if it matches a word from the word bank array. If it does, then it's crossed out.
     */
    public boolean guess(String word)
    {
        for(String w : words)
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
                tv.setTypeface(Typeface.DEFAULT);
                //tv.setHighlightColor(Color.YELLOW);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //tv.setTextColor(Color.RED);
                break;
            }
        }
    }


    private enum Direction
    {
        north, east, south, west, northwest, northeast, southeast, southwest;
    }
}
