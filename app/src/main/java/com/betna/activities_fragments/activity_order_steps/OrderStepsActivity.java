package com.betna.activities_fragments.activity_order_steps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.betna.R;
import com.betna.databinding.ActivityOrderStepsBinding;
import com.betna.interfaces.Listeners;
import com.betna.language.Language;
import com.betna.models.NotFireModel;
import com.betna.models.RateDataModel;
import com.betna.models.RateModel;
import com.betna.models.SingleOrderModel;
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

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStepsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderStepsBinding binding;
    private String lang;
    private SingleOrderModel orderModel;
    private int order_id;
    private Animation animation;
    private int rate;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_steps);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getIntExtra("order_id", 0);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(NotFireModel notFireModel) {
        if (notFireModel.getId() == order_id) {
            getOrderById();

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            EventBus.getDefault().register(this);

        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        // binding.setModel(orderModel);
        getOrderById();
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
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
        binding.imageCloseSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSheet();
            }
        });
        binding.btnOpenRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSheet();
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


    public void openSheet() {
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

    private void getOrderById() {
        try {

            Api.getService(Tags.base_url)
                    .getOrderById(order_id)
                    .enqueue(new Callback<SingleOrderModel>() {
                        @Override
                        public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    orderModel = response.body();
                                    binding.scrollView.setVisibility(View.VISIBLE);
                                    updateOrderStatus();
                                } else {
                                    Toast.makeText(OrderStepsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                binding.progBar.setVisibility(View.GONE);

                                if (response.code() == 500) {
                                    Toast.makeText(OrderStepsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(OrderStepsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        // Toast.makeText(OrderStepsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(OrderStepsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void updateOrderStatus() {
        if (orderModel.getData().getStatus().equals("sent")) {
            updateUi1();
        } else if (orderModel.getData().getStatus().equals("accept")) {
            updateUi2();
        } else if (orderModel.getData().getStatus().equals("start")) {
            updateUi3();
        } else if (orderModel.getData().getStatus().equals("waiting")) {
            updateUi4();

        } else if (orderModel.getData().getStatus().equals("end")) {
            updateUi5();

        }
    }


    private void updateUi1() {
        binding.tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.tv2.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv3.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv4.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv5.setTextColor(getResources().getColor(R.color.gray4));
        binding.tvOrder1.setVisibility(View.VISIBLE);
        binding.tvOrder2.setVisibility(View.GONE);
        binding.tvOrder3.setVisibility(View.GONE);
        binding.tvOrder4.setVisibility(View.GONE);
        binding.tvOrder5.setVisibility(View.GONE);
        binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image4.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image5.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.view4.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.btnOpenRate.setVisibility(View.GONE);
    }

    private void updateUi2() {
        binding.tv1.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.tv3.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv4.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv5.setTextColor(getResources().getColor(R.color.gray4));
        binding.tvOrder1.setVisibility(View.GONE);
        binding.tvOrder2.setVisibility(View.VISIBLE);
        binding.tvOrder3.setVisibility(View.GONE);
        binding.tvOrder4.setVisibility(View.GONE);
        binding.tvOrder5.setVisibility(View.GONE);
        binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image4.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image5.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.view4.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.btnOpenRate.setVisibility(View.GONE);

    }

    private void updateUi3() {
        binding.tv1.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv2.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.tv4.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv5.setTextColor(getResources().getColor(R.color.gray4));
        binding.tvOrder1.setVisibility(View.GONE);
        binding.tvOrder2.setVisibility(View.GONE);
        binding.tvOrder3.setVisibility(View.VISIBLE);
        binding.tvOrder4.setVisibility(View.GONE);
        binding.tvOrder5.setVisibility(View.GONE);
        binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image4.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.image5.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view4.setBackgroundColor(getResources().getColor(R.color.gray4));
        binding.btnOpenRate.setVisibility(View.GONE);


    }

    private void updateUi4() {
        binding.tv1.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv2.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv3.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.tv5.setTextColor(getResources().getColor(R.color.gray4));
        binding.tvOrder1.setVisibility(View.GONE);
        binding.tvOrder2.setVisibility(View.GONE);
        binding.tvOrder3.setVisibility(View.GONE);
        binding.tvOrder4.setVisibility(View.VISIBLE);
        binding.tvOrder5.setVisibility(View.GONE);
        binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image4.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image5.setColorFilter(ContextCompat.getColor(this, R.color.gray4), PorterDuff.Mode.SRC_IN);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.btnOpenRate.setVisibility(View.GONE);


    }

    private void updateUi5() {
        binding.tv1.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv2.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv3.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv4.setTextColor(getResources().getColor(R.color.gray4));
        binding.tv5.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.tvOrder1.setVisibility(View.GONE);
        binding.tvOrder2.setVisibility(View.GONE);
        binding.tvOrder3.setVisibility(View.GONE);
        binding.tvOrder4.setVisibility(View.GONE);
        binding.tvOrder5.setVisibility(View.VISIBLE);
        binding.image1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image3.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image4.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.image5.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.view4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.btnOpenRate.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void back() {
        finish();
    }

    private void addRateOffer(String comment, int rate) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .addRate(userModel.getUser().getId() + "", orderModel.getData().getService().getId() + "", comment, rate)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                //getOrderById();
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