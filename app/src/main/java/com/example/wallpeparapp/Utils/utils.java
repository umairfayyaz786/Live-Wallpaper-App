package com.example.wallpeparapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.wallpeparapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class utils {

    public static boolean save(Context context, Bitmap bitmap, String appDir, String name) {
        Log.d("TAG", "save: called");
        OutputStream stream = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + appDir);
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), values);
            if (uri == null)
                return false;
            try {
                stream = context.getContentResolver().openOutputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File imagesDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/" + appDir);
                if (!imagesDir.exists()) {
                    boolean b = imagesDir.mkdirs();
                }
                File image = new File(imagesDir, name + ".jpg");
                try {
                    stream = new FileOutputStream(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (stream == null)
            return false;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
        return true;
    }

    public static void Review(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback);
        Button btnExit = dialog.findViewById(R.id.Exit);
        Button btnFeedback = dialog.findViewById(R.id.feedback);
        TextView btnclose = dialog.findViewById(R.id.close);


        btnclose.setOnClickListener(v -> dialog.dismiss());
        btnExit.setOnClickListener(v -> dialog.dismiss());
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kachariya.smallbusinessplan"));
            activity.startActivity(intent);
        });


        // Show the dialog
        dialog.show();
    }

    public static void share(Activity activity) {

        String url = "https://play.google.com/store/apps/details?id=com.kachariya.smallbusinessplan";
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, url);
        i.setType("text/plain");
        activity.startActivity(i.createChooser(i, "Share to:"));
    }

    public static void BackpressedClosedApplication(Activity activity) {

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.feedback);

        Button exit = dialog.findViewById(R.id.Exit);
        Button feedback = dialog.findViewById(R.id.feedback);
        TextView close = dialog.findViewById(R.id.close);

        dialog.show();

        exit.setOnClickListener(v -> {
            activity.finish();
        });

        feedback.setOnClickListener(v -> {
            ToastUtil.showToast(activity, "feedback");
        });

        close.setOnClickListener(v -> {
            dialog.dismiss(); // Close the dialog
        });
    }

    public static void showbottomdialog(Activity activity) {

        final  Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetofabout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public static void BottomSheetOfInfo(Context context, String resolution, String imageType) {

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottomsheetimageinfo, null);
        TextView type = bottomSheetView.findViewById(R.id.typejpg);
        TextView resol = bottomSheetView.findViewById(R.id.resolutionresult);
        type.setText(imageType);
        resol.setText(resolution);
        dialog.setContentView(bottomSheetView);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
