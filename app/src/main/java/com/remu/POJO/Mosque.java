package com.remu.POJO;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.HashMap;

public class Mosque {

    private LatLng geoLocation;
    private HashMap<String, LatLng> geoViewport;
    private String icon;
    private String id;
    private String name;
    private boolean openNow;
    private String placeId;
    private String compoundCode;
    private String globalCode;
    private String rating;
    private String reference;
    private String scope;
    private JSONArray types;
    private String userRatingsTotal;
    private String vicinity;

    public Mosque(LatLng geoLocation, HashMap<String, LatLng> geoViewport, String icon, String id, String name, boolean openNow, String placeId, String compoundCode, String globalCode, String rating, String reference, String scope, JSONArray types, String userRatingsTotal, String vicinity) {
        this.geoLocation = geoLocation;
        this.geoViewport = geoViewport;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.openNow = openNow;
        this.placeId = placeId;
        this.compoundCode = compoundCode;
        this.globalCode = globalCode;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.types = types;
        this.userRatingsTotal = userRatingsTotal;
        this.vicinity = vicinity;
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

    public boolean getOpenNow() { return openNow; }

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

    public String getScope() { return scope; }

    public JSONArray getTypes() {
        return types;
    }

    public String getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public String getVicinity() { return vicinity; }

}
