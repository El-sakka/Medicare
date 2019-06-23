package com.sakkawy.medicare.View;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sakkawy.medicare.Adapter.ViewPagerAdapter;
import com.sakkawy.medicare.Fragments.CalenderFragment;
import com.sakkawy.medicare.Fragments.ChatFragment;
import com.sakkawy.medicare.Fragments.DoctorFragment;
import com.sakkawy.medicare.Fragments.HomeFragment;
import com.sakkawy.medicare.Fragments.NotifcationFragment;
import com.sakkawy.medicare.R;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    TabLayout tabLayout ;
    ViewPager viewPager ;

    View view;
    ImageView setting;

    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.ic_doctors_icon,
            R.drawable.ic_chat_icon,
            R.drawable.ic_calendar_icon,
            R.drawable.ic_notifications_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view = findViewById(R.id.header_setting);
        setting = view.findViewById(R.id.iv_setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

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
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new DoctorFragment());
        adapter.addFragment(new ChatFragment());
        adapter.addFragment(new CalenderFragment());
        adapter.addFragment(new NotifcationFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void status(String status){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("Status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");

    }
}
