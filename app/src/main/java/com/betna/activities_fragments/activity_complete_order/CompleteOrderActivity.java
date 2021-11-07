package com.betna.activities_fragments.activity_complete_order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.betna.R;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.betna.adapters.TypeAdapter;
import com.betna.databinding.ActivityCompleteOrderBinding;
import com.betna.databinding.ActivitySendOrderBinding;
import com.betna.interfaces.Listeners;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.ServiceModel;
import com.betna.models.StatusResponse;
import com.betna.models.TypeDataModel;
import com.betna.models.TypeModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCompleteOrderBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private AddServiceModel serviceModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_order);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        serviceModel = (AddServiceModel) intent.getSerializableExtra("data");

    }


    private void initView() {

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(serviceModel);

        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendorder();
            }
        });

    }

    private void sendorder() {
Log.e("mddmmd",serviceModel.getArea()+" "+serviceModel.getNotes()+" "+serviceModel.getService_id()+" "+serviceModel.getType_id()+"   "+serviceModel.getDate()+"   "+serviceModel.getLatitude()+" "+serviceModel.getLongitude()+" "+serviceModel.getTotal());
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .storeOrder(userModel.getUser().getId() + " ", serviceModel.getService_id() + " ", serviceModel.getType_id() + " ", serviceModel.getArea(), serviceModel.getLongitude(), serviceModel.getLatitude(), serviceModel.getNotes(), serviceModel.getTotal(), serviceModel.getDate())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        Log.e("ldkkf",response.body().getStatus()+" "+response.code());
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                Intent intent = new Intent(CompleteOrderActivity.this, HomeActivity.class);
                                intent.putExtra("type", "order");
                                startActivity(intent);
                                finishAffinity();


                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                //    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    @Override
    public void back() {
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
