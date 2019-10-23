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
import com.remu.POJO.Restoran;

import java.io.IOException;


public class RestoranActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    public static String kategori= "kategori";
    public static String Jenis = "jenis";
    public static String lat = "lat";
    public static String lang="lang";
    public static String nama= "nama";
    private EditText Namarestoran, Deskripsi;
    private Button Next, AlamatRestoran;
    private ImageView foto;
    private Uri filePath;
    private TextView Batal;
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

        this.Namarestoran = findViewById(R.id.inputKategori);
        this.AlamatRestoran = findViewById(R.id.inputAlamat);
        this.Deskripsi = findViewById(R.id.deskripsiSingkat);
        this.Next = findViewById(R.id.btn_Selanjutnya);
        this.Batal = findViewById(R.id.btn_batal);
        this.foto = findViewById(R.id.imageLogo);

        AlamatRestoran.setOnClickListener(View -> setAlamat());

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
                try {
                    StorageReference restoran = storageReference.child("Food").child("Restoran").child(getIntent().getStringExtra(Jenis));
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
                            Intent get = getIntent();
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String id;
                                String nama = Namarestoran.getText().toString();
                                String alamat = get.getStringExtra(lat) + ", " + get.getStringExtra(lang);
                                String deskripsi = Deskripsi.getText().toString();
                                String foto1 = downloadUri.toString();
                                String Kategori = get.getStringExtra(kategori);
                                String jenis = get.getStringExtra(Jenis);


                                Restoran restoran = new Restoran(nama, alamat, foto1, deskripsi);
                                try{
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child(jenis).child(Kategori).push();
                                id = databaseReference.toString();
                                databaseReference.setValue(restoran).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RestoranActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }catch (Exception e){
                                    Toast.makeText(RestoranActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(RestoranActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void setAlamat(){
        String jenis = getIntent().getStringExtra(Jenis);
        String kate = getIntent().getStringExtra(kategori);
        Intent in = new Intent(this, SetAddressActivity.class);
        in.putExtra(SetAddressActivity.Jenis,jenis);
        in.putExtra(SetAddressActivity.Kategori, kate);
        startActivity(in);
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
