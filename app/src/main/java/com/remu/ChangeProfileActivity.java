package com.remu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;
import com.saber.chentianslideback.SlideBackActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChangeProfileActivity extends SlideBackActivity {

    private TextView saveButton;
    private TextInputEditText name, dateOfBirth, about;
    private AutoCompleteTextView genderSpinner;

    private boolean isSaveButtonDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        initializeUI();
        Animatoo.animateSlideLeft(this);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setTextColor(getResources().getColor(R.color.trevuMidPink));
                isSaveButtonDisabled = false;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        genderSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setTextColor(getResources().getColor(R.color.trevuMidPink));
                isSaveButtonDisabled = false;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        dateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setTextColor(getResources().getColor(R.color.trevuMidPink));
                isSaveButtonDisabled = false;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        about.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setTextColor(getResources().getColor(R.color.trevuMidPink));
                isSaveButtonDisabled = false;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        dateOfBirth.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        switch (monthOfYear) {
                            case 0:
                                dateOfBirth.setText(dayOfMonth + " " + "January" + " " + year1);
                                break;
                            case 1:
                                dateOfBirth.setText(dayOfMonth + " " + "February" + " " + year1);
                                break;
                            case 2:
                                dateOfBirth.setText(dayOfMonth + " " + "March" + " " + year1);
                                break;
                            case 3:
                                dateOfBirth.setText(dayOfMonth + " " + "April" + " " + year1);
                                break;
                            case 4:
                                dateOfBirth.setText(dayOfMonth + " " + "May" + " " + year1);
                                break;
                            case 5:
                                dateOfBirth.setText(dayOfMonth + " " + "June" + " " + year1);
                                break;
                            case 6:
                                dateOfBirth.setText(dayOfMonth + " " + "July" + " " + year1);
                                break;
                            case 7:
                                dateOfBirth.setText(dayOfMonth + " " + "August" + " " + year1);
                                break;
                            case 8:
                                dateOfBirth.setText(dayOfMonth + " " + "September" + " " + year1);
                                break;
                            case 9:
                                dateOfBirth.setText(dayOfMonth + " " + "October" + " " + year1);
                                break;
                            case 10:
                                dateOfBirth.setText(dayOfMonth + " " + "November" + " " + year1);
                                break;
                            case 11:
                                dateOfBirth.setText(dayOfMonth + " " + "December" + " " + year1);
                                break;
                        }
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(year - 10, month, day).getTimeInMillis());
            datePickerDialog.show();
        });

        String[] arrayOfGender = new String[]{"Male", "Female"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.adapter_gender_edit_profile, arrayOfGender);
        genderSpinner.setAdapter(arrayAdapter);

        saveButton.setOnClickListener(v -> {
            if (!isSaveButtonDisabled) {
                //TODO: Save edited info

                finish();
            }
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
        saveButton = findViewById(R.id.ep_save_info);
        name = findViewById(R.id.ep_name);
        dateOfBirth = findViewById(R.id.ep_dateofbirth);
        genderSpinner = findViewById(R.id.ep_gender);
        about = findViewById(R.id.ep_about);
        isSaveButtonDisabled = true;
    }

}
