package com.example.wallpeparapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.wallpeparapp.Activities.WallpaperActivity;
import com.example.wallpeparapp.Models.WallpaperFavouriteDataModel;
import com.example.wallpeparapp.R;

import java.util.List;

public class WallpaperFavouriteAdapter extends RecyclerView.Adapter<WallpaperFavouriteAdapter.WallpaperViewHolder> {

    private final Context context;
    private final List<WallpaperFavouriteDataModel> wallpapers;

    public WallpaperFavouriteAdapter(Context context, List<WallpaperFavouriteDataModel> wallpapers) {
        this.context = context;
        this.wallpapers = wallpapers;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_rv_item, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        WallpaperFavouriteDataModel wallpaper = wallpapers.get(position);

        // Load the image using Glide
        Glide.with(context)
                .load(wallpaper.getImageUrl())
                .into(holder.imageView);

        // Set click listener to open WallpaperActivity with the selected wallpaper
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WallpaperActivity.class);
            intent.putExtra("imgUrl", wallpaper.getImageUrl());
            context.startActivity(intent);
        });

//        holder.favoriteIcon.setOnClickListener(v -> {
//            if (FavoriteUtilDatabase.isFavorite(context, "imgUrl")) {
//                FavoriteUtilDatabase.removeFavorite(context, "imgUrl");
//                holder.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24); // Update UI
//                ToastUtil.showToast(context, "Removed from favorites");
//            } else {
//                FavoriteUtilDatabase.saveFavorite(context, "imgUrl");
//                holder.favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24); // Update UI
//                ToastUtil.showToast(context, "Added to favorites");
//            }
//        });

        // Optional: Display favorite status, if needed
        if (wallpaper.isFavorite()) {
            holder.favoriteIcon.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    static class WallpaperViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView favoriteIcon;

        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.idIVWallpaper);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}
