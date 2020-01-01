package com.remu;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.internal.runners.statements.RunAfters;

public class SplashscreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0984E3, android.graphics.PorterDuff.Mode.SRC_ATOP);


        mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            FirebaseUser user = mAuth.getCurrentUser();

            if (sharedPreferences.getBoolean("isFirstTime", true)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstTime", false).apply();

                Intent intent = new Intent(SplashscreenActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (user != null) {
                    Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
}
