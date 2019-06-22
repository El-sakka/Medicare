package com.sakkawy.medicare.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.HomeRecycleViewAdapter;
import com.sakkawy.medicare.Model.Folder;
import com.sakkawy.medicare.Model.ItemHome;
import com.sakkawy.medicare.R;
import com.sakkawy.medicare.View.SubFolderActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.ItemClickListener {

    private static final String TAG = "HomeFragment";
    RecyclerView mRecyclerView;
    HomeRecycleViewAdapter adapter;
    List<Folder> mList = new ArrayList<>();
    boolean flag = true;

    TextView tvAddItem;

    DatabaseReference dbFolder;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getActivity().findViewById(R.id.rv_home_fragment);
        tvAddItem = getActivity().findViewById(R.id.tv_add_item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbFolder = FirebaseDatabase.getInstance().getReference().child("FolderCollection");

        adapter = new HomeRecycleViewAdapter(getContext(),HomeFragment.this,mList);
        mRecyclerView.setAdapter(adapter);


        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();

            }
        });

        updateFolders();
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
                        if(folder.getParent().equals("empty") && folder.getUserId().equals(currentUserId)){
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



    private void addItem() {
        //mList.add(new ItemHome(R.drawable.ic_name_icon,));
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View updateDialogView = factory.inflate(R.layout.dialog_add_item_layout, null);
        final AlertDialog updatePassDialog = new AlertDialog.Builder(getActivity()).create();
        updatePassDialog.setView(updateDialogView);
        updateDialogView.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemEditText = updateDialogView.findViewById(R.id.tv_item_name);
                final String folderName = itemEditText.getText().toString();
                if (folderName.isEmpty()) {
                    itemEditText.setError(getActivity().getResources().getString(R.string.error));
                } else {
                    //todo confirm on operation

                    String parent = "empty";
                    String key = dbFolder.push().getKey();
                    Folder folder = new Folder(parent,currentUserId,folderName,key);

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
        Intent intent = new Intent(getActivity(), SubFolderActivity.class);
        intent.putExtra(SubFolderActivity.KEY,folder.getFolderId());
        startActivity(intent);
        Toast.makeText(getContext(), folder.getFolderName(), Toast.LENGTH_SHORT).show();
    }
}
