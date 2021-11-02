package com.betna.models;

import java.io.Serializable;

public class ServiceModel implements Serializable {
    private int id;
    private int department_id;
    private String title;
    private String color;
    private String photo;
    private String desc;
    private double price;
    private String is_active;
    private String is_best;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDesc() {
        return desc;
    }

    public double getPrice() {
        return price;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getIs_best() {
        return is_best;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}


