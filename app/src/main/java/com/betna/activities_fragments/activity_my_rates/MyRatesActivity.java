package com.betna.activities_fragments.activity_my_rates;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.betna.R;
import com.betna.adapters.PreviousOrderAdapter;
import com.betna.adapters.RateAdapter;
import com.betna.databinding.ActivityMyRatesBinding;
import com.betna.databinding.ActivityPreviousorderBinding;
import com.betna.language.Language;
import com.betna.models.RateDataModel;
import com.betna.models.OrderModel;
import com.betna.models.RateDataModel;
import com.betna.models.RateModel;
import com.betna.models.StatusResponse;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRatesActivity extends AppCompatActivity {
    private ActivityMyRatesBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<RateModel> list;
    private RateAdapter adapter;
    private Animation animation;
    private RateModel rateModel;
    private int rate;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_rates);
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
        adapter = new RateAdapter(list, this);
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
        binding.imageCloseSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSheet();
            }
        });
        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = binding.edtDesc.getText().toString();
                if (desc.trim().isEmpty()) {
                    binding.edtDesc.setError(getResources().getString(R.string.field_req));
                } else {
                    addRateOffer(desc, rate);
                }
            }
        });
        binding.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 1;
                updateui1();
            }
        });

        binding.fl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 2;
                updateui2();
            }
        });
        binding.fl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 3;
                updateui3();
            }
        });
        binding.fl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 4;
                updateui4();
            }
        });
        binding.fl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 5;
                updateui5();
            }
        });
        rate = 1;
        updateui1();
    }

    private void updateui1() {
        binding.fl.setBackground(getResources().getDrawable(R.drawable.rounded_strock_primary));
        binding.imagestar.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.tvStar.setTextColor(getResources().getColor(R.color.colorPrimary));

        binding.fl1.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar1.setTextColor(getResources().getColor(R.color.black));

        binding.fl2.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar2.setTextColor(getResources().getColor(R.color.black));

        binding.fl3.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar3.setTextColor(getResources().getColor(R.color.black));

        binding.fl4.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar4.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar4.setTextColor(getResources().getColor(R.color.black));
    }

    private void updateui2() {
        binding.fl1.setBackground(getResources().getDrawable(R.drawable.rounded_strock_primary));
        binding.imagestar1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.tvStar1.setTextColor(getResources().getColor(R.color.colorPrimary));

        binding.fl.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar.setTextColor(getResources().getColor(R.color.black));

        binding.fl2.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar2.setTextColor(getResources().getColor(R.color.black));

        binding.fl3.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar3.setTextColor(getResources().getColor(R.color.black));

        binding.fl4.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar4.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar4.setTextColor(getResources().getColor(R.color.black));
    }

    private void updateui3() {
        binding.fl2.setBackground(getResources().getDrawable(R.drawable.rounded_strock_primary));
        binding.imagestar2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.tvStar2.setTextColor(getResources().getColor(R.color.colorPrimary));

        binding.fl1.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar1.setTextColor(getResources().getColor(R.color.black));

        binding.fl.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar.setTextColor(getResources().getColor(R.color.black));

        binding.fl3.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar3.setTextColor(getResources().getColor(R.color.black));

        binding.fl4.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar4.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar4.setTextColor(getResources().getColor(R.color.black));
    }

    private void updateui4() {
        binding.fl3.setBackground(getResources().getDrawable(R.drawable.rounded_strock_primary));
        binding.imagestar3.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.tvStar3.setTextColor(getResources().getColor(R.color.colorPrimary));

        binding.fl1.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar1.setTextColor(getResources().getColor(R.color.black));

        binding.fl2.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar2.setTextColor(getResources().getColor(R.color.black));

        binding.fl.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar.setTextColor(getResources().getColor(R.color.black));

        binding.fl4.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar4.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar4.setTextColor(getResources().getColor(R.color.black));
    }

    private void updateui5() {
        binding.fl4.setBackground(getResources().getDrawable(R.drawable.rounded_strock_primary));
        binding.imagestar4.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.tvStar4.setTextColor(getResources().getColor(R.color.colorPrimary));

        binding.fl1.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar1.setTextColor(getResources().getColor(R.color.black));

        binding.fl2.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar2.setTextColor(getResources().getColor(R.color.black));

        binding.fl3.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar3.setTextColor(getResources().getColor(R.color.black));

        binding.fl.setBackground(getResources().getDrawable(R.drawable.rounded_strock_gray7));
        binding.imagestar.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        binding.tvStar.setTextColor(getResources().getColor(R.color.black));
    }

    public void getData() {
        userModel = preferences.getUserData(this);

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

        Api.getService(Tags.base_url).getRates(userModel.getUser().getId()).
                enqueue(new Callback<RateDataModel>() {
                    @Override
                    public void onResponse(Call<RateDataModel> call, Response<RateDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        // Log.e("error_code", response.body().getStatus() + "_");

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
                    public void onFailure(Call<RateDataModel> call, Throwable t) {
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

    public void openSheet(RateModel rateModel) {
        this.rateModel = rateModel;
        binding.edtDesc.setText(rateModel.getDesc());
        if(rateModel.getRate()==1){
            updateui1();
        }
        else  if(rateModel.getRate()==2){
            updateui2();
        }
        else  if(rateModel.getRate()==3){
            updateui3();
        }
        else  if(rateModel.getRate()==4){
            updateui4();
        }
        else  if(rateModel.getRate()==5){
            updateui5();
        }
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.flSheet.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flSheet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet() {
        binding.flSheet.clearAnimation();
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        binding.flSheet.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flSheet.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void addRateOffer(String comment, int rate) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .reRate(rateModel.getId()+"",userModel.getUser().getId() + "", rateModel.getService().getId() + "", comment, rate)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                       // Log.e("dkkd",response.body().getStatus()+"");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getData();
                                closeSheet();
                            }

                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

}