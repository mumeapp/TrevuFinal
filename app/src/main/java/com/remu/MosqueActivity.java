package com.remu;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remu.POJO.PrayerTime;
import com.saber.chentianslideback.SlideBackActivity;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;

public class MosqueActivity extends SlideBackActivity implements OnMapReadyCallback {

    private static final String TAG = "MosqueActivity";

    private PrayerTime prayerTime;

    private String latitude, longitude;

    ExpandableCardView jamSolat;
    RelativeLayout someInformation;
    TextView jamSolatSelanjutnya, solatSelanjutnya, timeFajr, timeDhuhr, timeAsr, timeMahgrib, timeIsha;
    LinearLayout layoutFajr, layoutDhuhr, layoutAsr, layoutMaghrib, layoutIsha;

    MultiSnapRecyclerView listMasjid;

    //sydney, change later to malang
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    // The entry points to the Places API.
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    private GoogleMap mMap;
    private LatLng latLng;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    RecyclerView.Adapter mAdapter;
    ArrayList<String> mDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");

        Log.e(TAG, latitude);
        Log.e(TAG, longitude);

        //initialize ui
        initializeUI();

        //set title for expandable card
        jamSolat.setOnExpandedListener((v, isExpanded) -> {
            if (isExpanded) {
                jamSolat.setTitle("Jadwal Sholat Hari Ini");
                someInformation.setVisibility(View.INVISIBLE);
            } else {
                jamSolat.setTitle("Jadwal Sholat Selanjutnya");
                someInformation.setVisibility(View.VISIBLE);
            }
        });

        //start the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        mDataSet = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDataSet.add("Title #" + i);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listMasjid.setLayoutManager(layoutManager);
        mAdapter = new MosqueAdapter(mDataSet);
        listMasjid.setAdapter(mAdapter);
        listMasjid.setOnSnapListener(position -> {

        });
        setSlideBackDirection(SlideBackActivity.LEFT);
    }

    @Override
    protected void slideBackSuccess() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        int Zoomlevel = 16;

        if (latLng != null) {
            //mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoomlevel));
        }
    }

    private void getLocationPermission() {
        //WILL BE MOVED TO PERMISSIONACTIVITY LATER!
        Dexter.withActivity(MosqueActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mLocationPermissionGranted = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        mLocationPermissionGranted = false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.toString());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    mLastKnownLocation = task.getResult();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), 15));
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.getException());
                    mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(mDefaultLocation, 15));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.toString());
        }
    }

    private void initializeUI() {
        jamSolat = findViewById(R.id.jamSolat);
        someInformation = findViewById(R.id.someInformation);
        listMasjid = findViewById(R.id.listMasjid);
        jamSolatSelanjutnya = someInformation.findViewById(R.id.jamSolatSelanjutnya);
        solatSelanjutnya = someInformation.findViewById(R.id.solatSelanjutnya);
        timeFajr = jamSolat.findViewById(R.id.time_fajr);
        timeDhuhr = jamSolat.findViewById(R.id.time_dhuhr);
        timeAsr = jamSolat.findViewById(R.id.time_asr);
        timeMahgrib = jamSolat.findViewById(R.id.time_maghrib);
        timeIsha = jamSolat.findViewById(R.id.time_isha);
        ArrayList<TextView> textViews = new ArrayList<TextView>(){{
            add(jamSolatSelanjutnya);
            add(solatSelanjutnya);
            add(timeFajr);
            add(timeDhuhr);
            add(timeAsr);
            add(timeMahgrib);
            add(timeIsha);
        }};
        layoutFajr = jamSolat.findViewById(R.id.layout_fajr);
        layoutDhuhr = jamSolat.findViewById(R.id.layout_dhuhr);
        layoutAsr = jamSolat.findViewById(R.id.layout_asr);
        layoutMaghrib = jamSolat.findViewById(R.id.layout_maghrib);
        layoutIsha = jamSolat.findViewById(R.id.layout_isha);
        ArrayList<LinearLayout> linearLayouts = new ArrayList<LinearLayout>(){{
            add(layoutFajr);
            add(layoutDhuhr);
            add(layoutAsr);
            add(layoutMaghrib);
            add(layoutIsha);
        }};
        prayerTime = new PrayerTime(this.getApplicationContext(), TAG, latitude, latitude, textViews, linearLayouts);
        prayerTime.execute();
    }

}
