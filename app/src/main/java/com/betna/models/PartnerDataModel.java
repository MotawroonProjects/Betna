package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class PartnerDataModel extends StatusResponse implements Serializable {
    private List<PartnerModel> data;

    public List<PartnerModel> getData() {
        return data;
    }
}
