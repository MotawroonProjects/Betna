package com.betna.activities_fragments.activity_send_order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.activities_fragments.activity_login.LoginActivity;
import com.betna.adapters.PreWorkAdapter;
import com.betna.adapters.Rate2Adapter;
import com.betna.adapters.RateAdapter;
import com.betna.adapters.SpinnerCityAdapter;
import com.betna.adapters.SpinnerGoveAdapter;
import com.betna.adapters.SubTypeAdapter;
import com.betna.adapters.TypeAdapter;
import com.betna.databinding.ActivitySendOrderBinding;
import com.betna.databinding.ActivityServiceDetialsBinding;
import com.betna.interfaces.Listeners;
import com.betna.language.Language;
import com.betna.models.AddServiceModel;
import com.betna.models.Cities_Model;
import com.betna.models.Governate_Model;
import com.betna.models.RateModel;
import com.betna.models.ServiceDataModel;
import com.betna.models.ServiceModel;
import com.betna.models.SingleServiceDataModel;
import com.betna.models.StatusResponse;
import com.betna.models.SubTypeDataModel;
import com.betna.models.SubTypeModel;
import com.betna.models.TypeDataModel;
import com.betna.models.TypeModel;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;
import com.betna.remote.Api;
import com.betna.share.Common;
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
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
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
    private List<SubTypeModel> subTypeModelList;
    private SubTypeAdapter subTypeAdapter;
    private List<Governate_Model.Data> dataList;
    private SpinnerGoveAdapter adapter;
    private SpinnerCityAdapter spinnerCityAdapter;
    private List<Cities_Model.Data> cityList;
    private ActivityResultLauncher<Intent> launcher;

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
    private List<Integer> ids;

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

    public void getType(int type) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        subTypeModelList.clear();
        subTypeAdapter.notifyDataSetChanged();
        Api.getService(Tags.base_url)
                .getSubTYpe(type)
                .enqueue(new Callback<SubTypeDataModel>() {
                    @Override
                    public void onResponse(Call<SubTypeDataModel> call, Response<SubTypeDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    subTypeModelList.clear();
                                    subTypeModelList.addAll(response.body().getData());
                                    subTypeAdapter.notifyDataSetChanged();
                                }
                            } else {

                                //  Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {


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
                    public void onFailure(Call<SubTypeDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Calendar endDate = Calendar.getInstance(TimeZone.getDefault());
        endDate.add(Calendar.MONTH, 1);
        Calendar startDate = Calendar.getInstance(TimeZone.getDefault());
        // startDate.add(Calendar.MONTH, -1);
        date = dateFormat.format(new Date(startDate.getTimeInMillis()));

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)

                .range(startDate, endDate).datesNumberOnScreen(5)

                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar dates, int position) {
                // on below line we are printing date
                // in the logcat which is selected.
                //  Log.e("TAG", "CURRENT DATE IS " + date.getTime());
                date = dateFormat.format(new Date(dates.getTimeInMillis()));


            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            userModel=preferences.getUserData(this);
              sendorder();

        });

        addServiceModel = new AddServiceModel();
        typeModelList = new ArrayList<>();
        subTypeModelList = new ArrayList<>();
        dataList = new ArrayList<>();
        cityList = new ArrayList<>();
        ids = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(serviceModel);
        binding.progressBar.incrementProgressBy(10);
        typeAdapter = new TypeAdapter(typeModelList, this);
        binding.recviewtype.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        binding.recviewtype.setAdapter(typeAdapter);
        subTypeAdapter = new SubTypeAdapter(subTypeModelList, this);
        binding.recviewsubtype.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        binding.recviewsubtype.setAdapter(subTypeAdapter);
        adapter = new SpinnerGoveAdapter(dataList, this);
        spinnerCityAdapter = new SpinnerCityAdapter(cityList, this);
        binding.spGover.setAdapter(adapter);
        binding.spCity.setAdapter(spinnerCityAdapter);
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

