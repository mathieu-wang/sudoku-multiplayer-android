package com.example.sudokumultiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


public class SinglePlayerGame extends ActionBarActivity {
    Difficulty difficulty = Difficulty.EASY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game);
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

        //noinspection SimplifiableIfStatement
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
            switch(view.getId()) {
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

    public void startGame (View view) {
        System.out.println("Starting game with difficulty: " + difficulty.name());
        String sudokuString = generateSudokuString();
        String[] numbers = sudokuString.split(",");
        
    }

    private String generateSudokuString() {
        String sudokuJsonString =  "{\n" +
                "    \"sudoku\": \"8,6,0,9,0,0,0,4,3,9,0,0,2,0,3,8,6,1,0,4,3,0,6,1,9,7,0,0,0,9,1,5,0,4,3,0,0,0,7,4,3,0,0,8,0,4,3,2,6,8,9,1,0,7,0,1,0,0,9,6,3,0,4,0,9,6,0,0,4,7,1,8,0,0,0,7,1,8,5,0,0\"\n" +
                "}\n";

        String sudokuString = "";
        String[] tokens = sudokuJsonString.split("\"");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("sudoku")) {
                sudokuString = tokens[i + 2];
            }
        }
        return sudokuString;
    }

    public void leaveGameButtonPress(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("End Game")
                .setMessage("Are you sure you want to end the game?")
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
}