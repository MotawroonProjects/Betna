package com.betna.models;

import java.io.Serializable;

public class OrderResponseModel extends StatusResponse implements Serializable {
    private String data;

    public String getData() {
        return data;
    }
}
