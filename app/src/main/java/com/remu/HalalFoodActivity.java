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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.HalalFood;

public class HalalFoodActivity extends AppCompatActivity {

    private RecyclerView rvFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        initializeUI();
        FirebaseDatabase.getInstance().getReference().child("Food").child("HalalFood");
        DatabaseReference databaseReference;
        rvFood.setHasFixedSize(true);
        rvFood.setLayoutManager(new LinearLayoutManager(HalalFoodActivity.this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Gift");
        Query query = databaseReference.orderByKey();
        FirebaseRecyclerOptions<HalalFood> options = new FirebaseRecyclerOptions.Builder<HalalFood>()
                .setQuery(query, HalalFood.class).build();


        FirebaseRecyclerAdapter<HalalFood, HalalFoodViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HalalFood, HalalFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalFoodViewHolder halalFoodViewHolder, int i, @NonNull HalalFood HalalFood) {
                halalFoodViewHolder.setGambar(HalalFood.getGambar());
                halalFoodViewHolder.setJudul(HalalFood.getNama());
                halalFoodViewHolder.setJumlah(HalalFood.getJumlah());
                halalFoodViewHolder.setJarak(HalalFood.getJarak());

                String id = HalalFood.getId();


                halalFoodViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(HalalFoodActivity.this, HalalFoodRestaurantActivity.class);
                    intent.putExtra(HalalFoodRestaurantActivity.id, id);
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
    }

    private void initializeUI() {
        rvFood = findViewById(R.id.HalalFoodCategories);
    }

    public class HalalFoodViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoMkn;
        TextView judul;
        TextView jarak;
        TextView jumlah;

        HalalFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoMkn = itemView.findViewById(R.id.Gambarkategoi);
            judul = itemView.findViewById(R.id.NamaKategori);
            jumlah = itemView.findViewById(R.id.JumlahRestoran);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        void setGambar(String foto) {
            Glide.with(HalalFoodActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(fotoMkn);
        }

        void setJudul(String text) {
            judul.setText(text);
        }

        void setJumlah(String text) {
            jumlah.setText(text);
        }

        void setJarak(String text) {
            jarak.setText(text);
        }
    }

}
