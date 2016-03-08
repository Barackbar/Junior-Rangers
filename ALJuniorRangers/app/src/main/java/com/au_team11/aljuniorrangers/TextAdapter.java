package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.util.Log;
import java.lang.Object;
import java.util.Arrays;
import java.util.ArrayList;
import android.widget.Toast;

/**
 * Text Adapter class for the Grid
 * Created by Zac on 1/31/2016.
 */
public class TextAdapter extends BaseAdapter {
    private String[] words = {"TREE", "MAPLE", "LEAF", "CHEWACLA"};
    private String[] xmlwords;
    private int wordsLeft = words.length;
    private ArrayList<TextView> currGuess = new ArrayList<TextView>();
    private Context ctx;
    private Direction dir = null;
    // 240 horizontal spacing
    // 190 vertical spacing
    private String[] letters = {
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
    };


    // row number: index / 6
    // column number: index % 6
    private TextView[] grid = new TextView[letters.length];
    private TextView[] wordBanktv = new TextView[words.length];
    // wordBank added to connect the two
    private TextAdapter2 wordBank;
    //private String[] wordBank;

    public TextAdapter(Context contextIn, String[] wordBankIn, String[] lettersIn) {

        ctx = contextIn;
        xmlwords = ctx.getResources().getStringArray(R.array.word_bank_1);
        //words = wordBankIn;
        //letters = lettersIn;
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
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(ctx);
            tv.setLayoutParams(new GridView.LayoutParams(85, 85));
        } else {
            tv = (TextView) convertView;
        }

        tv.setText(letters[position]);
        tv.setTextColor(Color.BLACK);
        tv.setOnTouchListener(new TextTouchListener());

        return tv;
    }


    public void setWordBankAdapter(TextAdapter2 wb)
    {
        wordBank = wb;
    }

    /**
     * Checks to see if currGuess contains a word.
     *
    public void checkGuess()
    {
        // Check that guess is of a valid length
        if (currGuess.size() > 1)
        {
            Toast.makeText(ctx, "Checking Guess...", Toast.LENGTH_SHORT).show();
            System.out.println("Checking Guess......");
            String guess = "";
            // Pull word from TextViews
            for (TextView tv : currGuess)
                guess += tv.getText();
            Toast.makeText(ctx, "Current Guess: " + guess, Toast.LENGTH_SHORT).show();
            System.out.println("Current Guess: " + guess);
            // Compare guess to each word to see if there's a match

            // if curGuess contains a valid word
            if (wordBank.guess(guess))
            {
                // mark word as green
                for (TextView tv : currGuess) {
                    tv.setTextColor(Color.GREEN);
                    // Going to modify listener to add an if Color.GREEN section
//                     tv.setOnTouchListener(null);
                }
                // Reset currGuess and dir
                currGuess = new ArrayList<TextView>();
                dir = null;
            }
        }
    }
     */

    /** OLD CHECK GUESS
     * Checks to see if current highlighted letters make a
     * valid word.
     */
    public void checkGuess()
    {
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
                // If there is a match
                if (s.equals(guess)) {
                    // TODO: Print out that user found word
                    Toast.makeText(ctx, "Guess Found! Crossing out word!", Toast.LENGTH_SHORT).show();
                    wordBank.crossOut(guess);
                    // Set letters so that they are locked from being touched
                    for (TextView tv : currGuess) {
                        tv.setTextColor(Color.GREEN);
                        tv.setOnTouchListener(null);
                    }

                    // Empty guess list and set direction to null
                    currGuess = new ArrayList<TextView>();
                    dir = null;

                    // Remove word from word bank
                    words[i] = words[words.length - 1];
                    words = Arrays.copyOf(words, words.length - 1);
                    wordsLeft = words.length;

                    // See if the user finished
                    // TODO: checkWin();
                }
            }
        }
    }

    /* TODO
    public void checkWin()
    {
        if(wordsLeft == 0)
            System.out.println("Congratulations, you won the word search!");
        else
            System.out.println("Doing great! You've got " + wordsLeft + " words left.");
    }
    */


    /**
     * Touch listener which allows a letter to be pressed.
     * When pressed, the letter will toggle between black and blue.
     */
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
                            // TODO: This doesn't work if in same direction if >1 letter away. Fix it.
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



    /**OLD LISTENER
     * Touch listener which allows a letter to be pressed.
     * When pressed, the letter will toggle between black and blue.
     *
    private class TextTouchListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // If a letter is touched
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Record it
                TextView tv = (TextView) v;

                    //If it is black, turn it blue and add the letter
                    //to the guess list and see if the new guess list
                    //is a valid word

                if (tv.getCurrentTextColor() == Color.BLACK)
                {
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

                        Toast.makeText(ctx, "TextView Value Pressed: " + tv.getText(), Toast.LENGTH_SHORT).show();
                        tv.setTextColor(Color.BLUE);
                        currGuess.add(tv);
                    }
                    // if third or later letter
                    else if (currGuess.size() >= 2)
                    {
                        TextView temp = currGuess.get(currGuess.size() - 1);

                        int xSpacing = Math.abs(temp.getLeft() - tv.getLeft());
                        int ySpacing = Math.abs(temp.getTop() - tv.getTop());

                        // check number is in same direction
                        if ((xSpacing == 0 && dir == Direction.vertical) || (ySpacing == 0 && dir == Direction.horizontal)
                                || (xSpacing > 0 && ySpacing > 0 && dir == Direction.diagonal))
                        {
                            Toast.makeText(ctx, "TextView Value Pressed: " + tv.getText(), Toast.LENGTH_SHORT).show();
                            tv.setTextColor(Color.BLUE);
                            currGuess.add(tv);
                            checkGuess();
                        }
                    }
                    // if first letter
                    else
                    {

                        Toast.makeText(ctx, "TextView Value Pressed: " + tv.getText(), Toast.LENGTH_SHORT).show();
                        tv.setTextColor(Color.BLUE);
                        currGuess.add(tv);
                        checkGuess();
                    }
                }


                    //If it is blue, make sure it's the last letter selected
                    //(this way no disconnected letter groupings happen), if
                    //it is, then turn it black and remove it from the guess
                    //list.

                else if (tv.getCurrentTextColor() == Color.BLUE)
                {
                    TextView temp = currGuess.get(currGuess.size() - 1);
                    if (tv.getLeft() == temp.getLeft() &&
                            tv.getTop() == temp.getTop())
                    {
                        // System.out.println(tv.getText() + " deselected");
                        tv.setTextColor(Color.BLACK);
                        currGuess.remove(currGuess.size() - 1);
                    }

                    if (currGuess.size() == 0)
                        dir = null;
                }
            }
            String temp1 = "";
            // Pull word from TextViews
            for (TextView tv1 : currGuess)
                temp1 += tv1.getText();
            Toast.makeText(ctx, "Current Guess: " + temp1, Toast.LENGTH_SHORT).show();
            return true;
        }
    }
     */




    private enum Direction
    {
        vertical, horizontal, diagonal;
    }
}
