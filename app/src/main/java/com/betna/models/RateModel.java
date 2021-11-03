package com.betna.models;

import java.io.Serializable;

public class RateModel implements Serializable {
    private int id;
    private int service_id;
    private int user_id;
    private int rate;
    private String desc;
    private String created_at;
    private String updated_at;
    private ServiceModel service;
    private UserModel.User user;

    public int getId() {
        return id;
    }

    public int getService_id() {
        return service_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRate() {
        return rate;
    }

    public String getDesc() {
        return desc;
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

    public UserModel.User getUser() {
        return user;
    }
}



