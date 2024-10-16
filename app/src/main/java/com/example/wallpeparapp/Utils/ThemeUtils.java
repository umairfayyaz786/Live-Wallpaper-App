package com.example.wallpeparapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeUtils {

        private SharedPreferences mySharedPref;

        public ThemeUtils(Context context) {
            mySharedPref = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        }

        public void setThemeState(String key, int value) {
            SharedPreferences.Editor editor = mySharedPref.edit();
            editor.putInt(key, value);
            editor.apply();
        }

        public int getThemeState(String key) {
            return mySharedPref.getInt(key, 2); // Default to SYSTEM
        }
}
