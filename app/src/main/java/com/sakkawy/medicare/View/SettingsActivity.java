package com.sakkawy.medicare.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.MainActivity;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    TextView name, age, changePassword ,drugAllur ,foodAllur , treatment , longTerm , disabilities ,
             previousOperations  , logOut ;

    CircleImageView ivProfile;


    FirebaseUser user ;
    DatabaseReference reference;
    Spinner genderSpinner;
    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        genderSpinner = findViewById(R.id.sp_gender);
        name = findViewById(R.id.tv_userName);
        ivProfile = findViewById(R.id.iv_profile);

        changePassword = findViewById(R.id.tv_changePassword);
        drugAllur = findViewById(R.id.tv_drugAllergies);
        foodAllur = findViewById(R.id.tv_foodAllergies);
        treatment = findViewById(R.id.treatment);
        longTerm = findViewById(R.id.long_terms);
        disabilities = findViewById(R.id.disapility);
        previousOperations = findViewById(R.id.previous);
        logOut = findViewById(R.id.logout);




        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getUserName());
                    genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if(user.getImageUri() != null){
                        Glide.with(SettingsActivity.this).load(user.getImageUri()).into(ivProfile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
