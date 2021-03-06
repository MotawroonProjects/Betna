package com.betna.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private int id;
    private String title;
    private String photo;
    private String is_active;
    private String created_at;
    private String updated_at;
    private boolean selected;

    public CategoryModel(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}


