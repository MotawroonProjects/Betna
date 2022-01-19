package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class TypeModel implements Serializable {
    private int id;
    private String title;
    private String created_at;
    private String updated_at;
    private boolean selected;
    private String price;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public String getPrice() {
        return price;
    }
}


