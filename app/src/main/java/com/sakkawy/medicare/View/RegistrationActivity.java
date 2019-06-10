package com.sakkawy.medicare.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sakkawy.medicare.Helper.ViewHelper;
import com.sakkawy.medicare.Model.Patient;
import com.sakkawy.medicare.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    ImageView  ivEditProfile;
    CircleImageView ivProfile;
    Button btnDoctor, btnPatient, btnSignUp;
    EditText etName, etUserName, etEmail,etPassword, etDate,etGender;


    DatabaseReference db ;
    StorageReference filePath;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener fireAuthStateListener;
    private Uri resultUri;
    AlertDialog alertDialog;
    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    
    String userId;
    String name, userName , email , password , gender , birthOfDate , imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ivProfile = findViewById(R.id.iv_profile);
        ivEditProfile = findViewById(R.id.iv_edit_profile);
        btnDoctor = findViewById(R.id.btn_doctor);
        btnPatient = findViewById(R.id.btn_patient);
        btnSignUp = findViewById(R.id.btn_signUp);
        etName = findViewById(R.id.et_name);
        etUserName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etDate = findViewById(R.id.et_date);
        etGender = findViewById(R.id.gender);


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients");

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(RegistrationActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        alertDialog = new SpotsDialog.Builder().setContext(RegistrationActivity.this).build();



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ViewHelper.checkViews(getApplicationContext(),etName,etUserName,etEmail,etPassword,etDate,etGender)){
                    // do nothing
                }else{
                    alertDialog.show();
                    name = etName.getText().toString();
                    userName = etUserName.getText().toString();
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    gender= etGender.getText().toString();
                    birthOfDate = etDate.getText().toString();

                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            saveImage();
                            

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            ivProfile.setImageURI(resultUri);
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }
    
    private void saveImage(){
        if(resultUri != null){
            filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            byte[] data = baos.toByteArray();
            //upload image to database
            UploadTask uploadTask = filePath.putBytes(data);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUri = downloadUri.toString();
                        Log.d(TAG, "onComplete: "+imageUri);

                        Patient patient = new Patient(name,userName,email,password,birthOfDate,gender,imageUri);
                        Log.d(TAG, "onSuccess: patiunt" +  patient.toString());
                        db.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(patient)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegistrationActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {
                        // hanle failers
                    }
                }
            });

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(fireAuthStateListener);
    }
}
