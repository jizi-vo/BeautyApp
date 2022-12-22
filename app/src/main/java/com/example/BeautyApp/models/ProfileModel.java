package com.example.BeautyApp.models;

public class ProfileModel {
    String profileImg;
    String name;
    String email;
    String number;
    String address;

    public ProfileModel() {
    }

    public ProfileModel(String profileImg, String name, String email, String number, String address) {
        this.profileImg = profileImg;
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
