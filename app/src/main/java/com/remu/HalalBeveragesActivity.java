package com.remu;

import android.annotation.SuppressLint;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.HttpHandler;
import com.remu.POJO.MyCallBack;
import com.remu.POJO.MyComparator;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.Rating;
import com.remu.POJO.Weighting;
import com.remu.adapter.FoodBeveragesTourismResultAdapter;
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

    private ShimmerFrameLayout recommendedShimmerLoad;
    private RecyclerView listCategory, listRecommendedBeverages;
    private EditText manualCategory;
    private String userId;
    private ArrayList<PlaceModel> places;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_beverages);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));
        userId = FirebaseAuth.getInstance().getUid();

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListCategory();
        Runnable getGoogleJson = this::getGoogleJson;
        Runnable getFirebaseData = () -> getFirebaseData(value -> {
            doWeighting();
            listRecommendedBeverages.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, LinearLayoutManager.VERTICAL, false));
            FoodBeveragesTourismResultAdapter recommendedAdapter = new FoodBeveragesTourismResultAdapter(getApplication(), HalalBeveragesActivity.this, "HalalBeverages", places);
            listRecommendedBeverages.setAdapter(recommendedAdapter);
            recommendedShimmerLoad.stopShimmer();
            recommendedShimmerLoad.setVisibility(View.GONE);
        });

        new GetRecommended().execute(getGoogleJson, getFirebaseData);

        manualCategory.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!manualCategory.getText().toString().equals("")) {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageTourismResult.class);
                    intent.putExtra("sender", "HalalBeverages");
                    intent.putExtra("category", changeSpace(manualCategory.getText().toString()));
                    intent.putExtra("name", manualCategory.getText().toString());
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

    private void getGoogleJson() {
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
        doWeighting();
    }

    private void getFirebaseData(MyCallBack myCallBack) {
        for (int i = 0; i < places.size(); i++) {
            DatabaseReference intensity = firebaseDatabase.getReference().child("UserData").child(userId).child(places.get(i).getPlaceId()).child("Intensity");
            int finalI = i;
            intensity.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        places.get(finalI).setPlaceIntensity(Integer.parseInt(dataSnapshot.getValue().toString()));
                        System.out.println("onDataChange" + places.get(finalI).getPlaceIntensity());
                        System.out.println("placeName " + places.get(finalI).getPlaceId() + " Name " + places.get(finalI).getPlaceName());
                        myCallBack.onCallback(places);

                    } catch (NullPointerException np) {
                        places.get(finalI).setPlaceIntensity(1);
//                        System.out.println("onDataChange" + places.get(finalI).getPlaceIntensity());
//                        System.out.println("placeName "+places.get(finalI).getPlaceId()+" Name "+places.get(finalI).getPlaceName());

                        myCallBack.onCallback(places);
                    }

                    DatabaseReference databaseReview = FirebaseDatabase.getInstance().getReference().child("Places Review").child(places.get(finalI).getPlaceId());
                    databaseReview.addChildEventListener(new ChildEventListener() {
                        double rataRata = 0;
                        double jumlah = 0;


                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            try {
                                ++jumlah;
                                rataRata += Double.parseDouble(dataSnapshot.getValue(Rating.class).getRating());
                                rataRata /= jumlah;

                                places.get(finalI).setTrevuRating(rataRata);
                                System.out.println("Ratefood " + places.get(finalI).getTrevuRating());
                                myCallBack.onCallback(places);

                            } catch (NullPointerException np) {
                                places.get(finalI).setTrevuRating(1);
                                System.out.println("Ratefood " + places.get(finalI).getTrevuRating());
                                myCallBack.onCallback(places);

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void doWeighting() {
        Weighting weighting = new Weighting();
        ArrayList<Double> weight;

        weight = weighting.doWeighting(latitude, longitude, places);

        for (int i = 0; i < places.size(); i++) {
            places.get(i).setPlaceWeight(weight.get(i));
        }
        Collections.sort(places, new MyComparator());
        if (places.size() > 20) {
            places = new ArrayList<>(places.subList(0, 20));
        }
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

    @Override
    protected void onPause() {
        recommendedShimmerLoad.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manualCategory.setText("");
        recommendedShimmerLoad.startShimmer();
    }

    private void initializeUI() {
        recommendedShimmerLoad = findViewById(R.id.shimmer_load_recommended_beverages);
        listCategory = findViewById(R.id.listBeveragesCategory);
        listRecommendedBeverages = findViewById(R.id.listRecommendedBeverages);
        manualCategory = findViewById(R.id.et_manual_beverages_category);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private String changeSpace(String input) {
        String[] strings = input.split(" ");
        StringBuilder returnVal = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (i + 1 != strings.length) {
                returnVal.append(strings[i]).append("%20");
            } else {
                returnVal.append(strings[i]);
            }
        }

        return returnVal.toString();
    }

    private void generateListCategory() {
        ArrayList<HashMap<String, Object>> categoryDataSet = new ArrayList<HashMap<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("category_name", "Bubble Tea");
                put("keyword", "bubble%20tea");
                put("category_image", R.drawable.beveragescategory_bubbletea);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Coffee");
                put("keyword", "coffee");
                put("category_image", R.drawable.beveragescategory_coffee);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Juice");
                put("keyword", "juice");
                put("category_image", R.drawable.beveragescategory_juice);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Tea");
                put("keyword", "tea");
                put("category_image", R.drawable.beveragescategory_tea);
            }});
        }};

        listCategory.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, RecyclerView.HORIZONTAL, false));
        RecyclerView.Adapter<CatergoryViewHolder> categoryAdapter = new RecyclerView.Adapter<CatergoryViewHolder>() {
            @NonNull
            @Override
            public CatergoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_category_tourfoodbeverages, parent, false);
                return new CatergoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CatergoryViewHolder holder, int position) {
                holder.categoryImage.setImageDrawable(getDrawable((int) categoryDataSet.get(position).get("category_image")));
                holder.categoryName.setText((String) categoryDataSet.get(position).get("category_name"));

                holder.categoryCard.setOnClickListener((v) -> {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageTourismResult.class);
                    intent.putExtra("sender", "HalalBeverages");
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

    private class GetRecommended extends AsyncTask<Runnable, Void, Void> {

        GetRecommended() {
            places = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Runnable... runnables) {
            for (Runnable task : runnables) {
                task.run();
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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


