package com.remu;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.HalalFood;
import com.remu.POJO.HalalFoodRestaurant;
import com.remu.POJO.Restoran;

public class HalalFoodRestaurantActivity extends AppCompatActivity {

    public static String Nama= "Nama";
    private String nama = null;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Restoran, HalalFoodRestaurantActivity.HalalFoodRestaurantViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvRestaurant;
    private CardView cd;
    private Intent getID;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);

        initializeUI();

        rvRestaurant.setLayoutManager(new LinearLayoutManager(HalalFoodRestaurantActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
                .setQuery(query, Restoran.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, HalalFoodRestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalFoodRestaurantViewHolder halalFoodRestaurantViewHolder, int i, @NonNull Restoran halalFoodRestaurant) {

                halalFoodRestaurantViewHolder.setGambar(halalFoodRestaurant.getFoto());

                halalFoodRestaurantViewHolder.setNamaRestoran(halalFoodRestaurant.getNamaRestoran());
                halalFoodRestaurantViewHolder.setRating("5.0");
                halalFoodRestaurantViewHolder.setJarak("0.3 KM");

                id = halalFoodRestaurant.getId();

                halalFoodRestaurantViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(HalalFoodRestaurantActivity.this, HalalRestaurantDetailActivity.class);
                    intent.putExtra(HalalRestaurantDetailActivity.ID, id);
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
    }

    private void addFood(){
        String Jenis = "HalalFood";
        Intent intent = new Intent(HalalFoodRestaurantActivity.this, RestoranActivity.class);
        intent.putExtra(RestoranActivity.kategori, nama);
        intent.putExtra(RestoranActivity.Jenis, Jenis);
        startActivity(intent);

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
