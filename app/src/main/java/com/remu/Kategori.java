package com.remu;

public class Kategori {

    String nama, gambar;

    public Kategori(String nama, String gambar) {
        this.nama = nama;
        this.gambar = gambar;
    }



    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
