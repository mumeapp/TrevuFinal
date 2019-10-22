package com.remu;

import android.Manifest;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.saber.chentianslideback.SlideBackActivity;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MosqueActivity extends SlideBackActivity implements OnMapReadyCallback {

    private static final String TAG = "MosqueActivity";

    private static String url = "http://api.aladhan.com/v1/timingsByCity?city=Selangor&country=Malaysia&method=3";

    ArrayList<HashMap<String, String>> prayerList;

    ExpandableCardView jamSolat;
    RelativeLayout someInformation;
    TextView jamSolatSelanjutnya;
    TextView solatSelanjutnya;
    TextView timeFajr;
    TextView timeDhuhr;
    TextView timeAsr;
    TextView timeMahgrib;
    TextView timeIsha;

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

        //initialize ui
        initializeUI();
        new GetData().execute();

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

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(SalatTimeActivity.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject timings = new JSONObject(jsonStr).getJSONObject("data").getJSONObject("timings");

                    String fajr = timings.getString("Fajr");
                    String dhuhr = timings.getString("Dhuhr");
                    String asr = timings.getString("Asr");
                    String maghrib = timings.getString("Maghrib");
                    String isha = timings.getString("Isha");

                    HashMap<String, String> fajrHash = new HashMap<>();
                    HashMap<String, String> dhuhrHash = new HashMap<>();
                    HashMap<String, String> asrHash = new HashMap<>();
                    HashMap<String, String> magribHash = new HashMap<>();
                    HashMap<String, String> ishaHash = new HashMap<>();

                    fajrHash.put("name", "Fajr");
                    fajrHash.put("time", fajr);
                    dhuhrHash.put("name", "Dhuhr");
                    dhuhrHash.put("time", dhuhr);
                    asrHash.put("name", "Asr");
                    asrHash.put("time", asr);
                    magribHash.put("name", "Maghrib");
                    magribHash.put("time", maghrib);
                    ishaHash.put("name", "Isha");
                    ishaHash.put("time", isha);

                    prayerList.add(fajrHash);
                    prayerList.add(dhuhrHash);
                    prayerList.add(asrHash);
                    prayerList.add(magribHash);
                    prayerList.add(ishaHash);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String currentTime = df.format(c.getTime());

            HashMap<String, String> temp = nextPrayerTime(currentTime);
            jamSolatSelanjutnya.setText(temp.get("time"));
            solatSelanjutnya.setText(temp.get("name"));

            timeFajr.setText(prayerList.get(0).get("time"));
            timeDhuhr.setText(prayerList.get(1).get("time"));
            timeAsr.setText(prayerList.get(2).get("time"));
            timeMahgrib.setText(prayerList.get(3).get("time"));
            timeIsha.setText(prayerList.get(4).get("time"));
//                        ListAdapter adapter = new SimpleAdapter(
//                    SalatTimeActivity.this, prayerList,
//                    R.layout.salat_time_list_item, new String[]{"name", "time"}, new int[]{R.id.name,
//                    R.id.time});
//
//            listView.setAdapter(adapter);
        }

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
        prayerList = new ArrayList<>();
        listMasjid = findViewById(R.id.listMasjid);
        jamSolatSelanjutnya = someInformation.findViewById(R.id.jamSolatSelanjutnya);
        solatSelanjutnya = someInformation.findViewById(R.id.solatSelanjutnya);
        timeFajr = jamSolat.findViewById(R.id.time_fajr);
        timeDhuhr = jamSolat.findViewById(R.id.time_dhuhr);
        timeAsr = jamSolat.findViewById(R.id.time_asr);
        timeMahgrib = jamSolat.findViewById(R.id.time_maghrib);
        timeIsha = jamSolat.findViewById(R.id.time_isha);
    }

    private HashMap<String, String> nextPrayerTime(String currentTime) {
        HashMap<String, String> currentPrayerTime = new HashMap<String, String>();

        int index = 0;

        for (int i = 0; i < prayerList.size() - 1; i++) {
            int rangeBottom = (Integer.parseInt(prayerList.get(i).get("time").split(":")[0]) * 60) + Integer.parseInt(prayerList.get(i).get("time").split(":")[1]);
            int rangeTop = (Integer.parseInt(prayerList.get(i + 1).get("time").split(":")[0]) * 60) + Integer.parseInt(prayerList.get(i + 1).get("time").split(":")[1]);
            int currentTimeMnt = (Integer.parseInt(currentTime.split(":")[0]) * 60) + Integer.parseInt(currentTime.split(":")[1]);

            Range<Integer> myRange = new Range<Integer>(rangeBottom, rangeTop);
            if (myRange.contains(currentTimeMnt)){
                index = i + 1;
                break;
            }
        }

        currentPrayerTime.put("name", prayerList.get(index).get("name"));
        currentPrayerTime.put("time", prayerList.get(index).get("time"));
        return currentPrayerTime;
    }
}
