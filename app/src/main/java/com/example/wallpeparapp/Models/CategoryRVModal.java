package com.example.wallpeparapp.Models;

public class CategoryRVModal {

    private String category;
    private String imgUrl;

    public CategoryRVModal(String category, String imgUrl) {
        this.category = category;
        this.imgUrl = imgUrl;
    }

    // creating a constructor, getter and setter methods.
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
