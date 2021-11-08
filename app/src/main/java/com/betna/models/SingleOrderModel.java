package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class SingleOrderModel extends StatusResponse implements Serializable {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }
}
