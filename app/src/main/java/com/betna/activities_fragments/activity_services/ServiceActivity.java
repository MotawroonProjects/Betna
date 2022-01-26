package com.betna.activities_fragments.activity_services;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.betna.R;
import com.betna.activities_fragments.activity_service_detials.ServiceDetialsActivity;
import com.betna.adapters.ServiceAdapter;
import com.betna.databinding.ActivityServicesBinding;
import com.betna.language.Language;
import com.betna.models.CategoryModel;
import com.betna.models.ServiceDataModel;
import com.betna.models.ServiceModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {
    private ActivityServicesBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;

    private CategoryModel categoryModel;
    private List<ServiceModel> serviceModelList;
    private ServiceAdapter ServiceAdapter;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            categoryModel = (CategoryModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {

        serviceModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setTitle(categoryModel.getTitle());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            //  EventBus.getDefault().register(this);

        }
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        // binding.progBar.setVisibility(View.GONE);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ServiceAdapter = new ServiceAdapter(serviceModelList, this, null);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recView.setAdapter(ServiceAdapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getServices();
        binding.swipeRefresh.setOnRefreshListener(this::getServices);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }


    public void getServices() {
        serviceModelList.clear();
        ServiceAdapter.notifyDataSetChanged();

        binding.tvNoDataService.setVisibility(View.GONE);

        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getservice(categoryModel.getId() + "")
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        serviceModelList.addAll(response.body().getData());
                                        ServiceAdapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoDataService.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                binding.tvNoDataService.setVisibility(View.VISIBLE);

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            binding.tvNoDataService.setVisibility(View.VISIBLE);

                            switch (response.code()) {
                                case 500:
                                    //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            binding.tvNoDataService.setVisibility(View.VISIBLE);
//                            binding.arrow.setVisibility(View.VISIBLE);
//
//                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //  Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public void showService(ServiceModel serviceModel) {
        req=1;
        Intent intent = new Intent(this, ServiceDetialsActivity.class);
        intent.putExtra("data", serviceModel);
        launcher.launch(intent);
    }
}