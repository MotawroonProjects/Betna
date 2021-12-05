package com.betna.activities_fragments.activity_sign_up;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.betna.R;

import com.betna.activities_fragments.activity_complete_order.CompleteOrderActivity;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.databinding.ActivitySignupBinding;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.SignUpModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
import com.betna.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private String lang;
    private SignUpModel model;
    private ActivityResultLauncher<Intent> launcher;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private int selectedReq = 0;
    private Uri uri = null;
    private String phone_code, phone;
    private Preferences preferences;
    private AddServiceModel addServiceModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
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
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        model = new SignUpModel();
        binding.setModel(model);
        preferences = Preferences.getInstance();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (selectedReq == READ_REQ) {
                    binding.icon.setVisibility(View.GONE);

                    uri = result.getData().getData();
                    File file = new File(Common.getImagePath(this, uri));
                    Picasso.get().load(file).fit().into(binding.image);

                } else if (selectedReq == CAMERA_REQ) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    binding.icon.setVisibility(View.GONE);
                    uri = getUriFromBitmap(bitmap);
                    if (uri != null) {
                        String path = Common.getImagePath(this, uri);

                        if (path != null) {
                            Picasso.get().load(new File(path)).fit().into(binding.image);

                        } else {
                            Picasso.get().load(uri).fit().into(binding.image);

                        }
                    }
                }
            }
        });
        binding.flImage.setOnClickListener(view -> openSheet());
        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });

        binding.btnCancel.setOnClickListener(view -> closeSheet());

        binding.btnSignup.setOnClickListener(v -> {
            // navigateToHomeActivity();
            if (model.isDataValid(this)) {

                // Common.CloseKeyBoard(this, binding.edtCv);
                if (uri == null) {
                    signUpWithoutImage();
                } else {
                    signUpWithImage();
                }
            }
        });
    }

    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }

    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {
        selectedReq = req;
        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            launcher.launch(intent);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    private void signUpWithoutImage() {

        // signUpModel.setPhone(phone);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUp(model.getFirst_name(), model.getSeconed_name(), phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            // Log.e("flkfkfk", response.body().getStatus() + "");
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                preferences.create_update_session(SignUpActivity.this, Tags.session_login);
                                navigateToHomeActivity();
                            } else if (response.body().getStatus() == 409) {
                                Toast.makeText(SignUpActivity.this, R.string.phone_found, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (response.code() == 500) {
                                //    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //  Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    private void signUpWithImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody firstname_part = Common.getRequestBodyText(model.getFirst_name());
        RequestBody secondename_part = Common.getRequestBodyText(model.getSeconed_name());
        RequestBody phone_part = Common.getRequestBodyText(phone);


        MultipartBody.Part logo = Common.getMultiPartImage(this, uri, "photo");


        Api.getService(Tags.base_url)
                .signUpWithImage(firstname_part, secondename_part, phone_part, logo)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {

                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                preferences.create_update_session(SignUpActivity.this, Tags.session_login);
                                navigateToHomeActivity();
                            } else {
                                if (response.body().getStatus() == 409) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.phone_found), Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            Log.e("nnnnnnnnnnnn", response.code() + "");
                            Log.e("555555", response.message());
                            try {
                                Log.e("2222222", response.errorBody().string() + "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                try {
                                    Log.e("msg_category_error", response.errorBody().string() + "__");
                                } catch (Exception e) {
                                    Log.e("aaaaaqqqq", e.toString() + "__");

                                }
                            } else {
                                Log.e("ERROR", response.message() + "");

                                //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToHomeActivity() {
        Intent intent = null;
        if (addServiceModel == null) {
            intent = new Intent(this, HomeActivity.class);
        }
//        } else {
//            intent = new Intent(this, CompleteOrderActivity.class);
//
//        }
        if(intent!=null){
        intent.putExtra("data", addServiceModel);
        startActivity(intent);}
        finish();
    }

}