package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewStatsActivity extends ActionBarActivity {
    private TextView[] textViews = new TextView[2];
    private SocketConnection connection = SocketConnection.getInstance();
    private String loginUser = connection.getCurrentUsername();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats2);

        textViews[0] = (TextView) findViewById(R.id.textView1);
        textViews[1] = (TextView) findViewById(R.id.textView2);

        if(loginUser!= null && !loginUser.isEmpty()) {
            textViews[0].setText("Number of game played: " + Constants.win[0]);
            textViews[1].setText("Number of victory: " + Constants.win[1]);
        }
        else {
            textViews[0].setText("Login to display statistics");
        }

        Button reset = (Button) findViewById(R.id.resetStats);

        if(connection.getCurrentUsername() == null || connection.getCurrentUsername().isEmpty()) {
            reset.setEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_stats, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ResetButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Reset Statistics")
                .setMessage("Are you sure you want to reset your game statistics?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.reset();

                        textViews[0].setText("Number of game played: " + Constants.win[0]);
                        textViews[1].setText("Number of victory: " + Constants.win[1]);

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage("Check your lifetime stats and the global leaderboards.")
                .setNegativeButton("Close", null)
                .show();
    }
}
