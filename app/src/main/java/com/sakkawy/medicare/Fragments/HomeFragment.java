package com.sakkawy.medicare.Fragments;


import android.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.HomeRecycleViewAdapter;
import com.sakkawy.medicare.Model.ItemHome;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.ItemClickListener {

    private static final String TAG = "HomeFragment";
    RecyclerView mRecyclerView;
    HomeRecycleViewAdapter adapter;
    List<ItemHome> mList = new ArrayList<>();
    boolean flag = true;

    TextView tvAddItem;

    DatabaseReference db;

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
        setupData();

//        tvAddItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addItem();
//            }
//        });

/*        db.push().setValue(new ItemHome(R.drawable.ic_eye_open,"Ophthalmology"));
        db.push().setValue(new ItemHome(R.drawable.ic_tooth,"Dentistry"));
        db.push().setValue(new ItemHome(R.drawable.ic_broken_bone,"Orthopedics"));
        db.push().setValue(new ItemHome(R.drawable.ic_walking_stick,"Elderly & Geriatric"));
        db.push().setValue(new ItemHome(R.drawable.ic_scissors,"General Surgery"));
        db.push().setValue(new ItemHome(R.drawable.ic_female_reproduction_system,"Infertility & Fertilization"));
        db.push().setValue(new ItemHome(R.drawable.ic_intestines,"Internal Medicine"));
        db.push().setValue(new ItemHome(R.drawable.ic_kidney,"Renal & Urinary Tract"));
        db.push().setValue(new ItemHome(R.drawable.ic_human_brain,"Neurosurgery"));*/

    }

    private void setupData() {

        db = FirebaseDatabase.getInstance().getReference().child("MainFolders");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ItemHome itemHome = snapshot.getValue(ItemHome.class);
                        Log.d(TAG, "onDataChange: itemHome " + itemHome);
                        mList.add(itemHome);
                    }
                    adapter = new HomeRecycleViewAdapter(getContext(),HomeFragment.this,mList);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        if(flag) {
//            mList.add(new ItemHome(R.drawable.ic_eye_open, "Ophthalmology"));
//            mList.add(new ItemHome(R.drawable.ic_tooth, "Dentistry"));
//            mList.add(new ItemHome(R.drawable.ic_broken_bone, "Orthopedics"));
//            mList.add(new ItemHome(R.drawable.ic_walking_stick, "Elderly & Geriatric"));
//            mList.add(new ItemHome(R.drawable.ic_scissors, "General Surgery"));
//            mList.add(new ItemHome(R.drawable.ic_female_reproduction_system, "Infertility & Fertilization"));
//            mList.add(new ItemHome(R.drawable.ic_intestines, "Internal Medicine"));
//            mList.add(new ItemHome(R.drawable.ic_kidney, "Renal & Urinary Tract"));
//            mList.add(new ItemHome(R.drawable.ic_human_brain, "Neurosurgery"));
//            flag = false;
//        }
//        adapter = new HomeRecycleViewAdapter(getContext(),this,mList);
//        mRecyclerView.setAdapter(adapter);

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
                final String itemName = itemEditText.getText().toString();
                if (itemName.isEmpty()) {
                    itemEditText.setError(getActivity().getResources().getString(R.string.error));
                } else {
                    //todo confirm on operation
                    mList.add(new ItemHome(R.drawable.ic_person_24dp, itemName));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    updatePassDialog.dismiss();
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
    public void onItemClickListener(ItemHome itemHome) {
        Toast.makeText(getContext(), itemHome.getText(), Toast.LENGTH_SHORT).show();
    }
}
