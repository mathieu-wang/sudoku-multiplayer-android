package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by mathieuwang on 15-03-16.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private int mPosition;
    public String[] filesnames = {
            "File 1",
            "File 2",
            "Roflcopters"
    };


    // Gets the context so it can be used later
    public ButtonAdapter(Context c) {
        mContext = c;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return filesnames.length;
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,
                        View convertView, ViewGroup parent) {
        final Button btn;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(100, 55));
            btn.setPadding(1, 1, 1, 1);
        }
        else {
            btn = (Button) convertView;
        }
        btn.setText(filesnames[position]);
        // filenames is an array of strings
        btn.setTextColor(Color.RED);
        btn.setId(position);
        this.mPosition = position;

        btn.setOnClickListener(new View.OnClickListener() {
            int position = mPosition;

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setTitle("Alert")
                        .setMessage(
                                "Button id: " + position)
                        .setNegativeButton("Close", null).show();
            }
        });

        return btn;
    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Button btn;
//        if (convertView == null) { // if it's not recycled, initialize some
//            btn = new Button (mContext);
//            btn.setLayoutParams(new GridView.LayoutParams(190, 190));
//            btn.setPadding(1, 1, 1, 1);
//        } else {
//            btn = (Button) convertView;
//        } // btn.setText(filesnames[position]); // filenames is an array of
//        // strings //btn.setTextColor (Color.WHITE);
//        // btn.setBackgroundResource (R.drawable.sample_2);
//        // btn.setBackgroundColor (Color.BLACK);
//        btn.setHighlightColor(Color.GREEN);
//        btn.setId(position);
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Handle the click here
//                v.findViewById().setText("CLICKED");
//            }
//        });
//        return btn;
//
//    }
}
