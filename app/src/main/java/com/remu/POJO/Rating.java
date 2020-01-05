package com.remu.POJO;

public class Rating {
    private String idUser, review, rating, placeId, placeName;

    public Rating(){

    }
    public Rating(String idUser,  String review, String rating, String placeId, String placeName){
        this.idUser = idUser;
        this.review = review;
        this.rating = rating;
        this.placeId = placeId;
        this.placeName = placeName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
