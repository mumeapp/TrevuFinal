package com.remu;

import android.content.Context;
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

public class HalalFastFoodRestaurantActivity extends AppCompatActivity {

    public static String Nama= "Nama", gambar = "gambar";
    private String nama = null;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Restoran, HalalFastFoodRestaurantActivity.HalalFastFoodRestaurantViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvRestaurant;
    private CardView cd;
    private Intent getID;
    private String id, Gambar;
    private ImageView img;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);

        initializeUI();

        setImage(Gambar);
        tv.setText(nama);
        rvRestaurant.setLayoutManager(new LinearLayoutManager(HalalFastFoodRestaurantActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
                .setQuery(query, Restoran.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, HalalFastFoodRestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalFastFoodRestaurantViewHolder halalFoodRestaurantViewHolder, int i, @NonNull Restoran halalFoodRestaurant) {

                halalFoodRestaurantViewHolder.setGambar(halalFoodRestaurant.getFoto());

                halalFoodRestaurantViewHolder.setNamaRestoran(halalFoodRestaurant.getNamaRestoran());
                halalFoodRestaurantViewHolder.setRating("5.0");
                halalFoodRestaurantViewHolder.setJarak("0.3 KM");

                id = halalFoodRestaurant.getId();

                halalFoodRestaurantViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(HalalFastFoodRestaurantActivity.this, HalalRestaurantDetailActivity.class);
                    intent.putExtra(HalalRestaurantDetailActivity.ID, id);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalFastFoodRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restoran, parent, false);

                return new HalalFastFoodRestaurantViewHolder(view);
            }
        };

        rvRestaurant.setAdapter(firebaseRecyclerAdapter);
        cd.setOnClickListener(view -> addFood());

    }


    private void initializeUI() {
        getID = getIntent();
        nama = getID.getStringExtra(Nama);
        rvRestaurant = findViewById(R.id.HalalRestauran);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child("HalalFastFood").child(nama);
        cd = findViewById(R.id.addReastaurant);
        Gambar = getIntent().getStringExtra(gambar);
        tv = findViewById(R.id.kategori);
        this.img = findViewById(R.id.Gambarkategoi);
    }

    private void setImage(String url){
        img = findViewById(R.id.kategoriGambar);
        Glide.with(HalalFastFoodRestaurantActivity.this)
                .load(url)
                .into(img);
    }

    private void addFood(){
        String Jenis = "HalalFastFood";
        Intent intent = new Intent(HalalFastFoodRestaurantActivity.this, RestoranActivity.class);
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

    public class HalalFastFoodRestaurantViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView namaRestoran;
        TextView jarak;
        TextView rating;

        public HalalFastFoodRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.gambarRestaurant);
            namaRestoran = itemView.findViewById(R.id.judul);
            rating = itemView.findViewById(R.id.rating);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {

            Glide.with(HalalFastFoodRestaurantActivity.this)
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
