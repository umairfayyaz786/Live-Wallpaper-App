package com.example.wallpeparapp.Activities;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.ToastUtil;
import com.example.wallpeparapp.databinding.ActivityDisplayImageBinding;
import java.util.Objects;

public class DisplayImageActivity extends AppCompatActivity {

    ActivityDisplayImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDisplayImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml(getString(R.string.download)));

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkthemecolor));
        } else {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorOrange));
        }

        String imageUriString = getIntent().getStringExtra(getString(R.string.imageuri));

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            try {
                Glide.with(this)
                        .load(imageUri)
                        .into(binding.imageView);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast(this , getString(R.string.failed_to_load_image));
            }
        } else {
            ToastUtil.showToast(this , getString(R.string.no_image_data_found));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            ToastUtil.Intent(this, HomeScreenActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
}