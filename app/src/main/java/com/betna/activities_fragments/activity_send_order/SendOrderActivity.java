package com.betna.activities_fragments.activity_send_order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betna.R;
import com.betna.activities_fragments.activity_complete_order.CompleteOrderActivity;
import com.betna.activities_fragments.activity_login.LoginActivity;
import com.betna.adapters.PreWorkAdapter;
import com.betna.adapters.Rate2Adapter;
import com.betna.adapters.RateAdapter;
import com.betna.adapters.TypeAdapter;
import com.betna.databinding.ActivitySendOrderBinding;
import com.betna.databinding.ActivityServiceDetialsBinding;
import com.betna.interfaces.Listeners;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.RateModel;
import com.betna.models.ServiceDataModel;
import com.betna.models.ServiceModel;
import com.betna.models.SingleServiceDataModel;
import com.betna.models.TypeDataModel;
import com.betna.models.TypeModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderActivity extends AppCompatActivity implements Listeners.BackListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivitySendOrderBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private ServiceModel serviceModel;
    private String type;
    private List<TypeModel> typeModelList;
    private TypeAdapter typeAdapter;
    private int progress;
    private DatePickerDialog datePickerDialog;
    private String date;
    private AddServiceModel addServiceModel;
    private double lat = 0.0, lng = 0.0;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_order);
        getDataFromIntent();
        initView();

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        serviceModel = (ServiceModel) intent.getSerializableExtra("data");

    }

    public void getType() {
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getTYpe()
                .enqueue(new Callback<TypeDataModel>() {
                    @Override
                    public void onResponse(Call<TypeDataModel> call, Response<TypeDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        binding.scroll.setVisibility(View.VISIBLE);
                                        binding.fltotal.setVisibility(View.VISIBLE);
                                        updateData(response.body());
                                    }
                                }
                            } else {

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    //   Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //   Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<TypeDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

//                            binding.arrow.setVisibility(View.VISIBLE);
//
//                            binding.progBar.setVisibility(View.GONE);
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

    private void updateData(TypeDataModel body) {
        typeModelList.clear();
        TypeModel typeModel = body.getData().get(0);
        typeModel.setSelected(true);
        typeModelList.addAll(body.getData());
        typeModelList.set(0, typeModel);
        addServiceModel.setType_id(typeModel.getId());
        typeAdapter.notifyDataSetChanged();
    }


    private void initView() {
        addServiceModel = new AddServiceModel();
        typeModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(serviceModel);
        binding.progressBar.incrementProgressBy(10);
        typeAdapter = new TypeAdapter(typeModelList, this);
        binding.recviewtype.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        binding.recviewtype.setAdapter(typeAdapter);
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.progressBarinsideText.setText(seekBar.getProgress() + "");
                binding.progressBar.setTooltipText(seekBar.getProgress() + "");
                progress = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        getType();
        binding.tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "");
            }
        });
        binding.tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "");
            }
        });
        binding.btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServiceModel.setLatitude(lat + "");
                addServiceModel.setLongitude(lng + "");
                addServiceModel.setArea(progress+"");
                addServiceModel.setNotes(binding.edtNote.getText().toString());

                if(!addServiceModel.getNotes().isEmpty()){
                if(userModel!=null){
                Intent intent = new Intent(SendOrderActivity.this, CompleteOrderActivity.class);
                intent.putExtra("data", addServiceModel);
                startActivity(intent);}
                else{
                    Intent intent = new Intent(SendOrderActivity.this, LoginActivity.class);
                    intent.putExtra("data", addServiceModel);
                    startActivity(intent);
                }

            }
            else {
                binding.edtNote.setError(getResources().getString(R.string.field_req));
                }
            }

        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        date = dateFormat.format(new Date(System.currentTimeMillis()));
        binding.tvDay.setText(date);

        createDateDialog();
        updateData();
       // CheckPermission();

    }

    private void updateData() {
        addServiceModel.setService_id(serviceModel.getId());
        addServiceModel.setTitle(serviceModel.getTitle());
        addServiceModel.setDate(date);
        addServiceModel.setTotal(serviceModel.getPrice()+"");
    }

    private void createDateDialog() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOkText(this.getString(R.string.select));
        datePickerDialog.setCancelText(this.getString(R.string.cancel));
        datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    @Override
    public void back() {
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void setselection(TypeModel specialModel) {
        type = specialModel.getId() + "";
        addServiceModel.setType_id(specialModel.getId());

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        binding.tvDay.setText(date);
        addServiceModel.setDate(date);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(SendOrderActivity.this, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();


        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            if (locationCallback != null) {
                LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            startLocationUpdate();
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }

    }
}
