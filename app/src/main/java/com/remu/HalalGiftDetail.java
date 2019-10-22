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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.HalalFood;
import com.remu.POJO.HalalFoodRestaurant;
import com.remu.POJO.Restoran;

public class HalalGiftDetail extends AppCompatActivity {

    public static final String ID = "HalalFoodRestaurantActivity";
    public static String Nama= "Nama", gambar= "url";
    private String nama = null;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Restoran, HalalGiftDetail.HalalGiftDetailViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvRestaurant;
    private CardView cd;
    private Intent getID;
    private String id;
    private ImageView img;
    private TextView kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);

        initializeUI();

        setImage(getIntent().getStringExtra(gambar));
        kategori.setText(nama);
        rvRestaurant.setLayoutManager(new LinearLayoutManager(HalalGiftDetail.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
                .setQuery(query, Restoran.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, HalalGiftDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HalalGiftDetailViewHolder halalFoodRestaurantViewHolder, int i, @NonNull Restoran halalFoodRestaurant) {

                halalFoodRestaurantViewHolder.setGambar(halalFoodRestaurant.getFoto());
                halalFoodRestaurantViewHolder.setNamaRestoran(halalFoodRestaurant.getNamaRestoran());
                halalFoodRestaurantViewHolder.setRating("5.0");
                halalFoodRestaurantViewHolder.setJarak("0.3 KM");

                id = halalFoodRestaurant.getId();

                halalFoodRestaurantViewHolder.itemView.setOnClickListener(view -> {

                    Intent intent = new Intent(HalalGiftDetail.this, HalalRestaurantDetailActivity.class);
                    intent.putExtra(HalalRestaurantDetailActivity.ID, id);

                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HalalGiftDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restoran, parent, false);

                return new HalalGiftDetailViewHolder(view);
            }
        };

        rvRestaurant.setAdapter(firebaseRecyclerAdapter);
        cd.setOnClickListener(view -> addFood());

    }


    private void initializeUI() {
        getID = getIntent();
        nama = getID.getStringExtra(Nama);
        rvRestaurant = findViewById(R.id.HalalRestauran);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child("Gift").child(nama);
        cd = findViewById(R.id.addReastaurant);
        img = findViewById(R.id.kategoriGambar);
        kategori = findViewById(R.id.kategori);
    }

    private void addFood(){
        String Jenis = "Gift";
        Intent intent = new Intent(HalalGiftDetail.this, RestoranActivity.class);
        intent.putExtra(RestoranActivity.kategori, nama);
        intent.putExtra(RestoranActivity.Jenis, Jenis);
        startActivity(intent);

    }

    private void setImage(String url){
        img = findViewById(R.id.kategoriGambar);
        Glide.with(HalalGiftDetail.this)
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

    public class HalalGiftDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView namaRestoran;
        TextView jarak;
        TextView rating;

        public HalalGiftDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.gambarRestaurant);
            namaRestoran = itemView.findViewById(R.id.judul);
            rating = itemView.findViewById(R.id.rating);
            jarak = itemView.findViewById(R.id.Jarak);
        }

        public void setGambar(String foto) {

            Glide.with(HalalGiftDetail.this)
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
