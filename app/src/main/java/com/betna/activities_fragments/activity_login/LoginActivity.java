package com.betna.activities_fragments.activity_login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.view.animation.LinearInterpolator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.betna.R;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.betna.databinding.ActivityLoginBinding;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.LoginModel;
import com.betna.preferences.Preferences;
import com.betna.share.Common;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String lang;
    private LoginModel loginModel;
    private Preferences preferences;

    private String phone_code = "+20";
    private AddServiceModel addServiceModel;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            addServiceModel = (AddServiceModel) intent.getSerializableExtra("data");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getDataFromIntent();
        initView();
    }

    private void initView() {

        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        loginModel = new LoginModel();
        binding.setModel(loginModel);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });

        binding.btnLogin.setOnClickListener(view -> {
            //   navigateToHomeActivity();
            if (loginModel.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtPhone);
                login();
            }
        });

        binding.llBack.setOnClickListener(v -> {
            finish();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }


    private void login() {
        navigateToConfirmCode();


    }

    private void navigateToConfirmCode() {
        req = 1;
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("phone_code", loginModel.getPhone_code());
        intent.putExtra("phone", loginModel.getPhone());
        intent.putExtra("data", addServiceModel);
        launcher.launch(intent);

    }


}