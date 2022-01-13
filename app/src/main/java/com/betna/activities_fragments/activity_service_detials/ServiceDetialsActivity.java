package com.betna.activities_fragments.activity_service_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.betna.R;
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.adapters.PreWorkAdapter;
import com.betna.adapters.Rate2Adapter;
import com.betna.adapters.RateAdapter;
import com.betna.databinding.ActivityServiceDetialsBinding;
import com.betna.interfaces.Listeners;
import com.betna.language.Language;
import com.betna.models.RateModel;
import com.betna.models.ServiceModel;
import com.betna.models.SingleServiceDataModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityServiceDetialsBinding binding;
    private RateAdapter adapter;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private ServiceModel serviceModel;
    private List<ServiceModel.Images> imagesList;
    private PreWorkAdapter preWorkAdapter;
    private List<RateModel> rateModelList;
    private Rate2Adapter rate2Adapter;
    private int req;
    private ActivityResultLauncher<Intent> launcher;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        serviceModel = (ServiceModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        imagesList = new ArrayList<>();
        rateModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preWorkAdapter = new PreWorkAdapter(imagesList, this);
        binding.recviewpre.setLayoutManager(new GridLayoutManager(this, 3));
        rate2Adapter = new Rate2Adapter(rateModelList, this);
        binding.recviewpre.setAdapter(preWorkAdapter);
        binding.recviewRate.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recviewRate.setAdapter(rate2Adapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req =1;
                Intent intent = new Intent(ServiceDetialsActivity.this, SendOrderActivity.class);
                intent.putExtra("data", serviceModel);
                launcher.launch(intent);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {
                  
                }
            }
        });


        getservicebyId();


    }


    private void getservicebyId() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getServiceById(serviceModel.getId())
                .enqueue(new Callback<SingleServiceDataModel>() {
                    @Override
                    public void onResponse(Call<SingleServiceDataModel> call, Response<SingleServiceDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Log.e("code", response.body().getStatus() + "__");

                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (response.body().getData() != null) {
                                    updateUi(response.body());
                                }
                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    break;
                                default:
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SingleServiceDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUi(SingleServiceDataModel body) {
        imagesList.clear();
        preWorkAdapter.notifyDataSetChanged();
        rateModelList.clear();
        rate2Adapter.notifyDataSetChanged();
        serviceModel = body.getData();
        binding.setModel(serviceModel);
        binding.nested.setVisibility(View.VISIBLE);
        binding.btnOrder.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.GONE);
        if (serviceModel.getImages().size() > 0) {
            Log.e("dkkdk", serviceModel.getImages().size() + "");
            binding.llprework.setVisibility(View.VISIBLE);
            imagesList.clear();
            imagesList.addAll(serviceModel.getImages());
            preWorkAdapter.notifyDataSetChanged();
        } else {
            binding.llprework.setVisibility(View.GONE);
        }

        if (serviceModel.getRate().size() > 0) {
            binding.llRate.setVisibility(View.VISIBLE);
            rateModelList.clear();
            rateModelList.addAll(serviceModel.getRate());
            rate2Adapter.notifyDataSetChanged();
        } else {
            binding.llRate.setVisibility(View.GONE);
        }
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
