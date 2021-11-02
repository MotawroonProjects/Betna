package com.betna.activities_fragments.activity_previousorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.betna.R;
import com.betna.adapters.PreviousOrderAdapter;
import com.betna.databinding.ActivityPreviousorderBinding;
import com.betna.language.Language;
import com.betna.models.OrderDataModel;
import com.betna.models.OrderModel;
import com.betna.models.StatusResponse;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousOrderActivity extends AppCompatActivity {
    private ActivityPreviousorderBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    private List<OrderModel> list;
    private PreviousOrderAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previousorder);
        initView();
    }


    private void initView() {

        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
          //  EventBus.getDefault().register(this);

        }
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        // binding.progBar.setVisibility(View.GONE);
        adapter = new PreviousOrderAdapter(list, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        binding.swipeRefresh.setOnRefreshListener(this::getData);

    }

    public void getData() {
        userModel = preferences.getUserData(this);

        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        if (userModel == null) {
            binding.swipeRefresh.setRefreshing(false);
            binding.progBar.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }

        Api.getService(Tags.base_url).getOrders(userModel.getUser().getId()).
                enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        // Log.e("error_code", response.body().getStatus() + "_");

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getOld() != null && response.body().getStatus() == 200) {
                                if (response.body().getOld().size() > 0) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.addAll(response.body().getOld());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

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

    public void show(OrderModel orderModel) {

    }




    @Override
    public void onBackPressed() {
        finish();
    }


}