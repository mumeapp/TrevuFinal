package com.remu;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.TextProcess;

import java.io.IOException;

public class DictionaryActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    public static String hasilAw = "hasil", hasilAk= "asa";
    private FirebaseRecyclerAdapter<TextProcess, TextProcessViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvDictionary;
    private Button btn1;
    private ImageView awal, akhir;
    private String textAw, textAk;
    private int check, check2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

//        awal = findViewById(R.id.awal);
//        akhir = findViewById(R.id.akhir);
        Animatoo.animateSlideLeft(this);

        rvDictionary = findViewById(R.id.rv_listDictionary);
        rvDictionary.setHasFixedSize(true);
        rvDictionary.setLayoutManager(new LinearLayoutManager(DictionaryActivity.this));
        if(getIntent().getStringExtra(hasilAk)!=null&& getIntent().getStringExtra(hasilAw)!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Dictionary").child(getIntent().getStringExtra(hasilAw)+"-"+getIntent().getStringExtra(hasilAk));

        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Dictionary").child("melayu-inggris");


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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dictionary_result, parent, false);

                return new TextProcessViewHolder(view);
            }
        };

        rvDictionary.setAdapter(firebaseRecyclerAdapter);
        btn1 = findViewById(R.id.ButtonAdd);
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

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void awal(){
        Intent in = new Intent(DictionaryActivity.this, chooseLanguageActivity.class);
        in.putExtra(chooseLanguageActivity.test, "a");
        startActivity(in);
    }

    private void akhir(){
        Intent in = new Intent(DictionaryActivity.this, chooseLanguageActivity.class);
        in.putExtra(chooseLanguageActivity.test, "b");
        startActivity(in);
    }

    private void buttonAdd() {
        Intent intent = new Intent(DictionaryActivity.this, AddDictionary.class);
        startActivity(intent);
    }
}
