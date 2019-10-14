package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FoodActivity extends AppCompatActivity {

    ImageView buttonBack;
    CardView buttonHalalFood;

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

        //set intent for halal food category list
        buttonHalalFood.setOnClickListener(view -> {
            Intent halalFoodCategories = new Intent(FoodActivity.this, HalalFoodActivity.class);
            startActivity(halalFoodCategories);
        });
    }

    private void initializeUI() {
        buttonBack = findViewById(R.id.ButtonBack);
        buttonHalalFood = findViewById(R.id.halalFoodButton);

    }
}
