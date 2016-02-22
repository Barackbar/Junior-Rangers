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
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Context;

public class WordSearchFragment extends Fragment {

    //private Activity fActivity;
    //private LinearLayout lLayout;
    private GridView gridView;
    private GridView wordBank;
    private static Context context;

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

        //Create GridView Adapter
        TextAdapter gridViewAdapter = new TextAdapter(super.getActivity());
        //Set GridView Adapter
        gridView.setAdapter(gridViewAdapter);

        //Create WordBank Adapter from Array List Adapter
        //ArrayAdapter<TextView> wordBankAdapter = new ArrayAdapter<TextView>(context,
        //        android.R.layout.simple_list_item_1, gridViewAdapter.getWords());

        //Create WordBank Adapter from TextAdapter2
        TextAdapter2 wordBankAdapter = new TextAdapter2(super.getActivity());
        //Toast.makeText(context, "WordBank: " + gridViewAdapter.getWords(), Toast.LENGTH_SHORT).show();
        //Set Word Bank Adapter
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
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(context.getApplicationContext(), wordBank);
                //Inflating the Popup using xml file
                //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                //popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                //    public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(),
                //        "You Clicked : " + item.getTitle(),
                //        Toast.LENGTH_SHORT
                //).show();
                //return true;
                //}
                //});

                //popup.show(); //showing popup menu
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
}
