package com.example.sudokumultiplayer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SinglePlayerGame extends ActionBarActivity {
    Difficulty difficulty = Difficulty.EASY;
    static ArrayList<EditText> sudokuNumberSlots = new ArrayList<EditText>();
    ArrayList<TableRow> sudokuNumberRows = new ArrayList<TableRow>();
    private static ProgressBar mProgress;
    Thread counterThread;
    private static int threadStopper = 0;
    private static int mProgressStatus = 0;
    private static Handler mHandler = new Handler();
    String[] solution;
    int maxHints = 5;
    int hintsUsed = 0;
    private String currentDifficult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game);
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

    public class sudokuStringRequest extends AsyncTask<String, Integer, String> {
        String result = "";

        public String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://104.131.185.217:3000/sudoku/generate-string/25";
            if (difficulty == Difficulty.MEDIUM) {
                url = "http://104.131.185.217:3000/sudoku/generate-string/40";
            } else if (difficulty == Difficulty.HARD) {
                url = "http://104.131.185.217:3000/sudoku/generate-string";
            }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_player_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
            case R.id.radioButtonEasy:
                difficulty = Difficulty.EASY;
                break;
            case R.id.radioButtonMedium:
                difficulty = Difficulty.MEDIUM;
                break;
            case R.id.radioButtonHard:
                difficulty = Difficulty.HARD;
                break;
            }
        }
    }

    public void startGame(View view) {

        if(currentDifficult.equals(difficulty.name())) {
            return;
        }

        currentDifficult = difficulty.name();

        System.out.println("Starting game with difficulty: "
                + difficulty.name());

        String sudokuString = generateSudokuString();
        Log.v("sudoku: ", sudokuString);
        String[] numbers = sudokuString.split(",");
        sudokuNumberSlots = getAllSudokuNumberSlots();
        // Write numbers into grid
        resetAllSudokuNumberSlots();
        if (numbers.length == sudokuNumberSlots.size()) {
            for (int i = 0; i < numbers.length; i++) {
                // skip 0, left empty
                if (!numbers[i].equals("0")) {
                    sudokuNumberSlots.get(i).setText(numbers[i]);
                    sudokuNumberSlots.get(i).setFocusable(false);
                } else
                    sudokuNumberSlots.get(i).setText("");
            }
        }
        // get solution
        String sol = "";
        String board = "";
        for (int i = 0; i < sudokuNumberSlots.size(); i++) {
            sudokuNumberSlots.get(i).setId(i);
            if (sudokuNumberSlots.get(i).getText().length() == 0) {
                board = board + "0";
            } else {
                board = board + sudokuNumberSlots.get(i).getText();
            }
            if (i != sudokuNumberSlots.size() - 1) {
                board = board + ",";
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
        //remove content of the first element
        if (solution[0].startsWith("{\"sudoku\":["))
            solution[0] = solution[0].replace("{\"sudoku\":[", "");
        //remove content of the last element
        if(Character.isDigit(solution[solution.length-1].charAt(0))){
            solution[solution.length-1] = "" + solution[solution.length-1].charAt(0);
        }

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setMax(81);

        counterThread = new Thread(new CounterLoop());
        counterThread.start();

    }

    private String generateSudokuString() {
        String sudokuJsonString = "";
        String sudokuString = "";
        try {
            sudokuStringRequest response = new sudokuStringRequest();
            sudokuJsonString = response.execute().get();

            String[] tokens = sudokuJsonString.split("\"");
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("sudoku")) {
                    sudokuString = tokens[i + 2];
                }
            }

        } catch (Exception e) {
        }

        Log.v("sudoku: ", sudokuString);
        return sudokuString;
    }

    public void leaveGameButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("End Game")
                .setMessage("Are you sure you want to end the game?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                stop();
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
    }

    public void submitButtonPress(View view) {
        boolean isCorrect = false;
        String userResults = new String("");
        String solutionString = new String("");
        for (int i = 0; i < sudokuNumberSlots.size(); i++) {
            userResults += sudokuNumberSlots.get(i).getText().toString();
        }

        for (int i = 0; i < solution.length; i++) {
            solutionString += solution[i];
        }

        System.out.println(userResults);
        System.out.println(solutionString);

        if (userResults.equals(solutionString))
            isCorrect = true;

        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Game Result");
        if (isCorrect)
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

    public void checkErrorButtonPress(View view) {
        final ArrayList<EditText> sudokuNumberSlots = getAllSudokuNumberSlots();

        if (solution.length == sudokuNumberSlots.size()) {
            for (int i = 0; i < solution.length; i++) {
                // when not empty, compare with solution
                String userInputNumber = sudokuNumberSlots.get(i).getText()
                        .toString();
                if (!userInputNumber.isEmpty()) {
                    if (!userInputNumber.equals(solution[i])) {
                        sudokuNumberSlots.get(i).setTextColor(Color.RED);
                    }
                }
            }
        }

        // After 3 seconds, reset background
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                // reset background
                for (int i = 0; i < sudokuNumberSlots.size(); i++) {
                    sudokuNumberSlots.get(i).setTextColor(Color.BLACK);
                }
            }
        }.start();
    }

    public void helpButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("Help")
                // TODO: add a custom layout showing the rules of sudoku
                .setMessage(
                        "Click on hint to automatically fill in one of the squares, be careful since hints are limited.")
                .setNegativeButton("Close", null).show();
    }

    // get all EditText elements
    private ArrayList<EditText> getAllSudokuNumberSlots() {
        TableLayout myTableLayout = (TableLayout) findViewById(R.id.singlePlayerTableLayout);
        ArrayList<EditText> sudokuNumberSlots = new ArrayList<EditText>();
        ArrayList<TableRow> sudokuNumberRows = new ArrayList<TableRow>();
        for (int i = 0; i < myTableLayout.getChildCount(); i++) {
            if (myTableLayout.getChildAt(i) instanceof TableRow)
                sudokuNumberRows.add((TableRow) myTableLayout.getChildAt(i));
        }
        // System.out.println("Table Rows = " + sudokuNumberRows.size());

        for (int i = 0; i < sudokuNumberRows.size(); i++) {
            for (int j = 0; j < sudokuNumberRows.get(i).getChildCount(); j++) {
                if (sudokuNumberRows.get(i).getChildAt(j) instanceof EditText)
                    sudokuNumberSlots.add((EditText) sudokuNumberRows.get(i)
                            .getChildAt(j));
            }
        }
        // System.out.println("Slot amount = " + sudokuNumberSlots.size());
        return sudokuNumberSlots;
    }

    // reset all EditText elements to empty
    private void resetAllSudokuNumberSlots() {
        for (int i = 0; i < sudokuNumberSlots.size(); i++) {
            sudokuNumberSlots.get(i).setText("");
        }
    }

    public void hintButtonPress(View view) throws ExecutionException,
            InterruptedException {
        TableLayout tl = (TableLayout) findViewById(R.id.singlePlayerTableLayout);
        View v = tl.findFocus();
        String res = "";
        if (v instanceof EditText && hintsUsed < maxHints) {
            hintsUsed++;
            EditText selected = (EditText) v;
            if (selected.getText().length() != 0) {
                return;
            }
            int id = selected.getId();
            selected.setText(solution[id]);
        } else if (hintsUsed >= maxHints) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setTitle("Hint limit reached.")
                    .setMessage("No more hints left!")
                    .setNegativeButton("Close", null).show();
        }
    }

    private static int countProgress() {
        int progressCounter = 0;
        for (int i = 0; i < sudokuNumberSlots.size(); i++) {
            if (sudokuNumberSlots.get(i).getText().length() != 0)
                progressCounter++;
        }
        Log.v("Counter", "" + progressCounter);
        return progressCounter;
    }

    private static class CounterLoop implements Runnable {
        public void run() {
            while (mProgressStatus < 81 && threadStopper == 0) {
                mProgressStatus = countProgress();

                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgress.setProgress(mProgressStatus);
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void stop() {
        threadStopper = 1;
    }
}
