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


public class LoginActivity extends ActionBarActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    public String accessToken = "";
    public String refreshToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username_text);
        passwordEditText = (EditText) findViewById(R.id.password_text);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
*/
    private class loginServerRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://104.131.185.217:3000/oauth/token";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("grant_type", "password"));
            params.add(new BasicNameValuePair("client_id", "AndroidV1"));
            params.add(new BasicNameValuePair("client_secret", "abc123"));
            params.add(new BasicNameValuePair("username", strings[0]));
            params.add(new BasicNameValuePair("password", strings[1]));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                HttpResponse response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
        Log.v("HTTP RESPONSE: ", result);
    }
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

    public void loginButtonPress(View view) {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String msgContent = "";
        String res = "";
        int index;

        Log.v("LOGGING", username);
        Log.v("LOGGING", password);

        try{

            loginServerRequest response = new loginServerRequest();
            res = response.execute(username,password).get();
            String[] tokens = res.split("\"");
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("access_token")) {
                    accessToken = tokens[i+2];
                    Log.v("ACCESS_TOKEN : ", accessToken);
                }
                if (tokens[i].equals("refresh_token")) {
                    refreshToken = tokens[i+2];
                    Log.v("REFRESH_TOKEN : ", refreshToken);
                }
            }
            if (res.indexOf("access_token") > -1){

                Intent play_intent = new Intent(this, GuestMainActivity.class);
                startActivity(play_intent);

            }else if (res.indexOf("invalid") > -1){

                popUpMessage("Your username or password is invalid");
                usernameEditText.setText("");
                passwordEditText.setText("");

            }else{

                msgContent = "An unknown error has occurred. Please check your network connection.";
                usernameEditText.setText("");
                passwordEditText.setText("");

                popUpMessage(msgContent);
            }

        } catch(Exception e){}
    }

    public void forgotButtonPress(View view) {
        Intent forgot_intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgot_intent);
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
                .setMessage("If you do not have an account, you can go back to the main menu and click on Create Account, or Play as Guest.\n If you forgot your password, you can get a new one by pressing on Forgot Password")
                .setNegativeButton("Close", null)
                .show();
    }
}
