package com.example.orderfood.models;

public class ThongKeModel {
    String img;
    String name;
    String rating;
    int price;
    int quantity;
    String percent;

    public ThongKeModel(String name) {
        this.name = name;
    }

    public ThongKeModel(String img, String name, String rating, int price, int quantity, String percent) {
        this.img = img;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.quantity = quantity;
        this.percent = percent;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
