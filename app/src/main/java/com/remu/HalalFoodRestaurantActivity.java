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
import androidx.cardview.widget.CardView;
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

public class HalalFoodRestaurantActivity extends AppCompatActivity {

    public static final String ID = "HalalFOodRestaurantActivity", Jenis = "HalalFood";
    public static String Nama= "Nama", url= "url";
    private String nama = null, myLat, myLong;
    private DatabaseReference databaseReference, getId;
    private FirebaseRecyclerAdapter<Restoran, HalalFoodRestaurantActivity.HalalFoodRestaurantViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvRestaurant;
    private CardView cd;
    private Intent getID;
    private ImageView img;
    private TextView kategori;
    private Double akumulasi=0.0;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);

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

        setImage(getIntent().getStringExtra(url));
        kategori.setText(nama);
        rvRestaurant.setLayoutManager(new LinearLayoutManager(HalalFoodRestaurantActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
                .setQuery(query, Restoran.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, HalalFoodRestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalFoodRestaurantViewHolder halalFoodRestaurantViewHolder, int i, @NonNull Restoran halalFoodRestaurant) {
                String LatLong = halalFoodRestaurant.getAlamatRestoran();
                String getLatLong[] = LatLong.split(",");
                String getLat= getLatLong[0], getLong=getLatLong[1];
                DecimalFormat df = new DecimalFormat("#.##");
                String id;

                double jarak = getJarak(Double.parseDouble(myLat), Double.parseDouble(getLat),Double.parseDouble(myLong), Double.parseDouble(getLong));
                halalFoodRestaurantViewHolder.setGambar(halalFoodRestaurant.getFoto());
                halalFoodRestaurantViewHolder.setNamaRestoran(halalFoodRestaurant.getNamaRestoran());
                halalFoodRestaurantViewHolder.setJarak(df.format(jarak)+" KM");
                halalFoodRestaurantViewHolder.setRating(Double.toString(halalFoodRestaurant.getAkumulasiRating()));
                id = halalFoodRestaurant.getID();
                //Toast.makeText(HalalFoodRestaurantActivity.this, restoran,Toast.LENGTH_LONG).show();


                halalFoodRestaurantViewHolder.itemView.setOnClickListener(view -> {

                    Intent intent = new Intent(HalalFoodRestaurantActivity.this, PlaceDetailActivity.class);
                    intent.putExtra(PlaceDetailActivity.Nama, nama);
                    intent.putExtra(PlaceDetailActivity.ID,id);
                    intent.putExtra(PlaceDetailActivity.Jenis, Jenis);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalFoodRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restoran, parent, false);

                return new HalalFoodRestaurantViewHolder(view);
            }
        };

        rvRestaurant.setAdapter(firebaseRecyclerAdapter);
        cd.setOnClickListener(view -> addFood());

    }


    private void initializeUI() {
        getID = getIntent();
        nama = getID.getStringExtra(Nama);
        rvRestaurant = findViewById(R.id.HalalRestauran);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child("HalalFood").child(nama);
        cd = findViewById(R.id.addReastaurant);
        img = findViewById(R.id.kategoriGambar);
        kategori = findViewById(R.id.kategori);
    }

    private void addFood(){
        Intent intent = new Intent(HalalFoodRestaurantActivity.this, RestoranActivity.class);
        intent.putExtra(RestoranActivity.kategori, nama);
        intent.putExtra(RestoranActivity.Jenis, Jenis);
        startActivity(intent);

    }

    private void setImage(String url){
        img = findViewById(R.id.kategoriGambar);
        Glide.with(HalalFoodRestaurantActivity.this)
                .load(url)
                .into(img);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            firebaseRecyclerAdapter.startListening();
        }catch (Exception e){

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            firebaseRecyclerAdapter.stopListening();
        }catch (Exception e){

        }
    }

    private double getJarak(double lat1, double lat2, double long1, double long2){
        Distance distance = new Distance();
        return distance.distance(lat1, lat2, long1, long2);
    }

    public class HalalFoodRestaurantViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView namaRestoran;
        TextView jarak;
        TextView rating;

        public HalalFoodRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.gambarRestaurant);
            namaRestoran = itemView.findViewById(R.id.judul);
            rating = itemView.findViewById(R.id.rating);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {

            Glide.with(HalalFoodRestaurantActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(this.foto);
        }

        public void setNamaRestoran(String text) {
            namaRestoran.setText(text);
        }

        public void setRating(String text) {
            rating.setText(text);
        }

        public void setJarak(String text) {
                jarak.setText(text);
        }
    }

}
