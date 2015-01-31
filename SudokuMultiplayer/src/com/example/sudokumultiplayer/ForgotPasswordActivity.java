package com.example.sudokumultiplayer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ForgotPasswordActivity extends ActionBarActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button resetPasswordButton;
    private Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameEditText = (EditText) findViewById(R.id.username_text);
        passwordEditText = (EditText) findViewById(R.id.password_text);
        resetPasswordButton = (Button) findViewById(R.id.reset_pass_button);
        helpButton = (Button) findViewById(R.id.help_button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    public void resetButtonPress(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        RegisterActivity.UserAccount loginAccount = new RegisterActivity.UserAccount(username,password);

        storeNewPassword(loginAccount);
    }

    public void storeNewPassword(RegisterActivity.UserAccount account){
       // save new password

    }
}
