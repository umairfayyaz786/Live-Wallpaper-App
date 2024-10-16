package com.example.wallpeparapp.Activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.ToastUtil;
import com.example.wallpeparapp.databinding.ActivityContactBinding;
import java.util.Objects;

public class ContactActivity extends AppCompatActivity {

    ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml(getString(R.string.contact)));

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkthemecolor));
        } else {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorOrange));
        }

        binding.btnSend.setOnClickListener(v -> {
            String Name = Objects.requireNonNull(binding.txtInputName.getText()).toString();
            String Email = Objects.requireNonNull(binding.txtEmail.getText()).toString();
            String Message = Objects.requireNonNull(binding.txtMessage.getText()).toString();

            if (Name.isEmpty()) {
                binding.txtInputName.setError(getString(R.string.username_cannot_be_empty));
            } else if (Email.isEmpty()) {
                binding.txtEmail.setError(getString(R.string.email_cannot_be_empty));
            } else if (Message.isEmpty()) {
                binding.txtMessage.setError(getString(R.string.message_cannot_be_empty));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ToastUtil.Intent(this, HomeScreenActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
}