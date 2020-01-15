package com.remu.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Article;
import com.remu.R;

import java.util.Objects;

public class SavedFragment extends Fragment {

    private LinearLayout savedEmpty;
    private RecyclerView listArticle;
    private TextView articleText;
    private FirebaseRecyclerAdapter<Article, SavedFragment.SavedArticleViewHolder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saved, container, false);

        initializeUI(root);
        initializeArticle();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void initializeArticle() {
        LinearLayoutManager articleLayoutManager = new LinearLayoutManager(SavedFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        listArticle.setLayoutManager(articleLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Saved").child(FirebaseAuth.getInstance().getUid()).child("Article");

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Article> options = new FirebaseRecyclerOptions.Builder<Article>()
                .setQuery(query, Article.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, SavedFragment.SavedArticleViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull SavedFragment.SavedArticleViewHolder articleViewHolder, int i, @NonNull Article article) {
                savedEmpty.setVisibility(View.GONE);
                articleText.setVisibility(View.VISIBLE);

                articleViewHolder.setImage(article.getImage());
                articleViewHolder.setHighlight(article.getHighlight());
                articleViewHolder.setJudul(article.getTitle());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            savedEmpty.setVisibility(View.VISIBLE);
                            articleText.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference saved = FirebaseDatabase.getInstance().getReference().child("Saved").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Article").child(article.getTitle());
                saved.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.child(article.getTitle()).getValue().equals(true)) {
                                articleViewHolder.bookmark.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bookmark_fill_black_24dp));
                                articleViewHolder.bookmark.setOnClickListener(view -> {
                                    saved.removeValue();
                                    articleViewHolder.bookmark.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                                });
                            }
                        } catch (NullPointerException np) {
                            articleViewHolder.bookmark.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                            articleViewHolder.bookmark.setOnClickListener(view -> {
                                saved.child(article.getTitle()).setValue(true);
                                saved.child("highlight").setValue(article.getHighlight());
                                saved.child("image").setValue(article.getImage());
                                saved.child("source").setValue(article.getSource());
                                saved.child("title").setValue(article.getTitle());
                                articleViewHolder.bookmark.setImageDrawable(getActivity().getDrawable(R.drawable.ic_bookmark_fill_black_24dp));
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                articleViewHolder.explore.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(article.getSource()));
                    startActivity(intent);
                });

            }


            @NonNull
            @Override
            public SavedFragment.SavedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_article, parent, false);

                return new SavedFragment.SavedArticleViewHolder(view);
            }

        };

        listArticle.setAdapter(firebaseRecyclerAdapter);
    }

    private void initializeUI(View root) {
        savedEmpty = root.findViewById(R.id.saved_empty);
        listArticle = root.findViewById(R.id.ac_recyclerview);
        articleText = root.findViewById(R.id.article_text);
    }

    public class SavedArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView bookmark;
        TextView judul;
        TextView highlight;
        TextView explore;
        boolean isSaved;

        SavedArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_article);
            bookmark = itemView.findViewById(R.id.bookmark_article);
            judul = itemView.findViewById(R.id.title_article);
            highlight = itemView.findViewById(R.id.highlight_article);
            explore = itemView.findViewById(R.id.explore_article);
            isSaved = false;
        }

        void setJudul(String judul) {
            this.judul.setText(judul);
        }

        void setImage(String foto) {
            Glide.with(SavedFragment.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading_image)
                    .into(image);
        }

        void setHighlight(String waktu) {
            this.highlight.setText(waktu);
        }
    }

}
