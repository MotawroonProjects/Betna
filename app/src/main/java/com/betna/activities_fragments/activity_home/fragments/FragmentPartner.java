package com.betna.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.betna.R;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.betna.activities_fragments.activity_update_order.UpdateOrderActivity;
import com.betna.adapters.OrderAdapter;
import com.betna.adapters.PartnerAdapter;
import com.betna.databinding.FragmentOrdersBinding;
import com.betna.databinding.FragmentPartnerBinding;
import com.betna.models.OrderDataModel;
import com.betna.models.OrderModel;
import com.betna.models.PartnerDataModel;
import com.betna.models.PartnerModel;
import com.betna.models.StatusResponse;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPartner extends Fragment {

    private HomeActivity activity;
    private FragmentPartnerBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private List<PartnerModel> list;
    private PartnerAdapter adapter;
    private String lang;


    public static FragmentPartner newInstance() {

        return new FragmentPartner();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_partner, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        list = new ArrayList<>();
        adapter = new PartnerAdapter(list, activity);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getData);
        getData();

    }

    public void getData() {
        binding.swipeRefresh.setRefreshing(true);

        binding.llNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();

        Api.getService(Tags.base_url).getPartner()
                .enqueue(new Callback<PartnerDataModel>() {
                    @Override
                    public void onResponse(Call<PartnerDataModel> call, Response<PartnerDataModel> response) {
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    binding.llNoData.setVisibility(View.GONE);

                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    binding.llNoData.setVisibility(View.VISIBLE);

                                }
                            } else {
                                binding.llNoData.setVisibility(View.VISIBLE);

                            }

                        } else {


                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PartnerDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);
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


}