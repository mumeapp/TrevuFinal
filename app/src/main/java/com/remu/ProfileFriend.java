package com.remu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.User;

public class ProfileFriend extends AppCompatActivity {

    public static String id = "id";

    private String getIdUser;
    private DatabaseReference database;

    private ImageView gambarUser;
    private TextView namaUser;
    private TextView genderUser;
    private TextView UmurUser;
    private TextView EmailUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        namaUser = findViewById(R.id.nama);
        genderUser = findViewById(R.id.Gender);
        UmurUser = findViewById(R.id.Usia);
        EmailUser = findViewById(R.id.email_friend);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_friend);

        getIdUser = getIntent().getStringExtra(id);
        Toast.makeText(ProfileFriend.this, getIdUser,Toast.LENGTH_LONG).show();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                namaUser.setText(user.getNama());
                genderUser.setText(user.getGender());
                UmurUser.setText(user.getAge());
                EmailUser.setText(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
