package com.example.wallpeparapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class FavoriteUtilDatabase {
        private static final String PREF_NAME = "favorites_pref";
        private static final String KEY_FAVORITES = "favorites";

        public static void saveFavorite(Context context, String imageUrl) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
            favorites.add(imageUrl);
            prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
        }

        public static void removeFavorite(Context context, String imageUrl) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
            favorites.remove(imageUrl);
            prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
        }

        public static boolean isFavorite(Context context, String imageUrl) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
            return favorites.contains(imageUrl);
        }

        public static Set<String> getFavorites(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        }

}
