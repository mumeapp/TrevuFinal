package com.remu;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.saber.chentianslideback.SlideBackActivity;

public class FoodActivity extends SlideBackActivity {

    CardView buttonHalalFood, buttonFastFood, buttonGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        //initialize ui
        initializeUI();
        Animatoo.animateSlideLeft(this);

        //set intent for halal food category list
        buttonHalalFood.setOnClickListener(view -> {
            Intent halalFoodCategories = new Intent(FoodActivity.this, HalalFoodActivity.class);
            startActivity(halalFoodCategories);
        });

        //set intent to back to previous activity
        buttonFastFood.setOnClickListener(view -> {
            Intent BackToMenu = new Intent(FoodActivity.this, HalalFastFoodActivity.class);
            startActivity(BackToMenu);
        });

        //set intent to back to previous activity
        buttonGift.setOnClickListener(view -> {
            Intent BackToMenu = new Intent(FoodActivity.this, HalalGiftActivity.class);
            startActivity(BackToMenu);
        });

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        buttonHalalFood = findViewById(R.id.halalFoodButton);
        buttonFastFood = findViewById(R.id.HalalFastFoodButton);
        buttonGift = findViewById(R.id.GiftIdeaButton);
    }
}
