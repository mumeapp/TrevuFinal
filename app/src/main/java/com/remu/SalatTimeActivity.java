package com.remu;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SalatTimeActivity extends AppCompatActivity {

    private String TAG = SalatTimeActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    //    private ListView listView;
    private TextView timeFajr;
    private TextView timeDhuhr;
    private TextView timeAsr;
    private TextView timeMahgrib;
    private TextView timeIsha;

    private static String url = "http://api.aladhan.com/v1/timingsByCity?city=Selangor&country=Malaysia&method=3";

    ArrayList<HashMap<String, String>> prayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salat_time);

        prayerList = new ArrayList<>();
        timeFajr = findViewById(R.id.time_fajr);
        timeDhuhr = findViewById(R.id.time_dhuhr);
        timeAsr = findViewById(R.id.time_asr);
        timeMahgrib = findViewById(R.id.time_maghrib);
        timeIsha = findViewById(R.id.time_isha);

//        listView = findViewById(R.id.salat_list);
        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SalatTimeActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

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

    private void highlightCurrentTime() {

    }

    private int nextPrayerTime(String currentTime) {
        int index = 0;

        for (int i = 0; i < prayerList.size() - 1; i++) {
            int rangeBottom = (Integer.parseInt(prayerList.get(i).get("time").split(":")[0]) * 60) + Integer.parseInt(prayerList.get(i).get("time").split(":")[1]);
            int rangeTop = (Integer.parseInt(prayerList.get(i + 1).get("time").split(":")[0]) * 60) + Integer.parseInt(prayerList.get(i + 1).get("time").split(":")[1]);
            int currentTimeMnt = (Integer.parseInt(currentTime.split(":")[0]) * 60) + Integer.parseInt(currentTime.split(":")[1]);

            Range<Integer> myRange = new Range<Integer>(rangeBottom, rangeTop);
            if (myRange.contains(currentTimeMnt)) {
                index = i + 1;
                break;
            }
        }

        return index;
    }
}
