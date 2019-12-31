package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.saber.chentianslideback.SlideBackActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HalalFoodActivity extends SlideBackActivity {

//    private DatabaseReference databaseReference;
//    private FirebaseRecyclerAdapter<HalalFood, HalalFoodActivity.HalalFoodViewHolder> firebaseRecyclerAdapter;
//    private RecyclerView rvFood;
//    private CardView cd;

    private static final String TAG = "HalalFoodActivity";
    private static final String API_KEY = "AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

    private double latitude, longitude;
    private PlacesClient placesClient;

    private LinearLayout layoutMidnight;
    private RecyclerView listOpenAtNight, listRecommendedFood;
    private EditText manualCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), API_KEY);

        // Create a new Places client instance
        placesClient = Places.createClient(this);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

        generateListOpenNight();
        generateRecommendedFood();

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
        layoutMidnight = findViewById(R.id.ly_midnight);
        listOpenAtNight = findViewById(R.id.listOpenAtNight);
        listRecommendedFood = findViewById(R.id.listRecommendedFood);
        manualCategory = findViewById(R.id.et_manual_category);
    }

    private void generateListOpenNight() {
        switch (Integer.parseInt(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()))) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 22:
            case 23:
                layoutMidnight.setVisibility(View.VISIBLE);
                //TODO: Make recycler view adapter
                break;
            default:
                layoutMidnight.setVisibility(View.GONE);
        }
    }

    private void generateRecommendedFood() {
        //TODO: Make recycler view adapter
    }
//
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
