package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ViewLeaderboardsActivity extends ActionBarActivity {
    private TextView[] textViews = new TextView[5];
    private SocketConnection connection = SocketConnection.getInstance();
    private String loginUser = connection.getCurrentUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        textViews[0] = (TextView) findViewById(R.id.textView1);
        textViews[1] = (TextView) findViewById(R.id.textView2);
        textViews[2] = (TextView) findViewById(R.id.textView3);
        textViews[3] = (TextView) findViewById(R.id.textView4);
        textViews[4] = (TextView) findViewById(R.id.textView5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_leaderboards, menu);
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

    public void bestTimesButtonPress(View view){
        String bestTimes = "";
        try {
            sudokuBestTimesRequest response = new sudokuBestTimesRequest();
            bestTimes = response.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for(int i=0; i<5; i++){
            //TODO change after
            if(loginUser!=null && !loginUser.isEmpty() && loginUser.equalsIgnoreCase(Constants.users[i])){
                textViews[i].setTextColor(Color.BLUE);
                textViews[i].setText((i + 1) + " " + Constants.users[i] + " " + Constants.times[i]);
            } else {
                textViews[i].setTextColor(Color.BLACK);
                textViews[i].setText((i + 1) + " " + Constants.users[i] + " " + Constants.times[i]);
            }
        }
    }

    public void mostSudokuCompletedButtonPress(View view){
        String mostSudokuCompleted = "";
        try {
            sudokuMostSudokuCompletedRequest response = new sudokuMostSudokuCompletedRequest();
            mostSudokuCompleted = response.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=0; i<5; i++){
            //TODO change after
            if(loginUser!=null && !loginUser.isEmpty() && loginUser.equalsIgnoreCase(Constants.users[i])){
                textViews[i].setTextColor(Color.BLUE);
                textViews[i].setText((i + 1) + " " + Constants.users[i] + " " + Constants.sudokuCompleted[i]);
            } else {
                textViews[i].setTextColor(Color.BLACK);
                textViews[i].setText((i + 1) + " " + Constants.users[i] + " " + Constants.sudokuCompleted[i]);
            }
        }
    }

    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage("Check your lifetime stats and the global leaderboards.")
                .setNegativeButton("Close", null)
                .show();
    }


    public class sudokuBestTimesRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        public String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            //TODO change after
            String url = "http://104.131.185.217:3000/sudoku/generate-string/25";
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse response = httpClient.execute(httpGet);
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

    public class sudokuMostSudokuCompletedRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        public String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            //TODO change after
            String url = "http://104.131.185.217:3000/sudoku/generate-string/25";
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse response = httpClient.execute(httpGet);
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
}
