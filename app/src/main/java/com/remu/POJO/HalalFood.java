package com.remu.POJO;

public class HalalFood {
    private String nama;
    private String jumlah;
    private String jarak;
    private String id;
    private String gambar;

    public HalalFood(){

    }

    public HalalFood(String nama, String jumlah, String jarak, String id, String gambar){
        this.nama = nama;
        this.gambar = gambar;
        this.jumlah = jumlah;
        this.jarak = jarak;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
