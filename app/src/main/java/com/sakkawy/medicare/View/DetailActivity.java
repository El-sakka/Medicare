package com.sakkawy.medicare.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sakkawy.medicare.Fragments.DietLifestyleFragment;
import com.sakkawy.medicare.Fragments.LaboratoryFragment;
import com.sakkawy.medicare.Fragments.MedicalReportFragment;
import com.sakkawy.medicare.Fragments.PatientRegistrationFragment;
import com.sakkawy.medicare.Fragments.PrescriptionsFragment;
import com.sakkawy.medicare.Fragments.RadiologyFragment;
import com.sakkawy.medicare.R;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    Intent intent ;
    String name , id , parentFolder;
    View view;

    TextView headerName;
    ImageView backArrow;

    FragmentTransaction mFragmentTransaction;


    private boolean fabExpanded = false;
    private FloatingActionButton fabAdd;
    private LinearLayout layoutFabAddFolder;
    private LinearLayout layoutFabSaveImage;
    private LinearLayout layoutFabSavePres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        parentFolder = intent.getStringExtra("parent");

        Log.d(TAG, " id : "+id);

        view = findViewById(R.id.header_layout);
        headerName = view.findViewById(R.id.header_name);
        backArrow = view.findViewById(R.id.back_arrow);

        headerName.setText(name);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(id.equals("0")){
            PrescriptionsFragment prescriptionsFragment = new PrescriptionsFragment(parentFolder);
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frg_container,prescriptionsFragment);
            mFragmentTransaction.commit();
        }else if(id.equals("1")){
            RadiologyFragment radiologyFragment = new RadiologyFragment(parentFolder);
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frg_container,radiologyFragment);
            mFragmentTransaction.commit();

        }else if(id.equals("2")){
            LaboratoryFragment laboratoryFragment = new LaboratoryFragment(parentFolder);
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frg_container,laboratoryFragment);
            mFragmentTransaction.commit();


        }else if(id.equals("3")){
            MedicalReportFragment medicalReportFragment = new MedicalReportFragment(parentFolder);
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frg_container,medicalReportFragment);
            mFragmentTransaction.commit();
        }else{
            DietLifestyleFragment dietLifestyleFragment = new DietLifestyleFragment(parentFolder);
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frg_container,dietLifestyleFragment);
            mFragmentTransaction.commit();
        }

    }



}
