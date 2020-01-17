package com.remu.POJO;

public class User {
    private String name, gender,  birthdate, about, email, image, LatLong, id;

    public User(){

    }

    public User(String id, String foto, String birthdate, String gender, String name){
        this.id = id;
        this.image = foto;
        this.birthdate = birthdate;
        this.gender = gender;
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return name;
    }

    public void setNama(String nama) {
        this.name = nama;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTanggal() {
        return birthdate;
    }

    public void setTanggal(String tanggal) {
        this.birthdate = tanggal;
    }

    public String getDeskripsi() {
        return about;
    }

    public void setDeskripsi(String deskripsi) {
        this.about = deskripsi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return image;
    }

    public void setFoto(String foto) {
        this.image = foto;
    }

    public String getLatLong() {
        return LatLong;
    }

    public void setLatLong(String LatLong) {
        this.LatLong = LatLong;
    }



}
