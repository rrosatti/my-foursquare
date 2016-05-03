package com.example.rodri.myfoursquare.location;

/**
 * Created by rodri on 5/3/2016.
 */
public class Venue {

    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String categoryName;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setHereNow(int hereNow) {
        this.hereNow = hereNow;
    }

    private int hereNow;

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getHereNow() {
        return hereNow;
    }

}
