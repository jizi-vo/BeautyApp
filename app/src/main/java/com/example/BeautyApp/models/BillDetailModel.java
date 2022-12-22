package com.example.BeautyApp.models;

public class BillDetailModel {
    String mahd;
    int mamon;
    String tenmon;
    int sl;
    int gia;
    String danhgia;
    String hinhanh;

    public BillDetailModel() {
    }

    public BillDetailModel(String mahd, int mamon, String tenmon, int sl, int gia, String danhgia, String hinhanh) {
        this.mahd = mahd;
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.sl = sl;
        this.gia = gia;
        this.danhgia = danhgia;
        this.hinhanh = hinhanh;
    }

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public int getMamon() {
        return mamon;
    }

    public void setMamon(int mamon) {
        this.mamon = mamon;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
