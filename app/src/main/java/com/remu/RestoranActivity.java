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


public class RestoranActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private EditText Namarestoran, AlamatRestoran, Deskripsi;
    private Button Next, Batal;
    private ImageView foto;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private UploadTask uploadTask;
    private DatabaseReference databaseReference;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_food_restaurant);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.idUser = currentUser.getUid();

        this.Namarestoran = findViewById(R.id.nameRestoran);
        this.AlamatRestoran = findViewById(R.id.alamatRestoran);
        this.Deskripsi = findViewById(R.id.deskripsi);
        this.Next = findViewById(R.id.Next);
        this.Batal = findViewById(R.id.batal);
        this.foto = findViewById(R.id.FotoRestoran);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference restoran = storageReference.child("Food").child("Fast Food");
                uploadTask = restoran.putFile(filePath);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RestoranActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        return restoran.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String id;
                            String nama = Namarestoran.getText().toString();
                            String alamat = AlamatRestoran.getText().toString();
                            String deskripsi = Deskripsi.getText().toString();
                            String foto1 = downloadUri.toString();

                            Restoran restoran = new Restoran(nama, alamat, foto1, deskripsi);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").push();
                            id = databaseReference.toString();
                            databaseReference.setValue(restoran).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RestoranActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
