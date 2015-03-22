package com.example.sudokumultiplayer;

/**
 * Created by YK on 15-03-22.
 */
public class Constants {
    public static String[] users = {"Dommy", "Michael", "Mathieu", "Chen", "dummyfoo"};
    public static String[] times = {"1:25", "1:58", "2:30", "2:35", "5:30"};
    public static String[] sudokuCompleted = {"345", "245", "67", "33", "5"};
    public static String[] win = {"11", "8", "6", "5", "0"};
    public static void reset(){
        users = new String[5];
        times = new String[5];
        sudokuCompleted = new String[5];
        win = new String[5];
    }
}
