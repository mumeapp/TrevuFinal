package com.remu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.model.Place;
import com.rtchagas.pingplacepicker.PingPlacePicker;

public class SetAddressActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1;
    public static String Jenis= "jenis", Kategori= "kategori";
    private String lat, lang, nama;
    private String jenis, kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);
        jenis = getIntent().getStringExtra(Jenis);
        kategori = getIntent().getStringExtra(Kategori);
        showPlacePicker();
    }
    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8")
                .setMapsApiKey("AIzaSyA2yW_s0jqKnavh2AxISXB272VuSE56WI8");

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
        }
        catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);

            if (place != null) {
                lat = Double.toString(place.getLatLng().latitude);
                lang = Double.toString(place.getLatLng().longitude);
                nama = place.getName();
                Toast.makeText(this, place.getAddress(), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(this, RestoranActivity.class);
                in.putExtra(RestoranActivity.lat, lat);
                in.putExtra(RestoranActivity.lang, lang);
                in.putExtra(RestoranActivity.nama, nama);
                in.putExtra(RestoranActivity.Jenis, jenis);
                in.putExtra(RestoranActivity.kategori, kategori);
                startActivity(in);
                finish();
            }
        }
    }

}
