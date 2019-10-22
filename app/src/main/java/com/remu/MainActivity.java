package com.remu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static String url = "http://api.aladhan.com/v1/timingsByCity?city=Selangor&country=Malaysia&method=3";

    ArrayList<HashMap<String, String>> prayerList;

    CardView mosqueCardView, foodButton, dictionaryButton, friendButton, tourButton;
    String name;
    TextView nama;
    TextView jamSolatSelanjutnya;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize uI
        initializeUI();

        new GetData().execute();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set nama
        getCurrentUser(currentUser);
        //go to mosque activity
        mosqueCardView.setOnClickListener(view -> {
            Intent viewMosque = new Intent(MainActivity.this, MosqueActivity.class);
            startActivity(viewMosque);
        });

        //go to food activity
        foodButton.setOnClickListener(view -> {
            Intent viewFood = new Intent(MainActivity.this, FoodActivity.class);
            startActivity(viewFood);
        });

        //go to Dictionary Activity
        dictionaryButton.setOnClickListener(view -> {
            Intent viewDictonary = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(viewDictonary);
        });
        friendButton.setOnClickListener(view -> {
            Intent viewFriend = new Intent(MainActivity.this, FindFriendsActivity.class);
            startActivity(viewFriend);
        });
        tourButton.setOnClickListener(view -> {
            Intent viewTour = new Intent(MainActivity.this, TourismActivity.class);
            startActivity(viewTour);
        });

    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String currentTime = df.format(c.getTime());

            HashMap<String, String> temp = nextPrayerTime(currentTime);
            jamSolatSelanjutnya.setText(temp.get("time"));
        }

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

    private void initializeUI() {
        mosqueCardView = findViewById(R.id.MosqueCardView);
        foodButton = findViewById(R.id.foodButton);
        dictionaryButton = findViewById(R.id.dictionaryButton);
        friendButton = findViewById(R.id.findFriendsButton);
        tourButton = findViewById(R.id.tourismButton);
        nama = findViewById(R.id.nama);
        prayerList = new ArrayList<>();
        jamSolatSelanjutnya = findViewById(R.id.jamSolatSelanjutnya);
    }
    private void getCurrentUser(FirebaseUser user){
        if(user !=null){
            String name =user.getDisplayName();
            nama.setText(name);
        }
    }
}
