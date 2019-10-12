package com.remu;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.alespero.expandablecardview.ExpandableCardView;

public class MosqueActivity extends AppCompatActivity {

    ExpandableCardView jamSolat;
    RelativeLayout someInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque);

        //initialize ui
        initializeUI();

        jamSolat.setOnExpandedListener((v, isExpanded) -> {
            if (isExpanded) {
                jamSolat.setTitle("Jadwal Sholat Hari Ini");
                someInformation.setVisibility(View.INVISIBLE);
            } else {
                jamSolat.setTitle("Jadwal Sholat Selanjutnya");
                someInformation.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initializeUI() {
        jamSolat = findViewById(R.id.jamSolat);
        someInformation = findViewById(R.id.someInformation);
    }
}
