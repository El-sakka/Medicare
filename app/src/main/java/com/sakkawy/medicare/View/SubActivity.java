package com.sakkawy.medicare.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakkawy.medicare.Adapter.HomeRecycleViewAdapter;
import com.sakkawy.medicare.Model.SpecailityItem;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity implements HomeRecycleViewAdapter.ItemClickListener{

    RecyclerView mRecyclerView;
    List<SpecailityItem> mList = new ArrayList<>();
    HomeRecycleViewAdapter mAdapter;

    String folderName,folderId;
    Intent intent;
    View view;

    TextView headerName;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        intent = getIntent();
        folderName = intent.getStringExtra("keyName");
        folderId = intent.getStringExtra("keyId");


        view = findViewById(R.id.include_layout);
        headerName = view.findViewById(R.id.header_name);
        backArrow = view.findViewById(R.id.back_arrow);

        headerName.setText(folderName);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList.add(new SpecailityItem(R.drawable.ic_prescription,"Prescriptions","0"));
        mList.add(new SpecailityItem(R.drawable.ic_lungs_radiography,"Radiology","1"));
        mList.add(new SpecailityItem(R.drawable.ic_lab_flask,"Laboratory","2"));
        mList.add(new SpecailityItem(R.drawable.ic_medical,"Medical report","3"));
        mList.add(new SpecailityItem(R.drawable.ic_apple_black_silhouette_with_a_leaf,"Diet & Lifestyle","4"));

        mAdapter = new HomeRecycleViewAdapter(this,this,mList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onItemClickListener(SpecailityItem item) {

        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("parent",folderId);
        intent.putExtra("name",item.getName());
        intent.putExtra("id",item.getId());
        startActivity(intent);


    }
}
