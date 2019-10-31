package com.remu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Rating;
import com.remu.POJO.User;

public class CommentActivity extends AppCompatActivity {

    public static String Jenis= "jenis", Nama = "nama", NamaRestoran = "restoran";
    private String jenis, nama, namaRestoran;
    private DatabaseReference databaseReference;
    private RecyclerView rvComment;
    private FirebaseRecyclerAdapter<Rating, CommentActivity.CommentViewHolder> firebaseRecyclerAdapter;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        jenis = getIntent().getStringExtra(Jenis);
        nama = getIntent().getStringExtra(Nama);
        namaRestoran = getIntent().getStringExtra(NamaRestoran);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review").child(jenis).child(nama).child(namaRestoran);
        rvComment = findViewById(R.id.comment);

        rvComment.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                .setQuery(query, Rating.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Rating, CommentActivity.CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentActivity.CommentViewHolder commentViewHolder, int i, @NonNull Rating rating) {
                commentViewHolder.setReviewUser(rating.getReview());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(rating.getIdUser());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        commentViewHolder.setNamaUser(user.getNama());
                        commentViewHolder.setGambar(user.getFoto());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public CommentActivity.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review, parent, false);

                return new CommentActivity.CommentViewHolder(view);
            }
        };

        rvComment.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            firebaseRecyclerAdapter.startListening();
        }catch (Exception e){

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            firebaseRecyclerAdapter.stopListening();
        }catch (Exception e){

        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView gambarUser;
        TextView namaUser;
        TextView reviewUser;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarUser = itemView.findViewById(R.id.profile_image);
            namaUser = itemView.findViewById(R.id.nama);
            reviewUser = itemView.findViewById(R.id.review);
        }

        public void setGambar(String foto) {
            Glide.with(CommentActivity.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(gambarUser);
        }

        public void setReviewUser(String text) {
            reviewUser.setText(text);
        }

        public void setNamaUser(String text) {
            namaUser.setText(text);
        }

    }
}
