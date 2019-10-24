package com.remu;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.Distance;
import com.remu.POJO.Restoran;

import java.text.DecimalFormat;

public class HalalGiftActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Restoran, HalalGiftActivity.HalalGiftViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvFood;
    private ImageView addGift;
    private String myLat, myLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_gift);


        initializeUI();

        //Get Current Location
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    myLat = Double.toString(location.getLatitude());
                    myLong = Double.toString(location.getLongitude());
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    // Display in Toast
//                    Toast.makeText(HalalFastFoodRestaurantActivity.this,
//                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
//                            Toast.LENGTH_LONG).show();
                }
            }
        });

        rvFood.setLayoutManager(new LinearLayoutManager(HalalGiftActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
                .setQuery(query, Restoran.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, HalalGiftActivity.HalalGiftViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalGiftActivity.HalalGiftViewHolder halalGiftViewHolder, int i, @NonNull Restoran halalGift) {
                String LatLong = halalGift.getAlamatRestoran();
                String getLatLong[] = LatLong.split(",");
                String getLat= getLatLong[0], getLong=getLatLong[1];
                DecimalFormat df = new DecimalFormat("#.##");
                double jarak = getJarak(Double.parseDouble(myLat), Double.parseDouble(getLat),Double.parseDouble(myLong), Double.parseDouble(getLong));

                halalGiftViewHolder.setGambar(halalGift.getFoto());
                halalGiftViewHolder.setJudul(halalGift.getNamaRestoran());
                halalGiftViewHolder.setJarak(df.format(jarak)+" KM");
                halalGiftViewHolder.setRating("5,0");

                String id = halalGift.getId();

                halalGiftViewHolder.itemView.setOnClickListener(view -> {
                    String gambar = halalGift.getFoto();
                    Intent intent = new Intent(HalalGiftActivity.this, HalalFoodRestaurantActivity.class);
                    intent.putExtra(HalalFoodRestaurantActivity.Nama, id);
                    intent.putExtra(HalalFoodRestaurantActivity.url,gambar);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalGiftActivity.HalalGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restoran, parent, false);

                return new HalalGiftActivity.HalalGiftViewHolder(view);
            }
        };

        rvFood.setAdapter(firebaseRecyclerAdapter);
        addGift.setOnClickListener(View -> addGift());
    }

    private void initializeUI() {
        rvFood = findViewById(R.id.rv_listGift);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Restoran").child("Gift").child("Gift");
        addGift = findViewById(R.id.addGift);
        //cd = findViewById(R.id.addFood);
    }

    private void addGift(){
        String jenis= "Gift";
        Intent intent = new Intent(HalalGiftActivity.this, RestoranActivity.class);
        intent.putExtra(RestoranActivity.kategori, "Gift");
        intent.putExtra(RestoranActivity.Jenis, jenis);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {
            firebaseRecyclerAdapter.startListening();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            firebaseRecyclerAdapter.stopListening();
        } catch (Exception e) {

        }
    }

    private double getJarak(double lat1, double lat2, double long1, double long2){
        Distance distance = new Distance();
        return distance.distance(lat1, lat2, long1, long2);
    }

    public class HalalGiftViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoMkn;
        TextView judul;
        TextView jarak;
        TextView rating;

        public HalalGiftViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoMkn = itemView.findViewById(R.id.gambarRestaurant);
            judul = itemView.findViewById(R.id.judul);
            rating = itemView.findViewById(R.id.rating);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {
            Glide.with(HalalGiftActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(fotoMkn);
        }

        public void setJudul(String text) {
            judul.setText(text);
        }

        public void setRating(String text) {
            rating.setText(text);
        }

        public void setJarak(String text) {
            jarak.setText(text);
        }
    }

}
