package com.example.groupattendancerollcall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AdminAddMember extends AppCompatActivity {


    EditText etGroupName, etEmail;
    Button btnAddMember;

    private FirebaseDatabase database;
    private DatabaseReference addedByAdminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_member);

        etGroupName = findViewById(R.id.etGroupName);
        etEmail = findViewById(R.id.etEmail);


        database = FirebaseDatabase.getInstance();
        addedByAdminRef = database.getReference().child("AddedByAdmin");

        btnAddMember = findViewById(R.id.btnAddMember);


        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String randomKey = UUID.randomUUID().toString();

                addedByAdminRef.child(randomKey).child("GroupName").setValue(etGroupName.getText().toString());
                addedByAdminRef.child(randomKey).child("MemberEmail").setValue(etEmail.getText().toString());


                Toast.makeText(AdminAddMember.this, "Member Can now register with the group name and email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}