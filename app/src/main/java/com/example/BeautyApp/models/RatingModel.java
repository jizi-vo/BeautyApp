package com.example.BeautyApp.models;

public class RatingModel {
    String img;
    String ten;
    String danhgia;
    String date;
    String cmt;

    public RatingModel() {
    }

    public RatingModel(String img, String ten, String danhgia, String date, String cmt) {
        this.img = img;
        this.ten = ten;
        this.danhgia = danhgia;
        this.date = date;
        this.cmt = cmt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }
}
