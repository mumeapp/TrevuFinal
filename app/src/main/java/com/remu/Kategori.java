package com.remu;

public class Kategori {

    String kategori, foto;

    public Kategori(String kategori, String foto) {
        this.kategori = kategori;
        this.foto = foto;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setFoto() {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }
}
