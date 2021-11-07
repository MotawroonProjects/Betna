package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class TypeDataModel extends StatusResponse implements Serializable {
    private List<TypeModel> data;

    public List<TypeModel> getData() {
        return data;
    }
}
