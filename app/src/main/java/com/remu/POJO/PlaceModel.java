package com.remu.POJO;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

public class PlaceModel {

    private String placeId;
    private String placeName;
    private String placeAddress;
    private double placeRating;
    private LatLng placeLocation;
    private double placeWeight;
    private String placePhotoUri;

    public PlaceModel(String placeId, String placeName, String placeAddress, double placeRating, LatLng placeLocation) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeRating = placeRating;
        this.placeLocation = placeLocation;
    }

    public PlaceModel(String placeId, String placeName, String placeAddress, double placeRating, LatLng placeLocation, String placePhotoUri) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeRating = placeRating;
        this.placeLocation = placeLocation;
        this.placePhotoUri = placePhotoUri;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public double getPlaceRating() {
        return placeRating;
    }

    public LatLng getPlaceLocation() {
        return placeLocation;
    }

    public String getPlacePhotoUri() {
        return placePhotoUri;
    }

    public void setPlaceWeight(double weight) {
        this.placeWeight = weight;
    }

    public double getPlaceWeight() {
        return placeWeight;
    }

}

