package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ForgotPasswordActivity extends ActionBarActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameEditText = (EditText) findViewById(R.id.username_text);
        passwordEditText = (EditText) findViewById(R.id.password_text);
    }

    /*
    private class resetPassServerRequest extends AsyncTask<String, Integer, String> {
        // save new password

        String result = "";

        protected String doInBackground(String... strings) {
            return result;
        }

        protected void onPostExecute(String result) {
            Log.v("HTTP RESPONSE: ", result);
        }

    }*/

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
        String msgContent = "";

        /*
        Log.v("LOGGING", username);
        Log.v("LOGGING", password);

        loginServerRequest response = new loginServerRequest();
        response.execute(username,password);
        msgContent = response.result;

        if (reset successfully){

            //go to login page
            Intent login_intent = new Intent(this, LoginActivity.class);
            startActivity(login_intent);

        }else{

            popUpMessage(msgContent);;
            usernameEditText.setText("");
            passwordEditText.setText("");
        }*/
    }

    private void popUpMessage(String content) {

        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Error Message");
        msg.setMessage(content);
        msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // close the dialog
            }
        });
        msg.setIcon(android.R.drawable.ic_dialog_alert);
        msg.create().show();
    }
    
    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage("An email will be sent to your email account with a new password.")
                .setNegativeButton("Close", null)
                .show();
    }
}
