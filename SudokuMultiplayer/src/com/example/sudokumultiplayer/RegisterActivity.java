package com.example.sudokumultiplayer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Dialog;
import android.content.*;

public class RegisterActivity extends ActionBarActivity {


    private EditText nameEditText;
    private EditText pass1EditText;
    private EditText pass2EditText;
    private EditText emailEditText;
    private Button registerButton;
    private Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = (EditText) findViewById(R.id.userName);
        emailEditText = (EditText) findViewById(R.id.userEmail);
        pass1EditText = (EditText) findViewById(R.id.userPass1);
        pass2EditText = (EditText) findViewById(R.id.userPass2);
        registerButton = (Button) findViewById(R.id.register_button);
        helpButton = (Button) findViewById(R.id.help_button);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
*/

    public static class UserAccount {
        public String username;
        public String password;
        public String email;

        public UserAccount(String username,String password){
            this.username = username;
            this.password = password;
        }

        public UserAccount(String username,String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    public void dataRequest(){

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

    public void registerButtonPress(View view) {

        String userName = nameEditText.getText().toString();
        String userEmail = emailEditText.getText().toString();
        String userPass1 = pass1EditText.getText().toString();
        String userPass2 = pass2EditText.getText().toString();

        if(userPass1.equals(userPass2)){
            //send user information to the server
            UserAccount loginAccount = new UserAccount(userName,userPass1,userEmail);
            storeNewPassword(loginAccount);
            //goes to log in screen
        }
        else {
            //have some error popup happen
            nameEditText.setText("");
            emailEditText.setText("");
            pass1EditText.setText("");
            pass2EditText.setText("");
        }
    }

    public void storeNewPassword(UserAccount account){
        //send request to server
    }

}
