package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class SubTypeDataModel extends StatusResponse implements Serializable {
    private List<SubTypeModel> data;

    public List<SubTypeModel> getData() {
        return data;
    }
}
