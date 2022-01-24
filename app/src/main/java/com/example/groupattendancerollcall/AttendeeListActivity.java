package com.example.groupattendancerollcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendeeListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewAdapter viewAdapter;
    ImageView ivBack, ivAdd;

    private FirebaseDatabase database;
    private DatabaseReference attendeeDataRef;

    private SharedPreferences prefs;
    String staffMemberEmail;
    String groupName;

    ArrayList<String> id,firstName,lastName,contactPersonName, contactPersonEmail,contactPersonMobile,note,imageUrl,active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);

        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);


        database = FirebaseDatabase.getInstance();
        attendeeDataRef = database.getReference().child("AttendeeData");



        id = new ArrayList<String>();
        firstName = new ArrayList<String>();
        lastName = new ArrayList<String>();
        contactPersonName = new ArrayList<String>();
        contactPersonMobile = new ArrayList<String>();
        contactPersonEmail = new ArrayList<String>();
        note = new ArrayList<String>();
        imageUrl = new ArrayList<String>();
        active = new ArrayList<String>();


        prefs = getSharedPreferences("StaffMember", MODE_PRIVATE);
        staffMemberEmail = prefs.getString("staffMemberEmail", "No Mail defined");
        groupName = prefs.getString("groupName", "No Mail defined");


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendeeListActivity.this, AddAttendeeActivity.class);
                intent.putExtra("staffMemberEmail", staffMemberEmail);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });
    }


    private void getListView() {

        attendeeDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (staffMemberEmail.equals(ds.child("StaffMemberEmail").getValue().toString()) && groupName.equals(ds.child("GroupName").getValue().toString())) {
                        id.add(ds.child("AttendeeId").getValue().toString());
                        firstName.add(ds.child("AttendeeFirstName").getValue().toString());
                        lastName.add(ds.child("AttendeeLastName").getValue().toString());
                        contactPersonName.add(ds.child("AttendeeContactPersonName").getValue().toString());
                        contactPersonMobile.add(ds.child("AttendeeContactPersonMobile").getValue().toString());
                        contactPersonEmail.add(ds.child("AttendeeContactPersonEmail").getValue().toString());
                        note.add(ds.child("AttendeeNote").getValue().toString());
                        imageUrl.add(ds.child("ImageUrl").getValue().toString());
                        active.add(ds.child("Active").getValue().toString());

                        setRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAdapter = new ViewAdapter(this, getList());
        recyclerView.setAdapter(viewAdapter);
    }

    private List<ViewModel> getList() {
        List<ViewModel> viewList = new ArrayList<>();

        viewList.clear();

        for (int i = 0; i < id.size() && i < note.size(); i++) {

            viewList.add(new ViewModel(id.get(i),firstName.get(i),lastName.get(i),contactPersonName.get(i), contactPersonName.get(i),contactPersonMobile.get(i),note.get(i),imageUrl.get(i),active.get(i)));
        }

        return viewList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        id.clear();
        firstName.clear();
        lastName.clear();
        contactPersonName.clear();
        contactPersonEmail.clear();
        contactPersonMobile.clear();
        note.clear();
        imageUrl.clear();
        active.clear();
        getListView();
    }

}