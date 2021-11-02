package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class ServiceDataModel extends StatusResponse implements Serializable {
    private List<ServiceModel> data;

    public List<ServiceModel> getData() {
        return data;
    }
}
