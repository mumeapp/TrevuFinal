package com.remu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.DictionaryActivity;
import com.remu.FindFriendActivity;
import com.remu.FoodActivity;
import com.remu.MosqueActivity;
import com.remu.POJO.Article;
import com.remu.POJO.PrayerTime;
import com.remu.POJO.Tips;
import com.remu.R;
import com.remu.TourismActivity;
import com.remu.adapter.TipsAdapter;
import com.takusemba.multisnaprecyclerview.MultiSnapHelper;
import com.takusemba.multisnaprecyclerview.SnapGravity;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private String latitude, longitude;

    private CardView mosqueCardView;
    private LinearLayout fnBButton, tourismButton, findFriendButton, dictionaryButton;
    private TextView userName;
    private RecyclerView listArticle, listTips;

    private FirebaseRecyclerAdapter<Article, HomeFragment.ArticleViewHolder> firebaseRecyclerAdapter;
    private ArrayList<Tips> tipsDataSet;

    private NestedScrollView homeScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        latitude = Objects.requireNonNull(getActivity().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Objects.requireNonNull(getActivity().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        Log.e(TAG, "Latitude: " + latitude);
        Log.e(TAG, "Longitude: " + longitude);

        initializeUI(root);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set name
        getCurrentUser(currentUser);

        homeScrollView = root.findViewById(R.id.home_scroll);
        homeScrollView.post(() -> homeScrollView.scrollTo(0, 0));

        //go to mosque activity
        mosqueCardView.setOnClickListener(view -> {
            Intent viewMosque = new Intent(HomeFragment.super.getContext(), MosqueActivity.class);
            startActivity(viewMosque);
        });

        //go to food activity
        fnBButton.setOnClickListener(view -> {
            Intent viewFood = new Intent(HomeFragment.super.getContext(), FoodActivity.class);
            startActivity(viewFood);
        });

        //go to tourism activity
        tourismButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(HomeFragment.super.getContext(), TourismActivity.class);
            startActivity(viewTour);
        });

        //go to tourism activity
        findFriendButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(HomeFragment.super.getContext(), FindFriendActivity.class);
            startActivity(viewTour);
        });

        //go to Dictionary Activity
        dictionaryButton.setOnClickListener(view -> {
            Intent viewDictonary = new Intent(HomeFragment.super.getContext(), DictionaryActivity.class);
            startActivity(viewDictonary);
        });

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

    private void initializeUI(View root) {
        // jam card
        mosqueCardView = root.findViewById(R.id.mosque_card_view);

        // menu
        fnBButton = root.findViewById(R.id.fnb_button);
        tourismButton = root.findViewById(R.id.tourism_button);
        findFriendButton = root.findViewById(R.id.findfriend_button);
        dictionaryButton = root.findViewById(R.id.dictionary_button);

        // hello guest
        userName = root.findViewById(R.id.nama);

        // explore
        listArticle = root.findViewById(R.id.list_article);
        initializeArticle();

        // tips
        listTips = root.findViewById(R.id.list_tips);
        tipsDataSet = new ArrayList<Tips>() {{
            add(new Tips(getActivity().getDrawable(R.drawable.ic_img_tips), "Tips #1", ""));
            add(new Tips(getActivity().getDrawable(R.drawable.ic_img_tips), "Tips #2", ""));
            add(new Tips(getActivity().getDrawable(R.drawable.ic_img_tips), "Tips #3", ""));
        }};
        initializeTips();

        // next prayer time
        ArrayList<TextView> textViews = new ArrayList<TextView>() {{
            add(root.findViewById(R.id.jam_solat_selanjutnya));
        }};
        new PrayerTime(this.getContext(), TAG, latitude, longitude, textViews).execute();
    }

    private void initializeArticle() {
        LinearLayoutManager articleLayoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        listArticle.setLayoutManager(articleLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Article");

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<Article> options = new FirebaseRecyclerOptions.Builder<Article>()
                .setQuery(query, Article.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i, @NonNull Article article) {
                articleViewHolder.setImage(article.getImage());
                articleViewHolder.setHighlight(article.getHighlight());
                articleViewHolder.setJudul(article.getTitle());
            }

            @NonNull
            @Override
            public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_article, parent, false);
                return new ArticleViewHolder(view);
            }
        };

        listArticle.setAdapter(firebaseRecyclerAdapter);
        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 1, 100);
        multiSnapHelper.attachToRecyclerView(listArticle);
    }

    private void initializeTips() {
        LinearLayoutManager tipsLayoutManager = new LinearLayoutManager(HomeFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        listTips.setLayoutManager(tipsLayoutManager);
        RecyclerView.Adapter tipsAdapter = new TipsAdapter(getActivity().getApplication(), tipsDataSet);
        listTips.setAdapter(tipsAdapter);
    }

    private void getCurrentUser(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            userName.setText(name);
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView judul;
        TextView highlight;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_article);
            judul = itemView.findViewById(R.id.title_article);
            highlight = itemView.findViewById(R.id.highlight_article);
        }

        void setJudul(String judul) {
            this.judul.setText(judul);
        }

        void setImage(String foto) {
            Glide.with(HomeFragment.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading_image)
                    .into(image);
        }

        void setHighlight(String waktu) {
            this.highlight.setText(waktu);
        }
    }

}
