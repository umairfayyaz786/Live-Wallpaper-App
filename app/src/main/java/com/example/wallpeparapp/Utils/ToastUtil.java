package com.example.wallpeparapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ToastUtil {

    static public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static public void Intent(Context context, Class<?> targetActivity){
        Intent i = new Intent(context , targetActivity);
        context.startActivity(i);
    }
}
