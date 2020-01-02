package com.remu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String latitude, longitude;
    private LatLngRetriever latLngRetriever = new LatLngRetriever();

    private ImageView backgroundMorning, backgroundDay, backgroundEvening, backgroundNight;
    private View viewMorning, viewDay, viewEvening, viewNight;

    private CardView mosqueCardView, foodButton, dictionaryButton, tourButton;
    private TextView halo, nama;
    private TextView jamSolatSelanjutnya;

    private RecyclerView listArticle;
    private ArrayList<Article> articleDataSet;

    private RecyclerView listTips;
    private ArrayList<Tips> tipsDataSet;

    private NestedScrollView mainScrollView;

    public LocationResult locationResult = new LocationResult() {
        @Override
        public void gotLocation(Location location) {
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
        setBackgroundByTime();
        initializeArticle();
        initializeTips();
        Animatoo.animateSlideLeft(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set nama
        getCurrentUser(currentUser);

        mainScrollView = findViewById(R.id.main_scroll);
        mainScrollView.post(() -> mainScrollView.scrollTo(0, 0));

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

        //go to tourism activity
        tourButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(MainActivity.this, TourismActivity.class);
            startActivity(viewTour);
        });

        //go to Dictionary Activity
        dictionaryButton.setOnClickListener(view -> {
            Intent viewDictonary = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(viewDictonary);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setBackgroundByTime();
    }

    private void initializeUI() {
        mosqueCardView = findViewById(R.id.MosqueCardView);
        foodButton = findViewById(R.id.foodButton);
        dictionaryButton = findViewById(R.id.dictionaryButton);
        tourButton = findViewById(R.id.tourismButton);
        halo = findViewById(R.id.halo);
        nama = findViewById(R.id.nama);
        jamSolatSelanjutnya = findViewById(R.id.jamSolatSelanjutnya);
        ArrayList<TextView> textViews = new ArrayList<TextView>() {{
            add(jamSolatSelanjutnya);
        }};
        new PrayerTime(this, TAG, latitude, longitude, textViews).execute();

        backgroundMorning = findViewById(R.id.placeIllustrationMorning);
        viewMorning = findViewById(R.id.view_morning);
        backgroundDay = findViewById(R.id.placeIllustrationDay);
        viewDay = findViewById(R.id.view_day);
        backgroundEvening = findViewById(R.id.placeIllustrationEvening);
        viewEvening = findViewById(R.id.view_evening);
        backgroundNight = findViewById(R.id.placeIllustrationNight);
        viewNight = findViewById(R.id.view_night);

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

    private void setBackgroundByTime() {
        int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
        int currentMinutes = Integer.parseInt(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));

        backgroundMorning.setAlpha(0f);
        viewMorning.setAlpha(0f);
        backgroundDay.setAlpha(0f);
        viewDay.setAlpha(0f);
        backgroundEvening.setAlpha(0f);
        viewEvening.setAlpha(0f);
        backgroundNight.setAlpha(0f);
        viewNight.setAlpha(0f);

        switch (currentHour) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                halo.setTextColor(Color.parseColor("#FFFFFF"));
                nama.setTextColor(Color.parseColor("#FFFFFF"));
                backgroundNight.setAlpha(1f);
                viewNight.setAlpha(1f);
                break;
            case 5:
                backgroundNight.setAlpha(1f);
                viewNight.setAlpha(1f);
                if (currentMinutes <= 15) {
                    backgroundMorning.setAlpha(0.25f);
                    viewMorning.setAlpha(0.25f);
                } else if (currentMinutes <= 30) {
                    backgroundMorning.setAlpha(0.5f);
                    viewMorning.setAlpha(0.5f);
                } else if (currentMinutes <= 45) {
                    backgroundMorning.setAlpha(0.75f);
                    viewMorning.setAlpha(0.75f);
                } else {
                    backgroundMorning.setAlpha(1f);
                    viewMorning.setAlpha(1f);
                }
                break;
            case 6:
                if (currentMinutes <= 30) {
                    backgroundMorning.setAlpha(1f);
                    viewMorning.setAlpha(1f);
                } else if (currentMinutes <= 45) {
                    backgroundMorning.setAlpha(0.75f);
                    viewMorning.setAlpha(0.75f);
                    backgroundDay.setAlpha(0.25f);
                    viewDay.setAlpha(0.25f);
                } else {
                    backgroundMorning.setAlpha(0.5f);
                    viewMorning.setAlpha(0.5f);
                    backgroundDay.setAlpha(0.5f);
                    viewDay.setAlpha(0.5f);
                }
                break;
            case 7:
                if (currentMinutes <= 15) {
                    backgroundMorning.setAlpha(0.25f);
                    viewMorning.setAlpha(0.25f);
                    backgroundDay.setAlpha(0.75f);
                    viewDay.setAlpha(0.75f);
                } else {
                    backgroundMorning.setAlpha(0f);
                    viewMorning.setAlpha(0f);
                    backgroundDay.setAlpha(1f);
                    viewDay.setAlpha(1f);
                }
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                backgroundDay.setAlpha(1f);
                viewDay.setAlpha(1f);
                break;
            case 16:
                if (currentMinutes <= 30) {
                    backgroundDay.setAlpha(1f);
                    viewDay.setAlpha(1f);
                } else if (currentMinutes <= 45) {
                    backgroundDay.setAlpha(0.75f);
                    viewDay.setAlpha(0.75f);
                    backgroundEvening.setAlpha(0.25f);
                    viewEvening.setAlpha(0.25f);
                } else {
                    backgroundDay.setAlpha(0.5f);
                    viewDay.setAlpha(0.5f);
                    backgroundEvening.setAlpha(0.5f);
                    viewEvening.setAlpha(0.5f);
                }
                break;
            case 17:
                if (currentMinutes <= 15) {
                    backgroundDay.setAlpha(0.25f);
                    viewDay.setAlpha(0.25f);
                    backgroundEvening.setAlpha(0.75f);
                    viewEvening.setAlpha(0.75f);
                } else {
                    backgroundDay.setAlpha(0f);
                    viewDay.setAlpha(0f);
                    backgroundEvening.setAlpha(1f);
                    viewEvening.setAlpha(1f);
                }
                break;
            case 18:
                halo.setTextColor(Color.parseColor("#FFFFFF"));
                nama.setTextColor(Color.parseColor("#FFFFFF"));
                backgroundNight.setAlpha(1f);
                viewNight.setAlpha(1f);
                if (currentMinutes <= 15) {
                    backgroundEvening.setAlpha(0.75f);
                    viewEvening.setAlpha(0.75f);
                } else if (currentMinutes <= 30) {
                    backgroundEvening.setAlpha(0.5f);
                    viewEvening.setAlpha(0.5f);
                } else if (currentMinutes <= 45) {
                    backgroundEvening.setAlpha(0.25f);
                    viewEvening.setAlpha(0.25f);
                } else {
                    backgroundEvening.setAlpha(0f);
                    viewEvening.setAlpha(0f);
                }
                break;
        }
    }

    private void initializeArticle() {
        LinearLayoutManager articleLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        listArticle.setLayoutManager(articleLayoutManager);
        RecyclerView.Adapter articleAdapter = new ArticleAdapter(getApplication(), articleDataSet);
        listArticle.setAdapter(articleAdapter);
        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 1, 100);
        multiSnapHelper.attachToRecyclerView(listArticle);
    }

    private void initializeTips() {
        LinearLayoutManager tipsLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        listTips.setLayoutManager(tipsLayoutManager);
        RecyclerView.Adapter tipsAdapter = new TipsAdapter(getApplication(), tipsDataSet);
        listTips.setAdapter(tipsAdapter);
    }

    private void getCurrentUser(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            nama.setText(name);
        }
    }

}
