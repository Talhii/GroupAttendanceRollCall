package com.example.groupattendancerollcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEventMainActivity extends AppCompatActivity {

    ImageView ivBack;
    Spinner spinner;
    EditText etFromDate, etFromTime, etToDate, etToTime, etDescription;
    Button btnDone, btnEventAttendeeList;


    private SharedPreferences prefs;
    private FirebaseDatabase database;
    private DatabaseReference eventTypeRef, eventRef;

    String staffMemberEmail;
    String groupName;

    ArrayList<String> nameList, descList;

    String fromDate, fromTime, toDate, toTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_main);

        ivBack = findViewById(R.id.ivBack);

        spinner = findViewById(R.id.spinner);

        spinner = findViewById(R.id.spinner);


        etFromDate = findViewById(R.id.etFromDate);
        etFromTime = findViewById(R.id.etFromTime);
        etToDate = findViewById(R.id.etToDate);
        etToTime = findViewById(R.id.etToTime);
        etDescription = findViewById(R.id.etDescription);

        btnDone = findViewById(R.id.btnDone);
        btnEventAttendeeList = findViewById(R.id.btnAttendeeList);

        prefs = getSharedPreferences("StaffMember", MODE_PRIVATE);

        staffMemberEmail = prefs.getString("staffMemberEmail", "No Mail defined");
        groupName = prefs.getString("groupName", "No Mail defined");


        nameList = new ArrayList<String>();
        descList = new ArrayList<String>();


        database = FirebaseDatabase.getInstance();
        eventTypeRef = database.getReference().child("EventType");
        eventRef = database.getReference().child("Event");


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventMainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        fromDate = year + "/" + month + "/" + dayOfMonth;
                        etFromDate.setText(fromDate);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        etFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR);
                int mins = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventMainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        fromTime = hourOfDay + ":" + minute;
                        etFromTime.setText(fromTime);
                    }
                }, hours, mins, true);

                timePickerDialog.show();
            }
        });


        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventMainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        toDate = year + "/" + month + "/" + dayOfMonth;
                        etToDate.setText(toDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        etToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR);
                int mins = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventMainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        toTime = hourOfDay + ":" + minute;
                        etToTime.setText(toTime);
                    }
                }, hours, mins, true);

                timePickerDialog.show();
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFromDate.getText().toString())) {
                    etFromDate.setError("Fill this first");
                    return;
                } else if (TextUtils.isEmpty(etFromTime.getText().toString())) {
                    etFromTime.setError("Fill this first");
                    return;
                } else if (TextUtils.isEmpty(etToTime.getText().toString())) {
                    etToTime.setError("Fill this first");
                    return;
                } else if (TextUtils.isEmpty(etToDate.getText().toString())) {
                    etToDate.setError("Fill this first");
                    return;
                } else if (TextUtils.isEmpty(etDescription.getText().toString())) {
                    etDescription.setError("Fill this first");
                    return;
                } else {

                    final String randomKey = UUID.randomUUID().toString();

                    eventRef.child(randomKey).child("EventTypeName").setValue(spinner.getSelectedItem().toString());
                    eventRef.child(randomKey).child("EventFromDate").setValue(fromDate);
                    eventRef.child(randomKey).child("EventFromTime").setValue(fromTime);
                    eventRef.child(randomKey).child("EventToDate").setValue(toDate);
                    eventRef.child(randomKey).child("EventToTime").setValue(toTime);
                    eventRef.child(randomKey).child("EventDescription").setValue(etDescription.getText().toString());
                    eventRef.child(randomKey).child("StaffMemberEmail").setValue(getIntent().getStringExtra("staffMemberEmail"));
                    eventRef.child(randomKey).child("GroupName").setValue(getIntent().getStringExtra("groupName"));


                    Toast.makeText(AddEventMainActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getSpinnerValues(){
        eventTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (staffMemberEmail.equals(ds.child("StaffMemberEmail").getValue().toString()) && groupName.equals(ds.child("GroupName").getValue().toString())) {
                        nameList.add(ds.child("EventTypeName").getValue().toString());
                        descList.add(ds.child("EventTypeDescription").getValue().toString());

                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEventMainActivity.this, android.R.layout.simple_spinner_item, nameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        nameList.clear();
        descList.clear();
        getSpinnerValues();
    }
}