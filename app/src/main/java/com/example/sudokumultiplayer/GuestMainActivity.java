package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class GuestMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest_main, menu);
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

    public void singlePlayerButtonPress(View view) {
        Intent single_player_intent = new Intent(this, SinglePlayerGame.class);
        startActivity(single_player_intent);
    }

    public void multiPlayerButtonPress(View view) {
        Intent multiplayer_intent = new Intent(this, MultiPlayerGame.class);
        startActivity(multiplayer_intent);
    }

    public void viewStatsButtonPress(View view) {
        Intent view_stats_intent = new Intent(this, ViewStatsActivity.class);
        startActivity(view_stats_intent);
    }

    public void signOutButtonPress(View view) {
        final Intent main_page_intent = new Intent(this, MainActivity.class);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //logout logic should go here
                        startActivity(main_page_intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage("Play against the computer or against other players.\n Stats won't be recorded in Guest Mode.")
                .setNegativeButton("Close", null)
                .show();
    }
}
