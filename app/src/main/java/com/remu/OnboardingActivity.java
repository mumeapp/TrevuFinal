package com.remu;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.remu.POJO.FragmentChangeListener;


public class OnboardingActivity extends FragmentActivity implements FragmentChangeListener {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_onboard);
    }

    @Override
    public void replaceFragment(int idFragment) {
        switch (idFragment) {
            case 2:
                navController.navigate(R.id.nav_onboard2);
                break;
            case 3:
                navController.navigate(R.id.nav_onboard3);
                break;
        }
    }
}
