package com.remu.POJO;

public class Rating {
    private String idUser, biaya, lama, review, rating;

    public Rating(){

    }
    public Rating(String idUser, String biaya, String lama, String review, String rating){
        this.idUser = idUser;
        this.biaya = biaya;
        this.lama= lama;
        this.review = review;
        this.rating = rating;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getLama() {
        return lama;
    }

    public void setLama(String lama) {
        this.lama = lama;
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
