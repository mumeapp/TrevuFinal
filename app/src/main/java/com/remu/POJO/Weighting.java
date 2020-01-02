package com.remu.POJO;

import java.util.ArrayList;

public class Weighting {

    public ArrayList<Double> doWeighting(double latitude, double longitude, ArrayList<PlaceModel> item) {

        double weightDistance = -0.3, weightGoogleRate = 0.167, weightTrevuRate = 0.25, weightIntencity = 0.25;
        double pembagi = 0;
        int count = 0;
        ArrayList<Double> s = new ArrayList<>();
        ArrayList<Double> v = new ArrayList<>();

        for (PlaceModel place : item) {
            double lat = place.getPlaceLocation().latitude;
            double lng = place.getPlaceLocation().longitude;
            s.add(Math.pow(Distance.distance(lat, latitude, lng, longitude), weightDistance) *
                    Math.pow(place.getPlaceRating(), weightGoogleRate) *
                    Math.pow(1, weightTrevuRate) *
                    Math.pow(1, weightIntencity));
            pembagi += s.get(count);
            count++;
        }

        for (Double vectorS : s) {
            v.add(vectorS / pembagi);
        }

        return v;
    }
}
