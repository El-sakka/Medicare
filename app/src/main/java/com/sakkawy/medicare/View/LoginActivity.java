package com.sakkawy.medicare.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.util.Map;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    Button btnSignUp , btnLogIn ;
    EditText etUserName,etPassword;

    String email , password , userId;

    AlertDialog alertDialog ;
    FirebaseAuth auth;
    DatabaseReference db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        

        etUserName = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);
        auth = FirebaseAuth.getInstance();

        alertDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).build();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etUserName.getText().toString();
                password = etPassword.getText().toString();

                if(email.equals("") || email == null){
                    etUserName.setError("Required");
                }
                else if (password.equals("") || password == null){
                    etPassword.setError("Required");
                }else{
                    alertDialog.show();

                    auth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    alertDialog.dismiss();
                                    userId = authResult.getUser().getUid();
                                    Log.d(TAG, "onClick: "+userId);
                                    db = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    db.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                                                Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                                                Log.d(TAG, "onDataChange: "+map.get("userType").toString());
                                                String userType = map.get("userType").toString();
                                                if(userType.equals("Patient")){
                                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                }else{
                                                    Intent intent = new Intent(LoginActivity.this,DoctorHomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismiss();
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //sssss

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
