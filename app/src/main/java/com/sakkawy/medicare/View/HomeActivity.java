package com.sakkawy.medicare.View;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sakkawy.medicare.Adapter.ViewPagerAdapter;
import com.sakkawy.medicare.Fragments.CalenderFragment;
import com.sakkawy.medicare.Fragments.ChatFragment;
import com.sakkawy.medicare.Fragments.DoctorFragment;
import com.sakkawy.medicare.Fragments.HomeFragment;
import com.sakkawy.medicare.Fragments.NotifcationFragment;
import com.sakkawy.medicare.R;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout ;
    ViewPager viewPager ;

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

    private void setUpViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new DoctorFragment());
        adapter.addFragment(new ChatFragment());
        adapter.addFragment(new CalenderFragment());
        adapter.addFragment(new NotifcationFragment());
        viewPager.setAdapter(adapter);
    }
}
