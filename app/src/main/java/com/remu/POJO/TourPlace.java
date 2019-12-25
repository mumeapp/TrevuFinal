package com.remu.POJO;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

public class TourPlace {

    private Drawable image;
    private String title;
    private double rating;
    private LatLng position;

    public TourPlace(Drawable image, String title, double rating, LatLng position) {
        this.image = image;
        this.title = title;
        this.rating = rating;
        this.position = position;
    }

    public Drawable getImage() { return image; }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public LatLng getPosition() {
        return position;
    }
}
