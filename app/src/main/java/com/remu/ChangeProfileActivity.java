package com.remu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.saber.chentianslideback.SlideBackActivity;

public class ChangeProfileActivity extends SlideBackActivity {

    private AutoCompleteTextView genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        initializeUI();
        Animatoo.animateSlideLeft(this);

        initializeSpinner();

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
        genderSpinner = findViewById(R.id.ep_gender);
    }

    private void initializeSpinner() {
        String[] arrayOfGender = new String[]{"Male", "Female"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.adapter_gender_edit_profile, arrayOfGender);
        genderSpinner.setAdapter(arrayAdapter);
    }

}
