package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodActivity extends AppCompatActivity {

    ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        //initialize ui
        initializeUI();

        //set intent to back to previous activity
        buttonBack.setOnClickListener(view -> {
            Intent BackToMenu = new Intent(FoodActivity.this, MainActivity.class);
            startActivity(BackToMenu);
        });
    }

    private void initializeUI() {
        buttonBack = findViewById(R.id.ButtonBack);
    }
}
