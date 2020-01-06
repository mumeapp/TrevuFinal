package com.remu;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.remu.POJO.Distance;
import com.saber.chentianslideback.SlideBackActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TourismDetail extends SlideBackActivity {

    private static final String TAG = "TourismDetail";

    private TextView tpdName, tpdRating1, tpdTotalRating1, tpdRating2, tpdTotalRating2, tpdCityLocation,
            tpdDistance, tpdAddress, tpdPlusCode, tpdIsOpen, tpdClosingHours, tpdPhone;
    private CardView tpdDiscoverButton;
    private ImageView tpdPhoto, tpdBookmarkBorder, tpdBookmarkFilled, tpdProfilePicture;
    private RatingBar tpdInputRating;
    private EditText tpdInputReview;
    private RecyclerView tpdListUserReview;

    private PlacesClient placesClient;
    private Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_detail);

        Places.initialize(getApplicationContext(), "AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8");
        placesClient = Places.createClient(this);
        mGeocoder = new Geocoder(this, Locale.getDefault());

        initializeUI();
        Animatoo.animateSlideDown(this);

        getPlace(getIntent().getStringExtra("place_id"));

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
                tpdRating2.setText("-");
                tpdTotalRating1.setText("(0)");
                tpdTotalRating2.setText("(0)");
            } else {
                tpdRating1.setText(String.format("%.1f", tourismPlace.getRating()));
                tpdTotalRating1.setText(String.format("(%d)", tourismPlace.getUserRatingsTotal()));
                tpdRating2.setText(String.format("%.1f", tourismPlace.getRating()));
                tpdTotalRating2.setText(String.format("(%d)", tourismPlace.getUserRatingsTotal()));
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

            if (tourismPlace.getPhoneNumber().isEmpty()) {
                tpdPhone.setText("-");
            } else {
                tpdPhone.setText(tourismPlace.getPhoneNumber());
            }
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

        tpdInputRating = findViewById(R.id.tpd_input_rating);
        LayerDrawable stars = (LayerDrawable) tpdInputRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        tpdInputReview = findViewById(R.id.tpd_input_review);
        tpdRating2 = findViewById(R.id.tpd_rating_2);
        tpdTotalRating2 = findViewById(R.id.tpd_user_rating_total_2);
        tpdListUserReview = findViewById(R.id.tpd_list_review);
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

}
