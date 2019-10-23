package com.remu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.remu.POJO.PrayerTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PrayerTime prayerTime;

    CardView mosqueCardView, foodButton, dictionaryButton, friendButton, tourButton;
    String name;
    TextView nama;
    TextView jamSolatSelanjutnya;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize uI
        initializeUI();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set nama
        getCurrentUser(currentUser);
        //go to mosque activity
        mosqueCardView.setOnClickListener(view -> {
            Intent viewMosque = new Intent(MainActivity.this, MosqueActivity.class);
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
        prayerTime = new PrayerTime(this.getApplicationContext(), TAG, textViews);
        prayerTime.execute();
    }

    private void getCurrentUser(FirebaseUser user){
        if(user !=null){
            String name =user.getDisplayName();
            nama.setText(name);
        }
    }
}
