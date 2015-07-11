package br.com.gallotti.desafioandroid.util;

import android.util.Log;

public class SimpleLog{

    private static String LOG = "DesafioAndroid";

    public static void write(String text){
        Log.d(LOG, text);
    }

}
