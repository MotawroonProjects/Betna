package com.betna.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.betna.BR;
import com.betna.R;


public class SignUpModel extends BaseObservable {
    private String first_name;
    private String seconed_name;


    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_seconed_name = new ObservableField<>();
    private int isAcceptTerms;


    public boolean isDataValid(Context context) {
        if (!first_name.trim().isEmpty()
                &&
                !seconed_name.trim().isEmpty()
                && isAcceptTerms == 1
            //&&
//               department_id != 0


        ) {
            error_first_name.set(null);
            error_seconed_name.set(null);

            return true;
        } else {

            if (first_name.trim().isEmpty()) {
                error_first_name.set(context.getString(R.string.field_req));

            } else {
                error_first_name.set(null);

            }
            if (seconed_name.trim().isEmpty()) {
                error_seconed_name.set(context.getString(R.string.field_req));

            } else {
                error_seconed_name.set(null);

            }

            if (isAcceptTerms == 0) {
                Toast.makeText(context, context.getString(R.string.please_accept_terms), Toast.LENGTH_SHORT).show();
            }

//            if (department_id == 0) {
//                Toast.makeText(context, context.getResources().getString(R.string.ch_dep), Toast.LENGTH_LONG).show();
//            }

            return false;
        }
    }

    public SignUpModel() {
        // setDepartment_id(1);
        setFirst_name("");

        setSeconed_name("");
        setIsAcceptTerms(0);
    }


    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);

    }

    @Bindable
    public String getSeconed_name() {
        return seconed_name;
    }

    public void setSeconed_name(String seconed_name) {
        this.seconed_name = seconed_name;
        notifyPropertyChanged(BR.seconed_name);

    }

    public void setIsAcceptTerms(int isAcceptTerms) {
        this.isAcceptTerms = isAcceptTerms;
    }

    @Bindable
    public int getIsAcceptTerms() {
        return isAcceptTerms;
    }


}