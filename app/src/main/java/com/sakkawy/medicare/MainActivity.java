package com.sakkawy.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sakkawy.medicare.View.AddMedicineActivity;
import com.sakkawy.medicare.View.LoginActivity;
import com.sakkawy.medicare.View.addEventActivity;

public class MainActivity extends AppCompatActivity {

    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, addEventActivity.class );
                Intent intent = new Intent(MainActivity.this , AddMedicineActivity.class) ;
                 startActivity(intent);
            }
        });
    }
}
