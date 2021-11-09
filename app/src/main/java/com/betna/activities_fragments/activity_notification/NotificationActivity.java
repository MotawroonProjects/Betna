package com.betna.activities_fragments.activity_notification;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.betna.R;
import com.betna.adapters.NotificationAdapter;
import com.betna.databinding.ActivityNotificationBinding;
import com.betna.language.Language;
import com.betna.models.NotificationDataModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private String lang;
    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private NotificationAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
    }


    private void initView() {
        notificationModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationModelList, this);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getNotifications);


        binding.llBack.setOnClickListener(view -> finish());
        getNotifications();
    }

    private void getNotifications() {
        Log.e("dlkkdkdk",userModel.getUser().getId()+"");
        binding.swipeRefresh.setRefreshing(false);
        try {
            binding.progBar.setVisibility(View.VISIBLE);

            if (userModel == null) {
                binding.progBar.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);

                return;
            }
            Api.getService(Tags.base_url)
                    .getNotifications(userModel.getUser().getId(),0)
                    .enqueue(new Callback<NotificationDataModel>() {
                        @Override
                        public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null) {
                               // Log.e("suuuu",response.body().getStatus()+"");
                                if (response.body().getStatus() == 200) {
                                    //Log.e("llll",response.body().getData().size()+"");
                                    if (response.body().getData().size() > 0) {
                                        notificationModelList.clear();
                                        notificationModelList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        notificationModelList.clear();
                                        adapter.notifyDataSetChanged();
                                        binding.tvNoData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                }
                            } else {
                                binding.progBar.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (response.code() == 500) {


                                } else {

                                    try {

                                        Log.e("errorsss", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.swipeRefresh.setRefreshing(false);

                                if (t.getMessage() != null) {
                                    Log.e("errorsss", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    } else {
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

//    public void setItemData(NotificationModel model) {
//
//    }


}
