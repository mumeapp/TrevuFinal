package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class TourismDetailActivity extends AppCompatActivity {
    public static String gambar = "gambar", nama="nama", id = "id", latlong="latlong", rating="rating";
    private TextView namaTempat, ratingTempat, loadReview;
    private ImageView gambarTempat;
    private Button addReview;
    private String idTempat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_detail);

        initialize();

        namaTempat.setText(getIntent().getStringExtra(nama));
        ratingTempat.setText(Double.toString(getIntent().getDoubleExtra(rating, 0)));

        Glide.with(TourismDetailActivity.this)
                .load(getIntent().getStringExtra(gambar))
                .placeholder(R.drawable.bg_loading)
                .into(gambarTempat);

        loadReview.setOnClickListener(VIew->loadReview());
        addReview.setOnClickListener(View ->addReview());
    }

    private void initialize(){
        namaTempat = findViewById(R.id.nama);
        ratingTempat = findViewById(R.id.rating);
        gambarTempat = findViewById(R.id.gambarTempat);
        loadReview = findViewById(R.id.loadReview);
        addReview = findViewById(R.id.addReview);
        idTempat = getIntent().getStringExtra(id);
    }
    private void addReview(){
        Intent intent = new Intent(TourismDetailActivity.this, RatingActivity.class);
        intent.putExtra(RatingActivity.ID, idTempat);
        intent.putExtra(RatingActivity.NamaRestoran, getIntent().getStringExtra(nama));
        intent.putExtra(RatingActivity.Nama, "Wisata");
        intent.putExtra(RatingActivity.Jenis, "Wisata");
        intent.putExtra("akumulasi", getIntent().getDoubleExtra(rating, 0));
        startActivity(intent);
    }

    private void loadReview(){
        Intent in = new Intent(TourismDetailActivity.this, CommentActivity.class);
        in.putExtra(CommentActivity.Nama, "Wisata");
        in.putExtra(CommentActivity.Jenis, "Wisata");
        in.putExtra(CommentActivity.NamaRestoran, getIntent().getStringExtra(nama));
        startActivity(in);
    }
}
