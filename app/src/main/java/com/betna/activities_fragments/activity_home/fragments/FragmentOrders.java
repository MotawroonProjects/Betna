package com.betna.activities_fragments.activity_home.fragments;

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

import com.betna.adapters.OrderAdapter;
import com.betna.databinding.FragmentOrdersBinding;

import com.betna.models.OrderDataModel;
import com.betna.models.OrderModel;
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

public class FragmentOrders extends Fragment {

    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private List<OrderModel> list;
    private OrderAdapter adapter;
    private String lang;


    public static FragmentOrders newInstance() {

        return new FragmentOrders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        adapter = new OrderAdapter(list, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getData);
        getData();
        binding.btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayFragmentDepartments();
            }
        });
    }

    public void getData() {

        binding.progBar.setVisibility(View.VISIBLE);
        binding.llNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        if (userModel == null) {
            binding.swipeRefresh.setRefreshing(false);
            binding.progBar.setVisibility(View.GONE);
            binding.llNoData.setVisibility(View.VISIBLE);
            return;
        }

        Api.getService(Tags.base_url).getOrders(userModel.getUser().getId()).
                enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getCurrent() != null && response.body().getStatus() == 200) {
                                if (response.body().getCurrent().size() > 0) {
                                    binding.llNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getCurrent());
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
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.progBar.setVisibility(View.GONE);
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