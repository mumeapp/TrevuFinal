package com.remu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.remu.POJO.User;

import java.io.IOException;

public class Profil extends AppCompatActivity {

    private DatabaseReference database;

    // variable fields EditText dan Button
    private final int PICK_IMAGE_REQUEST = 71;
    private Button btUpdate;
    private EditText etNama;
    private EditText etKelamin;
    private EditText etUsia;
    private EditText tglLahir;
    private ImageView etPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        etNama = (EditText) findViewById(R.id.et_nama);
        etKelamin = (EditText) findViewById(R.id.et_kelamin);
        etUsia = (EditText) findViewById(R.id.et_umur);
        tglLahir = (EditText) findViewById(R.id.et_tanggalLahir);
        btUpdate = (Button) findViewById(R.id.update_Profil);
        etPhoto = (ImageView) findViewById(R.id.upload);

        etPhoto.setOnClickListener(new View.OnClickListener() {     // klik
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });


        database = FirebaseDatabase.getInstance().getReference();

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String id = currentUser.getUid();
                    if(currentUser != null) {
                        database.child("User").child(id).child("nama").setValue(etNama.getText().toString());
                        database.child("User").child(id).child("gender").setValue(etKelamin.getText().toString());
                        database.child("User").child(id).child("age").setValue(etUsia.getText().toString());
                        database.child("User").child(id).child("tanggal").setValue(tglLahir.getText().toString());
                        database.child("User").child(id).child("foto").setValue(tglLahir.getText().toString());
                        finish();
//                    updateBarang(user);
                    }
                }
            });

    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void updateBarang(User user) {


    }


    public static Intent getActIntent(Activity activity) {

        return new Intent(activity, Profil.class);
    }
}
