package com.app.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.myapplication.Constant.ApiLinks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowWeather extends AppCompatActivity {

    ProgressDialog dialog;

    TextInputEditText city_name_et;
    String city_name;
    Button button;
    TextView show_lat, show_lon, show_cen, show_fah;
    TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        dialog = new ProgressDialog(ShowWeather.this);
        dialog.setMessage("Fetching data...");

        textInputLayout2 = findViewById(R.id.textInputLayout2);

        show_lat = findViewById(R.id.show_lat);
        show_lon = findViewById(R.id.show_lon);

        show_cen = findViewById(R.id.show_cen);
        show_fah = findViewById(R.id.show_fah);
        city_name_et = findViewById(R.id.city_name_et);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_name = city_name_et.getText().toString();
                if(city_name.isEmpty()){
                    textInputLayout2.setError("Please enter city name");
                }
                else{
                    dialog.show();
                    getWeatherData(city_name);
                }
            }
        });
    }

    public void getWeatherData(String cityName){
        JsonObjectRequest j_obj_req = new JsonObjectRequest(Request.Method.GET, ApiLinks.getWeather_end_point+cityName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.has("location")){
                        JSONObject location_obj = response.getJSONObject("location");
                        show_lat.setText(location_obj.getString("lat"));
                        show_lon.setText(location_obj.getString("lon"));
                    }
                    if(response.has("current")){
                        JSONObject current_obj = response.getJSONObject("current");
                        show_cen.setText(current_obj.getString("temp_c"));
                        show_fah.setText(current_obj.getString("temp_f"));
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowWeather.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(ShowWeather.this).add(j_obj_req);
    }
}