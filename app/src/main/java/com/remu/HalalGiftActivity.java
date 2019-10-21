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
import com.remu.POJO.HalalGift;

public class HalalGiftActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<HalalGift, HalalGiftActivity.HalalGiftViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_gift);


        initializeUI();

        rvFood.setLayoutManager(new LinearLayoutManager(HalalGiftActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<HalalGift> options = new FirebaseRecyclerOptions.Builder<HalalGift>()
                .setQuery(query, HalalGift.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HalalGift, HalalGiftActivity.HalalGiftViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalGiftActivity.HalalGiftViewHolder halalGiftViewHolder, int i, @NonNull HalalGift halalGift) {
                halalGiftViewHolder.setGambar(halalGift.getGambar());
                halalGiftViewHolder.setJudul(halalGift.getNama());
                halalGiftViewHolder.setJarak(halalGift.getJarak());

                String id = halalGift.getId();

                halalGiftViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(HalalGiftActivity.this, HalalGiftDetail.class);
                    intent.putExtra(HalalGiftDetail.ID, id);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalGiftActivity.HalalGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kategori, parent, false);

                return new HalalGiftActivity.HalalGiftViewHolder(view);
            }
        };

        rvFood.setAdapter(firebaseRecyclerAdapter);
    }

    private void initializeUI() {
        rvFood = findViewById(R.id.HalalFoodCategories);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Gift");
        //cd = findViewById(R.id.addFood);
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

    public class HalalGiftViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoMkn;
        TextView judul;
        TextView jarak;
        TextView jumlah;

        public HalalGiftViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoMkn = itemView.findViewById(R.id.Gambarkategoi);
            judul = itemView.findViewById(R.id.NamaKategori);
            jumlah = itemView.findViewById(R.id.JumlahRestoran);
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

        public void setJumlah(String text) {
            jumlah.setText(text);
        }

        public void setJarak(String text) {
            jarak.setText(text);
        }
    }

}
