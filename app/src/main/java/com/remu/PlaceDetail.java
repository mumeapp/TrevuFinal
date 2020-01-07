package com.remu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.DayOfWeek;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.remu.POJO.Distance;
import com.remu.POJO.Rating;
import com.saber.chentianslideback.SlideBackActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlaceDetail extends SlideBackActivity {

    private static final String TAG = "PlaceDetail";

    private TextView tpdName, tpdRating1, tpdTotalRating1, tpdRating2, tpdTotalRating2, tpdCityLocation,
            tpdDistance, tpdAddress, tpdPlusCode, tpdIsOpen, tpdClosingHours, tpdPhone;
    private TextView headerAddress, headerPlusCode, headerCloseHours, headerPhone;
    private ImageView starRating2;
    private CardView tpdDiscoverButton, tpdReviewCard;
    private ImageView tpdPhoto, tpdBookmarkBorder, tpdBookmarkFilled, tpdProfilePicture;
    private RatingBar tpdInputRating;
    private EditText tpdInputReview;
    private RecyclerView tpdListUserReview;
    private Button tpdSubmitButon;
    private String uId, email, nama;

    private PlacesClient placesClient;
    private Geocoder mGeocoder;
    private FirebaseRecyclerAdapter<Rating, PlaceDetail.TourismDetailAdapter> firebaseRecyclerAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_detail);

        Intent content = getIntent();

        uId = FirebaseAuth.getInstance().getUid();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        nama = email.split("@")[0];

        Places.initialize(getApplicationContext(), "AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8");
        placesClient = Places.createClient(this);
        mGeocoder = new Geocoder(this, Locale.getDefault());

        initializeUI();
        changeThemeBySender(Objects.requireNonNull(content.getStringExtra("sender"), "White"));
        Animatoo.animateSlideDown(this);
        checkUserReview();
        getMean();
        getReview();

        getPlace(content.getStringExtra("place_id"));

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Animatoo.animateSlideUp(this);
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

    private void changeThemeBySender(String sender) {
        int color = 0;
        switch (sender) {
            case "HalalFood":
                color = getResources().getColor(R.color.trevuYellow);
                break;
            case "HalalBeverages":
                color = getResources().getColor(R.color.trevuDarkRed);
                break;
            case "Tourism":
                color = getResources().getColor(R.color.trevuBlue);
                break;
        }

        tpdDiscoverButton.setCardBackgroundColor(color);
        headerAddress.setTextColor(color);
        headerPlusCode.setTextColor(color);
        headerCloseHours.setTextColor(color);
        headerPhone.setTextColor(color);
        tpdReviewCard.setCardBackgroundColor(color);
        tpdSubmitButon.setTextColor(color);
        starRating2.setColorFilter(color);
        tpdRating2.setTextColor(color);
    }

    private void getMean() {
        DatabaseReference databaseReview = FirebaseDatabase.getInstance().getReference().child("Places Review").child(getIntent().getStringExtra("place_id"));
        databaseReview.addChildEventListener(new ChildEventListener() {
            double rataRata = 0;
            double jumlah = 0;

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {
                    ++jumlah;
                    rataRata += Double.parseDouble(dataSnapshot.getValue(Rating.class).getRating());
                    rataRata /= jumlah;
                    DecimalFormat df = new DecimalFormat("#.#");
                    DecimalFormat dfJumlah = new DecimalFormat("#");
                    tpdRating2.setText(df.format(rataRata));
                    tpdTotalRating2.setText("(" + dfJumlah.format(jumlah) + ")");
                } catch (NullPointerException np) {
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getReview() {
        LinearLayoutManager articleLayoutManager = new LinearLayoutManager(PlaceDetail.this, LinearLayoutManager.VERTICAL, false);
        tpdListUserReview.setLayoutManager(articleLayoutManager);

        DatabaseReference databaseReview = FirebaseDatabase.getInstance().getReference().child("Places Review").child(getIntent().getStringExtra("place_id"));
        databaseReview.addChildEventListener(new ChildEventListener() {
            double rataRata = 0;
            double jumlah = 0;

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {
                    ++jumlah;
                    rataRata += Double.parseDouble(dataSnapshot.getValue(Rating.class).getRating());
                    rataRata /= jumlah;
                    DecimalFormat df = new DecimalFormat("#.#");
                    DecimalFormat dfJumlah = new DecimalFormat("#");
                    tpdRating2.setText(df.format(rataRata));
                    tpdTotalRating2.setText("(" + dfJumlah.format(jumlah) + ")");
                } catch (NullPointerException np) {
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = databaseReview.orderByChild(uId).equalTo(null);

        FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                .setQuery(query, Rating.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Rating, PlaceDetail.TourismDetailAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlaceDetail.TourismDetailAdapter tourismDetailAdapter, int i, @NonNull Rating rating) {
                tourismDetailAdapter.setNama(rating.getNamaUser());
                tourismDetailAdapter.setImage("");
                tourismDetailAdapter.setReview(rating.getReview());

            }

            @NonNull
            @Override
            public PlaceDetail.TourismDetailAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review_user, parent, false);
                return new PlaceDetail.TourismDetailAdapter(view);
            }

        };
        tpdListUserReview.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkUserReview() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Places Review").child(getIntent().getStringExtra("place_id")).child(uId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    tpdInputReview.setText(dataSnapshot.child("review").getValue().toString());
                    tpdInputRating.setRating(Float.parseFloat(dataSnapshot.child("rating").getValue().toString()));
                    tpdSubmitButon.setText("Edit");
                } catch (NullPointerException np) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPlace(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS,
                Place.Field.PLUS_CODE, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
            applyPlaceInfoToView(place);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                Log.e(TAG, "Place not found (ERROR[" + statusCode + "]): " + exception.getMessage());
            }
        });
    }

    private void applyPlaceInfoToView(Place tourismPlace) {
        if (tourismPlace != null) {
            tpdBookmarkBorder.setOnClickListener((view) -> {
                tpdBookmarkFilled.setVisibility(View.VISIBLE);
                tpdBookmarkBorder.setVisibility(View.GONE);
            });

            tpdBookmarkFilled.setOnClickListener((view) -> {
                tpdBookmarkBorder.setVisibility(View.VISIBLE);
                tpdBookmarkFilled.setVisibility(View.GONE);
            });

            if (tourismPlace.getPhotoMetadatas() != null) {
                PhotoMetadata photoMetadata = tourismPlace.getPhotoMetadatas().get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxHeight(500) // Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    tpdPhoto.setImageBitmap(bitmap);
                }).addOnFailureListener((exception) -> {
                    Log.e(TAG, exception.toString());
                });
            } else {
                LatLng location = tourismPlace.getLatLng();
                Picasso.get().load("https://maps.googleapis.com/maps/api/streetview?size=500x300&location=" + location.latitude + "," + location.longitude
                        + "&fov=120&pitch=10&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                        .error(R.drawable.bg_loading_image)
                        .placeholder(R.drawable.bg_loading_image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(tpdPhoto);
            }

            tpdName.setText(tourismPlace.getName());

            if (tourismPlace.getRating() == null) {
                tpdRating1.setText("-");
//                tpdRating2.setText("-");
                tpdTotalRating1.setText("(0)");
//                tpdTotalRating2.setText("(0)");
            } else {
                tpdRating1.setText(String.format("%.1f", tourismPlace.getRating()));
                tpdTotalRating1.setText(String.format("(%d)", tourismPlace.getUserRatingsTotal()));
//                tpdRating2.setText(String.format("%.1f", tourismPlace.getRating()));
//                tpdTotalRating2.setText(String.format("(%d)", tourismPlace.getUserRatingsTotal()));
            }

            try {
                tpdCityLocation.setText(getCityNameByCoordinates(tourismPlace.getLatLng().latitude, tourismPlace.getLatLng().longitude));
            } catch (IOException e) {
                e.printStackTrace();
            }

            tpdDistance.setText(String.format("%.2f km", countDistance(tourismPlace.getLatLng())));

            tpdDiscoverButton.setOnClickListener((view) -> {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + tourismPlace.getLatLng().latitude + "," + tourismPlace.getLatLng().longitude));
                startActivity(intent);
            });

            tpdAddress.setText(tourismPlace.getAddress());
            tpdPlusCode.setText(tourismPlace.getPlusCode().getCompoundCode());

            //TODO: CLOSING HOURS
            if (tourismPlace.getOpeningHours() != null) {
                try {
                    setOpenOrCLoseState(tourismPlace.getOpeningHours().getPeriods());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (tourismPlace.getPhoneNumber() == null) {
                tpdPhone.setText("-");
            } else {
                tpdPhone.setText(tourismPlace.getPhoneNumber());
            }
            tpdSubmitButon.setOnClickListener(view -> {
                progressDialog.show();
                Rating rating = new Rating(uId, nama, tpdInputReview.getText().toString(), Float.toString(tpdInputRating.getRating()), getIntent().getStringExtra("place_id"), tpdName.getText().toString());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Places Review").child(getIntent().getStringExtra("place_id"));
                databaseReference.child(uId).setValue(rating).addOnSuccessListener(aVoid -> {
                    databaseReference.child(uId).child(uId).setValue(true);
                    tpdSubmitButon.setText("Edit");
                    getMean();
                    progressDialog.dismiss();
                });
            });
        }
    }

    private void initializeUI() {
        tpdName = findViewById(R.id.tpd_name);
        tpdRating1 = findViewById(R.id.tpd_rating_1);
        tpdTotalRating1 = findViewById(R.id.tpd_user_rating_total_1);
        tpdCityLocation = findViewById(R.id.tpd_citylocation);
        tpdDistance = findViewById(R.id.tpd_distance);
        tpdDiscoverButton = findViewById(R.id.tpd_discoverbutton);
        tpdPhoto = findViewById(R.id.tpd_photo);
        tpdBookmarkBorder = findViewById(R.id.tpd_bookmark_border);
        tpdBookmarkFilled = findViewById(R.id.tpd_bookmark_filled);
        tpdAddress = findViewById(R.id.tpd_address);
        tpdPlusCode = findViewById(R.id.tpd_plus_code);
        tpdIsOpen = findViewById(R.id.tpd_is_open);
        tpdClosingHours = findViewById(R.id.tpd_closing_hours);
        tpdPhone = findViewById(R.id.tpd_phone);
        tpdProfilePicture = findViewById(R.id.tpd_profile_picture);
        tpdSubmitButon = findViewById(R.id.submitButton);

        tpdInputRating = findViewById(R.id.tpd_input_rating);
        LayerDrawable stars = (LayerDrawable) tpdInputRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        tpdInputReview = findViewById(R.id.tpd_input_review);
        tpdRating2 = findViewById(R.id.tpd_rating_2);
        tpdTotalRating2 = findViewById(R.id.tpd_user_rating_total_2);
        tpdListUserReview = findViewById(R.id.tpd_list_review);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching result...");
        progressDialog.setCancelable(false);

        headerAddress = findViewById(R.id.tpd_header_address);
        headerPlusCode = findViewById(R.id.tpd_header_plus_code);
        headerCloseHours = findViewById(R.id.tpd_header_close_hours);
        headerPhone = findViewById(R.id.tpd_header_phone);
        starRating2 = findViewById(R.id.tpd_star_rating_2);
        tpdReviewCard = findViewById(R.id.tpd_card_review);
    }

    private double countDistance(LatLng latLng) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null)),
                Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null)));
        return Distance.distance(currentLatLng.latitude, latLng.latitude, currentLatLng.longitude, latLng.longitude);
    }

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        return null;
    }

    private void setOpenOrCLoseState(List<Period> openOrCLosePeriod) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                Period sundayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.SUNDAY) {
                        sundayPeriod = period;
                        break;
                    }
                }

                if (sundayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < sundayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(sundayPeriod.getOpen().getTime().getHours() + ":" +
                                        sundayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == sundayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < sundayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(sundayPeriod.getOpen().getTime().getHours() + ":" +
                                            sundayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(sundayPeriod.getClose().getTime().getHours() + ":" +
                                            sundayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < sundayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(sundayPeriod.getClose().getTime().getHours() + ":" +
                                            sundayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == sundayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < sundayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(sundayPeriod.getClose().getTime().getHours() + ":" +
                                                sundayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.SUNDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.SUNDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.SUNDAY);
                }
                break;
            case Calendar.MONDAY:
                Period mondayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.MONDAY) {
                        mondayPeriod = period;
                        break;
                    }
                }

                if (mondayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < mondayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(mondayPeriod.getOpen().getTime().getHours() + ":" +
                                        mondayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == mondayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < mondayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(mondayPeriod.getOpen().getTime().getHours() + ":" +
                                            mondayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(mondayPeriod.getClose().getTime().getHours() + ":" +
                                            mondayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < mondayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(mondayPeriod.getClose().getTime().getHours() + ":" +
                                            mondayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == mondayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < mondayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(mondayPeriod.getClose().getTime().getHours() + ":" +
                                                mondayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.MONDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.MONDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.MONDAY);
                }
                break;
            case Calendar.TUESDAY:
                Period tuesdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.TUESDAY) {
                        tuesdayPeriod = period;
                        break;
                    }
                }

                if (tuesdayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < tuesdayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(tuesdayPeriod.getOpen().getTime().getHours() + ":" +
                                        tuesdayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == tuesdayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < tuesdayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(tuesdayPeriod.getOpen().getTime().getHours() + ":" +
                                            tuesdayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(tuesdayPeriod.getClose().getTime().getHours() + ":" +
                                            tuesdayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < tuesdayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(tuesdayPeriod.getClose().getTime().getHours() + ":" +
                                            tuesdayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == tuesdayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < tuesdayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(tuesdayPeriod.getClose().getTime().getHours() + ":" +
                                                tuesdayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.TUESDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.TUESDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.TUESDAY);
                }
                break;
            case Calendar.WEDNESDAY:
                Period wednesdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.WEDNESDAY) {
                        wednesdayPeriod = period;
                        break;
                    }
                }

                if (wednesdayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < wednesdayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(wednesdayPeriod.getOpen().getTime().getHours() + ":" +
                                        wednesdayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == wednesdayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < wednesdayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(wednesdayPeriod.getOpen().getTime().getHours() + ":" +
                                            wednesdayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(wednesdayPeriod.getClose().getTime().getHours() + ":" +
                                            wednesdayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < wednesdayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(wednesdayPeriod.getClose().getTime().getHours() + ":" +
                                            wednesdayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == wednesdayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < wednesdayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(wednesdayPeriod.getClose().getTime().getHours() + ":" +
                                                wednesdayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.WEDNESDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.WEDNESDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.WEDNESDAY);
                }
                break;
            case Calendar.THURSDAY:
                Period thursdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.THURSDAY) {
                        thursdayPeriod = period;
                        break;
                    }
                }

                if (thursdayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < thursdayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(thursdayPeriod.getOpen().getTime().getHours() + ":" +
                                        thursdayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == thursdayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < thursdayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(thursdayPeriod.getOpen().getTime().getHours() + ":" +
                                            thursdayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(thursdayPeriod.getClose().getTime().getHours() + ":" +
                                            thursdayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < thursdayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(thursdayPeriod.getClose().getTime().getHours() + ":" +
                                            thursdayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == thursdayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < thursdayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(thursdayPeriod.getClose().getTime().getHours() + ":" +
                                                thursdayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.THURSDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.THURSDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.THURSDAY);
                }
                break;
            case Calendar.FRIDAY:
                Period fridayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.FRIDAY) {
                        fridayPeriod = period;
                        break;
                    }
                }

                if (fridayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < fridayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(fridayPeriod.getOpen().getTime().getHours() + ":" +
                                        fridayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == fridayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < fridayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(fridayPeriod.getOpen().getTime().getHours() + ":" +
                                            fridayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(fridayPeriod.getClose().getTime().getHours() + ":" +
                                            fridayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < fridayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(fridayPeriod.getClose().getTime().getHours() + ":" +
                                            fridayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == fridayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < fridayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(fridayPeriod.getClose().getTime().getHours() + ":" +
                                                fridayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.FRIDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.FRIDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.FRIDAY);
                }
                break;
            case Calendar.SATURDAY:
                Period saturdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.SATURDAY) {
                        saturdayPeriod = period;
                        break;
                    }
                }

                if (saturdayPeriod != null) {
                    String[] currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()).split(":");

                    if (Integer.parseInt(currentTime[0]) < saturdayPeriod.getOpen().getTime().getHours()) {
                        tpdIsOpen.setText("Closed");
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(saturdayPeriod.getOpen().getTime().getHours() + ":" +
                                        saturdayPeriod.getOpen().getTime().getMinutes())) + " today");
                    } else if (Integer.parseInt(currentTime[0]) == saturdayPeriod.getOpen().getTime().getHours()) {
                        if (Integer.parseInt(currentTime[1]) < saturdayPeriod.getOpen().getTime().getMinutes()) {
                            tpdIsOpen.setText("Closed");
                            tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(saturdayPeriod.getOpen().getTime().getHours() + ":" +
                                            saturdayPeriod.getOpen().getTime().getMinutes())) + " today");
                        } else {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(saturdayPeriod.getClose().getTime().getHours() + ":" +
                                            saturdayPeriod.getClose().getTime().getMinutes())) + " today");
                        }
                    } else {
                        if (Integer.parseInt(currentTime[0]) < saturdayPeriod.getClose().getTime().getHours()) {
                            tpdIsOpen.setText("Open");
                            tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                    .parse(saturdayPeriod.getClose().getTime().getHours() + ":" +
                                            saturdayPeriod.getClose().getTime().getMinutes())) + " today");
                        } else if (Integer.parseInt(currentTime[0]) == saturdayPeriod.getOpen().getTime().getHours()) {
                            if (Integer.parseInt(currentTime[1]) < saturdayPeriod.getOpen().getTime().getMinutes()) {
                                tpdIsOpen.setText("Open");
                                tpdClosingHours.setText("Closes " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                        .parse(saturdayPeriod.getClose().getTime().getHours() + ":" +
                                                saturdayPeriod.getClose().getTime().getMinutes())) + " today");
                            } else {
                                tpdIsOpen.setText("Closed");
                                getOpenHoursNextDay(openOrCLosePeriod, Calendar.SATURDAY);
                            }
                        } else {
                            tpdIsOpen.setText("Closed");
                            getOpenHoursNextDay(openOrCLosePeriod, Calendar.SATURDAY);
                        }
                    }
                } else {
                    tpdIsOpen.setText("Closed");
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.SATURDAY);
                }
                break;
        }
    }

    private void getOpenHoursNextDay(List<Period> openOrCLosePeriod, int day) throws ParseException {
        switch (day) {
            case Calendar.SUNDAY:
                Period mondayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.MONDAY) {
                        mondayPeriod = period;
                        break;
                    }
                }

                if (mondayPeriod != null) {
                    try {
                        tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                                .parse(mondayPeriod.getOpen().getTime().getHours() + ":" +
                                        mondayPeriod.getOpen().getTime().getMinutes())) + " MON");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.MONDAY);
                }
                break;
            case Calendar.MONDAY:
                Period tuesdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.TUESDAY) {
                        tuesdayPeriod = period;
                        break;
                    }
                }

                if (tuesdayPeriod != null) {
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(tuesdayPeriod.getOpen().getTime().getHours() + ":" +
                                    tuesdayPeriod.getOpen().getTime().getMinutes())) + " TUE");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.TUESDAY);
                }
                break;
            case Calendar.TUESDAY:
                Period wednesdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.WEDNESDAY) {
                        wednesdayPeriod = period;
                        break;
                    }
                }

                if (wednesdayPeriod != null) {
                    Log.e(TAG, wednesdayPeriod.getOpen().getTime().toString());
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(wednesdayPeriod.getOpen().getTime().getHours() + ":" +
                                    wednesdayPeriod.getOpen().getTime().getMinutes())) + " WED");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.WEDNESDAY);
                }
                break;
            case Calendar.WEDNESDAY:
                Period thursdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.THURSDAY) {
                        thursdayPeriod = period;
                        break;
                    }
                }

                if (thursdayPeriod != null) {
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(thursdayPeriod.getOpen().getTime().getHours() + ":" +
                                    thursdayPeriod.getOpen().getTime().getMinutes())) + " THU");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.THURSDAY);
                }
                break;
            case Calendar.THURSDAY:
                Period fridayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.FRIDAY) {
                        fridayPeriod = period;
                        break;
                    }
                }

                if (fridayPeriod != null) {
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(fridayPeriod.getOpen().getTime().getHours() + ":" +
                                    fridayPeriod.getOpen().getTime().getMinutes())) + " FRI");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.FRIDAY);
                }
                break;
            case Calendar.FRIDAY:
                Period saturdayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.SATURDAY) {
                        saturdayPeriod = period;
                        break;
                    }
                }

                if (saturdayPeriod != null) {
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(saturdayPeriod.getOpen().getTime().getHours() + ":" +
                                    saturdayPeriod.getOpen().getTime().getMinutes())) + " SAT");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.SATURDAY);
                }
                break;
            case Calendar.SATURDAY:
                Period sundayPeriod = null;
                for (Period period : openOrCLosePeriod) {
                    if (period.getOpen().getDay() == DayOfWeek.SUNDAY) {
                        sundayPeriod = period;
                        break;
                    }
                }

                if (sundayPeriod != null) {
                    tpdClosingHours.setText("Opens " + new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm")
                            .parse(sundayPeriod.getOpen().getTime().getHours() + ":" +
                                    sundayPeriod.getOpen().getTime().getMinutes())) + " SUN");
                } else {
                    getOpenHoursNextDay(openOrCLosePeriod, Calendar.SUNDAY);
                }
                break;
        }
    }

    public class TourismDetailAdapter extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nama;
        TextView review;

        public TourismDetailAdapter(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            nama = itemView.findViewById(R.id.username_review);
            review = itemView.findViewById(R.id.review_user);
        }

        public void setNama(String nama) {
            this.nama.setText(nama);
        }

        public void setImage(String foto) {
            Glide.with(PlaceDetail.this)
                    .load(foto)
                    .placeholder(R.drawable.profile_annasaha)
                    .into(image);
        }

        public void setReview(String review) {
            this.review.setText(review);
        }
    }

}
