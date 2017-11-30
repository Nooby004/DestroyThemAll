package com.example.mlallemant.destroythemall.Utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public class SharedPreferencesManager {

    private final String PREFS_NAME = "DESTROY_THEM_ALL_PREFS_NAME";

    private final String BEST_SCORE = "BEST_SCORE";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setBestScore(int bestScore){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BEST_SCORE, bestScore);
        editor.apply();
    }

    public int getBestScore(){
        return sharedPreferences.getInt(BEST_SCORE, 0);
    }
}
