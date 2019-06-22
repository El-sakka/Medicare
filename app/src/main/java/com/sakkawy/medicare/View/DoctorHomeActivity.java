package com.sakkawy.medicare.View;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sakkawy.medicare.Adapter.DoctorViewPagerAdapter;
import com.sakkawy.medicare.Fragments.ChatFragment;
import com.sakkawy.medicare.Fragments.MedicineFragment;
import com.sakkawy.medicare.Fragments.PatientFragment;
import com.sakkawy.medicare.R;

import java.util.HashMap;

public class DoctorHomeActivity extends AppCompatActivity {


    TabLayout tabLayout ;
    ViewPager viewPager ;
    private int[] tabIcons = {
            R.drawable.ic_patient,
            R.drawable.ic_chat_icon,
            R.drawable.ic_drugs,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        setUpViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();
    }

    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setUpViewPager(){
        DoctorViewPagerAdapter adapter = new DoctorViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PatientFragment());
        adapter.addFragment(new ChatFragment());
        adapter.addFragment(new MedicineFragment());
        viewPager.setAdapter(adapter);
    }

//    private void status(String status){
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(userId);
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("Status",status);
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("offline");
//    }
}
