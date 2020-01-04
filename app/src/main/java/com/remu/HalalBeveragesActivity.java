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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.MyComparator;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.Weighting;
import com.remu.adapter.FoodBeveragesResultAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HalalBeveragesActivity extends SlideBackActivity {

    private static final String TAG = "HalalBeveragesActivity";

    private double latitude, longitude;

    private RecyclerView listCategory, listRecommendedFood;
    private EditText manualCategory;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_beverages);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListCategory();
        new GetRecommended(this).execute();

        manualCategory.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!manualCategory.getText().toString().equals("")) {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageResult.class);
                    intent.putExtra("sender", "HalalBeverages");
                    intent.putExtra("category", manualCategory.getText().toString());
                    startActivity(intent);
                    return true;
                } else {
                    manualCategory.setError("Please put what category you want.");
                }
            }
            return false;
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
        listCategory = findViewById(R.id.listBeveragesCategory);
        listRecommendedFood = findViewById(R.id.listRecommendedBeverages);
        manualCategory = findViewById(R.id.et_manual_beverages_category);
        userId = FirebaseAuth.getInstance().getUid();
    }

    private void generateListCategory() {
        ArrayList<HashMap<String, Object>> categoryDataSet = new ArrayList<HashMap<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("category_name", "Bubble Tea");
                put("category_image", R.drawable.beveragescategory_bubbletea);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Coffee");
                put("category_image", R.drawable.beveragescategory_coffee);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Juice");
                put("category_image", R.drawable.beveragescategory_juice);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Tea");
                put("category_image", R.drawable.beveragescategory_tea);
            }});
        }};

        listCategory.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, RecyclerView.HORIZONTAL, false));
        RecyclerView.Adapter<CatergoryViewHolder> categoryAdapter = new RecyclerView.Adapter<CatergoryViewHolder>() {
            @NonNull
            @Override
            public CatergoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_menu_kategori_food, parent, false);
                return new CatergoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CatergoryViewHolder holder, int position) {
                holder.categoryImage.setImageDrawable(getDrawable((int) categoryDataSet.get(position).get("category_image")));
                holder.categoryName.setText((String) categoryDataSet.get(position).get("category_name"));

                holder.categoryCard.setOnClickListener((v) -> {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageResult.class);
                    intent.putExtra("sender", "HalalBeverages");
                    intent.putExtra("category", (String) categoryDataSet.get(position).get("category_name"));
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

    private class GetRecommended extends AsyncTask<Void, Void, Void> {

        private Context context;

        private ProgressDialog progressDialog;
        private ArrayList<PlaceModel> places;

        GetRecommended(Context context) {
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
                    "6&rankby=distance&keyword=bubble%20tea&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=tea&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=coffee&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
            String url4 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&keyword=juice&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

            ArrayList<String> arrayListJSON = new ArrayList<String>() {{
                add(httpHandler.makeServiceCall(url1));
                add(httpHandler.makeServiceCall(url2));
                add(httpHandler.makeServiceCall(url3));
                add(httpHandler.makeServiceCall(url4));
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
            places = new ArrayList<>(places.subList(0, 20));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listRecommendedFood.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, LinearLayoutManager.VERTICAL, false));
            FoodBeveragesResultAdapter recommendedAdapter = new FoodBeveragesResultAdapter(getApplication(), HalalBeveragesActivity.this, places);
            listRecommendedFood.setAdapter(recommendedAdapter);

            progressDialog.dismiss();
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
    }

    class CatergoryViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryName;
        CardView categoryCard;

        CatergoryViewHolder(View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.food_category_image);
            categoryName = itemView.findViewById(R.id.food_category_name);
            categoryCard = itemView.findViewById(R.id.food_category_card);
        }

    }

}

//class MyComparator implements Comparator<PlaceModel> {
//    @Override
//    public int compare(PlaceModel o1, PlaceModel o2) {
//        if (o1.getPlaceWeight() > o2.getPlaceWeight()) {
//            return -1;
//        } else if (o1.getPlaceWeight() < o2.getPlaceWeight()) {
//            return 1;
//        }
//        return 0;
//    }
//}
