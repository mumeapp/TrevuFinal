package com.remu.POJO;

public class Tourism {
    private String id, nama, tempat, rating, gambar;

    public Tourism(){

    }

    public Tourism(String id, String nama, String tempat, String gambar, String rating){
        this.id = id;
        this.nama = nama;
        this.tempat = tempat;
        this.gambar = gambar;
        this.rating = rating;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String lokasi) {
        this.tempat = lokasi;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
