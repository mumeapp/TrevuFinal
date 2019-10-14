package com.remu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodKategoriActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private UploadTask uploadTask;
    private EditText kategori;
    private Button selanjutnya, batal;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_kategori);

    }

    public void setKategori() {
        this.kategori = findViewById(R.id.inputKategori);
    }

    public void setFoto() {
        this.foto = findViewById(R.id.imageKategori);
        this.selanjutnya = findViewById(R.id.btn_Selanjutnya);
        this.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kategori1 = kategori.getText().toString();
            }
        });

    }


}
