package com.betna.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.betna.R;

import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_login.LoginActivity;
import com.betna.databinding.ActivitySplashBinding;
import com.betna.language.Language;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        new Handler()
                .postDelayed(() -> {

                        Intent intent;


                            intent = new Intent(this, HomeActivity.class);

                        startActivity(intent);
                        finish();
                    }
                , 2000);

    }
}
