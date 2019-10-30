package com.remu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Distance;
import com.remu.POJO.Restoran;
import com.remu.POJO.User;

import java.text.DecimalFormat;

public class ListOnline extends AppCompatActivity {

    private Button btUpdate;
    private DatabaseReference databaseReference;
    private LatLng latLong;
    private String lat,lng;
    private RecyclerView rvList;
    private final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView.LayoutManager layoutManager;
    public static String Nama = "Nama", gambar = "url";
    private String nama = null, myLat= null, myLong=null, id, idUser;
    private FirebaseRecyclerAdapter<User, ListOnline.ListOnlineViewHolder> firebaseRecyclerAdapter;
    private CardView cd;
    private Intent getID;
    private String id_new, Gambar, jarak;
    private ImageView img;
    private TextView tv;
    private Button btn_skuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        initializeUI();

        this.lat = getIntent().getStringExtra("latitude");
        this.lng = getIntent().getStringExtra("longitude");
        idUser= currentUser.getUid();



        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    myLat = Double.toString(location.getLatitude());
                    myLong = Double.toString(location.getLongitude());
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    // Display in Toast
//                    Toast.makeText(HalalFastFoodRestaurantActivity.this,
//                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
//                            Toast.LENGTH_LONG).show();
                }
            }
        });
         rvList.setLayoutManager(new LinearLayoutManager(ListOnline.this));

        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, ListOnlineViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListOnlineViewHolder listOnlineViewHolder, int i, @NonNull User user) {
                id = user.getId();
                String LatLong = user.getLatLong();
                String[] getLatLong = LatLong.split(",");
                String getLat= getLatLong[0];
                String getLong=getLatLong[1];
                DecimalFormat df = new DecimalFormat("#.##");
                if(!id.equals(idUser)){
                double jarak = getJarak(Double.parseDouble(myLat), Double.parseDouble(getLat),Double.parseDouble(myLong), Double.parseDouble(getLong));
                listOnlineViewHolder.setGambar(user.getFoto());
                listOnlineViewHolder.setNamaUser(user.getNama());
                listOnlineViewHolder.setJarak(df.format(jarak)+" KM");
                listOnlineViewHolder.setBtnSkuy(View.VISIBLE);
                }
                else{
                    listOnlineViewHolder.setGambar(user.getFoto());
                    listOnlineViewHolder.setNamaUser("Your Account");
                    listOnlineViewHolder.setJarak("0 KM");
                    listOnlineViewHolder.setBtnSkuy(View.INVISIBLE);
                }

//                listOnlineViewHolder.itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(ListOnline.this, PlaceDetailActivity.class);
//                    intent.putExtra(PlaceDetailActivity.ID, id);
//                    startActivity(intent);
//                });
            }
            @NonNull
            @Override
            public ListOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_online, parent, false);
                return new ListOnlineViewHolder(view);

            }
        };
//        if(!id.equals(idUser)){
            rvList.setAdapter(firebaseRecyclerAdapter);
     //   }


    }

    private void initializeUI() {
        rvList = findViewById(R.id.rv_listOnlineUser);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        this.img = findViewById(R.id.Gambarkategoi);
        btn_skuy = findViewById(R.id.stopButton);



    }

    private void setImage(String url) {
        System.out.println(url);
        img = findViewById(R.id.image_online);
        Glide.with(ListOnline.this)
                .load(url)
                .into(img);
    }



    @Override
    protected void onStart() {
        super.onStart();
        try {
            firebaseRecyclerAdapter.startListening();
            if(!id.equals(idUser)) {

            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            firebaseRecyclerAdapter.stopListening();
        } catch (Exception e) {

        }
    }

    private double getJarak(double lat1, double lat2, double long1, double long2){
        Distance distance = new Distance();
        return distance.distance(lat1, lat2, long1, long2);
    }

    public class ListOnlineViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nama_user;
        TextView jarak;
        Button btnSkuy;

        public ListOnlineViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.image_online);
            nama_user = itemView.findViewById(R.id.text_user);
            jarak = itemView.findViewById(R.id.text_Jarak);
            btn_skuy = itemView.findViewById(R.id.stopButton);
        }

        public void setGambar(String foto) {

            Glide.with(ListOnline.this)
                    .load(foto)
                    .placeholder(R.drawable.bg_loading)
                    .into(this.foto);
        }

        public void setNamaUser(String text) {
            nama_user.setText(text);
        }

        public void setJarak(String text) {
            jarak.setText(text);
        }

        public void setBtnSkuy(int Visible){
            btn_skuy.setVisibility(Visible);
        }
    }




}
