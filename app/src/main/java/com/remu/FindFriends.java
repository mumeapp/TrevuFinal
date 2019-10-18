package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriends extends AppCompatActivity {
    private ImageView img;
    private Button btn, btn2;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends2);



        btn = findViewById(R.id.invis);
        btn2 = findViewById(R.id.stopButton) ;
        btn.setVisibility(View.INVISIBLE);
        img = findViewById(R.id.imageView3);

        btn.setOnClickListener(view -> invis());
        mHandler.postDelayed(() -> {
            btn.performClick();
        }, 3000);
        btn2.setOnClickListener(view -> button());
        }

        public void invis(){

            img.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            System.out.println("berhasil");
        }
        public void button(){
            Intent in = new Intent(FindFriends.this, FriendResult.class);
            startActivity(in);
        }

    }


