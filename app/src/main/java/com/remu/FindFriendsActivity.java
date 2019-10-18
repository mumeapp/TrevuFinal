package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriendsActivity extends AppCompatActivity {
        private Button btn;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        database = FirebaseDatabase.getInstance().getReference();
        btn= findViewById(R.id.findFriend);
        btn.setOnClickListener(view -> button());

    }

    private void button (){
        Intent in = new Intent(FindFriendsActivity.this, FindFriends.class);
        startActivity(in);
    }
}
