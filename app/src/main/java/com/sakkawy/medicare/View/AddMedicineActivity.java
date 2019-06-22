package com.sakkawy.medicare.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import com.sakkawy.medicare.R;

public class AddMedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);



//        DisplayMetrics dm  = new DisplayMetrics() ;
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = (int)(dm.widthPixels*0.8) ;
//        int height = (int)(dm.heightPixels*0.8) ;
//
//        getWindow().setLayout(width, height);
    }
}
