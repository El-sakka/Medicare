package com.sakkawy.medicare.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sakkawy.medicare.R;

public class settings extends AppCompatActivity {

    TextView changePassword ,drugAllur ,foodAllur , treatment , longTerm , disabilities ,
             previousOperations  , logOut ;

    Button b ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
//        changePassword = (TextView) findViewById(R.id.tv_changePassword) ;

//        Toast.makeText(settings.this , "it's working" , Toast.LENGTH_SHORT ).show();

//        changePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changePassword.setText("word");
//            }
//        });
    }

}
