package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Restoran;

public class PlaceDetailActivity extends AppCompatActivity {

    public static String ID = "id", Jenis = "jenis", Nama = "nama";
    private TextView namaTempat, loadComment;
    private Button map, rating;
    private ImageView gambarTempat;
    private DatabaseReference databaseReference;
    private String namaRestoran;
    private double akumulasiRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        namaTempat = findViewById(R.id.namaTempat);
        map = findViewById(R.id.buttonMao);
        gambarTempat = findViewById(R.id.gambarTempat);
        rating = findViewById(R.id.comment);
        loadComment = findViewById(R.id.loadComment);

        String id = getIntent().getStringExtra(ID);
        String jenis = getIntent().getStringExtra(Jenis);
        String nama = getIntent().getStringExtra(Nama);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child(jenis).child(nama).child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restoran restoran = dataSnapshot.getValue(Restoran.class);
                namaTempat.setText(restoran.getNamaRestoran());
                akumulasiRating = restoran.getAkumulasiRating();
                namaRestoran = restoran.getNamaRestoran();
                Glide.with(PlaceDetailActivity.this)
                        .load(restoran.getFoto())
                        .placeholder(R.drawable.bg_loading)
                        .into(gambarTempat);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rating.setOnClickListener(View -> buttonRating(id, jenis, nama, namaRestoran, akumulasiRating));
        loadComment.setOnClickListener(View->loadComment(jenis, nama, namaRestoran));
//       namaTempat.setText(databaseReference.getKey());


    }

    private void buttonRating(String id, String jenis, String nama, String namaRestoran, Double akumulasiRating) {
        Intent intent = new Intent(PlaceDetailActivity.this, RatingActivity.class);
        intent.putExtra(RatingActivity.ID, id);
        intent.putExtra(RatingActivity.Jenis, jenis);
        intent.putExtra(RatingActivity.Nama, nama);
        intent.putExtra(RatingActivity.NamaRestoran, namaRestoran);
        intent.putExtra("akumulasi", akumulasiRating);
        startActivity(intent);
    }

    private void loadComment(String jenis, String nama, String namaRestoran){
        Intent in = new Intent(PlaceDetailActivity.this, CommentActivity.class);
        in.putExtra(CommentActivity.Jenis, jenis);
        in.putExtra(CommentActivity.Nama, nama);
        in.putExtra(CommentActivity.NamaRestoran, namaRestoran);
        startActivity(in);

    }

}
