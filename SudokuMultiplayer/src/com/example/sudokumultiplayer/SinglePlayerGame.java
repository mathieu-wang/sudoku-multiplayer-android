package com.example.sudokumultiplayer;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;


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
        String sudokuString = generateSudokuString(difficulty.name());
        String[] numbers = sudokuString.split(",");
        ArrayList<EditText> sodokuNumberSlots = getAllSudokuNumberSlots();

        //Write numbers into grid
        if(numbers.length == sodokuNumberSlots.size()){
            for (int i = 0; i < numbers.length; i++) {
                //skip 0, left empty
                if (!numbers[i].equals("0"))
                    sodokuNumberSlots.get(i).setText(numbers[i]);
            }
        }
    }

    private String generateSudokuString(String difficultly) {
    	//TODO web request, generate Sudoku string based on different difficultly
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
    
    public void checkErrorButtonPress(View view){
        final ArrayList<EditText> sodokuNumberSlots = getAllSudokuNumberSlots();
        //TODO web request for solution
        //String[] solutions = new String[81];
        String sudokuString = generateSudokuString(difficulty.name());
        String[] solutions = sudokuString.split(",");

        if(solutions.length == sodokuNumberSlots.size()){
            for (int i = 0; i < solutions.length; i++) {
                //when not empty, compare with solution
            	String userInputNumber = sodokuNumberSlots.get(i).getText().toString();
                if (!userInputNumber.isEmpty()){
                	if(!userInputNumber.equals(solutions[i])){
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
}
