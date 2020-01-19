package com.remu;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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
import com.remu.adapter.MidnightFoodAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class HalalFoodActivity extends SlideBackActivity {

    private static final String TAG = "HalalFoodActivity";

    private double latitude, longitude;

    private LinearLayout layoutMidnight;
    private RecyclerView listCategory, listOpenAtNight, listRecommendedFood;
    private EditText manualCategory;
    private ArrayList<PlaceModel> places;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));
        userId = FirebaseAuth.getInstance().getUid();

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListCategory();

        Runnable getGoogleJSON = this::getGoogleJson;
        Runnable getFirebaseData = () -> getFirebaseData(value -> {
            doWeighting();
            listRecommendedFood.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this, LinearLayoutManager.VERTICAL, false));
            FoodBeveragesTourismResultAdapter recommendedAdapter = new FoodBeveragesTourismResultAdapter(getApplication(), HalalFoodActivity.this, "HalalFood", places);
            listRecommendedFood.setAdapter(recommendedAdapter);
            progressDialog.dismiss();
        });

        generateListOpenNight();
        new GetRecommended(this).execute(getGoogleJSON, getFirebaseData);

        manualCategory.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!manualCategory.getText().toString().equals("")) {
                    Intent intent = new Intent(HalalFoodActivity.this, FoodBeverageTourismResult.class);
                    intent.putExtra("sender", "HalalFood");
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
        layoutMidnight = findViewById(R.id.ly_food_midnight);
        listCategory = findViewById(R.id.listFoodCategory);
        listOpenAtNight = findViewById(R.id.listFoodOpenAtNight);
        listRecommendedFood = findViewById(R.id.listRecommendedFood);
        manualCategory = findViewById(R.id.et_manual_food_category);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private String changeSpace(String input) {
        String[] strings = input.split(" ");
        String returnVal = "";

        for (int i = 0; i < strings.length; i++) {
            if (i + 1 != strings.length) {
                returnVal += strings[i] + "%20";
            } else {
                returnVal += strings[i];
            }
        }

        return returnVal;
    }

    private void generateListCategory() {
        ArrayList<HashMap<String, Object>> categoryDataSet = new ArrayList<HashMap<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("category_name", "Beef");
                put("keyword", "beef");
                put("category_image", R.drawable.foodcategory_beef);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Burgers");
                put("keyword", "burgers");
                put("category_image", R.drawable.foodcategory_burgers);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Chicken Delight");
                put("keyword", "chicken");
                put("category_image", R.drawable.foodcategory_chicken);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Chinese");
                put("keyword", "chinese");
                put("category_image", R.drawable.foodcategory_chinese);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Duck");
                put("keyword", "duck");
                put("category_image", R.drawable.foodcategory_duck);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Fried Chicken");
                put("keyword", "fried%20chicken");
                put("category_image", R.drawable.foodcategory_friedchicken);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Meatballs");
                put("keyword", "meatballs");
                put("category_image", R.drawable.foodcategory_meatballs);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Pizza and Pasta");
                put("keyword", "pizza%20pasta");
                put("category_image", R.drawable.foodcategory_pizzapasta);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Ramen");
                put("keyword", "ramen");
                put("category_image", R.drawable.foodcategory_ramen);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Seafood");
                put("keyword", "seafood");
                put("category_image", R.drawable.foodcategory_seafood);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Sushi");
                put("keyword", "sushi");
                put("category_image", R.drawable.foodcategory_sushi);
            }});
        }};

        listCategory.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this, RecyclerView.HORIZONTAL, false));
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
                    Intent intent = new Intent(HalalFoodActivity.this, FoodBeverageTourismResult.class);
                    intent.putExtra("sender", "HalalFood");
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

    private void getGoogleJson() {
        HttpHandler httpHandler = new HttpHandler();

        String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                "6&rankby=distance&keyword=chicken&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
        String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                "6&rankby=distance&keyword=seafood&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
        String url3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                "6&rankby=distance&keyword=beef&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";
        String url4 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                "6&rankby=distance&keyword=duck&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

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
                        myCallBack.onCallback(places);

                    } catch (NullPointerException np) {
                        places.get(finalI).setPlaceIntensity(1);
                        System.out.println("onDataChange" + places.get(finalI).getPlaceIntensity());
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

    private void generateListOpenNight() {
        int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));

        if (currentHour < 7 || currentHour > 21) {
            layoutMidnight.setVisibility(View.VISIBLE);
            new GetMidnight(this).execute();
        } else {
            layoutMidnight.setVisibility(View.GONE);
        }
    }

    private class GetRecommended extends AsyncTask<Runnable, Void, Void> {

        private Context context;

        private String userId;

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

    private class GetMidnight extends AsyncTask<Void, Void, Void> {

        private Context context;

        private ProgressDialog progressDialog;
        private ArrayList<PlaceModel> places;

        GetMidnight(Context context) {
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
                    "&radius=5000&keyword=muslim%20food&opennow&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

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

            listOpenAtNight.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this, LinearLayoutManager.HORIZONTAL, false));
            MidnightFoodAdapter openAtNightAdapter = new MidnightFoodAdapter(getApplication(), HalalFoodActivity.this, places);
            listOpenAtNight.setAdapter(openAtNightAdapter);

            progressDialog.dismiss();
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