//        binding.tvDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePickerDialog.show(getFragmentManager(), "");
//            }
//        });
        binding.spGover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    addServiceModel.setGovernorate_id(0);
                    binding.tvtitle.setText(getResources().getString(R.string.fees));

                } else {
                    addServiceModel.setGovernorate_id(dataList.get(i).getId());
                    binding.tvtitle.setText(getResources().getString(R.string.fees)+dataList.get(i).getPrice());
                    getCities(dataList.get(i).getId());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    addServiceModel.setCity_id(0);
                } else {
                    addServiceModel.setCity_id(cityList.get(i).getId());
                    if(cityList.get(i).getPrice()>0){
                        binding.tvtitle.setText(getResources().getString(R.string.fees)+cityList.get(i).getPrice());
                    }
                    else{
                        binding.tvtitle.setText(getResources().getString(R.string.fees)+dataList.get(binding.spGover.getSelectedItemPosition()).getPrice());

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServiceModel.setLatitude(lat + "");
                addServiceModel.setLongitude(lng + "");
                addServiceModel.setArea(progress + "");
                addServiceModel.setNotes(binding.edtNote.getText().toString());
                addServiceModel.setAddress(binding.edtAddress.getText().toString());

                if (!addServiceModel.getNotes().isEmpty() && !addServiceModel.getAddress().isEmpty() && addServiceModel.getGovernorate_id() != 0 && addServiceModel.getCity_id() != 0&&ids.size()>0) {
                    if (userModel != null) {
                        sendorder();
//                        Intent intent = new Intent(SendOrderActivity.this, CompleteOrderActivity.class);
//                        intent.putExtra("data", addServiceModel);
//                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SendOrderActivity.this, LoginActivity.class);
                        intent.putExtra("data", addServiceModel);
                       launcher.launch(intent);
                    }

                } else {
                    if (addServiceModel.getNotes().isEmpty()) {
                        binding.edtNote.setError(getResources().getString(R.string.field_req));
                    }
                    if (addServiceModel.getAddress().isEmpty()) {
                        binding.edtAddress.setError(getResources().getString(R.string.field_req));
                    }
                    if (addServiceModel.getGovernorate_id() == 0) {
                        Toast.makeText(SendOrderActivity.this, getResources().getString(R.string.ch_gover), Toast.LENGTH_LONG).show();
                    }
                    if (addServiceModel.getCity_id() == 0) {
                        Toast.makeText(SendOrderActivity.this, getResources().getString(R.string.ch_city), Toast.LENGTH_LONG).show();

                    }
                    if(ids.size()==0){
                        Toast.makeText(SendOrderActivity.this,getResources().getString(R.string.ch_sub),Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        createDateDialog();
        updateData();
        getGovernates();
        // CheckPermission();

    }

    private void updateData() {
        addServiceModel.setService_id(serviceModel.getId());
        addServiceModel.setTitle(serviceModel.getTitle());
        addServiceModel.setDate(date);
        addServiceModel.setTotal(serviceModel.getPrice() + "");
    }

    private void getGovernates() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getGovernates()
                    .enqueue(new Callback<Governate_Model>() {
                        @Override
                        public void onResponse(Call<Governate_Model> call, Response<Governate_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getData() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    // Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 422) {
                                    //  Common.CreateAlertDialog(activity,getResources().getString(R.string.em_exist));
                                } else {
                                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Governate_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //  Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void updateCityAdapter(Cities_Model body) {
        binding.flCity.setVisibility(View.VISIBLE);

        cityList.clear();
        cityList.add(new Cities_Model.Data("إختر المدينه", "Choose City"));
        cityList.addAll(body.getData());
        spinnerCityAdapter.notifyDataSetChanged();
    }

    private void getCities(int governateid) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getCities(governateid)
                    .enqueue(new Callback<Cities_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Model> call, Response<Cities_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getData() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    // Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 422) {
                                    //  Common.CreateAlertDialog(activity,getResources().getString(R.string.em_exist));
                                } else {
                                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //  Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void updateCityAdapter(Governate_Model body) {

        dataList.add(new Governate_Model.Data("إختر المحافظه", "Choose Governate"));
        if (body.getData() != null) {
            dataList.addAll(body.getData());
            adapter.notifyDataSetChanged();
        }
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
        ids.clear();
        type = specialModel.getId() + "";
        addServiceModel.setType_id(specialModel.getId());
        getType(specialModel.getId());

    }

    public void setsubselection(SubTypeModel specialModel) {
        //  type = specialModel.getId() + "";
        // addServiceModel.setType_id(specialModel.getId());
        if (!ids.contains(specialModel.getId())) {
            ids.add(specialModel.getId());
        } else {
            if (!specialModel.isSelected()) {
                ids.remove(ids.indexOf(specialModel.getId()));
            }
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        // binding.tvDay.setText(date);
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

    private void sendorder() {
        Log.e("lllll",ids.toString());
        //Log.e("mddmmd", serviceModel.getArea() + " " + serviceModel.getNotes() + " " + serviceModel.getService_id() + " " + serviceModel.getType_id() + "   " + serviceModel.getDate() + "   " + serviceModel.getLatitude() + " " + serviceModel.getLongitude() + " " + serviceModel.getTotal());
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .storeOrder(userModel.getUser().getId() + " ", addServiceModel.getService_id() + " ", addServiceModel.getType_id() + " ", addServiceModel.getArea(), addServiceModel.getLongitude(), addServiceModel.getLatitude(), addServiceModel.getNotes(), addServiceModel.getTotal(), addServiceModel.getDate(), addServiceModel.getAddress(), addServiceModel.getGovernorate_id() + "", addServiceModel.getCity_id() + "",ids)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        //    Log.e("ldkkf", response.body().getStatus() + " " + response.code());
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                Intent intent = new Intent(SendOrderActivity.this, HomeActivity.class);
                                intent.putExtra("type", "order");
                                startActivity(intent);
                                finishAffinity();


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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
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

}
