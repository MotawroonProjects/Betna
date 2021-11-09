package com.betna.models;

import java.io.Serializable;

public class NotFireModel implements Serializable {
    private  int id;

    public NotFireModel(int  id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
