package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.remu.POJO.Article;
import com.remu.POJO.PrayerTime;
import com.remu.POJO.Tips;
import com.remu.Service.UpdateLocation;
import com.remu.adapter.TipsAdapter;
import com.takusemba.multisnaprecyclerview.MultiSnapHelper;
import com.takusemba.multisnaprecyclerview.SnapGravity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "MainActivity";
//
//    private String latitude, longitude;
//
//    private CardView mosqueCardView, foodButton, dictionaryButton, tourButton;
//    private TextView nama;
//    private ImageView profile;
//
//    private FirebaseRecyclerAdapter<Article, MainActivity.ArticleViewHolder> firebaseRecyclerAdapter;
//    private RecyclerView listArticle;
//
//    private RecyclerView listTips;
//    private ArrayList<Tips> tipsDataSet;
//
//    private NestedScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animatoo.animateSlideLeft(this);

        BottomNavigationView navView = findViewById(R.id.main_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.main_nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }
//
//        latitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null);
//        longitude = getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null);
//
//
//        Log.e(TAG, "Latitude: " + latitude);
//        Log.e(TAG, "Longitude: " + longitude);
//
//        //initialize uI
//        initializeUI();
//        Animatoo.animateSlideLeft(this);
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        //set nama
//        getCurrentUser(currentUser);
//
//        mainScrollView = findViewById(R.id.main_scroll);
//        mainScrollView.post(() -> mainScrollView.scrollTo(0, 0));
//
//        //go to mosque activity
//        mosqueCardView.setOnClickListener(view -> {
//            Intent viewMosque = new Intent(MainActivity.this, MosqueActivity.class);
//            viewMosque.putExtra("latitude", latitude);
//            viewMosque.putExtra("longitude", longitude);
//            startActivity(viewMosque);
//        });
//
//        //go to food activity
//        foodButton.setOnClickListener(view -> {
//            Intent viewFood = new Intent(MainActivity.this, FoodActivity.class);
//            startActivity(viewFood);
//        });
//
//        //go to tourism activity
//        tourButton.setOnClickListener(view -> {
//            Intent viewTour = new Intent(MainActivity.this, TourismActivity.class);
//            startActivity(viewTour);
//        });
//
//        //go to Dictionary Activity
//        dictionaryButton.setOnClickListener(view -> {
//            Intent viewDictonary = new Intent(MainActivity.this, DictionaryActivity.class);
//            startActivity(viewDictonary);
//        });
//
//        //sign out
//        profile.setOnClickListener(view -> {
//            LoginManager loginManager = LoginManager.getInstance();
//            Intent stopService = new Intent(MainActivity.this, UpdateLocation.class);
//            stopService(stopService);
//            FirebaseAuth.getInstance().signOut();
//            loginManager.logOut();
//            Intent login = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(login);
//            finish();
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseRecyclerAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        firebaseRecyclerAdapter.stopListening();
//    }
//
//    private void initializeUI() {
//        mosqueCardView = findViewById(R.id.MosqueCardView);
//        foodButton = findViewById(R.id.foodButton);
//        dictionaryButton = findViewById(R.id.dictionaryButton);
//        tourButton = findViewById(R.id.tourismButton);
//        nama = findViewById(R.id.nama);
//        profile = findViewById(R.id.profile_picture);
//
//        listArticle = findViewById(R.id.listArticle);
//        initializeArticle();
//
//        listTips = findViewById(R.id.listTips);
//        tipsDataSet = new ArrayList<Tips>() {{
//            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #1", ""));
//            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #2", ""));
//            add(new Tips(getDrawable(R.drawable.ic_img_tips), "Tips #3", ""));
//        }};
//        initializeTips();
//
//        ArrayList<TextView> textViews = new ArrayList<TextView>() {{
//            add(findViewById(R.id.jamSolatSelanjutnya));
//        }};
//        new PrayerTime(this, TAG, latitude, longitude, textViews).execute();
//    }
//
//    private void initializeArticle() {
//        LinearLayoutManager articleLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        listArticle.setLayoutManager(articleLayoutManager);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Article");
//
//        Query query = databaseReference.orderByKey();
//
//        FirebaseRecyclerOptions<Article> options = new FirebaseRecyclerOptions.Builder<Article>()
//                .setQuery(query, Article.class).build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i, @NonNull Article article) {
//                articleViewHolder.setImage(article.getImage());
//                articleViewHolder.setHighlight(article.getHighlight());
//                articleViewHolder.setJudul(article.getTitle());
//            }
//
//            @NonNull
//            @Override
//            public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_article, parent, false);
//                return new ArticleViewHolder(view);
//            }
//        };
//        listArticle.setAdapter(firebaseRecyclerAdapter);
//        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.CENTER, 1, 100);
//        multiSnapHelper.attachToRecyclerView(listArticle);
//    }
//
//    private void initializeTips() {
//        LinearLayoutManager tipsLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        listTips.setLayoutManager(tipsLayoutManager);
//        RecyclerView.Adapter tipsAdapter = new TipsAdapter(getApplication(), tipsDataSet);
//        listTips.setAdapter(tipsAdapter);
//    }
//
//    private void getCurrentUser(FirebaseUser user) {
//        if (user != null) {
//            String name = user.getDisplayName();
//            nama.setText(name);
//            Glide.with(MainActivity.this)
//                    .load(user.getPhotoUrl())
//                    .placeholder(R.drawable.profile_annasaha)
//                    .into(profile);
//        }
//    }
//
//    public class ArticleViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
//        TextView judul;
//        TextView highlight;
//
//        ArticleViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.img_article);
//            judul = itemView.findViewById(R.id.title_article);
//            highlight = itemView.findViewById(R.id.highlight_article);
//        }
//
//        void setJudul(String judul) {
//            this.judul.setText(judul);
//        }
//
//        void setImage(String foto) {
//            Glide.with(MainActivity.this)
//                    .load(foto)
//                    .placeholder(R.drawable.bg_loading_image)
//                    .into(image);
//        }
//
//        void setHighlight(String waktu) {
//            this.highlight.setText(waktu);
//        }
//    }

}
