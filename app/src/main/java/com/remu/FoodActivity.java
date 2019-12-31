package com.remu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.remu.POJO.PlaceModel;
import com.remu.adapter.GiftAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodActivity extends SlideBackActivity {

    private static final String TAG = "FoodActivity";
    private static final String API_KEY = "AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

    private PlacesClient placesClient;

    private double latitude, longitude;
    private CardView buttonHalalFood, buttonFastFood;

    private RecyclerView listGift;
    private ArrayList<PlaceModel> places;
//    private GiftAdapter giftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
        new GetGiftData(this).execute();

        //initialize ui
        initializeUI();
        Animatoo.animateSlideLeft(this);

        //set intent for halal food category list
        buttonHalalFood.setOnClickListener(view -> {
            Intent halalFood = new Intent(FoodActivity.this, HalalFoodActivity.class);
            startActivity(halalFood);
        });

        //set intent to back to previous activity
        buttonFastFood.setOnClickListener(view -> {
            Intent halalFastFood = new Intent(FoodActivity.this, HalalFastFoodActivity.class);
            startActivity(halalFastFood);
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
        buttonFastFood = findViewById(R.id.HalalFastFoodButton);
        listGift = findViewById(R.id.rv_listGift);
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
            progressDialog.dismiss();
        }
    }
}
