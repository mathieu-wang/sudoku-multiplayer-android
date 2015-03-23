package com.example.sudokumultiplayer;

import java.util.Random;

/**
 * Created by YK on 15-03-22.
 */
public class Constants {
    public static String[] users = {"Dommy", "Michael", "Mathieu", "Chen", "dummyfoo"};
    public static String[] times = {"1:25", "1:58", "2:30", "2:35", "5:30"};
    public static String[] sudokuCompleted = {"345", "245", "67", "33", "5"};


    public static String[] win = {"11", "8"};

    public static void win() {
        win[0]  = String.valueOf(Integer.getInteger(win[0]) + 1);
        win[1] = String.valueOf(Integer.getInteger(win[1]) + 1);
    }

    public static void reset() {
        win[0] = "0";
        win[1] = "0";
    }
}
