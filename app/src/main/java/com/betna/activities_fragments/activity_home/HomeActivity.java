package com.betna.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.betna.R;
import com.betna.activities_fragments.activity_home.fragments.FragmentDepartments;
import com.betna.activities_fragments.activity_home.fragments.FragmentOrders;
import com.betna.activities_fragments.activity_home.fragments.Fragment_Home;
import com.betna.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.betna.activities_fragments.activity_login.LoginActivity;
import com.betna.databinding.ActivityHomeBinding;
import com.betna.language.Language;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private FragmentDepartments fragmentDepartments;
    private FragmentOrders fragmentOrders;

    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private boolean backPressed = false;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
       
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");

      
        displayFragmentMain();

        if (userModel != null) {
            //   updateFirebaseToken();
        }
        setUpBottomNavigation();

    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_category);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_order);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);

        updateBottomNavigationPosition(0);

        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {

            switch (position) {
                case 0:
                    displayFragmentMain();
                    break;
                case 1:

                    displayFragmentDepartments();


                    break;
                case 2:
                    if(userModel!=null){
                    displayFragmentOrders();}
                    else {
                        navigateToSignInActivity();
                    }
                    break;
                case 3:
                    displayFragmentProfile();
                    break;

            }
            return false;
        });


    }

    private void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);
    }



    public void displayFragmentMain() {
        try {
            if (fragment_home == null) {
                fragment_home = Fragment_Home.newInstance();
            }


            if (fragmentOrders != null && fragmentOrders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentOrders).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragmentDepartments != null && fragmentDepartments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentDepartments).commit();
            }
           

            if (fragment_home.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_home).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").commit();

            }
            updateBottomNavigationPosition(0);
            //   binding.setTitle(getString(R.string.home));

        } catch (Exception e) {
        }

    }


    public void displayFragmentOrders() {
        try {


            if (fragmentOrders == null) {
                fragmentOrders = FragmentOrders.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragmentDepartments != null && fragmentDepartments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentDepartments).commit();
            }
           

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }


            if (fragmentOrders.isAdded()) {
                fragmentManager.beginTransaction().show(fragmentOrders).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentOrders, "fragment_offer").commit();

            }
            updateBottomNavigationPosition(2);

            // binding.setTitle(getString(R.string.offers));
        } catch (Exception e) {
        }
    }

    public void displayFragmentDepartments() {
        try {
            if (fragmentDepartments == null) {
                fragmentDepartments = FragmentDepartments.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragmentOrders != null && fragmentOrders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentOrders).commit();
            }
           
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }


            if (fragmentDepartments.isAdded()) {
                fragmentManager.beginTransaction().show(fragmentDepartments).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentDepartments, "fragmentCart").addToBackStack("fragmentCart").commit();

            }
            updateBottomNavigationPosition(1);

            //  binding.setTitle(getString(R.string.cart));
        } catch (Exception e) {
        }
    }


    public void displayFragmentProfile() {
        try {

            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragmentDepartments != null && fragmentDepartments.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentDepartments).commit();
            }
            if (fragmentOrders != null && fragmentOrders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentOrders).commit();
            }


           
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").commit();

            }
            updateBottomNavigationPosition(3);

            //binding.setTitle(getString(R.string.profile));
        } catch (Exception e) {
        }
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    private void getNotificationCount() {

    }


    @Override
    public void onBackPressed() {
        backPressed = true;
        backPressed = false;

        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            if (userModel != null) {
                finish();
            } else {
                navigateToSignInActivity();
            }
        } else {
            displayFragmentMain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void logout() {

        navigateToSignInActivity();
//        if (userModel==null){
//            finish();
//            return;
//        }
//        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//        Api.getService(Tags.base_url)
//                .logout("Bearer " + userModel.getData().getToken())
//                .enqueue(new Callback<StatusResponse>() {
//                    @Override
//                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful()) {
//                            if (response.body() != null && response.body().getStatus() == 200) {
//                                navigateToSignInActivity();
//                            }
//
//                        } else {
//                            dialog.dismiss();
//                            try {
//                                Log.e("error", response.code() + "__" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (response.code() == 500) {
//                            } else {
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StatusResponse> call, Throwable t) {
//                        try {
//                            dialog.dismiss();
//                            if (t.getMessage() != null) {
//                                Log.e("error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                } else {
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });

    }


    public void navigateToSignInActivity() {
        preferences.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
