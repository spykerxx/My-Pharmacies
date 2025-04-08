package com.example.pharmacies;

import android.graphics.Bitmap;

public class Offer {
    private String name;
    private String image;
    private int id;
    private Image imageBlob;

    public Offer(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Image imageBlob) {
        this.imageBlob = imageBlob;
    }
}
