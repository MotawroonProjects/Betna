package com.betna.models;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.betna.BR;
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.activities_fragments.activity_update_order.UpdateOrderActivity;

import java.io.Serializable;

public class MetersModel extends BaseObservable implements Serializable {
    private String sub_cat_id;
    private String title;
    private String meter_number;
    private double meter_price;
    private double total_meter_price;
    private Context context;


    public MetersModel(Context context) {
        sub_cat_id ="";
        title = "";
        meter_number = "0";
        meter_price = 0;
        total_meter_price = 0;
        this.context= context;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getMeter_number() {
        if (meter_number.equals("0")){
            return "";

        }else {
            return meter_number;

        }
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
        notifyPropertyChanged(BR.meter_number);
        calculate_meter_price();
    }

    public double getMeter_price() {
        return meter_price;
    }

    public void setMeter_price(double meter_price) {
        this.meter_price = meter_price;

    }

    @Bindable
    public double getTotal_meter_price() {
        return total_meter_price;
    }

    public void setTotal_meter_price(double total_meter_price) {
        this.total_meter_price = total_meter_price;
        notifyPropertyChanged(BR.total_meter_price);
    }

    private void calculate_meter_price() {
        if (meter_number == null||meter_number.isEmpty()) {
            meter_number = "0";
        }
        total_meter_price = meter_price * Integer.parseInt(meter_number);
        if (context!=null&&context instanceof SendOrderActivity){
            SendOrderActivity activity = (SendOrderActivity) context;
            activity.calculateTotal();
        }else  if (context!=null&&context instanceof UpdateOrderActivity){
            UpdateOrderActivity activity = (UpdateOrderActivity) context;
            activity.calculateTotal();
        }
        setTotal_meter_price(total_meter_price);
    }
}
