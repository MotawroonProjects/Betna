package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class SendOrderModel implements Serializable {
    private String order_id;
    private String user_id;
    private String service_id;
    private String type_id;
    private String longitude;
    private String latitude;
    private String notes;
    private String total;
    private String fees;
    private String grand_total;
    private String date;
    private String location;
    private String governorate_id;
    private String city_id;
    private String pay_type;
    private List<Details> details;

    public SendOrderModel(String user_id, String service_id, String type_id, String longitude, String latitude, String notes, String total, String fees, String grand_total, String date, String location, String governorate_id, String city_id, List<Details> details,String pay_type) {
        this.user_id = user_id;
        this.service_id = service_id;
        this.type_id = type_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.notes = notes;
        this.total = total;
        this.fees = fees;
        this.grand_total = grand_total;
        this.date = date;
        this.location = location;
        this.governorate_id = governorate_id;
        this.city_id = city_id;
        this.details = details;
        this.pay_type = pay_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGovernorate_id() {
        return governorate_id;
    }

    public void setGovernorate_id(String governorate_id) {
        this.governorate_id = governorate_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public static class Details implements Serializable {
        private String sub_place_id;
        private double area;

        public Details(String sub_place_id, double area) {
            this.sub_place_id = sub_place_id;
            this.area = area;
        }

        public String getSub_place_id() {
            return sub_place_id;
        }

        public double getArea() {
            return area;
        }
    }
}
