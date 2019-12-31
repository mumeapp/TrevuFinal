package com.remu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.remu.POJO.Article;
import com.remu.POJO.LatLngRetriever;
import com.remu.POJO.LatLngRetriever.LocationResult;
import com.remu.POJO.PrayerTime;
import com.remu.POJO.Tips;
import com.takusemba.multisnaprecyclerview.MultiSnapHelper;
import com.takusemba.multisnaprecyclerview.SnapGravity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String latitude, longitude;
    LatLngRetriever latLngRetriever = new LatLngRetriever();
    private static final String TAG = "MainActivity";

    private PrayerTime prayerTime;

    CardView mosqueCardView, foodButton, dictionaryButton, tourButton;
    String name;
    TextView nama;
    TextView jamSolatSelanjutnya;
    private FirebaseAuth mAuth;

    LinearLayoutManager articleLayoutManager;
    RecyclerView listArticle;
    RecyclerView.Adapter articleAdapter;
    ArrayList<Article> articleDataSet;

    LinearLayoutManager tipsLayoutManager;
    RecyclerView listTips;
    RecyclerView.Adapter tipsAdapter;
    ArrayList<Tips> tipsDataSet;

    NestedScrollView mainScrollView;

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
        setContentView(R.layout.activity_main);

        latLngRetriever.getLocation(getApplicationContext(), locationResult);
        latitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null);
        longitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null);

        Log.e(TAG, "Latitude: " + latitude);
        Log.e(TAG, "Longitude: " + longitude);

        //initialize uI
        initializeUI();
        initializeArticle();
        initializeTips();
        Animatoo.animateSlideLeft(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set nama
        getCurrentUser(currentUser);

        mainScrollView = findViewById(R.id.main_scroll);
        mainScrollView.post(() -> {
            mainScrollView.scrollTo(0, 0);
        });

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
            viewFood.putExtra("latitude", latitude);
            viewFood.putExtra("longitude", longitude);
            startActivity(viewFood);
        });

        //go to tourism activity
        tourButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(MainActivity.this, TourismActivity.class);
            viewTour.putExtra("latitude", latitude);
            viewTour.putExtra("longitude", longitude);
            startActivity(viewTour);
        });

        //go to Dictionary Activity
        dictionaryButton.setOnClickListener(view -> {
            Intent viewDictonary = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(viewDictonary);
        });
    }

    private void initializeUI() {
        mosqueCardView = findViewById(R.id.MosqueCardView);
        foodButton = findViewById(R.id.foodButton);
        dictionaryButton = findViewById(R.id.dictionaryButton);
        tourButton = findViewById(R.id.tourismButton);
        nama = findViewById(R.id.nama);
        jamSolatSelanjutnya = findViewById(R.id.jamSolatSelanjutnya);
        ArrayList<TextView> textViews = new ArrayList<TextView>() {{
            add(jamSolatSelanjutnya);
        }};
        prayerTime = new PrayerTime(this, TAG, latitude, longitude, textViews);
        prayerTime.execute();
        listArticle = findViewById(R.id.listArticle);
        articleDataSet = new ArrayList<Article>() {{
            add(new Article(getDrawable(R.drawable.img_article), "Discover the relic!", "There was no mention of any time period, or the context of the conflict that took the purported and so on. "));
            add(new Article(getDrawable(R.drawable.img_article), "Discover the relic!", "There was no mention of any time period, or the context of the conflict that took the purported and so on. "));
        }};
        listTips = findViewById(R.id.listTips);
        tipsDataSet = new ArrayList<Tips>() {{
            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #1", ""));
            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #2", ""));
            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #3", ""));
        }};
    }

    private void initializeArticle() {
        articleLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        listArticle.setLayoutManager(articleLayoutManager);
        articleAdapter = new ArticleAdapter(getApplication(), articleDataSet);
        listArticle.setAdapter(articleAdapter);
        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 1, 100);
        multiSnapHelper.attachToRecyclerView(listArticle);
    }

    private void initializeTips() {
        tipsLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        listTips.setLayoutManager(tipsLayoutManager);
        tipsAdapter = new TipsAdapter(getApplication(), tipsDataSet);
        listTips.setAdapter(tipsAdapter);
        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 2, 100);
        multiSnapHelper.attachToRecyclerView(listTips);
    }

    private void getCurrentUser(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            nama.setText(name);
        }
    }

}
