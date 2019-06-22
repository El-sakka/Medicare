package com.sakkawy.medicare.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sakkawy.medicare.R;

public class addEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void  call(View v){
        Toast.makeText(addEventActivity.this , "working" , Toast.LENGTH_SHORT ).show();
    }
}
