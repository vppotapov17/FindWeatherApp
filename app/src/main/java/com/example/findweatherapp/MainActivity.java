package com.example.findweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GetDataFromInternet.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    @Override
    public void onResume(){
        super.onResume();

        EditText input = findViewById(R.id.input);
        MaterialButton button = findViewById(R.id.button);

        button.setOnClickListener(view -> {


            String city = input.getText().toString();

            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=ef5e9d0bd6895b61240dd042580f056c");
                new GetDataFromInternet(this).execute(url);
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT);
                throw new RuntimeException(e);
            }

        });


    }
    @Override
    public void processFinished(String output){
        Log.d("AAA", output);
        try {
            JSONObject resultJSON = new JSONObject(output);
            JSONObject weather = resultJSON.getJSONObject("main");
            JSONObject sys = resultJSON.getJSONObject("sys");

            Double temp = Double.parseDouble(weather.getString("temp"));
            temp -= 273.15;

            Double pressure = Double.parseDouble(weather.getString("pressure"));
            pressure *= 0.750064;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String sunrise = simpleDateFormat.format(new Date(Long.parseLong(sys.getString("sunrise")) * 1000));
            String sunset = simpleDateFormat.format(new Date(Long.parseLong(sys.getString("sunset")) * 1000));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");


            TextView tempValue = findViewById(R.id.tempValue);
            TextView pressValue = findViewById(R.id.pressValue);
            TextView sunriseValue = findViewById(R.id.sunriseValue);
            TextView sunsetValue = findViewById(R.id.sunsetValue);

            tempValue.setText(decimalFormat.format(temp) + " °C");
            pressValue.setText(decimalFormat.format(pressure) + " мм рт. ст.");
            sunriseValue.setText(sunrise);
            sunsetValue.setText(sunset);

        }catch (JSONException e){

        }

    }
}