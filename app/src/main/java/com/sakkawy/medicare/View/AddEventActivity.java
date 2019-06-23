package com.sakkawy.medicare.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sakkawy.medicare.R;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void  call(View v){
        Toast.makeText(AddEventActivity.this , "working" , Toast.LENGTH_SHORT ).show();
    }
}
