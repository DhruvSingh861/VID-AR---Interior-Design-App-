package com.example.project1.virtualinteriordesign.model;

import android.text.format.DateFormat;

import java.util.Date;

public class GalleryModel {
    String picture;
    int time;

    public GalleryModel(String uri, String filename) {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTime() {
        Date date = new Date(time);
        CharSequence sequence = DateFormat.format("d MMMM yyyy hh:mm", date.getTime());
        return sequence.toString();
    }

    public void setTime(int time) {
        this.time = time;
    }
}
