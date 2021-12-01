package com.betna.models;

import java.io.Serializable;

public class SubTypeModel implements Serializable {
   private int id;
   private String name;
   private int place_type;
    private boolean selected;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPlace_type() {
        return place_type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}


