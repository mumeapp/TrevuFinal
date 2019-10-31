package com.remu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Rating;

public class RatingActivity extends AppCompatActivity {

    public static String ID= "id", Jenis= "jenis", Nama="nama", NamaRestoran = "restoran";
    private String id, jenis, nama;
    private TextView namaRestoran;
    private EditText biaya, lama, review;
    private Button simpan, batal;
    private RatingBar rating;
    private String nilaiRating, nilaiBiaya, nilaiLama, nilaiReview, idUser, restoran;
    private DatabaseReference reference;
    private double ratingSekarang;
    private Double akumulasiRating=0.0;
    private ProgressDialog loading;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Initialize();

        addListenerOnRatingBar();
        namaRestoran.setText("Tambah Review "+restoran);
        simpan.setOnClickListener(View -> simpan());
        batal.setOnClickListener(View -> batal());

    }
    private void Initialize(){
        id = getIntent().getStringExtra(ID);
        jenis = getIntent().getStringExtra(Jenis);
        nama = getIntent().getStringExtra(Nama);
        namaRestoran = findViewById(R.id.namaRestoran);
        biaya = findViewById(R.id.biaya);
        lama = findViewById(R.id.lama);
        review = findViewById(R.id.review);
        simpan = findViewById(R.id.simpan);
        batal = findViewById(R.id.batal);
        rating = findViewById(R.id.Rating);
        restoran = getIntent().getStringExtra(NamaRestoran);
        ratingSekarang = getIntent().getDoubleExtra("akumulasi", 0.0);
        reference = FirebaseDatabase.getInstance().getReference().child("Review").child(jenis).child(nama).child(restoran).push();
    }

    public void addListenerOnRatingBar() {

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                nilaiRating = String.valueOf(rating);

            }
        });
    }
    public void simpan(){
        loading = ProgressDialog.show(RatingActivity.this,
                null,
                "please wait...",
                true,
                false);
        nilaiBiaya = biaya.getText().toString();
        nilaiLama = biaya.getText().toString();
        nilaiReview = review.getText().toString();
        idUser = currentUser.getUid();
        Rating rating = new Rating(idUser, nilaiBiaya, nilaiLama, nilaiReview, nilaiRating);
        reference.setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    akumulasiRating=Double.parseDouble(nilaiRating);
                    if(ratingSekarang==0){
                        akumulasiRating = akumulasiRating+ratingSekarang;}
                    else{
                        akumulasiRating = (akumulasiRating+ratingSekarang)/2;}
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child(jenis).child(nama).child(id);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReference.child("akumulasiRating").setValue(akumulasiRating);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    loading.dismiss();
                    finish();
                }
                else{

                }
            }
        });
    }
    public void batal(){
        finish();
    }
}
