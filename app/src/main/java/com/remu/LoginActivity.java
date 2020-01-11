package com.remu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.remu.POJO.LatLngRetriever;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    Button loginButton;
    private FirebaseAuth mAuth;
    private TextInputEditText loginEmail, loginPassword;
    static final int GOOGLE_SIGN = 123;
    GoogleSignInClient mGoogleSignInClient;

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
        setContentView(R.layout.activity_login);

        latLngRetriever.getLocation(getApplicationContext(), locationResult);

        initializeUI();
        Animatoo.animateSlideLeft(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> signIn());
    }

    public void registerClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginWithGoogle(View view) {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    public void loginWithFacebook(View view) {
        // TODO: Make Login with facebook

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("Tag", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                Log.d("TAG", "sign in success");
                FirebaseUser user = mAuth.getCurrentUser();
                //
                assert user != null;
                String email = user.getEmail();
                //
                assert email != null;
                int index = email.indexOf('@');
                String defaultName = email.substring(0, index);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(defaultName).build();
                user.updateProfile(profileUpdates);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signIn() {
        String email, password;
        TextInputLayout emailLayout = findViewById(R.id.input_layout_login_email);
        emailLayout.setError(null);
        TextInputLayout passwordLayout = findViewById(R.id.input_layout_login_password);
        passwordLayout.setError(null);
        boolean isError = false;
        email = Objects.requireNonNull(loginEmail.getText()).toString();
        password = Objects.requireNonNull(loginPassword.getText()).toString();

        if (loginEmail.getText().length() == 0) {
            emailLayout.setError("Email is required");
            isError = true;
        }
        if (loginPassword.getText().length() == 0) {
            passwordLayout.setError("Password is required");
            isError = true;
        }
        if (isError) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        loginPassword.setText("");
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
