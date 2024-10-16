package com.example.wallpeparapp.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.wallpeparapp.Activities.HomeScreenActivity;
import com.example.wallpeparapp.R;

import java.util.Objects;

public class GetStarted extends AppCompatActivity {

    CardView btnStarted;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Objects.requireNonNull(getSupportActionBar()).hide();

        btnStarted = findViewById(R.id.get_started);

        btnStarted.setOnClickListener(v -> {

            Intent i = new Intent(this, HomeScreenActivity.class);
            startActivity(i);
        });
    }
}