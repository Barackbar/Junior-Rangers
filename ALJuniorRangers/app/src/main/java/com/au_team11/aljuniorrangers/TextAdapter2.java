package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.MenuItem;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.RadioGroup.LayoutParams;

import android.graphics.Typeface;
import android.graphics.Paint;

import android.util.Log;
import java.util.Arrays;
import java.util.ArrayList;

import java.lang.Object;

import android.graphics.Path.Direction;



/**
 * Text Adapter class for the Word Bank
 * Created by nopenshaw on 2/20/16.
 */
public class TextAdapter2 extends BaseAdapter {

    private String[] words = {"TREE", "MAPLE", "LEAF", "CHEWACLA"};
    //private String[] xmlwords;
    private String[] JSONwords;
    private String[] wordInfo;
    private TextView[] bank;
    private int wordsLeft;
    // wordSearch added to connect the two
    private TextAdapter wordSearch;
    //private int wordsLeft = words.length;
    //private ArrayList<TextView> currentGuess = new ArrayList<TextView>();
    private Context ctx;
    private Direction dir = null;


    //private TextView[] wordBank = new TextView[JSONwords.length];

    public TextAdapter2(Context contextIn, String[] wordBankIn, String[] wordInfoIn) {

        ctx = contextIn;
        //xmlwords = ctx.getResources().getStringArray(R.array.word_bank_1);
        JSONwords = wordBankIn;
        wordInfo = wordInfoIn;
        bank = new TextView[JSONwords.length];
        wordsLeft = JSONwords.length;
    }

    public int getCount() {
        return JSONwords.length;
    }

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
            //tv.setLayoutParams(new GridView.LayoutParams(85, 85));
        } else {
            tv = (TextView) convertView;
        }

        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setText(JSONwords[position]);
        tv.setTextColor(Color.BLACK);
        tv.setOnTouchListener(new TextTouchListener());
        bank[position] = tv;

        return tv;
    }

    public void setWordBank(String[] wordBank){

        JSONwords = wordBank;
    }


    /*
    The highlighted word on the word search will be checked to see if it
    matches a word in the bank. If it does, then it's crossed out.
     */
    public boolean guess(String word)
    {
        for(String w : JSONwords)
        {
            if (w.equalsIgnoreCase(word))
            {
                wordsLeft--;
                // cross word off bank
                crossOut(word);
                checkWin();
                return true;
            }
        }
        return false;
    }

    /*
    Strikes thru a word.
     */
    public void crossOut(String word)
    {
        for (TextView tv : bank) {
            if (tv.getText().equals(word))
            {
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                tv.setTextColor(Color.RED);
                break;
            }
        }
    }

    /* TODO
    Sees if there are any words left, if not, then display a splash screen
    congratulating the user, awarding them points and exiting after.
     */
    public void checkWin()
    {
        if (wordsLeft == 0)
        {
            // do things here
        }
    }

    public void setWordSearchAdapter(TextAdapter wsa)
    {
        wordSearch = wsa;
    }

    /**
     * Touch listener which allows a word to be pressed.
     * When pressed, there should be an informational pop up screen.
     */
    private class TextTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //TODO: Handle touches to show animation, another view, or pop up screen
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                TextView tv = (TextView) v;
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(ctx, tv);
                //Inflating the Popup using xml file
                //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


                LayoutInflater layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_window, null);
                TextView popupText = (TextView) popupView.findViewById(R.id.popuptv);

                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);

                popupWindow.setSplitTouchEnabled(false);
                popupWindow.setFocusable(true);
                Button disButton = (Button)popupView.findViewById(R.id.disButton);
                disButton.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});

                Toast.makeText(ctx, "You Clicked : " + tv.getText(), Toast.LENGTH_SHORT).show();

                //registering popup with OnMenuItemClickListener
                //popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                //    public boolean onMenuItemClick(MenuItem item) {
                //        return true;
                //    }
                //});

                popupWindow.showAtLocation(tv, Gravity.FILL, 10, 10); //showing popup menu

                return true;

            }
            return true;
        }
    }

}
