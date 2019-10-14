package com.remu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HalalFoodActivity extends AppCompatActivity {

    RecyclerView halalFoodCategories;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food);

        initializeUI();
        mDataSet = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDataSet.add("Title #" + i);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        halalFoodCategories.setLayoutManager(layoutManager);
        mAdapter = new MosqueAdapter(mDataSet);
        halalFoodCategories.setAdapter(mAdapter);
    }

    private void initializeUI() {
        halalFoodCategories = findViewById(R.id.HalalFoodCategories);
    }


}
