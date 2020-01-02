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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.maps.model.LatLng;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.Weighting;
import com.remu.adapter.MidnightFoodAdapter;
import com.remu.adapter.RecommendedFoodAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class HalalFoodActivity extends SlideBackActivity {

//    private DatabaseReference databaseReference;
//    private FirebaseRecyclerAdapter<HalalFood, HalalFoodActivity.HalalFoodViewHolder> firebaseRecyclerAdapter;
//    private RecyclerView rvFood;
//    private CardView cd;

    private static final String TAG = "HalalFoodActivity";

    private double latitude, longitude;

    private LinearLayout layoutMidnight;
    private RecyclerView listCategory, listOpenAtNight, listRecommendedFood;
    private EditText manualCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListCategory();
        generateListOpenNight();
        new GetRecommended(this).execute();

        manualCategory.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (manualCategory.getText() != null) {
                    Intent intent = new Intent(HalalFoodActivity.this, HalalFoodRestaurantActivity.class);
                    intent.putExtra("category", manualCategory.getText().toString());
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });

//        rvFood.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this));
//
//        Query query = databaseReference.orderByKey();
//
//        FirebaseRecyclerOptions<HalalFood> options = new FirebaseRecyclerOptions.Builder<HalalFood>()
//                .setQuery(query, HalalFood.class).build();
//
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HalalFood, HalalFoodViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull HalalFoodViewHolder halalFoodViewHolder, int i, @NonNull HalalFood halalFood) {
//                halalFoodViewHolder.setGambar(halalFood.getGambar());
//                halalFoodViewHolder.setJudul(halalFood.getNama());
//                halalFoodViewHolder.setJumlah(halalFood.getJarak());
//                halalFoodViewHolder.setJarak(halalFood.getJarak());
//
//                String nama = halalFood.getNama();
//                String url = halalFood.getGambar();
//                halalFoodViewHolder.itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(HalalFoodActivity.this, HalalFoodRestaurantActivity.class);
//                    intent.putExtra(HalalFoodRestaurantActivity.Nama, nama);
//                    intent.putExtra(HalalFoodRestaurantActivity.url, url);
//                    startActivity(intent);
//                });
//            }
//
//            @NonNull
//            @Override
//            public HalalFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kategori, parent, false);
//
//                return new HalalFoodViewHolder(view);
//            }
//        };

//        rvFood.setAdapter(firebaseRecyclerAdapter);
//        cd.setOnClickListener(view -> addFood());

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
    }

    private void generateListCategory() {
        ArrayList<HashMap<String, Object>> categoryDataSet = new ArrayList<HashMap<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("category_name", "Meatballs");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Beef");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Chicken Delight");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Chinese");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Ramen");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Duck");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Japanese");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Kebabs");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Korean");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Seafood");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Sushi");
                put("category_image", R.drawable.food);
            }});
            add(new HashMap<String, Object>() {{
                put("category_name", "Western");
                put("category_image", R.drawable.food);
            }});
        }};
        listCategory.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this, RecyclerView.HORIZONTAL, false));
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
            }

            @Override
            public int getItemCount() { return categoryDataSet.size(); }
        };
        listCategory.setAdapter(categoryAdapter);
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

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "6&rankby=distance&type=restaurant&keyword=recommended&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

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

        private void doWeighting(){
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

            listRecommendedFood.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this, LinearLayoutManager.VERTICAL, false));
            RecommendedFoodAdapter recommendedAdapter = new RecommendedFoodAdapter(getApplication(), HalalFoodActivity.this, places);
            listRecommendedFood.setAdapter(recommendedAdapter);

            progressDialog.dismiss();
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
                    "&radius=5000&type=restaurant&keyword=muslim%20food&opennow&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8\n";

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

        CatergoryViewHolder(View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.food_category_image);
            categoryName = itemView.findViewById(R.id.food_category_name);
        }

    }

//    public class HalalFoodViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView fotoMkn;
//        TextView judul;
//        TextView jarak;
//        TextView jumlah;
//
//        public HalalFoodViewHolder(@NonNull View itemView) {
//            super(itemView);
//            fotoMkn = itemView.findViewById(R.id.Gambarkategoi);
//            judul = itemView.findViewById(R.id.NamaKategori);
//            jumlah = itemView.findViewById(R.id.JumlahRestoran);
//            jarak = itemView.findViewById(R.id.Jarak);
//        }
//
//        public void setGambar(String foto) {
//            Glide.with(HalalFoodActivity.this)
//                    .load(foto)
//                    .placeholder(R.drawable.bg_loading)
//                    .into(fotoMkn);
//        }
//
//        public void setJudul(String text) {
//            judul.setText(text);
//        }
//
//        public void setJumlah(String text) {
//            jumlah.setText(text);
//        }
//
//        public void setJarak(String text) {
//            jarak.setText(text);
//        }
//    }

}
