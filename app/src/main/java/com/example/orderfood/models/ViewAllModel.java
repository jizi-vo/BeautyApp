package com.example.orderfood.models;

public class ViewAllModel {

    int mamon;
    String tenmon;
    int gia;
    String danhgia;
    int maloai;
    String hinhanh;
    int tt;

    public ViewAllModel() {
    }

    public ViewAllModel(int mamon, String tenmon, int gia, String danhgia, int maloai, String hinhanh, int tt) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.gia = gia;
        this.danhgia = danhgia;
        this.maloai = maloai;
        this.hinhanh = hinhanh;
        this.tt = tt;
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

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }
}
