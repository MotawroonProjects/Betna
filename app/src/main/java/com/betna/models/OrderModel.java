package com.betna.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private int id;
    private int user_id;
    private int service_id;
    private String date;
    private int rev_id;
    private int type_id;
    private int area;
    private double longitude;
    private double latitude;
    private String notes;
    private String status;
    private double total;
    private int governorate_id;
    private int city_id;
    private String created_at;
    private String updated_at;
    private String location;
    private ServiceModel service;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getService_id() {
        return service_id;
    }

    public String getDate() {
        return date;
    }

    public int getRev_id() {
        return rev_id;
    }

    public int getType_id() {
        return type_id;
    }

    public int getArea() {
        return area;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ServiceModel getService() {
        return service;
    }

    public int getGovernorate_id() {
        return governorate_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getLocation() {
        return location;
    }
}


