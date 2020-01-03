package com.remu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Arrays;
import java.util.List;

public class HalalGiftDetail extends SlideBackActivity {

    public static final String TAG = "HalalGiftDetail";

    private PlacesClient placesClient;

    private ImageView giftImage, userImage;
    private TextView giftName, giftDistance, giftRating, giftAddress;
    private RatingBar giftRatingBar;
    private EditText giftReview;
    private Button giftReviewButton;
    private RecyclerView listReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_gift_detail);

        Places.initialize(getApplicationContext(), "AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8");
        placesClient = Places.createClient(this);

        initializeUI();
        Animatoo.animateSlideLeft(this);

        getPlace(getIntent().getStringExtra("place_id"));

        giftReviewButton.setOnClickListener((v -> {
            String review = giftReview.getText().toString();

            //TODO: PUT REVIEW IN FIREBASE
        }));

        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    private void getPlace(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.PHOTO_METADATAS, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.RATING, Place.Field.ADDRESS);
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

    private void applyPlaceInfoToView(Place giftPlace) {
        if (giftPlace != null) {
            if (giftPlace.getPhotoMetadatas() != null) {
                PhotoMetadata photoMetadata = giftPlace.getPhotoMetadatas().get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxHeight(500) // Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    giftImage.setImageBitmap(bitmap);
                }).addOnFailureListener((exception) -> {
                    Log.e(TAG, exception.toString());
                });
            } else {
                LatLng location = giftPlace.getLatLng();
                Picasso.get().load("https://maps.googleapis.com/maps/api/streetview?size=500x300&location=" + location.latitude + "," + location.longitude
                        + "&fov=120&pitch=10&key=AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                        .error(R.drawable.bg_loading)
                        .placeholder(R.drawable.bg_loading)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(giftImage);
            }

            giftName.setText(giftPlace.getName());
            giftDistance.setText(String.format("%.2f km", countDistance(giftPlace.getLatLng())));

            if (giftPlace.getRating() == null) {
                giftRating.setText("-");
            } else {
                giftRating.setText(String.format("%.1f", giftPlace.getRating()));
            }

            giftAddress.setText(giftPlace.getAddress());
        }
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
        giftImage = findViewById(R.id.gift_detail_image);
        userImage = findViewById(R.id.gift_detail_profile_image);
        giftName = findViewById(R.id.gift_detail_name);
        giftDistance = findViewById(R.id.gift_detail_distance);
        giftRating = findViewById(R.id.gift_detail_rating);
        giftAddress = findViewById(R.id.gift_detail_address);
        giftRatingBar = findViewById(R.id.gift_detail_rating_bar);
        giftReview = findViewById(R.id.gift_detail_review_edit_text);
        giftReviewButton = findViewById(R.id.gift_detail_submit_button);
        listReviews = findViewById(R.id.list_gift_detail_review_users);
    }

    private double countDistance(LatLng latLng) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Latitude", null)),
                Double.parseDouble(getApplication().getSharedPreferences("location", MODE_PRIVATE).getString("Longitude", null)));
        return Distance.distance(currentLatLng.latitude, latLng.latitude, currentLatLng.longitude, latLng.longitude);
    }

}
