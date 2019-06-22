package com.sakkawy.medicare.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sakkawy.medicare.Helper.ViewHelper;
import com.sakkawy.medicare.Interface.OnDataPass;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientRegistrationFragment extends Fragment {

    Button  btnSignUp;
    EditText etName, etUserName, etEmail,etPassword, etDate,etGender;

    OnDataPass dataPasser;
    String name, userName , email , password , gender , birthOfDate , imageUri , userType = "Patient";

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;



    public PatientRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_registration, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etName = getActivity().findViewById(R.id.et_name);
        etUserName = getActivity().findViewById(R.id.et_user_name);
        etEmail = getActivity().findViewById(R.id.et_email);
        etPassword = getActivity().findViewById(R.id.et_password);
        etDate = getActivity().findViewById(R.id.et_date);
        etGender = getActivity().findViewById(R.id.gender);
        btnSignUp = getActivity().findViewById(R.id.btn_signUp);


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ViewHelper.checkViews(getActivity(),etName,etUserName,etEmail,etPassword,etDate,etGender)){
                    // do nothing
                }else{
                    name = etName.getText().toString();
                    userName = etUserName.getText().toString();
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    gender= etGender.getText().toString();
                    birthOfDate = etDate.getText().toString();

                    User user = new User(name,userName,email,password,birthOfDate,gender,"",userType);

                    passData(user);

                }
            }
        });


    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(User user){
        dataPasser.onDataPass(user);
    }
}
