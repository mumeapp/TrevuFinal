package com.remu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class HalalFoodRestaurantActivity extends AppCompatActivity {
    public static String ID="id";
    private ImageView imgFotoMkn;
    private TextView nama, rating, deskripsi;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran);


//        nama = findViewById(R.id.tvNama1);
//        rating = findViewById(R.id.tvRating1);
//        deskripsi = findViewById(R.id.tvDeskripsi1);
//        imgFotoMkn = findViewById(R.id.imgFoto1);


        String id = getIntent().getStringExtra(ID);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("HalaFood").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Glide.with(HalalFoodRestaurantActivity.this).load(dataSnapshot.child("gambar").getValue().toString()).into(imgFotoMkn);

                nama.setText(dataSnapshot.child("nama").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

