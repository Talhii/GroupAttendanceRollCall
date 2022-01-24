package com.example.groupattendancerollcall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AddAttendeeActivity extends AppCompatActivity {

    ImageView ivBack;
    TextView tvTakePhoto, tvPick, tvMessage;
    EditText etId, etFirstname, etLastName, etContactPersonName, etContactPersonMobile, etContactPersonEmail, etNote;
    Button btnDone;

    public Uri image_uri;


    String Storage_Path = "AttendeeData/";
    String Database_Path = "AttendeeData";

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendee);

        ivBack = findViewById(R.id.ivBack);

        tvTakePhoto = findViewById(R.id.tvTakePhoto);
        tvPick = findViewById(R.id.tvPick);
        tvMessage = findViewById(R.id.tvMessage);


        storageReference = FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);


        etId = findViewById(R.id.etId);
        etFirstname = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etContactPersonName = findViewById(R.id.etContactPersonName);
        etContactPersonMobile = findViewById(R.id.etContactPersonMobile);
        etContactPersonEmail = findViewById(R.id.etContactPersonEmail);
        etNote = findViewById(R.id.etNote);

        btnDone = findViewById(R.id.btnDone);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });


        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            image_uri = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            image_uri = getImageUri(getApplicationContext(), photo);

            tvMessage.setText("Image Selected");


        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            image_uri = data.getData();
            tvMessage.setText("Image Selected");
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Attendee", null);
        return Uri.parse(path);
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void uploadData() {


        if (TextUtils.isEmpty(etId.getText().toString())) {
            etId.setError("Fill it first");
            return;
        }
        else if (etId.getText().toString().length()<6) {
            etId.setError("ID should be at least 6 characters");
            return;
        }else if (TextUtils.isEmpty(etFirstname.getText().toString())) {
            etFirstname.setError("Fill it first");
            return;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            etLastName.setError("Fill it first");
            return;
        } else if (TextUtils.isEmpty(etContactPersonName.getText().toString())) {
            etContactPersonName.setError("Fill it first");
            return;
        } else if (TextUtils.isEmpty(etContactPersonEmail.getText().toString())) {
            etContactPersonEmail.setError("Fill it first");
            return;
        } else if (TextUtils.isEmpty(etContactPersonMobile.getText().toString())) {
            etContactPersonMobile.setError("Fill it first");
            return;
        } else if (TextUtils.isEmpty(etNote.getText().toString())) {
            etNote.setError("Fill it first");
            return;
        } else if (image_uri == null) {
            Toast.makeText(this, "Select Image First", Toast.LENGTH_SHORT).show();
            return;
        } else {
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(image_uri));

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.show();
            storageReference2nd.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            // Getting image upload ID.

                            final String randomKey = UUID.randomUUID().toString();

                            databaseReference.child(randomKey).child("AttendeeId").setValue(etId.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeFirstName").setValue(etFirstname.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeLastName").setValue(etLastName.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeContactPersonName").setValue(etContactPersonName.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeContactPersonEmail").setValue(etContactPersonEmail.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeContactPersonMobile").setValue(etContactPersonMobile.getText().toString());
                            databaseReference.child(randomKey).child("AttendeeNote").setValue(etNote.getText().toString());
                            databaseReference.child(randomKey).child("ImageUrl").setValue(photoLink);
                            databaseReference.child(randomKey).child("StaffMemberEmail").setValue(getIntent().getStringExtra("staffMemberEmail"));
                            databaseReference.child(randomKey).child("GroupName").setValue(getIntent().getStringExtra("groupName"));
                            databaseReference.child(randomKey).child("Active").setValue("No");


                            Toast.makeText(AddAttendeeActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddAttendeeActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress_percentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded Percentage: " + (int) progress_percentage);
                }

            });
        }
    }
}