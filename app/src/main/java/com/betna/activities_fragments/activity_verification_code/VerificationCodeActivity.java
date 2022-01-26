package com.betna.activities_fragments.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.betna.R;

import com.betna.activities_fragments.activity_complete_order.CompleteOrderActivity;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_login.LoginActivity;
import com.betna.activities_fragments.activity_send_order.SendOrderActivity;
import com.betna.activities_fragments.activity_sign_up.SignUpActivity;
import com.betna.databinding.ActivityVerificationCodeBinding;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;
    private String lang;
    private String phone_code;
    private String phone;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private boolean canSend = false;
    private AddServiceModel addServiceModel;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private int req;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");
            addServiceModel = (AddServiceModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        String phone = phone_code + this.phone;
        binding.setPhone(phone);

        binding.tvResend.setOnClickListener(view -> {
            if (canSend) {
                sendSmsCode();
            }
        });
        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
            login();
//            if (!code.isEmpty()) {
//                binding.edtCode.setError(null);
//                Common.CloseKeyBoard(this, binding.edtCode);
//                checkValidCode(code);
//            } else {
//                binding.edtCode.setError(getString(R.string.field_req));
//            }

        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        sendSmsCode();
        //login();
    }

    private void sendSmsCode() {

        startTimer();

        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                binding.edtCode.setText(smsCode);
                checkValidCode(smsCode);
                Log.e("checkCode", "true");
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                VerificationCodeActivity.this.verificationId = verification_id;
                VerificationCodeActivity.this.forceResendingToken = forceResendingToken;
                Log.e("sent", verification_id);

            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                String msg = e.toString();
                if (msg.contains("We have blocked all requests")) {
                    Toast.makeText(VerificationCodeActivity.this, R.string.number_blocked, Toast.LENGTH_LONG).show();
                    if (timer != null) {
                        timer.onFinish();
                        timer.cancel();
                    }
                }
                Log.e("errorCodeVerification", e.toString());

            }
        };


        PhoneAuthOptions options;
        if (forceResendingToken != null) {
            options = new PhoneAuthOptions.Builder(mAuth)
                    .setCallbacks(mCallBack)
                    .setForceResendingToken(forceResendingToken)
                    .setActivity(this)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setPhoneNumber(phone_code + phone)
                    .build();


        } else {
            options = new PhoneAuthOptions.Builder(mAuth)
                    .setCallbacks(mCallBack)
                    .setActivity(this)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setPhoneNumber(phone_code + phone)
                    .build();

        }
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void startTimer() {
        binding.tvCounter.setVisibility(View.VISIBLE);
        binding.tvResend.setVisibility(View.INVISIBLE);
        canSend = false;
        binding.tvResend.setEnabled(false);
        timer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.tvCounter.setText(time);
            }

            @Override
            public void onFinish() {
                canSend = true;
                binding.tvCounter.setText("00:00");
                binding.tvCounter.setVisibility(View.INVISIBLE);
                binding.tvResend.setEnabled(true);
                binding.tvResend.setVisibility(View.VISIBLE);
            }
        };
        timer.start();
    }

    private void checkValidCode(String code) {

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {

                        login();
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                    // Common.CreateDialogAlert(this, e.getMessage());
                } else {
                    //       Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Toast.makeText(this, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }

    private void login() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {


                                navigateToHomeActivity(response.body());

                            } else if (response.body().getStatus() == 404) {
                                navigateToSignUpActivity();
                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                //    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    private void navigateToHomeActivity(UserModel body) {
        preferences.create_update_userdata(VerificationCodeActivity.this, body);
        preferences.create_update_session(VerificationCodeActivity.this, Tags.session_login);
      /*  Intent intent = null;
        if (addServiceModel == null) {
            intent = new Intent(this, HomeActivity.class);

        }

        if (intent != null) {
            // intent.putExtra("data", addServiceModel);
            startActivity(intent);
        } else {
            intent = getIntent();
            setResult(RESULT_OK, intent);
            //  finish();
        }*/

        setResult(RESULT_OK);
        finish();

    }

    private void navigateToSignUpActivity() {
        req = 1;
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("phone_code", phone_code);
        intent.putExtra("data", addServiceModel);
        launcher.launch(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


}
