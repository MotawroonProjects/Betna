package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class RateDataModel extends StatusResponse implements Serializable {
    private List<RateModel> data;

    public List<RateModel> getData() {
        return data;
    }
}
