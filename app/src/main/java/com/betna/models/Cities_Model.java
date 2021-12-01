package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class Cities_Model extends StatusResponse implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable{
        private int id;
        private int governorate_id;
        private String city_name_ar;
        private String city_name_en;
        private double price;

        public Data(String city_name_ar, String city_name_en) {
            this.city_name_ar = city_name_ar;
            this.city_name_en = city_name_en;
        }

        public void setCity_name_ar(String city_name_ar) {
            this.city_name_ar = city_name_ar;
        }

        public void setCity_name_en(String city_name_en) {
            this.city_name_en = city_name_en;
        }

        public int getId() {
            return id;
        }

        public int getGovernorate_id() {
            return governorate_id;
        }

        public String getCity_name_ar() {
            return city_name_ar;
        }

        public String getCity_name_en() {
            return city_name_en;
        }

        public double getPrice() {
            return price;
        }
    }
}

