package com.betna.activities_fragments.activity_about_us;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.betna.R;
import com.betna.databinding.ActivityAboutUsBinding;
import com.betna.language.Language;
import com.betna.models.SettingDataModel;
import com.betna.remote.Api;
import com.betna.tags.Tags;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {
    private ActivityAboutUsBinding binding;
    private String url = "";
    private String lang;
    private int type;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type=intent.getIntExtra("type",1);

    }

    private void initView() {
        if(type == 2){
            binding.tvtitle.setText(getResources().getString(R.string.terms_and_conditions));
            url="";
        }
        else {
            url="";
            binding.tvtitle.setText(getResources().getString(R.string.about_app));

        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(v -> {

            finish();
        });
        //createDialogAlert();
//        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        binding.webView.getSettings().setAllowContentAccess(true);
//        binding.webView.getSettings().setAllowFileAccess(true);
//        binding.webView.getSettings().setBuiltInZoomControls(false);
//        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setLoadWithOverviewMode(true);
//        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//        binding.webView.loadUrl(url);
//
//        binding.webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//
//            }
//
//            @Override
//            public void onPageCommitVisible(WebView view, String url) {
//                super.onPageCommitVisible(view, url);
//                binding.progBar.setVisibility(View.GONE);
//
//            }
//
//
//        });
getSetting();

    }
    private void getSetting() {
       binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getSetting()
                .enqueue(new Callback<SettingDataModel>() {
                    @Override
                    public void onResponse(Call<SettingDataModel> call, Response<SettingDataModel> response) {
                     binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                   binding.setModel(response.body().getData());
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SettingDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }


}