package com.sakkawy.medicare.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sakkawy.medicare.Fragments.DoctorRegistrationFragment;
import com.sakkawy.medicare.Fragments.PatientRegistrationFragment;
import com.sakkawy.medicare.Interface.OnDataPass;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistrationActivity extends AppCompatActivity implements OnDataPass {

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
    AlertDialog mAlertDialog;
    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    
    String userId;
    String imageUri = "default";

    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ivProfile = findViewById(R.id.iv_profile);
        ivEditProfile = findViewById(R.id.iv_edit_profile);
        btnDoctor = findViewById(R.id.btn_doctor);
        btnPatient = findViewById(R.id.btn_patient);

        if(mFragmentTransaction == null){
            PatientRegistrationFragment mPatientRegistrationFragment = new PatientRegistrationFragment();
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container,mPatientRegistrationFragment);
            mFragmentTransaction.commit();
        }

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentTransaction != null){
                    DoctorRegistrationFragment mDoctorRegistrationFragment = new DoctorRegistrationFragment();
                    mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.replace(R.id.fragment_container,mDoctorRegistrationFragment);
                    mFragmentTransaction.commit();
                }
            }
        });

        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentTransaction != null){
                    PatientRegistrationFragment mPatientRegistrationFragment = new PatientRegistrationFragment();
                    mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.replace(R.id.fragment_container,mPatientRegistrationFragment);
                    mFragmentTransaction.commit();
                }
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
        db = FirebaseDatabase.getInstance().getReference().child("Users");

        mAlertDialog = new SpotsDialog.Builder().setContext(RegistrationActivity.this).build();
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
    
    private void saveImage(final User user){
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
                        user.setImageUri(imageUri);
                        Log.d(TAG, "onSuccess: patiunt" +  user.toString());
                        db.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mAlertDialog.dismiss();
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
        else{
            db.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mAlertDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Done",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //auth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //auth.removeAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onDataPass(final User user) {
        if(user != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you Sure ?");
            builder.setTitle("Alert !");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    mAlertDialog.show();
                                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    user.setUserId(userId);
                                    saveImage(user);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
