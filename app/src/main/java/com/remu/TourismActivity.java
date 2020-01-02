package com.remu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.remu.POJO.PlaceModel;
import com.remu.POJO.TourPlace;
import com.remu.adapter.TourismAdapter;
import com.saber.chentianslideback.SlideBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourismActivity extends SlideBackActivity {

    private static final String TAG = "TourismActivity";

    //    private DatabaseReference databaseReference;
    //    private FirebaseRecyclerAdapter<Restoran, TourismActivity.TourismViewHolder> firebaseRecyclerAdapter;
    private RecyclerView rvTour;
    private TourismAdapter tourismAdapter;
    //    private ArrayList<PlaceModel> places;
    //    private CardView cvTour;
    //    private LatLng currentPosition;
    //    private String myLat, myLong;
    private double latitude, longitude;

    NestedScrollView tourScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);

        latitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null));
        longitude = Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null));

        initializeUI();
        Animatoo.animateSlideLeft(this);

//        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocation.getLastLocation().addOnSuccessListener(this, location -> {
//            if (location != null) {
//                myLat = Double.toString(location.getLatitude());
//                myLong = Double.toString(location.getLongitude());
//                // Do it all with location
//                Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
//                // Display in Toast
////                    Toast.makeText(HalalFastFoodRestaurantActivity.this,
////                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
////                            Toast.LENGTH_LONG).show();
//            }
//        });

        new GetTourPlace(this).execute();



//        Query query = databaseReference.orderByKey();
//
//        FirebaseRecyclerOptions<Restoran> options = new FirebaseRecyclerOptions.Builder<Restoran>()
//                .setQuery(query, Restoran.class).build();
//
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restoran, TourismActivity.TourismViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull TourismActivity.TourismViewHolder tourismViewHolder, int i, @NonNull Restoran tourism) {
//                String LatLong = tourism.getAlamatRestoran();
//                String getLatLong[] = LatLong.split(",");
//                String getLat= getLatLong[0], getLong=getLatLong[1];
//                DecimalFormat df = new DecimalFormat("#.##");
//                double jarak = getJarak(Double.parseDouble(myLat), Double.parseDouble(getLat),Double.parseDouble(myLong), Double.parseDouble(getLong));
//
//
//                tourismViewHolder.setGambar(tourism.getFoto());
//                tourismViewHolder.setNama(tourism.getNamaRestoran());
//                tourismViewHolder.setTempat(df.format(tourism.getAkumulasiRating()));
//                tourismViewHolder.setRating(df.format(jarak)+" KM");
//
//                String ID = tourism.getID();
//
//                tourismViewHolder.itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(TourismActivity.this, TourismDetailActivity.class);
//                    intent.putExtra(TourismDetailActivity.gambar, tourism.getFoto());
//                    intent.putExtra(TourismDetailActivity.id, ID);
//                    intent.putExtra(TourismDetailActivity.nama, tourism.getNamaRestoran());
//                    intent.putExtra(TourismDetailActivity.rating, tourism.getAkumulasiRating());
//                    startActivity(intent);
//                });
//            }
//
//            @NonNull
//            @Override
//            public TourismActivity.TourismViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kategori, parent, false);
//
//                return new TourismActivity.TourismViewHolder(view);
//            }
//        };

//        rvTour.setAdapter(firebaseRecyclerAdapter);
//        cvTour.setOnClickListener(View->addTour());

        tourScrollView = findViewById(R.id.tour_scroll);
        tourScrollView.post(() -> {
            tourScrollView.scrollTo(0, 0);
        });
        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideRight(this);
    }

    private void initializeUI() {
        rvTour = findViewById(R.id.TourismCategories);
//        cvTour = findViewById(R.id.addTour);
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child("Restoran").child("Wisata").child("Wisata");
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        try {
//            firebaseRecyclerAdapter.startListening();
//        }catch (Exception e){
//
//        }
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            firebaseRecyclerAdapter.stopListening();
//        }catch (Exception e){
//
//        }
//    }
//
//    private void addTour(){
//        Intent in = new Intent(TourismActivity.this, AddTourismActivity.class);
//        in.putExtra(AddTourismActivity.Jenis, "Wisata");
//        in.putExtra(AddTourismActivity.kategori, "Wisata");
//        startActivity(in);
//    }
//
//    private double getJarak(double lat1, double lat2, double long1, double long2){
//        Distance distance = new Distance();
//        return distance.distance(lat1, lat2, long1, long2);
//    }
//
//    public class TourismViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView foto;
//        TextView nama;
//        TextView tempat;
//        TextView rating;
//
//        public TourismViewHolder(@NonNull View itemView) {
//            super(itemView);
//            foto = itemView.findViewById(R.id.Gambarkategoi);
//            nama = itemView.findViewById(R.id.NamaKategori);
//            tempat = itemView.findViewById(R.id.JumlahRestoran);
//            rating = itemView.findViewById(R.id.Jarak);
//        }
//
//        public void setGambar(String foto) {
//            Glide.with(TourismActivity.this)
//                    .load(foto)
//                    .placeholder(R.drawable.bg_loading)
//                    .into(this.foto);
//        }
//
//        public void setNama(String text) {
//            nama.setText(text);
//        }
//
//        public void setTempat(String text) {
//            tempat.setText(text);
//        }
//
//        public void setRating(String text) {
//            rating.setText(text);
//        }
//    }

    private class GetTourPlace extends AsyncTask<Void, Void, Void> {

        private Context context;

        private ProgressDialog progressDialog;
        private ArrayList<PlaceModel> places;

        GetTourPlace(Context context) {
            this.context = context;
            places = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching result...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                    "&rankby=distance&type=tourist_attraction&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8";

            String jsonStr = httpHandler.makeServiceCall(url);

            Log.d(TAG, url);
            Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray results = new JSONObject(jsonStr).getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject row = results.getJSONObject(i);

                        if (row.isNull("photos")) {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
                            ));
                        } else {
                            places.add(new PlaceModel(
                                    row.getString("place_id"),
                                    row.getString("name"),
                                    row.getString("vicinity"),
                                    row.getDouble("rating"),
                                    new LatLng(row.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            row.getJSONObject("geometry").getJSONObject("location").getDouble("lng")),
                                    row.getJSONArray("photos").getJSONObject(0).getString("photo_reference")
                            ));
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            rvTour.setLayoutManager(new GridLayoutManager(TourismActivity.this, 2));
            tourismAdapter = new TourismAdapter(getApplication(), getParent(), places, new LatLng(latitude, longitude));
            tourismAdapter.setClickListener((view, position) -> {
                //set what happend when clicked
                Log.i("TAG", "You clicked number " + tourismAdapter.getItem(position) + ", which is at cell position " + position);

            });
            rvTour.setAdapter(tourismAdapter);

            progressDialog.dismiss();
        }
    }

}
