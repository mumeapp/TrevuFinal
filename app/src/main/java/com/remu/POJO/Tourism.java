package com.remu.POJO;

public class Tourism {
    private String id, nama, gambar, deskripsi, alamatTempat;
    private double rating;

    public Tourism(){

    }

    public Tourism(String nama, String gambar, String deskripsi, String alamatTempat){
        this.nama = nama;
        this.gambar = gambar;
        this.alamatTempat = alamatTempat;
        this.deskripsi = deskripsi;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
