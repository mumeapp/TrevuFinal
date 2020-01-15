package com.remu.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.remu.BuildConfig;
import com.remu.ChangeProfileActivity;
import com.remu.HelpCenterActivity;
import com.remu.LoginActivity;
import com.remu.PrivacyPolicyActivity;
import com.remu.R;
import com.remu.Service.UpdateLocation;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ImageView profilePicture;
    private TextView profileName, profileId, versionId;
    private LinearLayout changeProfile, privacyPolicy, helpCenter;
    private Switch searchable;
    private Button signOutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeUI(root);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = FirebaseAuth.getInstance().getUid();
        final String[] gender = new String[1];
        final String[] birthdate = new String[1];
        final String[] about = new String[1];
        if (currentUser != null) {
            Glide.with(ProfileFragment.this)
                    .load(currentUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(profilePicture);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile").child(FirebaseAuth.getInstance().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gender[0] = dataSnapshot.child("gender").getValue().toString();
                    birthdate[0] = dataSnapshot.child("birthdate").getValue().toString();
                    about[0] = dataSnapshot.child("about").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String name = currentUser.getDisplayName();
            profileName.setText(name);

            String id = "ID: " + FirebaseAuth.getInstance().getUid();
            profileId.setText(id);
        }

        changeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileFragment.super.getContext(), ChangeProfileActivity.class);
            if(currentUser!=null){
                intent.putExtra("name", currentUser.getDisplayName());
                intent.putExtra("image", currentUser.getPhotoUrl().toString());
                intent.putExtra("birthdate", birthdate[0]);
                intent.putExtra("gender", gender[0]);
                intent.putExtra("about", about[0]);
            }
            startActivity(intent);
        });

        if (getActivity().getSharedPreferences("privacy", MODE_PRIVATE).contains("searchable")) {
            searchable.setChecked(getActivity().getSharedPreferences("privacy", MODE_PRIVATE).getBoolean("searchable", true));
        } else {
            searchable.setChecked(true);
        }

        searchable.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User Location").child(userId);
            try {
                SharedPreferences privacyPreference = getActivity()
                        .getSharedPreferences("privacy", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = privacyPreference.edit();
                prefsEditor.putBoolean("searchable", isChecked);
                prefsEditor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getActivity().getSharedPreferences("privacy", MODE_PRIVATE).getBoolean("searchable", true)) {
                databaseReference.child("status").setValue(true);
            }
            else{
                databaseReference.child("status").setValue(false);
            }
        }));


        privacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileFragment.super.getContext(), PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        helpCenter.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileFragment.super.getContext(), HelpCenterActivity.class);
            startActivity(intent);
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
        searchable = root.findViewById(R.id.searchable);
        privacyPolicy = root.findViewById(R.id.privacy_policy);
        helpCenter = root.findViewById(R.id.help_center);
        versionId = root.findViewById(R.id.version_id);
        signOutButton = root.findViewById(R.id.sign_out_button);
    }

}
