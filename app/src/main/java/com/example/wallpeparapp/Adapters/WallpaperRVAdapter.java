package com.example.wallpeparapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.wallpeparapp.Activities.FavoriteWallpapersActivity;
import com.example.wallpeparapp.Activities.WallpaperActivity;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.FavoriteUtilDatabase;
import com.example.wallpeparapp.Utils.ToastUtil;
import java.util.ArrayList;

public class WallpaperRVAdapter extends RecyclerView.Adapter<WallpaperRVAdapter.ViewHolder> {

    private ArrayList<String> wallPaperList;
    private Context context;
    private FavoriteWallpapersActivity activity;

    // Constructor
    public WallpaperRVAdapter(ArrayList<String> wallPaperList, Context context) {
        this.wallPaperList = wallPaperList;
        this.context = context;
    }

    @NonNull
    @Override
    public WallpaperRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperRVAdapter.ViewHolder holder, int position) {
        String imageUrl = wallPaperList.get(position);

        // Load the image using Glide
        Glide.with(context).load(imageUrl).into(holder.wallpaperIV);

        updateFavoriteIcon(holder.fav_icon, imageUrl);

        // Add click listener to open WallpaperActivity with the selected wallpaper
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, WallpaperActivity.class);
            i.putExtra("imgUrl", imageUrl);
            context.startActivity(i);
        });


        // Add click listener to handle the favorite/unfavorite action
        holder.fav_icon.setOnClickListener(v -> {
            if (FavoriteUtilDatabase.isFavorite(context, imageUrl)) {
                FavoriteUtilDatabase.removeFavorite(context, imageUrl);
                holder.fav_icon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                ToastUtil.showToast(context, "Removed from favorites");

                if (context instanceof FavoriteWallpapersActivity) {
                    ((FavoriteWallpapersActivity) context).loadFavoriteWallpapers();
                }

            } else {
                FavoriteUtilDatabase.saveFavorite(context, imageUrl);
                holder.fav_icon.setImageResource(R.drawable.ic_baseline_favorite_24);
                ToastUtil.showToast(context, "Added to favorites");
            }
        });
    }
    private void updateFavoriteIcon(ImageView favIcon, String imageUrl) {
        if (FavoriteUtilDatabase.isFavorite(context, imageUrl)) {
            favIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            favIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }

    @Override
    public int getItemCount() {
        return wallPaperList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Variables for views in the layout file
        private CardView imageCV;
        private ImageView wallpaperIV, fav_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing the views
            wallpaperIV = itemView.findViewById(R.id.idIVWallpaper);
            imageCV = itemView.findViewById(R.id.idCVWallpaper);
            fav_icon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}
