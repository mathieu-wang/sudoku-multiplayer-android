package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loginButtonPress(View view) {
        Intent login_intent = new Intent(this, LoginActivity.class);
        startActivity(login_intent);
    }

    public void registerButtonPress(View view) {
        Intent register_intent = new Intent(this, RegisterActivity.class);
        startActivity(register_intent);
    }

    public void facebookButtonPress(View view) {
        //do some fb integration
        Intent guest_intent = new Intent(this, GuestMainActivity.class);
        startActivity(guest_intent);
    }

    public void guestButtonPress(View view) {
        Intent guest_intent = new Intent(this, GuestMainActivity.class);
        startActivity(guest_intent);
    }
    
    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage("If you do not have an account, create a new account or press the play as a guest button to play without an account")
                .setNegativeButton("Close", null)
                .show();
    }
}
