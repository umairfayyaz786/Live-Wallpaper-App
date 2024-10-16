package com.example.wallpeparapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.ApplyWallpaper;
import com.example.wallpeparapp.Utils.FavoriteUtilDatabase;
import com.example.wallpeparapp.Utils.LoadingDialog;
import com.example.wallpeparapp.Utils.ToastUtil;
import com.example.wallpeparapp.Utils.utils;
import com.example.wallpeparapp.databinding.ActivityWallpaperBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WallpaperActivity extends AppCompatActivity {

    // Other variables
    private Bitmap imageBitmap;  // Bitmap to be used for saving
    private LoadingDialog loadingDialog;
    Animation top;
    ActivityWallpaperBinding binding;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityWallpaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = new LoadingDialog(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String url = getIntent().getStringExtra(getString(R.string.imgurl));
        String imageType = getImageTypeFromUrl(url); // Get the image name
        final String[] resolution = {""}; // Initialize with a default value

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        binding.allThings.setAnimation(top);


        Glide.with(this).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        loadingDialog.dismiss();
                        ToastUtil.showToast(getApplicationContext(), getString(R.string.load_failed));
                        return false;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        loadingDialog.dismiss();
                        imageBitmap = bitmap;  // Assign the loaded bitmap

                        // Extract the resolution from the loaded bitmap
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        resolution[0] = width + " x " + height; // Example: "1080 x 1920"

                        // Set both resolution and image type in the same TextView
                        binding.jpg.setText(resolution[0] + "            " + imageType);
                        return false;
                    }
                })
                .into(binding.image);


        binding.info.setOnClickListener(v -> {
            utils.BottomSheetOfInfo(this, resolution[0], imageType);
        });
        binding.Editing.setOnClickListener(v -> {
            ToastUtil.showToast(this, "This feature is not available right now..");
        });
        binding.idBtnSetWallpaper.setOnClickListener(v -> {
            askForApplyWallpaper();
        });

        if (FavoriteUtilDatabase.isFavorite(this, url))
            binding.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            binding.favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);


        binding.favourite.setOnClickListener(v -> {
            if (FavoriteUtilDatabase.isFavorite(this, url)) {
                FavoriteUtilDatabase.removeFavorite(this, url);
                binding.favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24); // Update UI
                ToastUtil.showToast(this, getString(R.string.removed_from_favorites));
            } else {
                FavoriteUtilDatabase.saveFavorite(this, url);
                binding.favourite.setImageResource(R.drawable.ic_baseline_favorite_24); // Update UI
                ToastUtil.showToast(this, getString(R.string.added_to_favorites));
            }
        });
        binding.shareImg.setOnClickListener(v -> {
            if (imageBitmap == null) {
                ToastUtil.showToast(this, getString(R.string.image_not_loaded));
                return;
            }


            // Check for storage permission (needed if your app targets Android Q and below)
            if (isStoragePermissionNotGranted()) {
                ActivityCompat.requestPermissions(WallpaperActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }

            try {
                // Create a temporary file in the cache directory
                File cachePath = new File(getCacheDir(), getString(R.string.images));
                cachePath.mkdirs(); // Create directory if it doesn't exist
                File file = new File(cachePath, getString(R.string.shared_image_png));
                FileOutputStream stream = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

                // Get URI of the file
                Uri contentUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

                if (contentUri != null) {
                    // Create share intent
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.showToast(this, getString(R.string.share_failed));
            }
        });


        ////////Save images Code Start from here......////////

        binding.download.setOnClickListener(v -> {
            if (imageBitmap == null) {
                ToastUtil.showToast(this, getString(R.string.image_not_loaded));
                return;
            }
            if (isStoragePermissionNotGranted()) {
                ActivityCompat.requestPermissions(WallpaperActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
            saveImage();
        });
    }

    // Method to extract image type from URL
    private String getImageTypeFromUrl(String url) {
        if (url != null && url.contains(getString(R.string.dot))) {
            String imageName = url.substring(url.lastIndexOf(getString(R.string.slash)) + 1); // Get the image name
            if (imageName.contains(getString(R.string.dot))) {
                String imageType = imageName.substring(imageName.lastIndexOf(getString(R.string.dot)) + 1); // Get the file extension
                // Check for and remove query parameters, if any
                if (imageType.contains(getString(R.string.questionmark))) {
                    imageType = imageType.substring(0, imageType.indexOf(getString(R.string.questionmark)));
                }
                return imageType;
            }
        }
        return getString(R.string.unknown); // Fallback if no extension is found
    }

    private void askForApplyWallpaper() {
        View v = getLayoutInflater().inflate(R.layout.layout_set_on, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AlertDialog dialog = new AlertDialog.Builder(WallpaperActivity.this).setView(v).create();

            Button homeScreenButton = v.findViewById(R.id.on_home_screen_btn);
            Button lockScreenButton = v.findViewById(R.id.on_lock_screen_btn);
            Button bothScreenButton = v.findViewById(R.id.on_both_screen_btn);

            homeScreenButton.setBackgroundColor(getResources().getColor(R.color.ColorParriot));
            lockScreenButton.setBackgroundColor(getResources().getColor(R.color.ColorParriot));
            bothScreenButton.setBackgroundColor(getResources().getColor(R.color.ColorParriot));

            homeScreenButton.setOnClickListener(view -> {

                dialog.dismiss();
                applyWallpaper(1);
            });
            lockScreenButton.setOnClickListener(view -> {
                dialog.dismiss();
                applyWallpaper(2);
            });
            bothScreenButton.setOnClickListener(view -> {
                dialog.dismiss();
                applyWallpaper(3);
            });
            dialog.show();
        } else {
            applyWallpaper(0);
        }
    }

    private void applyWallpaper(int where) {

        if (where == 1) {
            ToastUtil.showToast(this, getString(R.string.set_home_screen));
        } else if (where == 2) {
            ToastUtil.showToast(this, getString(R.string.set_lock_screen));

        } else if (where == 3) {
            ToastUtil.showToast(this, getString(R.string.set_both));
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            final boolean b = ApplyWallpaper.fromBitmap(this, imageBitmap, where);
            if (b) {
                processStopIfDone();
            } else {
                ToastUtil.showToast(this, getString(R.string.set_failed));
                processStopIfDone();
            }

        });

    }

    private void saveImage() {
        if (imageBitmap == null) return;
        if (isStoragePermissionNotGranted()) return;

        // Start saving the image

        boolean isSaved = utils.save(this, imageBitmap, getString(R.string.app_name), getString(R.string.image_name));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isSaved) {
                loadingDialog.show();
                ToastUtil.showToast(this, getString(R.string.image_download));
                processStopIfDone();
            } else {
                ToastUtil.showToast(this, getString(R.string.image_download_error));
                processStopIfDone();
            }
        }, 500);
    }

    private boolean isStoragePermissionNotGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void processStopIfDone() {
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                loadingDialog.dismiss(), 800);
    }

    ////////Save images Code End here......////////


}

