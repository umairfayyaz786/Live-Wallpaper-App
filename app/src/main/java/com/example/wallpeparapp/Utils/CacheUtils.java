package com.example.wallpeparapp.Utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class CacheUtils {


    public static void clearCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
                Toast.makeText(context, "Cache Cleared", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error Clearing Cache", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to delete cache files recursively
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
