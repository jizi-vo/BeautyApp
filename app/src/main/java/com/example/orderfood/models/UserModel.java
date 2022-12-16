package com.example.orderfood.models;


public class UserModel {
    int matk;
    String tentk;
    String sdt;
    String mk;
    String diachi;
    String quyen;
    String hinhanh;

    public UserModel() {
    }

    public UserModel(int matk, String tentk, String sdt, String mk, String diachi, String quyen, String hinhanh) {
        this.matk = matk;
        this.tentk = tentk;
        this.sdt = sdt;
        this.mk = mk;
        this.diachi = diachi;
        this.quyen = quyen;
        this.hinhanh = hinhanh;
    }

    public int getMatk() {
        return matk;
    }

    public void setMatk(int matk) {
        this.matk = matk;
    }

    public String getTentk() {
        return tentk;
    }

    public void setTentk(String tentk) {
        this.tentk = tentk;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
