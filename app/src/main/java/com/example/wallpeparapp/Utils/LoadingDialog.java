package com.example.wallpeparapp.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import com.example.wallpeparapp.R;

public class LoadingDialog {

    private Dialog dialog;
    Handler handler = new Handler(Looper.getMainLooper());

    public LoadingDialog(Context context){
        if (dialog == null) {
            dialog = new Dialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
        }
    }
    public void show() {

        // Show progress bar with a delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 500); // Delay in milliseconds (e.g., 1000ms = 1 second)


//        if (dialog != null && !dialog.isShowing()) {
//            dialog.show();
//        }
    }
    public void dismiss() {
        // Hide progress bar after a delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000); // Delay in milliseconds


//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }



}
