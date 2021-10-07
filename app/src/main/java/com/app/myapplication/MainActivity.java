package com.app.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.myapplication.Constant.ApiLinks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;

    String[] gender = {"Gender", "Male", "Female"};
    int mdd, mmm, myy, th,tm;
    Spinner gender_spinner;
    TextInputEditText pin_code_et, dob_et, mob_no, full_name_et, address_1_et, address_2_et;
    Button check_pin_code, submit;
    TextView district_name, state_name, gender_error;
    TextInputLayout textInputLayout, full_name, dob, address_1, address_2, pin_code;
    String select_gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Registration");

        dialog = new ProgressDialog(MainActivity.this);

        /**/
        gender_spinner = findViewById(R.id.gender_spinner);
        ArrayAdapter<String> gender_obj = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, gender);
        gender_spinner.setAdapter(gender_obj);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_gender = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mob_no = findViewById(R.id.mob_no);
        full_name_et = findViewById(R.id.full_name_et);
        address_1_et = findViewById(R.id.address_1_et);
        address_2_et = findViewById(R.id.address_2_et);
        district_name = findViewById(R.id.district_name);
        state_name = findViewById(R.id.state_name);
        submit = findViewById(R.id.submit);

        /**/
        textInputLayout = findViewById(R.id.textInputLayout);
        full_name = findViewById(R.id.full_name);
        gender_error = findViewById(R.id.gender_error);
        dob = findViewById(R.id.dob);
        address_1 = findViewById(R.id.address_1);
        address_2 = findViewById(R.id.address_2);
        pin_code = findViewById(R.id.pin_code);

        pin_code_et = findViewById(R.id.pin_code_et);
        check_pin_code = findViewById(R.id.check_pin_code);
        pin_code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0 && charSequence.length() == 6){
                    check_pin_code.setEnabled(true);
                }
                else{
                    check_pin_code.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dob_et = findViewById(R.id.dob_et);
        dob_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar obj = Calendar.getInstance();
                mdd = obj.get(Calendar.DAY_OF_MONTH);
                mmm = obj.get(Calendar.MONTH);
                myy = obj.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String ChoosenDate = dayOfMonth+"/"+(month+1)+"/"+year;
                        dob_et.setText(ChoosenDate);
                    }
                }, myy, mmm, mdd);
                dpd.show();
            }
        });

        check_pin_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDistrictState(pin_code_et.getText().toString());
                // dialog.setTitle("Loading");
                dialog.setMessage("State and District data fetching...");
                dialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Make default*/
                textInputLayout.setError(null);
                full_name.setError(null);
                gender_error.setText("");
                dob.setError(null);
                address_1.setError(null);
                address_2.setError(null);
                pin_code.setError(null);

                /**/
                String mobile = mob_no.getText().toString();
                String full_name_text = full_name_et.getText().toString();
                String dob_text = dob_et.getText().toString();
                String addr_1 = address_1_et.getText().toString();
                String addr_2 = address_2_et.getText().toString();
                String pin_code_text = pin_code_et.getText().toString();
                Boolean isError = false;

                if(mobile.length()<1){
                    textInputLayout.setError("Enter phone number");
                    isError = true;
                }
                if(full_name_text.length()<1 || full_name_text.length()>50){
                    full_name.setError("Enter valid name");
                    isError = true;
                }
                if(select_gender.equalsIgnoreCase("gender")){
                    gender_error.setText("Please select gender");
                    isError = true;
                }
                if(dob_text.length()<1){
                    dob.setError("Select DOB");
                    isError = true;
                }
                if(addr_1.length()<3 || addr_1.length()>50){
                    address_1.setError("Enter valid address line 1");
                    isError = true;
                }
                if(addr_2.length()>50){
                    address_2.setError("Enter valid address line 2");
                    isError = true;
                }
                if(pin_code_text.length()<1){
                    pin_code.setError("Enter pin code");
                    isError = true;
                }
                if(!isError){
                    Intent i = new Intent(MainActivity.this, ShowWeather.class);
                    startActivity(i);
                }
            }
        });
    }

    public void getDistrictState(String pincode){
        JsonArrayRequest j_arr_obj = new JsonArrayRequest(Request.Method.GET, ApiLinks.pin_code_end_point + pincode, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    pin_code.setError(null);
                    // Log.i("Data", String.valueOf(response));
                    JSONObject is_success_obj = (JSONObject) response.get(0);
                    // Log.i("Data", String.valueOf(is_success_obj));
                    if(is_success_obj.has("Status") && is_success_obj.getString("Status").equalsIgnoreCase("Success")){
                        if(is_success_obj.has("PostOffice")){
                            JSONArray post_office_obj = is_success_obj.getJSONArray("PostOffice");
                            JSONObject state_district = (JSONObject) post_office_obj.get(0);
                            // Log.i("state", state_district.getString("District")+state_district.getString("State"));
                            district_name.setText(state_district.getString("District"));
                            state_name.setText(state_district.getString("State"));
                            dialog.dismiss();
                        }
                    }
                    else{
                        pin_code.setError("Enter valid pin code");
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Volley.newRequestQueue(MainActivity.this).add(j_arr_obj);
    }
}