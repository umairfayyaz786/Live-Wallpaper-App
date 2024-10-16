package com.example.wallpeparapp.Activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.FavoriteUtilDatabase;
import com.example.wallpeparapp.Adapters.WallpaperRVAdapter;
import com.example.wallpeparapp.Utils.ToastUtil;
import com.example.wallpeparapp.databinding.ActivityFavoriteWallpapersBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FavoriteWallpapersActivity extends AppCompatActivity {

    private WallpaperRVAdapter adapter;
    private List<String> favoriteWallpapers;
    ActivityFavoriteWallpapersBinding binding;
    Animation bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFavoriteWallpapersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml(getString(R.string.favourites)));

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkthemecolor));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.emptyFavoritesText.setTextColor(getColor(R.color.white));
            }
        } else {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorOrange));
        }

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        binding.recyclerView.setAnimation(bottom);

        // Load favorite wallpapers
        loadFavoriteWallpapers();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void loadFavoriteWallpapers() {
        Set<String> favorites = FavoriteUtilDatabase.getFavorites(this);
        favoriteWallpapers = new ArrayList<>(favorites);

        if (favoriteWallpapers.isEmpty()) {
            binding.errorLayout.setVisibility(View.VISIBLE);
            binding.emptyFavoritesText.setText(R.string.nothing_favourite);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.errorLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);

            adapter = new WallpaperRVAdapter(new ArrayList<>(favoriteWallpapers), this);
            binding.recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ToastUtil.Intent(this, HomeScreenActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

}