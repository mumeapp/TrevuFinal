package com.remu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.remu.POJO.HttpHandler;
import com.remu.POJO.MyComparator;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.Weighting;
import com.remu.adapter.TourismAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TourismActivity extends SlideBackActivity {

    private static final String TAG = "TourismActivity";

    private RecyclerView rvTour;
    private TourismAdapter tourismAdapter;
    private double latitude, longitude;
    private String userId;

    NestedScrollView tourScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        new GetTourPlace(this).execute();

        tourScrollView = findViewById(R.id.tour_scroll);
        tourScrollView.post(() -> {
            tourScrollView.scrollTo(0, 0);
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
        rvTour = findViewById(R.id.TourismCategories);
        userId = FirebaseAuth.getInstance().getUid();
    }

    private class GetTourPlace extends AsyncTask<Void, Void, Void> {

        private Context context;

        private ProgressDialog progressDialog;
        private ArrayList<PlaceModel> places;

        GetTourPlace(Context context) {
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
            //tourist_attraction, museum, zoo, aquarium, park
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "&rankby=distance&type=tourist_attraction&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

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
            doWeighting();
            return null;
        }

        private void doWeighting() {
            Weighting weighting = new Weighting();
            ArrayList<Double> weight;
            weight = weighting.doWeighting(latitude, longitude, places);

            for (int i = 0; i < places.size(); i++) {
                places.get(i).setPlaceWeight(weight.get(i));
            }

            Collections.sort(places, new MyComparator());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            rvTour.setLayoutManager(new GridLayoutManager(TourismActivity.this, 2));
            tourismAdapter = new TourismAdapter(getApplication(), getParent(), places, new LatLng(latitude, longitude));
            tourismAdapter.setClickListener((view, position) -> {
                //set what happend when clicked
                Log.i("TAG", "You clicked number " + tourismAdapter.getItem(position) + ", which is at cell position " + position);

            });
            rvTour.setAdapter(tourismAdapter);

            progressDialog.dismiss();
        }
    }

}
