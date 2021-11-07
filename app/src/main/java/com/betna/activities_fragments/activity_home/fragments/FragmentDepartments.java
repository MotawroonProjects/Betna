package com.betna.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betna.R;
import com.betna.activities_fragments.activity_doctor_detials.ServiceDetialsActivity;
import com.betna.activities_fragments.activity_home.HomeActivity;

import com.betna.adapters.CategoryAdapter;
import com.betna.adapters.ServiceAdapter;
import com.betna.databinding.FragmentCategoriesBinding;
import com.betna.models.CategoryDataModel;
import com.betna.models.CategoryModel;
import com.betna.models.ServiceDataModel;
import com.betna.models.ServiceModel;
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

public class FragmentDepartments extends Fragment {

    private HomeActivity activity;
    private FragmentCategoriesBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<CategoryModel> categoryModelList;
    private CategoryAdapter departmentAdapter;
    private List<ServiceModel> serviceModelList;
    private ServiceAdapter serviceAdapter;
    private String departmentid="0", query;

    public static FragmentDepartments newInstance() {
        return new FragmentDepartments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        categoryModelList = new ArrayList<>();
        serviceModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        departmentAdapter = new CategoryAdapter(categoryModelList, activity, this);
        binding.recViewDepartments.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false));
        binding.recViewDepartments.setAdapter(departmentAdapter);
        serviceAdapter = new ServiceAdapter(serviceModelList, activity, this);
        binding.recViewService.setLayoutManager(new GridLayoutManager(activity, 3));
        binding.recViewService.setAdapter(serviceAdapter);
        getDepartments();
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    query = "";
                } else {


                    query = editable.toString();
                }
                getServices();
            }
        });
        getServices();


    }

    public void getDepartments() {
        binding.tvNoDataDepartments.setVisibility(View.GONE);

        binding.progBarDepartments.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getDepartments()
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        binding.progBarDepartments.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        categoryModelList.add(new CategoryModel(getResources().getString(R.string.all),true));
                                        categoryModelList.addAll(response.body().getData());

                                        departmentAdapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoDataDepartments.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                binding.tvNoDataDepartments.setVisibility(View.VISIBLE);

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            binding.tvNoDataDepartments.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBarDepartments.setVisibility(View.GONE);
                            binding.tvNoDataDepartments.setVisibility(View.VISIBLE);
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

    public void getServices() {
        serviceModelList.clear();
        serviceAdapter.notifyDataSetChanged();
        binding.tvNoDataService.setVisibility(View.GONE);

        binding.progBarService.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .searchService(departmentid, query)
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                        binding.progBarService.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        serviceModelList.addAll(response.body().getData());
                                        serviceAdapter.notifyDataSetChanged();
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
                            binding.progBarService.setVisibility(View.GONE);
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


    public void show(String s) {
        departmentid = s;

        getServices();
    }

    public void showService(ServiceModel serviceModel) {
        Intent intent = new Intent(activity, ServiceDetialsActivity.class);
        intent.putExtra("data", serviceModel);
        startActivity(intent);
    }
}

