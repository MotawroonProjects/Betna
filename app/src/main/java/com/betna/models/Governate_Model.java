package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class Governate_Model extends StatusResponse implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable{
        private int id;
        private String governorate_name_ar;
        private String governorate_name_en;
        private int price;
        private int is_active;

        public Data(String governorate_name_ar, String governorate_name_en) {
            this.governorate_name_ar = governorate_name_ar;
            this.governorate_name_en = governorate_name_en;
        }

        public int getId() {
            return id;
        }

        public String getGovernorate_name_ar() {
            return governorate_name_ar;
        }

        public String getGovernorate_name_en() {
            return governorate_name_en;
        }

        public int getPrice() {
            return price;
        }

        public int getIs_active() {
            return is_active;
        }
    }
}

