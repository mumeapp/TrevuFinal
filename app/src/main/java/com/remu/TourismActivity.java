package com.remu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.HashMap;

public class TourismActivity extends SlideBackActivity {

    private static final String TAG = "TourismActivity";

    private RecyclerView listCategory, rvTour;
    private TourismAdapter tourismAdapter;
    private double latitude, longitude;
    private String userId;

    private NestedScrollView tourScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListCategory();

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
        listCategory = findViewById(R.id.listTourismCategories);
        rvTour = findViewById(R.id.listRecommendedTourism);
        userId = FirebaseAuth.getInstance().getUid();
    }

    private void generateListCategory() {
        ArrayList<HashMap<String, Object>> categoryDataSet = new ArrayList<HashMap<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("category_name", "Amusement park");
                put("keyword", "theme%20park");
                put("category_image", R.drawable.tourismcategory_amusementpark);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Aquarium");
                put("keyword", "aquarium%20attraction");
                put("category_image", R.drawable.tourismcategory_aquarium);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Beach");
                put("keyword", "beach");
                put("category_image", R.drawable.tourismcategory_beach);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Museum");
                put("keyword", "museum");
                put("category_image", R.drawable.tourismcategory_museum);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Waterfall");
                put("keyword", "waterfall");
                put("category_image", R.drawable.tourismcategory_waterfall);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Zoo");
                put("keyword", "zoo");
                put("category_image", R.drawable.tourismcategory_zoo);
            }});
        }};

        listCategory.setLayoutManager(new LinearLayoutManager(TourismActivity.this, RecyclerView.HORIZONTAL, false));
        RecyclerView.Adapter<CatergoryViewHolder> categoryAdapter = new RecyclerView.Adapter<CatergoryViewHolder>() {
            @NonNull
            @Override
            public CatergoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_category_tourism, parent, false);
                return new CatergoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CatergoryViewHolder holder, int position) {
                holder.categoryImage.setImageDrawable(getDrawable((int) categoryDataSet.get(position).get("category_image")));
                holder.categoryName.setText((String) categoryDataSet.get(position).get("category_name"));

                holder.categoryCard.setOnClickListener((v) -> {
                    Intent intent = new Intent(TourismActivity.this, FoodBeverageTourismResult.class);
                    intent.putExtra("sender", "Tourism");
                    intent.putExtra("category", (String) categoryDataSet.get(position).get("keyword"));
                    intent.putExtra("name", (String) categoryDataSet.get(position).get("category_name"));
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return categoryDataSet.size();
            }
        };
        listCategory.setAdapter(categoryAdapter);
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

            String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=museum&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=zoo&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=aquarium%20attraction&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url4 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=theme%20park&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url5 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=waterfall&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url6 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=beach&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

            ArrayList<String> arrayListJSON = new ArrayList<String>() {{
                add(httpHandler.makeServiceCall(url1));
                add(httpHandler.makeServiceCall(url2));
                add(httpHandler.makeServiceCall(url3));
                add(httpHandler.makeServiceCall(url4));
                add(httpHandler.makeServiceCall(url5));
                add(httpHandler.makeServiceCall(url6));
            }};

            ArrayList<String> placeIds = new ArrayList<>();

            for (String jsonStr : arrayListJSON) {
                Log.d(TAG, url1);
                Log.d(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    parseJSON(jsonStr, placeIds);
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                }
            }

            getTopRating();
            doWeighting();
            return null;
        }

        private void parseJSON(String jsonStr, ArrayList<String> placeIds) {
            try {
                JSONArray results = new JSONObject(jsonStr).getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject row = results.getJSONObject(i);

                    if (row.isNull("photos")) {
                        if (!placeIds.contains(row.getString("place_id"))) {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
                            ));
                            placeIds.add(row.getString("place_id"));
                        }
                    } else {
                        if (!placeIds.contains(row.getString("place_id"))) {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng")),
                                    row.getJSONArray("photos").getJSONObject(0).getString("photo_reference")
                            ));
                            placeIds.add(row.getString("place_id"));
                        }
                    }
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        private void getTopRating() {
            ArrayList<PlaceModel> topRating = new ArrayList<>();

            for (PlaceModel place : places) {
                if (place.getPlaceRating() >= 4) {
                    topRating.add(place);
                }
            }

            places.clear();
            places.addAll(topRating);
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

    class CatergoryViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryName;
        CardView categoryCard;

        CatergoryViewHolder(View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.tourism_category_image);
            categoryName = itemView.findViewById(R.id.tourism_category_name);
            categoryCard = itemView.findViewById(R.id.tourism_category_card);
        }

    }

}
