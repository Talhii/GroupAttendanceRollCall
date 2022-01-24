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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventTypeListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventTypeListViewAdapter viewAdapter;
    ImageView ivBack, ivAdd;

    private FirebaseDatabase database;
    private DatabaseReference eventTypeRef;
    private SharedPreferences prefs;
    String staffMemberEmail;
    String groupName;

    ArrayList<String> nameList, descList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type_list);


        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);


        prefs = getSharedPreferences("StaffMember", MODE_PRIVATE);


        staffMemberEmail = prefs.getString("staffMemberEmail", "No Mail defined");
        groupName = prefs.getString("groupName", "No Mail defined");


        nameList = new ArrayList<String>();
        descList = new ArrayList<String>();

        database = FirebaseDatabase.getInstance();
        eventTypeRef = database.getReference().child("EventType");


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventTypeListActivity.this, AddEventActivity.class);
                intent.putExtra("staffMemberEmail", staffMemberEmail);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });

    }

    private void getListView() {

        eventTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (staffMemberEmail.equals(ds.child("StaffMemberEmail").getValue().toString()) && groupName.equals(ds.child("GroupName").getValue().toString())) {
                        nameList.add(ds.child("EventTypeName").getValue().toString());
                        descList.add(ds.child("EventTypeDescription").getValue().toString());

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

        viewAdapter = new EventTypeListViewAdapter(this, getList());
        recyclerView.setAdapter(viewAdapter);
    }

    private List<EventTypeListViewModel> getList() {

        List<EventTypeListViewModel> viewList = new ArrayList<>();
        viewList.clear();

        for (int i = 0; i < nameList.size() && i < descList.size(); i++) {

            viewList.add(new EventTypeListViewModel(nameList.get(i), descList.get(i)));
        }


        return viewList;

    }

    @Override
    protected void onResume() {
        super.onResume();
        nameList.clear();
        descList.clear();
        getListView();
    }
}