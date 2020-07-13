package com.example.gallaryapplication.model;

import android.graphics.Bitmap;

public class ImageModel {

    private Bitmap bitmap;
    private String dateHeader;
    private boolean isHeader;

    public ImageModel(Bitmap bitmap, String dateHeader, boolean isHeader) {
        this.bitmap = bitmap;
        this.dateHeader = dateHeader;
        this.isHeader = isHeader;
    }
    public ImageModel(){}

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDateHeader() {
        return dateHeader;
    }

    public void setDateHeader(String dateHeader) {
        this.dateHeader = dateHeader;
    }

    public boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
}
