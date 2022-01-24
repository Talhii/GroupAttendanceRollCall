package com.example.groupattendancerollcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Signup extends AppCompatActivity {

    private EditText etGroupName, etMemberName, etEmail, etPassword;
    private Button btnRegisterMain;
    private TextView tvLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase database;
    private DatabaseReference addedByAdminRef, registerStaffRef;
    String isMatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        addedByAdminRef = database.getReference().child("AddedByAdmin");
        registerStaffRef = database.getReference().child("RegisteredStaff");


        progressDialog = new ProgressDialog(this);


        etGroupName = findViewById(R.id.etGroupName);
        etMemberName = findViewById(R.id.etMemberName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegisterMain = findViewById(R.id.btnRegisterMain);
        tvLogin = findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(etGroupName.getText().toString())) {
                    etGroupName.setError("Group Name cannot be empty");
                    return;
                } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    etEmail.setError("Email cannot be empty");
                    return;
                } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    etPassword.setError("Password cannot be empty");
                    return;
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError("Password length should not be less than 6");
                    return;
                } else if (!checkEmail(etEmail.getText().toString())) {
                    etEmail.setError("Email is not valid");
                    return;
                } else {
                    checkUser();
                }
            }
        });

    }


    private void checkUser() {
        isMatched = "false";
        addedByAdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if ((etGroupName.getText().toString()).equals(ds.child("GroupName").getValue().toString()) && ds.child("MemberEmail").getValue().toString().equals(etEmail.getText().toString())) {
                        RegisterUser();
                        isMatched = "true";
                    }
                }
                if (isMatched.equals("false")) {
                    Toast.makeText(Signup.this, "Not Added by AdminSignIn Yet with this email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RegisterUser() {

        final String group_name = etGroupName.getText().toString();
        final String member_name = etMemberName.getText().toString();
        final String email = etEmail.getText().toString();
        String pin = etPassword.getText().toString();

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.createUserWithEmailAndPassword(email, pin).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    final String randomKey = UUID.randomUUID().toString();

                    registerStaffRef.child(randomKey).child("GroupName").setValue(group_name);
                    registerStaffRef.child(randomKey).child("MemberName").setValue(member_name);
                    registerStaffRef.child(randomKey).child("MemberEmail").setValue(email);

                    Toast.makeText(Signup.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.w("Tag", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Signup.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }


    private boolean checkEmail(CharSequence mail) {
        return (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches());
    }
}
