package com.example.pharmacies;

import android.graphics.Bitmap;

public class Image {
    private int id;
    private Bitmap bitmap;
    private String tag;

    public Image(){

    }
    public Image( Bitmap bitmap, String tag) {
        this.bitmap = bitmap;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getTag() {
        return tag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

