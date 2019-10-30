package com.remu;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileFriend extends AppCompatActivity {

    public static String id = "id";

    private String getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_friend);

        getId = getIntent().getStringExtra(id);
        Toast.makeText(ProfileFriend.this, getId,Toast.LENGTH_LONG).show();
    }
}
