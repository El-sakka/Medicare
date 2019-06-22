package com.sakkawy.medicare.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.HomeRecycleViewAdapter;
import com.sakkawy.medicare.Fragments.HomeFragment;
import com.sakkawy.medicare.Model.Folder;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.List;

public class SubFolderActivity extends AppCompatActivity implements HomeRecycleViewAdapter.ItemClickListener {
    private static final String TAG = "SubFolderActivity";
    public static final String KEY = "key";
    FloatingActionButton fbAddSubFolder;
    RecyclerView mRecyclerView;
    String parentId;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbFolder;

    HomeRecycleViewAdapter adapter;
    List<Folder> mList = new ArrayList<>();


    private boolean fabExpanded = false;
    private FloatingActionButton fabAdd;
    private LinearLayout layoutFabAddFolder;
    private LinearLayout layoutFabSaveImage;
    private LinearLayout layoutFabSavePres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_folder);

        parentId = getIntent().getStringExtra(KEY);
        Log.d(TAG, "onCreate: parent ID "+ parentId);


        dbFolder = FirebaseDatabase.getInstance().getReference().child("FolderCollection");

        fabAdd = this.findViewById(R.id.fabMain);
        layoutFabAddFolder = this.findViewById(R.id.layoutFabSaveFolder);
        layoutFabSaveImage = this.findViewById(R.id.layoutFabSaveImage);
        layoutFabSavePres = this.findViewById(R.id.layoutFabSavePrescription);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabExpanded == true){
                    closeSubMenusFab();
                }else{
                    openSubMenusFab();
                }
            }
        });

        layoutFabAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        mRecyclerView = findViewById(R.id.rv_sub_folders);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HomeRecycleViewAdapter(this, this,mList);
        mRecyclerView.setAdapter(adapter);
        closeSubMenusFab();
        updateFolders();

    }

    private void closeSubMenusFab(){
        layoutFabSavePres.setVisibility(View.INVISIBLE);
        layoutFabAddFolder.setVisibility(View.INVISIBLE);
        layoutFabSaveImage.setVisibility(View.INVISIBLE);
        fabAdd.setImageResource(R.drawable.ic_add_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        layoutFabSavePres.setVisibility(View.VISIBLE);
        layoutFabAddFolder.setVisibility(View.VISIBLE);
        layoutFabSaveImage.setVisibility(View.VISIBLE);
        fabAdd.setImageResource(R.drawable.ic_done_24dp);
        fabExpanded = true;
    }

    private void updateFolders(){
        //if(mList.size() != 0)   mList.clear();

        dbFolder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Folder folder = snapshot.getValue(Folder.class);
                        if(folder.getParent().equals(parentId) && folder.getUserId().equals(currentUserId)){
                            mList.add(folder);

                            Log.d(TAG, "onDataChange: list Size "+mList.size());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addItem(){
        //mList.add(new ItemHome(R.drawable.ic_name_icon,));
        final LayoutInflater factory = LayoutInflater.from(SubFolderActivity.this);
        final View updateDialogView = factory.inflate(R.layout.dialog_add_item_layout, null);
        final AlertDialog updatePassDialog = new AlertDialog.Builder(SubFolderActivity.this).create();
        updatePassDialog.setView(updateDialogView);
        updateDialogView.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemEditText = updateDialogView.findViewById(R.id.tv_item_name);
                final String folderName = itemEditText.getText().toString();
                if (folderName.isEmpty()) {
                    itemEditText.setError(getResources().getString(R.string.error));
                } else {
                    //todo confirm on operation

                    //String parent = parentId;
                    String key = dbFolder.push().getKey();
                    Folder folder = new Folder(parentId,currentUserId,folderName,key);
                    dbFolder.child(key).setValue(folder);
                    updatePassDialog.dismiss();
                    //adapter.notifyDataSetChanged();

                }
            }
        });
        updateDialogView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassDialog.dismiss();
            }
        });
        updatePassDialog.show();

    }

    @Override
    public void onItemClickListener(Folder folder) {
        Intent intent = new Intent(SubFolderActivity.this,SubFolderActivity.class);
        intent.putExtra(SubFolderActivity.KEY,folder.getFolderId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(SubFolderActivity.this,folder.getFolderName(),Toast.LENGTH_SHORT).show();
    }
}
