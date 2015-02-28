package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Dialog;
import android.content.*;

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
import java.lang.Character;

public class RegisterActivity extends ActionBarActivity {


    private EditText nameEditText;
    private EditText pass1EditText;
    private EditText pass2EditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = (EditText) findViewById(R.id.userName);
        emailEditText = (EditText) findViewById(R.id.userEmail);
        pass1EditText = (EditText) findViewById(R.id.userPass1);
        pass2EditText = (EditText) findViewById(R.id.userPass2);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
*/

    private class registerServerRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://104.131.185.217:3000/api/register";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
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

    public void registerButtonPress(View view){

        String userName = nameEditText.getText().toString();
        String userEmail = emailEditText.getText().toString();
        String userPass1 = pass1EditText.getText().toString();
        String userPass2 = pass2EditText.getText().toString();
        String msgContent = "";
        String res = "";

        Boolean validUsername = (userName.length() <= 60) && (userName.length()>= 2);

        if (validUsername) {
            for (int i = 0; i < userName.length(); i++) {
                if (!Character.isLetterOrDigit(userName.charAt(i))) {
                    validUsername = false;
                    break;
                }
            }
        }

        if (!validUsername){

            msgContent = "Your username should only contains letters or digits, with a minimum of 2 and maximum of 60 characters.";
            nameEditText.setText("");
            emailEditText.setText("");
            pass1EditText.setText("");
            pass2EditText.setText("");

            popUpMessage(msgContent);

        }else if (userEmail.indexOf("@") <= -1){

            msgContent = "The email address you've entered is invalid.";
            nameEditText.setText("");
            emailEditText.setText("");
            pass1EditText.setText("");
            pass2EditText.setText("");

            popUpMessage(msgContent);

        }else if(!userPass1.equals(userPass2)){

            msgContent = "The first password does not match with the second one";
            nameEditText.setText("");
            emailEditText.setText("");
            pass1EditText.setText("");
            pass2EditText.setText("");

            popUpMessage(msgContent);

        }else if (userPass1.length() < 6) {

            msgContent = "The length of your password should be longer than or equal to 6.";
            nameEditText.setText("");
            emailEditText.setText("");
            pass1EditText.setText("");
            pass2EditText.setText("");

            popUpMessage(msgContent);

        }else{

            Log.v("LOGGING", userName);
            Log.v("LOGGING", userEmail);
            Log.v("LOGGING", userPass1);
            Log.v("LOGGING", userPass2);
            //send user information to the server


            try{

                registerServerRequest response = new registerServerRequest();
                res = response.execute(userName,userPass1).get();


                if (res.indexOf("Successful") > -1){
                    //goes to log in screen
                    Intent login_intent = new Intent(this, LoginActivity.class);
                    startActivity(login_intent);

                }else if (res.indexOf("exists") > -1){
                    //pop up some message
                    msgContent = "Account already exists.";
                    nameEditText.setText("");
                    emailEditText.setText("");
                    pass1EditText.setText("");
                    pass2EditText.setText("");

                    popUpMessage(msgContent);

                }else{

                    msgContent = "An unknown error has occurred. Please check your network connection.";
                    nameEditText.setText("");
                    emailEditText.setText("");
                    pass1EditText.setText("");
                    pass2EditText.setText("");

                    popUpMessage(msgContent);
                }

            }catch(Exception e){}

        }
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
                .setMessage("By creating an account, you can save your stats and compare them to other players.\n If you forget your password, it can be reset in the login screen.")
                .setNegativeButton("Close", null)
                .show();
    }
}
