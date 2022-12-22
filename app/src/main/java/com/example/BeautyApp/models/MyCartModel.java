package com.example.BeautyApp.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    int mamon;
    String tenmon;
    int gia;
    String danhgia;
    int maloai;
    int soluong;
    String hinhanh;

    public MyCartModel() {
    }

    public MyCartModel(int mamon, String tenmon, int gia, String danhgia, int maloai, int soluong, String hinhanh) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.gia = gia;
        this.danhgia = danhgia;
        this.maloai = maloai;
        this.soluong = soluong;
        this.hinhanh = hinhanh;
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
