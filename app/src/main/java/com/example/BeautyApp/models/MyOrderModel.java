package com.example.BeautyApp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderModel {
    @SerializedName("Mahd")
    @Expose
    String mahd;
    @SerializedName("Sdt")
    @Expose
    String sdt;
    @SerializedName("Date")
    @Expose
    String date;
    @SerializedName("Tt")
    @Expose
    String tt;
    @SerializedName("Dc")
    @Expose
    String dc;

    public MyOrderModel() {
    }

    public MyOrderModel(String mahd, String sdt, String date, String tt, String dc) {
        this.mahd = mahd;
        this.sdt = sdt;
        this.date = date;
        this.tt = tt;
        this.dc = dc;
    }

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
