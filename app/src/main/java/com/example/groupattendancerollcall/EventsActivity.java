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

public class EventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventMainViewAdapter viewAdapter;
    ImageView ivBack, ivAdd;

    private FirebaseDatabase database;
    private DatabaseReference eventRef;
    private SharedPreferences prefs;
    String staffMemberEmail;
    String groupName;


    ArrayList<String> fromDateList,fromTimeList,toDateList,toTimeList, eventTypeNameList,eventDescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);


        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference().child("Event");


        fromDateList = new ArrayList<String>();
        fromTimeList = new ArrayList<String>();
        toDateList = new ArrayList<String>();
        toTimeList = new ArrayList<String>();
        eventTypeNameList = new ArrayList<String>();
        eventDescriptionList = new ArrayList<String>();

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
                Intent intent = new Intent(EventsActivity.this, AddEventMainActivity.class);
                intent.putExtra("staffMemberEmail", staffMemberEmail);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });
    }

    private void getListView() {

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (staffMemberEmail.equals(ds.child("StaffMemberEmail").getValue().toString()) && groupName.equals(ds.child("GroupName").getValue().toString())) {
                        fromDateList.add(ds.child("EventFromDate").getValue().toString());
                        fromTimeList.add(ds.child("EventFromTime").getValue().toString());
                        toDateList.add(ds.child("EventToDate").getValue().toString());
                        toTimeList.add(ds.child("EventToTime").getValue().toString());
                        eventTypeNameList.add(ds.child("EventTypeName").getValue().toString());
                        eventDescriptionList.add(ds.child("EventDescription").getValue().toString());

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
        viewAdapter = new EventMainViewAdapter(this, getList());
        recyclerView.setAdapter(viewAdapter);
    }

    private List<EventMainViewModel> getList() {
        List<EventMainViewModel> viewList = new ArrayList<>();

        viewList.clear();

        for (int i = 0; i < fromDateList.size() && i < eventTypeNameList.size(); i++) {

            viewList.add(new EventMainViewModel(fromDateList.get(i),fromTimeList.get(i),toDateList.get(i),toTimeList.get(i), eventTypeNameList.get(i),eventDescriptionList.get(i)));
        }

        return viewList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fromDateList.clear();
        fromTimeList.clear();
        toDateList.clear();
        toTimeList.clear();
        eventTypeNameList.clear();
        eventDescriptionList.clear();
        getListView();
    }
}