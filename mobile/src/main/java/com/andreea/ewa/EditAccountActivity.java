package com.andreea.ewa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by andreeagb on 1/11/2018.
 */

public class EditAccountActivity extends Activity  implements View.OnClickListener {

    private EditText birthDay;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private Spinner genderSpinner;

    private final String[] gender = { "Male", "Female" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaccount);

        birthDay = findViewById(R.id.eBirthday);
        genderSpinner = (Spinner) findViewById(R.id.spinnerGender);

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        // setting adapteers to spinners
        genderSpinner.setAdapter(spin_adapter);

        birthDay.setInputType(InputType.TYPE_NULL);
        birthDay.requestFocus();

        setDateTimeField();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    }

    private void setDateTimeField() {
        birthDay.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDay.setText(simpleDateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View v) {
        if(v == birthDay)
            datePickerDialog.show();
    }
}
