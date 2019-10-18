package com.remu.POJO;

public class User {
    private String nama, gender, age, tanggal, deskripsi, email, foto;

    public User() {
    }

    public User(String nama, String gender, String age, String tanggal, String deskripsi, String email, String foto) {
        this.nama = nama;
        this.gender = gender;
        this.age = age;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.email = email;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
