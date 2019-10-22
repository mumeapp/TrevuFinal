package com.remu.POJO;

public class HalalFoodRestaurant {
    private String judul, rating, jarak, id, foto;

    public HalalFoodRestaurant(){

    }
    public HalalFoodRestaurant(String judul, String rating, String jarak, String id, String foto){
        this.id = id;
        this.jarak = jarak;
        this.judul = judul;
        this.rating = rating;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
