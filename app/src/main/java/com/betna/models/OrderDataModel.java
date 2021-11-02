package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel extends StatusResponse implements Serializable {
    private List<OrderModel> current;
    private List<OrderModel> old;

    public List<OrderModel> getCurrent() {
        return current;
    }

    public List<OrderModel> getOld() {
        return old;
    }
}
