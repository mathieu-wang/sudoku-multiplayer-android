package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

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
import java.util.concurrent.ExecutionException;


public class SolverActivity extends ActionBarActivity {

    static ArrayList<EditText> sodokuNumberSlots = new ArrayList<EditText>();
    ArrayList<TableRow> sodokuNumberRows = new ArrayList<TableRow>();
    String[] solution;
    private class solutionServerRequest extends AsyncTask<String, Integer, String> {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);

    }
    @Override
    protected void onStart() {
        super.onStart();
        sodokuNumberSlots = getAllSudokuNumberSlots();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solver, menu);
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

    public void submitButtonPress(View view) {

        String sol = "";
        String board = "";
        for (int i = 0; i < sodokuNumberSlots.size(); i++) {
            sodokuNumberSlots.get(i).setId(i);
            if (sodokuNumberSlots.get(i).getText().length() == 0) {
                board = board + "0";
            } else {
                board = board + sodokuNumberSlots.get(i).getText();
            }
            if (i != sodokuNumberSlots.size() - 1) {
                board = board + ",";
            }
        }
        solutionServerRequest req = new solutionServerRequest();
        try {
            sol = req.execute(board).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        solution = sol.split(",");
        //remove content of the first element
        if (solution[0].startsWith("{\"sudoku\":["))
            solution[0] = solution[0].replace("{\"sudoku\":[", "");
        //remove content of the last element
        if(Character.isDigit(solution[solution.length-1].charAt(0))){
            solution[solution.length-1] = "" + solution[solution.length-1].charAt(0);
        }
        for (int i = 0; i < solution.length; i++) {
            sodokuNumberSlots.get(i).setText(solution[i]);
        }
    }

    public void resetButtonPress(View view) {
        for (int i = 0; i < sodokuNumberSlots.size(); i++) {
            sodokuNumberSlots.get(i).setText("");
        }
    }
    // get all EditText elements
    private ArrayList<EditText> getAllSudokuNumberSlots() {
        TableLayout myTableLayout = (TableLayout) findViewById(R.id.singlePlayerTableLayout);
        ArrayList<EditText> sodokuNumberSlots = new ArrayList<EditText>();
        ArrayList<TableRow> sodokuNumberRows = new ArrayList<TableRow>();
        for (int i = 0; i < myTableLayout.getChildCount(); i++) {
            if (myTableLayout.getChildAt(i) instanceof TableRow)
                sodokuNumberRows.add((TableRow) myTableLayout.getChildAt(i));
        }

        for (int i = 0; i < sodokuNumberRows.size(); i++) {
            for (int j = 0; j < sodokuNumberRows.get(i).getChildCount(); j++) {
                if (sodokuNumberRows.get(i).getChildAt(j) instanceof EditText)
                    sodokuNumberSlots.add((EditText) sodokuNumberRows.get(i)
                            .getChildAt(j));
            }
        }
        return sodokuNumberSlots;
    }

    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                .setMessage(
                        "Enter a Sudoku grid that you need help solving and click on submit.")
                .setNegativeButton("Close", null).show();
    }
}
