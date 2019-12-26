package com.remu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.remu.POJO.LatLngRetriever;
import com.remu.POJO.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText registerEmail, registerPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference; //1
    Button registerButton;

    LatLngRetriever latLngRetriever = new LatLngRetriever();

    public LatLngRetriever.LocationResult locationResult = new LatLngRetriever.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            // TODO Auto-generated method stub
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

            Log.d(TAG, "Got Location");

            try {
                SharedPreferences locationpref = getApplication()
                        .getSharedPreferences("location", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = locationpref.edit();
                prefsEditor.putString("Longitude", Longitude + "");
                prefsEditor.putString("Latitude", Latitude + "");
                prefsEditor.apply();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        latLngRetriever.getLocation(getApplicationContext(), locationResult);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initializeUI();
        Animatoo.animateSlideLeft(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> registerNewUser());
    }

    public void have_account(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerNewUser() {
        String email, password;
        email = registerEmail.getText().toString();
        password = registerPassword.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateName(user);

                            String userID = user.getUid();

                            User userr = new User("", "", "", "", "", email, "default", "", userID);

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
                            databaseReference.setValue(userr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateUI(user);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Register gagal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        registerEmail = findViewById(R.id.RegisterEmail);
        registerPassword = findViewById(R.id.RegisterPassword);
    }

    private void updateName(FirebaseUser user) {
        String email = registerEmail.getText().toString();
        int index = email.indexOf('@');
        String defaultName = email.substring(0, index);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(defaultName).build();
        user.updateProfile(profileUpdates);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Please login!", Toast.LENGTH_LONG).show();
        }

    }
}
