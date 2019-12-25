package com.remu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.remu.POJO.TextProcess;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class AddDictionary extends AppCompatActivity {

    private DatabaseReference database;

    private EditText etTextAwal;
    private EditText etTextTranslete;
    private String Audio;
    private ProgressDialog loading;

    private Button mbtnRecord;
    private MediaRecorder recorder;
    private String fileName = null;
    private static final String LOG_TAG = "Record_Log";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private StorageReference mStorage;
    private String uniqueID = UUID.randomUUID().toString();

    private Spinner spinAwal, spinAkhir;
    private Awal awal;
    private Akhir akhir;
    private String textAwal;
    private String textAkhir;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dictionary);

//        awal = new Awal();
//        akhir = new Akhir();
//
//        spinAwal = findViewById(R.id.bhsawal1);
//        spinAkhir = findViewById(R.id.bhstrans1);
//        ArrayAdapter<CharSequence> adaptAwal = ArrayAdapter.createFromResource(this, R.array.bahasaAwal, android.R.layout.simple_spinner_item);
//        adaptAwal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinAwal.setAdapter(adaptAwal);
//        spinAwal.setOnItemSelectedListener(awal);
//
//        ArrayAdapter<CharSequence> adaptAkhir = ArrayAdapter.createFromResource(this, R.array.bahasaTrans, android.R.layout.simple_spinner_item);
//        adaptAkhir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinAkhir.setAdapter(adaptAkhir);
//        spinAkhir.setOnItemSelectedListener(akhir);
//
//        mStorage= FirebaseStorage.getInstance().getReference();
//        ActivityCompat.requestPermissions(AddDictionary.this, permissions,REQUEST_RECORD_AUDIO_PERMISSION);
//        mbtnRecord = findViewById(R.id.btnRecord);
//        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        fileName += "/"+uniqueID+".mp3";
//
//        database = FirebaseDatabase.getInstance().getReference();
//
//        final Button btn = findViewById(R.id.okButton);
//        etTextAwal = findViewById(R.id.textAwal);
//        etTextTranslete = findViewById(R.id.textTranslete);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                String StextAwal = etTextAwal.getText().toString();
//                String StextTranslete = etTextTranslete.getText().toString();
//
//                String id = currentUser.getUid();
//
//                if (StextAwal.equals("")){
//                    etTextAwal.setError("Field Tidak Boleh Kosong");
//                    etTextAwal.requestFocus();
//                }
//                else if (StextTranslete.equals("")){
//                    etTextTranslete.setError("Field Tidak Boleh Kosong");
//                    etTextTranslete.requestFocus();
//                }
//                else {
//                    loading = ProgressDialog.show(AddDictionary.this,
//                            null,
//                            "please wait...",
//                            true,
//                            false);
//
//                    String child = null;
//                    if ((awal.getText().equalsIgnoreCase("jepang") && akhir.getText().equalsIgnoreCase("Indonesia")) || (awal.getText().equalsIgnoreCase("indonesia") && akhir.getText().equalsIgnoreCase("jepang"))) {
//                        child = "indonesia-jepang";
//                        textAwal = "Jepang";
//                        textAkhir = "Indonesia";
//                        submitUser(new TextProcess(
//                                StextAwal.toLowerCase(),
//                                StextTranslete.toLowerCase(),textAwal,textAkhir,Audio,id),child);
//                        link();
//                    } else if ((awal.getText().equalsIgnoreCase("jepang") && akhir.getText().equalsIgnoreCase("Inggris")) || (awal.getText().equalsIgnoreCase("ingris") && akhir.getText().equalsIgnoreCase("jepang"))) {
//                        child = "jepang-inggris";
//                        textAwal = "Jepang";
//                        textAkhir = "Inggris";
//                        submitUser(new TextProcess(
//                                StextAwal.toLowerCase(),
//                                StextTranslete.toLowerCase(),textAwal,textAkhir,Audio,id),child);
//                        link();
//                    } else if ((awal.getText().equalsIgnoreCase("inggris")&&akhir.getText().equalsIgnoreCase("Indonesia"))||(awal.getText().equalsIgnoreCase("indonesia")&&akhir.getText().equalsIgnoreCase("inggris"))) {
//                        child = "inggris-indonesia";
//                        textAwal = "Inggris";
//                        textAkhir = "Indonesia";
//                        submitUser(new TextProcess(
//                                StextAwal.toLowerCase(),
//                                StextTranslete.toLowerCase(),textAwal,textAkhir,Audio,id),child);
//                        link();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Tidak bisa menggunakan bahasa yang sama", Toast.LENGTH_LONG).show();
//                        loading.dismiss();
//                    }
//
//                }
//            }
//        });
//        mbtnRecord.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//
//                    startRecording();
//                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//
//                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//
//                    stopRecording();
//                    Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
//                }
//
//                return false;
//            }
//        });
    }
    private void submitUser(TextProcess requests, String child){
        database.child("Dictionary").child(child).push().setValue(requests).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loading.dismiss();

                etTextAwal.setText("");
                etTextTranslete.setText("");


                Toast.makeText(AddDictionary.this, "data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void link(){
        Intent intent = new Intent(AddDictionary.this, DictionaryActivity.class);
        startActivity(intent);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

//        recorder.start();
    }

    private void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            uploadAudio();
        }
        catch (Exception e){
            Log.e(LOG_TAG, "failed to save");
        }

    }

    private void uploadAudio(){

        loading = ProgressDialog.show(AddDictionary.this,
                null,
                "please wait...",
                true,
                false);
        StorageReference filepath = mStorage.child("Audio").child(uniqueID+".mp3");
        Uri uri = Uri.fromFile(new File(fileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loading.dismiss();
                Audio = uri.toString();
            }
        });
    }

}

class Awal implements AdapterView.OnItemSelectedListener{
    private String text;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getText() {
        return text;
    }
}

class Akhir implements AdapterView.OnItemSelectedListener{
    private String text;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getText() {
        return text;
    }
}
