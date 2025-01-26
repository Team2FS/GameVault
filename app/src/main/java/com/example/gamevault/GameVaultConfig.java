package com.example.gamevault;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
public class GameVaultConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // get variable from sharedpref
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", true);

        if (isDarkModeEnabled) {
            // For forcing Dark Mode always
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            // For forcing Light Mode always
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
