package com.sakkawy.medicare.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.HomeRecycleViewAdapter;
import com.sakkawy.medicare.MainActivity;
import com.sakkawy.medicare.Model.Folder;
import com.sakkawy.medicare.Model.ItemHome;
import com.sakkawy.medicare.Model.SpecailityItem;
import com.sakkawy.medicare.R;
import com.sakkawy.medicare.View.SubActivity;
import com.sakkawy.medicare.View.SubFolderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.ItemClickListener {

    private static final String TAG = "HomeFragment";
    RecyclerView mRecyclerView;
    HomeRecycleViewAdapter adapter;
    List<Folder> mList = new ArrayList<>();
    List<SpecailityItem> itemsList = new ArrayList<>();
    boolean flag = true;

    TextView tvAddItem;

    DatabaseReference dbFolder;
    DatabaseReference ref;
    DatabaseReference db;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    List<ItemHome> itemHomeList = new ArrayList<>();
    String [] items ;

    String userId;
    String key;

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

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        adapter = new HomeRecycleViewAdapter(getContext(), HomeFragment.this, itemsList);
        mRecyclerView.setAdapter(adapter);


        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerAddItems();
            }
        });

        updateFolders();
       // addSpeciality();
    }

    private void spinnerAddItems(){
        final String [] items = {"Ophthalmology","Dentistry","Orthopedics","Elderly & Geriatric","General Surgery","Infertility & Fertilization"
                ,"Internal Medicine","Renal & Urinary Tract","Neurosurgery"};


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add another specialty");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int position) {
                Log.d(TAG, "onClick: "+position);
                ref = FirebaseDatabase.getInstance().getReference().child("Speciality");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()> 0){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                SpecailityItem item = snapshot.getValue(SpecailityItem.class);

                                if(item.getId().equals(""+position)){
                                    String key = snapshot.getKey();
                                    Log.d(TAG, "onDataChange: key : "+key);
                                    ref = FirebaseDatabase.getInstance().getReference().child("Speciality").child(key).child("UsersId");
                                    ref.push().setValue(userId);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void addSpeciality() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Speciality");
        List<SpecailityItem> items = new ArrayList<>();
        items.add(new SpecailityItem(R.drawable.ic_eye_open, "Ophthalmology","0"));
        items.add(new SpecailityItem(R.drawable.ic_tooth, "Dentistry","1") );
        items.add(new SpecailityItem(R.drawable.ic_broken_bone, "Orthopedics","2") );
        items.add(new SpecailityItem(R.drawable.ic_walking_stick, "Elderly & Geriatric","3"));
        items.add(new SpecailityItem(R.drawable.ic_scissors, "General Surgery","4"));
        items.add(new SpecailityItem(R.drawable.ic_female_reproduction_system, "Infertility & Fertilization","4"));
        items.add(new SpecailityItem(R.drawable.ic_intestine, "Internal Medicine","6"));
        items.add(new SpecailityItem(R.drawable.ic_kidney, "Renal & Urinary Tract","7"));
        items.add(new SpecailityItem(R.drawable.ic_human_brain, "Neurosurgery","8"));
        for (SpecailityItem item : items) {
            reference.push().setValue(item);
        }
    }


    private void updateFolders() {
        //if(mList.size() != 0)   mList.clear();
        db = FirebaseDatabase.getInstance().getReference().child("Speciality");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final SpecailityItem item = snapshot.getValue(SpecailityItem.class);
                        String folderKey = snapshot.getKey();
                        db = FirebaseDatabase.getInstance().getReference().child("Speciality").child(folderKey).child("UsersId");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                                    for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                        String userkey = snapshot1.getValue(String.class);
                                        if(userkey.equals(userId)){
                                            SpecailityItem item1 = item;
                                            if(item1.getId().equals("0"))
                                                item1.setIcon(R.drawable.ic_eye_open);
                                            if(item1.getId().equals("1"))
                                                item1.setIcon(R.drawable.ic_tooth);
                                            if(item1.getId().equals("2"))
                                                item1.setIcon(R.drawable.ic_broken_bone);
                                            if(item1.getId().equals("3"))
                                                item1.setIcon(R.drawable.ic_walking_stick);
                                            if(item1.getId().equals("4"))
                                                item1.setIcon(R.drawable.ic_scissors);
                                            if(item1.getId().equals("5"))
                                                item1.setIcon(R.drawable.ic_female_reproduction_system);
                                            if(item1.getId().equals("6"))
                                                item1.setIcon(R.drawable.ic_intestine);
                                            if(item1.getId().equals("7"))
                                                item1.setIcon(R.drawable.ic_kidney);
                                            if(item1.getId().equals("8"))
                                                item1.setIcon(R.drawable.ic_human_brain);

                                            itemsList.add(item1);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//
//        dbFolder.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mList.clear();
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Folder folder = snapshot.getValue(Folder.class);
//                        if (folder.getParent().equals("empty") && folder.getUserId().equals(currentUserId)) {
//                            mList.add(folder);
//
//                            Log.d(TAG, "onDataChange: list Size " + mList.size());
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
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
                    Folder folder = new Folder(parent, currentUserId, folderName, key);

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
    public void onItemClickListener(SpecailityItem folder) {
        Intent intent = new Intent(getActivity(), SubActivity.class);
        intent.putExtra("keyName", folder.getName());
        intent.putExtra("keyId", folder.getId());
        startActivity(intent);
    }
}
