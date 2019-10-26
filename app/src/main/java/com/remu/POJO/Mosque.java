package com.remu.POJO;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.HashMap;

public class Mosque {

    private String formattedAddress;
    private LatLng geoLocation;
    private HashMap<String, LatLng> geoViewport;
    private String icon;
    private String id;
    private String name;
    private JSONArray photos;
    private String placeId;
    private String compoundCode;
    private String globalCode;
    private String rating;
    private String reference;
    private JSONArray types;
    private String userRatingsTotal;

    public Mosque(String formattedAddress, LatLng geoLocation, HashMap<String, LatLng> geoViewport, String icon, String id, String name, JSONArray photos, String placeId, String compoundCode, String globalCode, String rating, String reference, JSONArray types, String userRatingsTotal) {
        this.formattedAddress = formattedAddress;
        this.geoLocation = geoLocation;
        this.geoViewport = geoViewport;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.photos = photos;
        this.placeId = placeId;
        this.compoundCode = compoundCode;
        this.globalCode = globalCode;
        this.rating = rating;
        this.reference = reference;
        this.types = types;
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public LatLng getGeoLocation() {
        return geoLocation;
    }

    public HashMap<String, LatLng> getGeoViewport() {
        return geoViewport;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONArray getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getCompoundCode() {
        return compoundCode;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    public String getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public JSONArray getTypes() {
        return types;
    }

    public String getUserRatingsTotal() {
        return userRatingsTotal;
    }

}
