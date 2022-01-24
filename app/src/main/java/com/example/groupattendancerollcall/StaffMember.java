package com.example.groupattendancerollcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffMember extends AppCompatActivity {
    Button btnEvents, btnEventTypeList, btnAttendeeList, btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_member);

        btnEvents = findViewById(R.id.btnEvents);
        btnEventTypeList = findViewById(R.id.btnEventTypeList);
        btnAttendeeList = findViewById(R.id.btnAttendeeList);
        btnHelp = findViewById(R.id.btnHelp);

        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffMember.this, EventsActivity.class);
                startActivity(intent);
            }
        });

        btnEventTypeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffMember.this, EventTypeListActivity.class);
                startActivity(intent);
            }
        });

        btnAttendeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffMember.this, AttendeeListActivity.class);
                startActivity(intent);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffMember.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }
}