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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.HalalFood;

public class HalalFoodActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<HalalFood, HalalFoodActivity.HalalFoodViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvFood;
    private CardView cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        initializeUI();
        Animatoo.animateSlideLeft(this);
        rvFood.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<HalalFood> options = new FirebaseRecyclerOptions.Builder<HalalFood>()
                .setQuery(query, HalalFood.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HalalFood, HalalFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalFoodViewHolder halalFoodViewHolder, int i, @NonNull HalalFood halalFood) {
                halalFoodViewHolder.setGambar(halalFood.getGambar());
                halalFoodViewHolder.setJudul(halalFood.getNama());
                halalFoodViewHolder.setJumlah(halalFood.getJarak());
                halalFoodViewHolder.setJarak(halalFood.getJarak());

                String nama = halalFood.getNama();
                String url = halalFood.getGambar();
                halalFoodViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(HalalFoodActivity.this, HalalFoodRestaurantActivity.class);
                    intent.putExtra(HalalFoodRestaurantActivity.Nama, nama);
                    intent.putExtra(HalalFoodRestaurantActivity.url, url);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kategori, parent, false);

                return new HalalFoodViewHolder(view);
            }
        };

        rvFood.setAdapter(firebaseRecyclerAdapter);
        cd.setOnClickListener(view -> addFood());

    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        rvFood = findViewById(R.id.HalalFoodCategories);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("HalalFood");
        cd = findViewById(R.id.addFood);
    }

    private void addFood(){
        Intent intent = new Intent(HalalFoodActivity.this, FoodKategoriActivity.class);
        intent.putExtra(FoodKategoriActivity.Kategori, "HalalFood");
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

    public class HalalFoodViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoMkn;
        TextView judul;
        TextView jarak;
        TextView jumlah;

        public HalalFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoMkn = itemView.findViewById(R.id.Gambarkategoi);
            judul = itemView.findViewById(R.id.NamaKategori);
            jumlah = itemView.findViewById(R.id.JumlahRestoran);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {
            Glide.with(HalalFoodActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(fotoMkn);
        }

        public void setJudul(String text) {
            judul.setText(text);
        }

        public void setJumlah(String text) {
            jumlah.setText(text);
        }

        public void setJarak(String text) {
            jarak.setText(text);
        }
    }

}
