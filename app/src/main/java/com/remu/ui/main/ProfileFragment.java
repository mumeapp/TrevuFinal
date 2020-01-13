package com.remu.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.remu.BuildConfig;
import com.remu.LoginActivity;
import com.remu.R;
import com.remu.Service.UpdateLocation;

public class ProfileFragment extends Fragment {

    private ImageView profilePicture;
    private TextView profileName, profileId, versionId;
    private LinearLayout changeProfile, privacyPolicy, helpCenter;
    private Button signOutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeUI(root);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Glide.with(ProfileFragment.this)
                    .load(getActivity().getDrawable(R.drawable.profile_annasaha))
                    .placeholder(R.drawable.bg_loading_image)
                    .into(profilePicture);

            String name = currentUser.getDisplayName();
            profileName.setText(name);

            String id = "ID: " + FirebaseAuth.getInstance().getUid();
            profileId.setText(id);
        }

        changeProfile.setOnClickListener(v -> {

        });

        privacyPolicy.setOnClickListener(v -> {

        });

        helpCenter.setOnClickListener(v -> {

        });

        String versionName = "Version " + BuildConfig.VERSION_NAME + " (Beta)";
        versionId.setText(versionName);

        signOutButton.setOnClickListener(v -> {
            LoginManager loginManager = LoginManager.getInstance();
            Intent stopService = new Intent(ProfileFragment.super.getContext(), UpdateLocation.class);
            getActivity().stopService(stopService);
            FirebaseAuth.getInstance().signOut();
            loginManager.logOut();
            Intent login = new Intent(ProfileFragment.super.getContext(), LoginActivity.class);
            startActivity(login);
            getActivity().finish();
        });

        return root;
    }

    private void initializeUI(View root) {
        profilePicture = root.findViewById(R.id.profile_image);
        profileName = root.findViewById(R.id.profile_name);
        profileId = root.findViewById(R.id.profile_id);
        changeProfile = root.findViewById(R.id.change_profile);
        privacyPolicy = root.findViewById(R.id.privacy_policy);
        helpCenter = root.findViewById(R.id.help_center);
        versionId = root.findViewById(R.id.version_id);
        signOutButton = root.findViewById(R.id.sign_out_button);
    }

}
