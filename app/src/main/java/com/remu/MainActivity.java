package com.remu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.remu.POJO.LatLngRetriever;
import com.remu.POJO.LatLngRetriever.LocationResult;
import com.remu.POJO.PrayerTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String latitude, longitude, country;
    LatLngRetriever latLngRetriever = new LatLngRetriever();
    private static final String TAG = "MainActivity";

    private PrayerTime prayerTime;

    private ImageView placeIllustration;
    private CardView mosqueCardView, foodButton, dictionaryButton, friendButton, tourButton;
    private String name;
    private TextView nama;
    private TextView jamSolatSelanjutnya;
    private FirebaseAuth mAuth;

    public LocationResult locationResult = new LocationResult() {

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
                prefsEditor.commit();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latLngRetriever.getLocation(getApplicationContext(), locationResult);
        latitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null);
        longitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null);

        Log.e(TAG, "Latitude: " + latitude);
        Log.e(TAG, "Longitude: " + longitude);

        try {
            List<Address> addresses = new Geocoder(getBaseContext(), Locale.getDefault()).getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (!addresses.isEmpty()) {
                country = addresses.get(0).getCountryName();
            }

            Log.e(TAG, "Country: " + country);
        } catch (IOException e) {
            // do something..
        }

        //initialize uI
        initializeUI();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set nama
        getCurrentUser(currentUser);

        //go to mosque activity
        mosqueCardView.setOnClickListener(view -> {
            Intent viewMosque = new Intent(MainActivity.this, MosqueActivity.class);
            viewMosque.putExtra("latitude", latitude);
            viewMosque.putExtra("longitude", longitude);
            startActivity(viewMosque);
        });

        //go to food activity
        foodButton.setOnClickListener(view -> {
            Intent viewFood = new Intent(MainActivity.this, FoodActivity.class);
            startActivity(viewFood);
        });

        //go to Dictionary Activity
        dictionaryButton.setOnClickListener(view -> {
            Intent viewDictonary = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(viewDictonary);
        });
        friendButton.setOnClickListener(view -> {
            Intent viewFriend = new Intent(MainActivity.this, FindFriendsActivity.class);
            startActivity(viewFriend);
        });
        tourButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(MainActivity.this, TourismActivity.class);
            startActivity(viewTour);
        });

    }

    private void initializeUI() {
        placeIllustration = findViewById(R.id.placeIllustration);
        setPlaceIllustration(country);
        mosqueCardView = findViewById(R.id.MosqueCardView);
        foodButton = findViewById(R.id.foodButton);
        dictionaryButton = findViewById(R.id.dictionaryButton);
        friendButton = findViewById(R.id.findFriendsButton);
        tourButton = findViewById(R.id.tourismButton);
        nama = findViewById(R.id.nama);
        jamSolatSelanjutnya = findViewById(R.id.jamSolatSelanjutnya);
        ArrayList<TextView> textViews = new ArrayList<TextView>() {{
            add(jamSolatSelanjutnya);
        }};
        prayerTime = new PrayerTime(this.getApplicationContext(), TAG, latitude, longitude, textViews);
        prayerTime.execute();
    }

    private void getCurrentUser(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            nama.setText(name);
        }
    }

    private void setPlaceIllustration(String country) {

    }

}
