package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class chooseLanguageActivity extends AppCompatActivity {

    public static String test = "test";
    private ListView list;
    private ArrayList<String>arr= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        list= findViewById(R.id.listBahasa);
        arr.add("Indonesia");
        arr.add("Inggris");
        arr.add("Melayu");

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, arr);

        list.setAdapter(ad);

        list.setOnClickListener(View->click(list.getId()));

    }

    public void click(int i){
        String hasil = (String)list.getAdapter().getItem(i);
        Intent in = new Intent(chooseLanguageActivity.this, DictionaryActivity.class);
        if(getIntent().getStringExtra(test).equals("a")){
          in.putExtra(DictionaryActivity.hasilAw,hasil);}
        else if(getIntent().getStringExtra(test).equals("b")){
            in.putExtra(DictionaryActivity.hasilAk,hasil);
        }
        startActivity(in);
        finish();
    }
}
