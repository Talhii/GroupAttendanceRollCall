package com.example.groupattendancerollcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSignIn extends AppCompatActivity {


    EditText etAdminEmail, etAdminPassword;
    Button btnSignIn;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin);

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(etAdminEmail.getText().toString())) {
                    etAdminEmail.setError("Enter Email First");
                    return;
                } else if (TextUtils.isEmpty(etAdminPassword.getText().toString())) {
                    etAdminPassword.setError("Enter Password First");
                    return;
                } else {
                    String email = etAdminEmail.getText().toString();
                    String password = etAdminPassword.getText().toString();


                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);


                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AdminSignIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if(etAdminEmail.getText().toString().equals("admin@gmail.com")){
                                    Toast.makeText(AdminSignIn.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminSignIn.this, Admin.class);
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(AdminSignIn.this, "Only Admin can log in here", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AdminSignIn.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}