package com.sakkawy.medicare.Helper;

import android.content.Context;
import android.widget.EditText;

public class ViewHelper {

    public static Boolean checkEmpty(Context context , EditText editText){
        if(editText.getText().toString().equals("") || editText.getText().toString() == null){
            editText.setError("Enter this field");
            return true;
        }
        return false;
    }

    public static boolean checkViews(Context context , EditText et1 ,EditText et2 ,EditText et3 ,EditText et4 ,EditText et5 ,EditText et6) {
        if (et1.getText().toString().equals("") || et1.getText().toString() == null) {
            et1.setError("Enter this field");
            return false;

        } else if (et2.getText().toString().equals("") || et2.getText().toString() == null) {
            et2.setError("Enter this field");
            return false;

        } else if (et3.getText().toString().equals("") || et3.getText().toString() == null) {
            et3.setError("Enter this field");
            return false;
        } else if (et4.getText().toString().equals("") || et4.getText().toString() == null) {
            et4.setError("Enter this field");
            return false;
        } else if (et5.getText().toString().equals("") || et5.getText().toString() == null) {
            et5.setError("Enter this field");
            return false;
        } else if (et6.getText().toString().equals("") || et6.getText().toString() == null) {
            et6.setError("Enter this field");
            return false;
        }
        return true;
    }
}
