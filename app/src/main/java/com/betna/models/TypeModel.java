package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class TypeModel implements Serializable {
    private int id;
    private String title;
    private String created_at;
    private String updated_at;
    private boolean selected;
    private String price;
    private List<ServicePlaces> get_services_place_price_many;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPrice() {
        return price;
    }

    public List<ServicePlaces> getGet_services_place_price_many() {
        return get_services_place_price_many;
    }

    public class ServicePlaces implements Serializable {
        private int id;
        private int service_id;
        private int type_id;
        private int sub_type_id;
        private int price;
        private SubTypeModel sub_place;
        private boolean selected;

        public int getId() {
            return id;
        }

        public int getService_id() {
            return service_id;
        }

        public int getType_id() {
            return type_id;
        }

        public int getSub_type_id() {
            return sub_type_id;
        }

        public int getPrice() {
            return price;
        }

        public SubTypeModel getSub_place() {
            return sub_place;
        }

        public boolean isSelected() {
            return selected;

        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}


