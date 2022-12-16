package com.example.orderfood.models;

public class CustomerDetailModel {
    String tentk;
    String sdt;
    String diachi;
    String hd;
    String hdhuy;
    int tong;

    public CustomerDetailModel() {
    }

    public CustomerDetailModel(String tentk, String sdt, String diachi, String hd, String hdhuy, int tong) {
        this.tentk = tentk;
        this.sdt = sdt;
        this.diachi = diachi;
        this.hd = hd;
        this.hdhuy = hdhuy;
        this.tong = tong;
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

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getHdhuy() {
        return hdhuy;
    }

    public void setHdhuy(String hdhuy) {
        this.hdhuy = hdhuy;
    }

    public int getTong() {
        return tong;
    }

    public void setTong(int tong) {
        this.tong = tong;
    }
}
