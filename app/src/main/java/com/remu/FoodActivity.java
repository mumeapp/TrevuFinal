package com.remu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.remu.POJO.PlaceModel;
import com.remu.adapter.GiftAdapter;
import com.saber.chentianslideback.SlideBackActivity;
import com.takusemba.multisnaprecyclerview.MultiSnapHelper;
import com.takusemba.multisnaprecyclerview.SnapGravity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodActivity extends SlideBackActivity {

    private static final String TAG = "FoodActivity";

    private double latitude, longitude;
    private CardView buttonHalalFood, buttonHalalBeverages;
    private RecyclerView listGift;
    private ArrayList<PlaceModel> places;
    private String userId;

//    private GiftAdapter giftAdapter;

    private ScrollView foodScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));
        new GetGiftData(this).execute();

        //initialize ui
        initializeUI();
        Animatoo.animateSlideLeft(this);

        foodScrollView = findViewById(R.id.food_scroll);
        foodScrollView.post(() -> foodScrollView.scrollTo(0, 0));

        //set intent for halal food category list
        buttonHalalFood.setOnClickListener(view -> {
            Intent halalFood = new Intent(FoodActivity.this, HalalFoodActivity.class);
            startActivity(halalFood);
        });

        //set intent to back to previous activity
        buttonHalalBeverages.setOnClickListener(view -> {
            Intent halalBeverages = new Intent(FoodActivity.this, HalalBeveragesActivity.class);
            startActivity(halalBeverages);
        });

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        buttonHalalFood = findViewById(R.id.halalFoodButton);
        buttonHalalBeverages = findViewById(R.id.HalalBeveragesButton);
        listGift = findViewById(R.id.rv_listGift);
        userId = FirebaseAuth.getInstance().getUid();
    }

    private class GetGiftData extends AsyncTask<Void, Void, Void> {

        private Context context;

        private ProgressDialog progressDialog;
        private ArrayList<PlaceModel> places;

        GetGiftData(Context context) {
            this.context = context;
            places = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching result...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "&rankby=distance&type=store&keyword=souvenir%20shop&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

            String jsonStr = httpHandler.makeServiceCall(url);

            Log.d(TAG, url);
            Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray results = new JSONObject(jsonStr).getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject row = results.getJSONObject(i);

                        if (row.isNull("photos")) {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
                            ));
                        } else {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng")),
                                    row.getJSONArray("photos").getJSONObject(0).getString("photo_reference")
                            ));
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listGift.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.HORIZONTAL, false));
            GiftAdapter giftAdapter = new GiftAdapter(getApplication(), FoodActivity.this, places);
            listGift.setAdapter(giftAdapter);
            MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 1, 100);
            multiSnapHelper.attachToRecyclerView(listGift);

            progressDialog.dismiss();
        }
    }
}
