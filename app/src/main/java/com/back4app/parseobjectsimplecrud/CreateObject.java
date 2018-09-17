package com.back4app.parseobjectsimplecrud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateObject extends AppCompatActivity {
    private EditText itemName;
    private EditText itemAdd;
    private CalendarView itemDate;
    private Button create_button;
    private Switch isAvailable;
    private Date formatterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);

        itemName = (EditText) findViewById(R.id.edtItem);
        itemAdd = (EditText) findViewById(R.id.edtAdditionalInformation);
        itemDate = (CalendarView) findViewById(R.id.calendarView);
        create_button = findViewById(R.id.btnCreate);
        isAvailable = (Switch) findViewById(R.id.swiAvailable);

        // Get Date from CalendarView
        itemDate.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                String getDate = (dayOfMonth + "/" + (month+1) + "/" + year);
                formatterDate = convertStringToData(getDate);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, ");
                if (isEmptyText(itemName)) {
                    validationError = true;
                    validationErrorMessage.append("insert an name");
                }
                if (isEmptyDate()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("select a Date");
                }
                validationErrorMessage.append(".");
                if (validationError) {
                    Toast.makeText(CreateObject.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    saveObject();
                }
            }
        });
    }
    public static Date convertStringToData(String getDate){
        Date today = null;
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");

        try {
            today = simpleDate.parse(getDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return today;
    }
    private boolean isEmptyText(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isEmptyDate() {
        if (String.valueOf(formatterDate) != "null") {
            return false;
        } else {
            return true;
        }
    }
    public void saveObject(){
        // Configure Query
        ParseObject reminderList = new ParseObject("reminderList");

        // Store an object
        reminderList.put("itemName", itemName.getText().toString());
        reminderList.put("additionalInformation", itemAdd.getText().toString());
        reminderList.put("dateCommitment", formatterDate);
        reminderList.put("isAvailable", isAvailable.isChecked());
        reminderList.put("userId", ParseUser.getCurrentUser());

        // Saving object
        reminderList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(CreateObject.this, ReadObjects.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }
}