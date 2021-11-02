package com.betna.activities_fragments.activity_home.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.betna.R;

import com.betna.activities_fragments.activity_contactus.ContactUsActivity;
import com.betna.activities_fragments.activity_home.HomeActivity;
import com.betna.databinding.FragmentProfileBinding;
import com.betna.interfaces.Listeners;
import com.betna.models.UserModel;
import com.betna.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment implements Listeners.ProfileActions {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;

    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setActions(this);
        if (userModel != null) {
            binding.setModel(userModel);
        }
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                activity.refreshActivity(lang);

            }
        });


    }


    @Override
    public void onLogout() {
        activity.logout();
    }

    @Override
    public void onMyPreOrder() {

    }

    @Override
    public void onMyRates() {

    }

    @Override
    public void onChangeLanguage() {
        if (lang.equals("ar")) {
            activity.refreshActivity("en");


        } else {
            activity.refreshActivity("ar");


        }
    }

    @Override
    public void onAboutApp() {

    }

    @Override
    public void onContactUs() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRate() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));

        }
    }

    @Override
    public void onDelegeteApp() {

    }
}
