package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel implements Serializable {
    private List<SliderModel> data;
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<SliderModel> getData() {
        return data;
    }

    public static class SliderModel implements Serializable {
        private int id;
        private String photo;
        private String  created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getPhoto() {
            return photo;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
