package com.example.groupattendancerollcall;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity {


    private EditText etGroupName, etEmail, etPassword;
    private Button btnLoginMain, btnRegister;
    private TextView tvAsAdmin;


    private SharedPreferences.Editor editor;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference addedByAdminRef;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String isMatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editor = getSharedPreferences("StaffMember", MODE_PRIVATE).edit();

        firebaseAuth = FirebaseAuth.getInstance();
        addedByAdminRef = database.getReference().child("AddedByAdmin");
        progressDialog = new ProgressDialog(this);

        etGroupName = findViewById(R.id.etGroupName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        tvAsAdmin = findViewById(R.id.tvAsAdmin);

        btnLoginMain = findViewById(R.id.btnLoginMain);
        btnRegister = findViewById(R.id.btnRegister);


        btnLoginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    etEmail.setError("Email cannot be empty");
                    return;
                } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    etPassword.setError("Password cannot be empty");
                    return;
                } else if (!checkEmail(etEmail.getText().toString())) {
                    etEmail.setError("Email is not valid");
                    return;
                } else {
                    checkUser();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        tvAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AdminSignIn.class);
                startActivity(i);
            }
        });
    }

    private void checkUser() {
        isMatched = "false";
        addedByAdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if ((etGroupName.getText().toString()).equals(ds.child("GroupName").getValue().toString()) && etEmail.getText().toString().equals(ds.child("MemberEmail").getValue().toString())) {
                        Login();
                        isMatched = "true";
                    }
                }
                if (isMatched.equals("false")) {
                    Toast.makeText(MainActivity.this, "No Account with this email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Login() {

        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, StaffMember.class);
                    editor.putString("staffMemberEmail", email);
                    editor.putString("groupName", etGroupName.getText().toString());
                    editor.apply();

                    progressDialog.dismiss();
                    startActivity(intent);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Wrong User name or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkEmail(CharSequence mail) {
        return (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches());
    }
}
