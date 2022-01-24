package com.example.groupattendancerollcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    ImageView ivBack;
    EditText etName, etDescription;
    Button btnDone;

    private FirebaseDatabase database;
    private DatabaseReference eventTypeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ivBack = findViewById(R.id.ivBack);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        btnDone = findViewById(R.id.btnDone);

        database = FirebaseDatabase.getInstance();
        eventTypeRef = database.getReference().child("EventType");



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("Event Name cannot be empty");
                    return;
                } else if (TextUtils.isEmpty(etDescription.getText().toString())) {
                    etDescription.setError("Event Description cannot be empty");
                    return;
                }
                else{

                    final String randomKey = UUID.randomUUID().toString();

                    eventTypeRef.child(randomKey).child("EventTypeName").setValue(etName.getText().toString());
                    eventTypeRef.child(randomKey).child("EventTypeDescription").setValue(etDescription.getText().toString());
                    eventTypeRef.child(randomKey).child("StaffMemberEmail").setValue(getIntent().getStringExtra("staffMemberEmail"));
                    eventTypeRef.child(randomKey).child("GroupName").setValue(getIntent().getStringExtra("groupName"));


                    Toast.makeText(AddEventActivity.this, "Event Type added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}