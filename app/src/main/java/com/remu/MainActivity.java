package com.remu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView mosqueCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize uI
        initializeUI();

        mosqueCardView.setOnClickListener(view -> {
            Intent viewMosque = new Intent(MainActivity.this, MosqueActivity.class);
            startActivity(viewMosque);
        });
    }

    private void initializeUI() {
        mosqueCardView = findViewById(R.id.MosqueCardView);
    }
}
