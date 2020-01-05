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
import com.remu.POJO.HttpHandler;
import com.remu.POJO.MyCallBack;
import com.remu.POJO.MyComparator;
import com.remu.POJO.PlaceModel;
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

    private RecyclerView listCategory, listRecommendedFood;
    private EditText manualCategory;
    private String userId;
    private ArrayList<PlaceModel> places;
    private ProgressDialog progressDialog;


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

    private void getGoogleJson(){
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
    }

    private void getFirebaseData(MyCallBack myCallBack){
        for (int i = 0; i < 20; i++) {
            DatabaseReference intensity = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId).child(places.get(i).getPlaceId()).child("Intensity");
            DatabaseReference rating = FirebaseDatabase.getInstance().getReference().child("Places").child(places.get(i).getPlaceId()).child("Rating");
            int finalI = i;
            intensity.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        places.get(finalI).setPlaceIntensity(Integer.parseInt(dataSnapshot.getValue().toString()));
                        System.out.println("onDataChange" + places.get(finalI).getPlaceIntensity());
                        myCallBack.onCallback(places);

                    } catch (NullPointerException np) {
                        places.get(finalI).setPlaceIntensity(1);
                        System.out.println("onDataChange" + places.get(finalI).getPlaceIntensity());
                        myCallBack.onCallback(places);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
            rating.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        places.get(finalI).setTrevuRating(Double.parseDouble(dataSnapshot.getValue().toString()));
                        System.out.println("Rating "+places.get(finalI).getTrevuRating());
                    }catch (NullPointerException np){
                        places.get(finalI).setTrevuRating(1);
                        System.out.println("Rating "+places.get(finalI).getTrevuRating());
                    }
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
        places = new ArrayList<>(places.subList(0, 20));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_beverages);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        Runnable getGoogleJson = this::getGoogleJson;
        Runnable getFirebaseData = () -> getFirebaseData(value -> {
            doWeighting();

            listRecommendedFood.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, LinearLayoutManager.VERTICAL, false));
            FoodBeveragesTourismResultAdapter recommendedAdapter = new FoodBeveragesTourismResultAdapter(getApplication(), HalalBeveragesActivity.this, "HalalBeverages", places);
            listRecommendedFood.setAdapter(recommendedAdapter);

            progressDialog.dismiss();
        });

        generateListCategory();
        new GetRecommended(this).execute(getGoogleJson, getFirebaseData);

        manualCategory.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!manualCategory.getText().toString().equals("")) {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageTourismResult.class);
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
                        .inflate(R.layout.adapter_category_foodbeverages, parent, false);
                return new CatergoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CatergoryViewHolder holder, int position) {
                holder.categoryImage.setImageDrawable(getDrawable((int) categoryDataSet.get(position).get("category_image")));
                holder.categoryName.setText((String) categoryDataSet.get(position).get("category_name"));

                holder.categoryCard.setOnClickListener((v) -> {
                    Intent intent = new Intent(HalalBeveragesActivity.this, FoodBeverageTourismResult.class);
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

    private class GetRecommended extends AsyncTask<Runnable, Void, Void> {

        private Context context;

        GetRecommended(Context context) {
            this.context = context;
            places = new ArrayList<>();
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
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching result...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listRecommendedFood.setLayoutManager(new LinearLayoutManager(HalalBeveragesActivity.this, LinearLayoutManager.VERTICAL, false));
            FoodBeveragesTourismResultAdapter recommendedAdapter = new FoodBeveragesTourismResultAdapter(getApplication(), HalalBeveragesActivity.this, "HalalBeverages", places);
            listRecommendedFood.setAdapter(recommendedAdapter);

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


