package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class SingleServiceDataModel extends StatusResponse implements Serializable {
    private ServiceModel data;

    public ServiceModel getData() {
        return data;
    }
}
