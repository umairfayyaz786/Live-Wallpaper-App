package com.example.wallpeparapp.Models;

public class WallpaperFavouriteDataModel {
        private String imageUrl;
        private boolean isFavorite;

        // Constructor, getters, and setters
        public WallpaperFavouriteDataModel(String imageUrl, boolean isFavorite) {
            this.imageUrl = imageUrl;
            this.isFavorite = isFavorite;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

}
