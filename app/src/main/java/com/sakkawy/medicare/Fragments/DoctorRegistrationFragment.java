package com.sakkawy.medicare.Fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sakkawy.medicare.Helper.ViewHelper;
import com.sakkawy.medicare.Interface.OnDataPass;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorRegistrationFragment extends Fragment {

    Button btnSignUp;
    EditText etName, etUserName, etEmail,etPassword, etAddtess;
    Spinner mSpeciality;

    String name, userName , email , password , gender , address , imageUri , userType = "Doctor" , spciality;


    OnDataPass dataPasser;

    public DoctorRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_registration, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etName = getActivity().findViewById(R.id.et_name);
        etUserName = getActivity().findViewById(R.id.et_user_name);
        etEmail = getActivity().findViewById(R.id.et_email);
        etPassword = getActivity().findViewById(R.id.et_password);
        etAddtess = getActivity().findViewById(R.id.et_address);
        mSpeciality = getActivity().findViewById(R.id.sp_spciality);
        btnSignUp = getActivity().findViewById(R.id.btn_signUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ViewHelper.checkViews(getActivity(),etName,etUserName,etEmail,etPassword,etAddtess,etAddtess)){
                    // do nothing
                }else{
                    name = etName.getText().toString();
                    userName = etUserName.getText().toString();
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    address= etAddtess.getText().toString();
                    spciality = mSpeciality.getSelectedItem().toString();
                    User user = new User(name,userName,email,password,address,spciality,"",userType,0);

                    passData(user);

                }
            }
        });

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
