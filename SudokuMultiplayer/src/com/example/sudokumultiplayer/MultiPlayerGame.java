package com.example.sudokumultiplayer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;


public class MultiPlayerGame extends ActionBarActivity {
	
	ArrayList<EditText> sodokuNumberSlots = getAllSudokuNumberSlots();
	String[] solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_game);
        //get solution
        String sol ="";
        String board = "";
        for (int i = 0; i < sodokuNumberSlots.size(); i++) {
            sodokuNumberSlots.get(i).setId(i);
            if(sodokuNumberSlots.get(i).getText().length() == 0) {
                board = board+"0";
            }
            else {
                board = board+sodokuNumberSlots.get(i).getText();
            }
            if (i != sodokuNumberSlots.size()-1) {
                board = board+",";
            }
        }
        HintServerRequest req = new HintServerRequest();
        try {
            sol = req.execute(board).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        solution = sol.split(",");
        if(solution[0].startsWith("{\"sudoku\":["))
        	solution[0] = solution[0].replace("{\"sudoku\":[", "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_player_game, menu);
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

    public void leaveGameButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Leave Game")
                .setMessage("Are you sure you want to leave the current game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    public void submitButtonPress(View view) {
    	boolean isCorrect = false;
    	String userResults = new String("");
    	String solutionString = new String("");
    	for(int i=0; i<sodokuNumberSlots.size(); i++){
    		userResults+=sodokuNumberSlots.get(i).getText().toString();
    	}
    	
    	for (int i = 0; i < solution.length; i++) {
    		solutionString+=solution[i];
		}
    	
    	System.out.println(userResults);
    	System.out.println(solutionString);
    	
    	if(userResults.equals(solutionString))
    		isCorrect = true;
    	

        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Error Message");
        if(isCorrect)
        	msg.setMessage("You win!");
        else
        	msg.setMessage("You lose!");
        msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // close the dialog
            }
        });
        msg.setIcon(android.R.drawable.ic_dialog_alert);
        msg.create().show();
    	
    }

    public void checkErrorButtonPress(View view){
        final ArrayList<EditText> sodokuNumberSlots = getAllSudokuNumberSlots();

        if(solution.length == sodokuNumberSlots.size()){
            for (int i = 0; i < solution.length; i++) {
                //when not empty, compare with solution
                String userInputNumber = sodokuNumberSlots.get(i).getText().toString();
                if (!userInputNumber.isEmpty()){
                    if(!userInputNumber.equals(solution[i])){
                        sodokuNumberSlots.get(i).setTextColor(Color.RED);
                    }
                }
            }
        }

        //After 3 seconds, reset background
        new CountDownTimer(3000, 1000){
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                //reset background
                for (int i = 0; i < sodokuNumberSlots.size(); i++) {
                    sodokuNumberSlots.get(i).setTextColor(Color.BLACK);
                }
            }
        }.start();
    }
    
    
    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                //TODO: add a custom layout showing the rules of sudoku
                .setMessage("Click on hint to automatically fill in one of the squares, be careful since hints are limited.")
                .setNegativeButton("Close", null)
                .show();
    }
    
    //get all EditText elements
    private ArrayList<EditText> getAllSudokuNumberSlots(){
        TableLayout myTableLayout = (TableLayout) findViewById( R.id.myTableLayout);
        ArrayList<EditText> sodokuNumberSlots = new ArrayList<EditText>();
        ArrayList<TableRow> sodokuNumberRows = new ArrayList<TableRow>();
        for(int i = 0; i < myTableLayout.getChildCount(); i++){
            if(myTableLayout.getChildAt( i ) instanceof TableRow )
                sodokuNumberRows.add((TableRow) myTableLayout.getChildAt(i));
        }
        //System.out.println("Table Rows = " + sodokuNumberRows.size());

        for (int i = 0; i < sodokuNumberRows.size(); i++) {
            for (int j = 0; j < sodokuNumberRows.get(i).getChildCount(); j++) {
                if(sodokuNumberRows.get(i).getChildAt(j) instanceof EditText )
                    sodokuNumberSlots.add((EditText) sodokuNumberRows.get(i).getChildAt(j));
            }
        }
        //System.out.println("Slot amount = " + sodokuNumberSlots.size());
        return sodokuNumberSlots;
    }
    
    private class HintServerRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://104.131.185.217:3000/sudoku/solve";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("grid", strings[0]));

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
}
