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
import com.remu.POJO.Tourism;

public class TourismActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Tourism, TourismActivity.TourismViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);

        initializeUI();

        rvTour.setLayoutManager(new LinearLayoutManager(TourismActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Tourism> options = new FirebaseRecyclerOptions.Builder<Tourism>()
                .setQuery(query, Tourism.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tourism, TourismActivity.TourismViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TourismActivity.TourismViewHolder tourismViewHolder, int i, @NonNull Tourism tourism) {
                tourismViewHolder.setGambar(tourism.getGambar());
                tourismViewHolder.setNama(tourism.getNama());
                tourismViewHolder.setTempat(tourism.getTempat());
                tourismViewHolder.setRating(tourism.getRating());

                String ID = tourism.getId();

                tourismViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(TourismActivity.this, TourismDetail.class);
                    intent.putExtra(TourismDetail.ID, ID);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public TourismActivity.TourismViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kategori, parent, false);

                return new TourismActivity.TourismViewHolder(view);
            }
        };

        rvTour.setAdapter(firebaseRecyclerAdapter);
    }

    private void initializeUI() {
        //rvTour = findViewById(R.id.TourismCategorize);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Wisata");
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

    public class TourismViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nama;
        TextView tempat;
        TextView rating;

        public TourismViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.Gambarkategoi);
            nama = itemView.findViewById(R.id.NamaKategori);
            tempat = itemView.findViewById(R.id.JumlahRestoran);
            rating = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {
            Glide.with(TourismActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(this.foto);
        }

        public void setNama(String text) {
            nama.setText(text);
        }

        public void setTempat(String text) {
            tempat.setText(text);
        }

        public void setRating(String text) {
            rating.setText(text);
        }
    }

}
