package com.sakkawy.medicare.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.MedicineAdapter;
import com.sakkawy.medicare.Model.Medicine;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicineFragment extends Fragment {


    RecyclerView recyclerView;
    MedicineAdapter adapter;
    FloatingActionButton addMedicineFab;


    EditText medicineName , medicineDes ;
    Spinner dos1 , dos2,dos3 , dosType1,dosType2,dosType3, portion1,portion2,portion3;

    Button add , cancel;

    DatabaseReference reference ;

    FirebaseUser user;

    List<Medicine> medicineList = new ArrayList<>();

    public MedicineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView = getActivity().findViewById(R.id.rv_medicine);
        addMedicineFab = getActivity().findViewById(R.id.fab_add_medicine);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        user = FirebaseAuth.getInstance().getCurrentUser();

        addMedicineFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_medicine, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                medicineName = dialogView.findViewById(R.id.dialog_medicine_name);
                medicineDes = dialogView.findViewById(R.id.dialog_medicine_des);

                dos1 = dialogView.findViewById(R.id.dialog_sp_doses);
                dos2 = dialogView.findViewById(R.id.dialog_sp_doses2);
                dos3 = dialogView.findViewById(R.id.dialog_sp_doses3);

                dosType1 = dialogView.findViewById(R.id.dialog_doses_tpye);
                dosType2 = dialogView.findViewById(R.id.dialog_doses_tpye2);
                dosType3 = dialogView.findViewById(R.id.dialog_doses_tpye3);

                portion1 = dialogView.findViewById(R.id.dialog_sp_portions);
                portion2 = dialogView.findViewById(R.id.dialog_sp_portions2);
                portion3 = dialogView.findViewById(R.id.dialog_sp_portions3);

                add = dialogView.findViewById(R.id.dialog_btn_add);
                cancel = dialogView.findViewById(R.id.dialog_btn_cancel);



                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String medName = medicineName.getText().toString();
                        String medDes = medicineDes.getText().toString();
                        if(medName.equals("") || medDes.equals("")){
                            medicineName.setError("Required");
                            medicineDes.setError("Required");
                        }
                        else{
                            String dosStr1 , dosStr2 , dosStr3 , dosTpyeStr1,dosTpyeStr2,dosTpyeStr3, portionStr1,portionStr2,portionStr3;
                            dosStr1 = dos1.getSelectedItem().toString();
                            dosStr2 = dos2.getSelectedItem().toString();
                            dosStr3 = dos3.getSelectedItem().toString();
                            dosTpyeStr1 = dosType1.getSelectedItem().toString();
                            dosTpyeStr2 = dosType2.getSelectedItem().toString();
                            dosTpyeStr3 = dosType3.getSelectedItem().toString();
                            portionStr1 = portion1.getSelectedItem().toString();
                            portionStr2 = portion2.getSelectedItem().toString();
                            portionStr3 = portion3.getSelectedItem().toString();


                            Medicine medicine = new Medicine(medName,medDes,
                                    dosStr1,dosTpyeStr1,portionStr1,
                                    dosStr2,dosTpyeStr2,portionStr2,
                                    dosStr3,dosTpyeStr3,portionStr3
                                    );


                            reference = FirebaseDatabase.getInstance().getReference().child("Medicine").child(user.getUid());
                            reference.push().setValue(medicine)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });




                        }
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });


        readMedicines();
    }


    private void readMedicines(){
        reference = FirebaseDatabase.getInstance().getReference().child("Medicine").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    medicineList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        medicineList.add(medicine);
                    }
                    adapter = new MedicineAdapter(getActivity(),medicineList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
