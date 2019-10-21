package com.remu.POJO;

public class Restoran {
    private String namaRestoran, alamatRestoran, foto, deskripsi;

    public Restoran(String namaRestoran, String alamatRestoran, String foto, String deskripsi) {
        this.namaRestoran = namaRestoran;
        this.alamatRestoran = alamatRestoran;
        this.foto = foto;
        this.deskripsi = deskripsi;
    }

    public String getNamaRestoran() {
        return namaRestoran;
    }

    public void setNamaRestoran(String namaRestoran) {
        this.namaRestoran = namaRestoran;
    }

    public String getAlamatRestoran() {
        return alamatRestoran;
    }

    public void setAlamatRestoran(String alamatRestoran) {
        this.alamatRestoran = alamatRestoran;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
