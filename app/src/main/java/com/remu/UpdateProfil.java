package com.remu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateProfil extends AppCompatActivity {

    private Button btUpdate;
    private ImageView btDataUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btUpdate = (Button) findViewById(R.id.update_Profil);
        btDataUser = (ImageView) findViewById(R.id.profil_photo);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Profil.getActIntent(UpdateProfil.this));
            }
        });




    }

}
