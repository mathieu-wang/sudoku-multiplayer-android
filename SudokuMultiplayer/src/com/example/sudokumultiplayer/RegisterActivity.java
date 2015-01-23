package com.example.sudokumultiplayer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
*/
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

    public void registerButtonPress(View view) {
        //get information user entered
        EditText nameEditText = (EditText) findViewById(R.id.userName);
        EditText emailEditText = (EditText) findViewById(R.id.userEmail);
        EditText pass1EditText = (EditText) findViewById(R.id.userPass1);
        EditText pass2EditText = (EditText) findViewById(R.id.userPass2);
        String userName = nameEditText.getText().toString();
        String userEmail = emailEditText.getText().toString();
        String userPass1 = pass1EditText.getText().toString();
        String userPass2 = pass2EditText.getText().toString();

        if(userPass1.equals(userPass2)){
            //send user information to the server

            //goes to log in screen
            Intent login_intent = new Intent(this, LoginActivity.class);
            startActivity(login_intent);
        }
        else {
            //have some error popup happen
        }
    }



}
