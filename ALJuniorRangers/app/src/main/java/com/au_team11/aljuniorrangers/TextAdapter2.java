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
import android.graphics.Path.Direction;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.view.MenuItem;

/**
 * Text Adapter class for the Word Bank
 * Created by nopenshaw on 2/20/16.
 */
public class TextAdapter2 extends BaseAdapter {

    private String[] words = {"TREE", "MAPLE", "LEAF", "CHEWACLA"};
    private String[] xmlwords;
    private int wordsLeft = words.length;
    private ArrayList<TextView> currentGuess = new ArrayList<TextView>();
    private Context ctx;
    private Direction dir = null;

    private TextView[] wordBank = new TextView[words.length];

    public TextAdapter2(Context contextIn) {

        ctx = contextIn;
        xmlwords = ctx.getResources().getStringArray(R.array.word_bank_1);
    }

    public int getCount() {
        return words.length;
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

        tv.setText(xmlwords[position]);
        tv.setTextColor(Color.RED);
        tv.setOnTouchListener(new TextTouchListener());

        return tv;
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
                PopupMenu popup = new PopupMenu(ctx, tv);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                Toast.makeText(ctx, "You Clicked : " + tv.getText(), Toast.LENGTH_SHORT).show();

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                return true;

            }
            return true;
        }
    }

}
