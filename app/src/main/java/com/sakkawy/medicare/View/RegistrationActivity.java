package com.sakkawy.medicare.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sakkawy.medicare.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    ImageView  ivEditProfile;
    CircleImageView ivProfile;
    Button btnDoctor, btnPatient, btnSignUp;
    EditText etName, etUserName, etEmail,etPassword, etDate,etGender;


    DatabaseReference db ;
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

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Patient");


    }
}
