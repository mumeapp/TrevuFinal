package com.remu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class FoodKategoriActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private UploadTask uploadTask;
    private EditText kategori;
    private Button selanjutnya;
    private TextView batal;
    private ImageView foto;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String idUser;
    public static String Kategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_kategori);
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.idUser = currentUser.getUid();
        setKategori();
        setFoto();
        batal = findViewById(R.id.btn_batal);
        //batalin input kategori
        batal.setOnClickListener(view -> {
            Intent intent = new Intent(this, HalalFoodActivity.class);
            startActivity(intent);
        });

    }

    public void setKategori() {
        this.kategori = findViewById(R.id.inputKategori);
    }

    public EditText getKategori() {
        return this.kategori;
    }

    public void setFoto() {
        this.foto = findViewById(R.id.setImage);
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
                StorageReference kategoriFood = storageReference.child("Food").child("kategori");
                uploadTask = kategoriFood.putFile(filePath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(FoodKategoriActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        return kategoriFood.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String id;
                            String kategori = getKategori().getText().toString();
                            String foto = downloadUri.toString();
                            String kate = getIntent().getStringExtra(Kategori);

                            Kategori kategori1 = new Kategori(kategori, foto);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child(kate);
                            id = databaseReference.getKey();
                            databaseReference.setValue(kategori1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(FoodKategoriActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        databaseReference.child("id").setValue(id);
                                        databaseReference.child("jumlah").setValue("0 Restaurant");
                                        databaseReference.child("jarak").setValue("0 KM");
                                        Intent in = new Intent(FoodKategoriActivity.this, FoodActivity.class);
                                        startActivity(in);
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   // nampilin gambar
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



