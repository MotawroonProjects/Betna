package com.betna.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.betna.activities_fragments.activity_home.fragments.FragmentPartner;
import com.betna.activities_fragments.activity_notification.NotificationActivity;
import com.betna.models.NotFireModel;
import com.betna.remote.Api;
import com.betna.tags.Tags;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private FragmentDepartments fragmentDepartments;
    private FragmentOrders fragmentOrders;
    private FragmentPartner fragmentPartner;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private boolean backPressed = false;
    private String type;
    private Fragment currentFragment;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");

        }
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();

        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        if (userModel != null) {
            EventBus.getDefault().register(this);

        }

        binding.imageNotification.setOnClickListener(view -> {
            userModel = preferences.getUserData(this);
            if (userModel != null) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            } else {
                navigateToSignInActivity();
            }
        });



        if (userModel != null) {
            updateTokenFireBase();
        }
        setUpBottomNavigation();
        if (type != null && type.equals("order")) {
            displayFragmentOrders();
        } else {
            displayFragmentMain();

        }

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req==1){
                if (result.getResultCode()==RESULT_OK){
                    userModel = preferences.getUserData(this);
                    if(fragment_profile!=null){
                        fragment_profile.updateUserData();
                    }
                    updateTokenFireBase();
                }
            }
        });
    }

    private void updateTokenFireBase() {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                Log.e("dkdkkd", token);
                try {

                    try {

                        Api.getService(Tags.base_url)
                                .updatePhoneToken(token, 0, userModel.getUser().getId(), "android")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            Log.e("token", "updated successfully");
                                        } else {
                                            try {

                                                Log.e("error", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("error", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    //Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    } catch (Exception e) {


                    }
                } catch (Exception e) {


                }

            }
        });
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_category);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_order);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_partener);

        AHBottomNavigationItem item5 = new AHBottomNavigationItem("", R.drawable.ic_user);

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
        binding.ahBottomNav.addItem(item5);

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
                    userModel = preferences.getUserData(this);
                    if (userModel != null) {
                        displayFragmentOrders();
                    } else {
                        navigateToSignInActivity();
                    }
                    break;
                case 3:
                    displayFragmentPartner();
                    break;
                case 4:
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
            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).commit();

            }
            currentFragment = fragment_home;


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

            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).commit();

            }
            currentFragment = fragmentOrders;


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


            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).commit();

            }
            currentFragment = fragmentDepartments;

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

    public void displayFragmentPartner() {
        try {
            if (fragmentPartner == null) {
                fragmentPartner = FragmentPartner.newInstance();
            }


            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).commit();

            }
            currentFragment = fragmentPartner;

            if (fragmentPartner.isAdded()) {
                fragmentManager.beginTransaction().show(fragmentPartner).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentPartner, "fragmentCart").commit();

            }
            updateBottomNavigationPosition(3);

            //  binding.setTitle(getString(R.string.cart));
        } catch (Exception e) {
        }
    }


    public void displayFragmentProfile() {
        try {

            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).commit();

            }
            currentFragment = fragment_profile;

            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();
                fragment_profile.updateUserData();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").commit();

            }
            updateBottomNavigationPosition(4);

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
            finish();

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





    public void navigateToSignInActivity() {
        req =1;
        Intent intent = new Intent(this, LoginActivity.class);
        launcher.launch(intent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(NotFireModel notFireModel) {
        if (fragmentOrders != null && fragmentOrders.isVisible()) {
            fragmentOrders.getData();
        }
    }

}
