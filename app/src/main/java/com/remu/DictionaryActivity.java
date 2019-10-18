package com.remu;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.TextProcess;

import java.io.IOException;

public class DictionaryActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<TextProcess, TextProcessViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvDictionary;
    private Button btn1, btn2;
    private Spinner spinAwal, spinAkhir;
    private String textAw, textAk;
    private int check, check2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);


//        spinAwal = findViewById(R.id.bhsawal);
//        spinAkhir = findViewById(R.id.bhstrans);
//
//        check=0;
//        check2=0;
//
//        ArrayAdapter<CharSequence> adaptAw = ArrayAdapter.createFromResource(DictionaryActivity.this, R.array.bahasaAwal, android.R.layout.simple_spinner_item);
//        adaptAw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinAwal.setAdapter(adaptAw);
//        spinAwal.setSelected(false);
//        spinAwal.setSelection(0,false);
//        spinAwal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                textAw = (String)spinAwal.getSelectedItem().toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        ArrayAdapter<CharSequence> adaptAk = ArrayAdapter.createFromResource(DictionaryActivity.this, R.array.bahasaTrans, android.R.layout.simple_spinner_item);
//        adaptAk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinAkhir.setAdapter(adaptAk);
//        spinAkhir.setSelected(false);
//        spinAkhir.setSelection(0,false);
//        spinAkhir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                textAk = (String)spinAkhir.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        textAk = (String)spinAkhir.getSelectedItem().toString();
//        textAw = (String)spinAwal.getSelectedItem().toString();
        //btn2.setVisibility(View.INVISIBLE);
        //new Handler().postDelayed(() -> btn2.performClick(), 500);
//        System.out.println(spinAwal.getSelectedItem().toString());
        rvDictionary = findViewById(R.id.rv_listDictionary);
        rvDictionary.setHasFixedSize(true);
        rvDictionary.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));


        //btn2.setOnClickListener(view -> invisible());
//        String child = null;
//        if(textAw!=null&&textAk!=null){
//        if ((textAw.equalsIgnoreCase("jepang") && textAk.equalsIgnoreCase("Indonesia")) || (textAw.equalsIgnoreCase("indonesia") && textAk.equalsIgnoreCase("jepang"))) {
//            child = "indonesia-jepang";
//        } else if ((textAw.equalsIgnoreCase("jepang") && textAk.equalsIgnoreCase("Inggris")) || (textAw.equalsIgnoreCase("inggris") && textAk.equalsIgnoreCase("jepang"))) {
//            child = "jepang-inggris";
//        } else if ((textAw.equalsIgnoreCase("inggris") && textAk.equalsIgnoreCase("Indonesia")) || (textAw.equalsIgnoreCase("indonesia") && textAk.equalsIgnoreCase("inggris"))) {
//            child = "inggris-indonesia";
//        } else {
//            Toast.makeText(getApplicationContext(), "Tidak bisa menggunakan bahasa yang sama", Toast.LENGTH_LONG).show();
//        }
//        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Dictionary").child("jepang-inggris");


        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<TextProcess> options = new FirebaseRecyclerOptions.Builder<TextProcess>()
                .setQuery(query, TextProcess.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TextProcess, TextProcessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TextProcessViewHolder textProcessViewHolder, int i, @NonNull TextProcess textProcess) {
                textProcessViewHolder.setText1(textProcess.getTextAwal());
                textProcessViewHolder.setText2(textProcess.getTextTranslete());
                textProcessViewHolder.setAudio(textProcess.getAudio());

            }

            @NonNull
            @Override
            public TextProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_dictionary, parent, false);

                return new TextProcessViewHolder(view);
            }
        };

        rvDictionary.setAdapter(firebaseRecyclerAdapter);
        btn1 = findViewById(R.id.addButton);
        btn1.setOnClickListener(view -> buttonAdd());

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        firebaseRecyclerAdapter.stopListening();
    }

    public class TextProcessViewHolder extends RecyclerView.ViewHolder {

        TextView text1;
        TextView text2;
        Button audio;

        public TextProcessViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.awal);
            text2 = itemView.findViewById(R.id.translete);
            audio = itemView.findViewById(R.id.audio);
        }

        public void setText1(String text) {


            text1.setText(text);
        }

        public void setText2(String text) {

            text2.setText(text);
        }

        public void setAudio(String aud) {
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayer player = new MediaPlayer();
                    try {

                        player.setDataSource(aud);
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();

                            }
                        });
                        player.prepare();
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                }
            });
        }
    }
    private void buttonAdd() {
        Intent intent = new Intent(DictionaryActivity.this, AddDictionary.class);
        startActivity(intent);
    }
}
