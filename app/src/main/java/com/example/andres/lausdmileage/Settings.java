package com.example.andres.lausdmileage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public class Settings extends AppCompatActivity {

    EditText oneWayTrip_EditText;
    EditText street_EditText;
    EditText city_EditText;
    EditText zipCode_EditText;
    MaterialButton button_Save;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ONE_WAY_TRIP = "oneWayTrip";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String ZIPCODE = "zipCode";
    public static final String ADDRESS = "address";

    private String oneWayTrip;
    private String street;
    private String city;
    private String zipCode;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        oneWayTrip_EditText = findViewById(R.id.oneWayTrip_EditText);
        street_EditText = findViewById(R.id.street_EditText);
        city_EditText = findViewById(R.id.city_EditText);
        zipCode_EditText = findViewById(R.id.zipCode_EditText);
        button_Save = findViewById(R.id.button_Save);

        loadData();
        updateViews();

    }

    public void save(View view) {

        String street = street_EditText.getText().toString();
        String city = city_EditText.getText().toString();
        String zipCode = zipCode_EditText.getText().toString();

        address = street + " " + city + " " + zipCode;

        if ((oneWayTrip_EditText.getText().toString().equals("")) || (address.equals(""))) {

            Toast.makeText(this, "Oops, you forgot to fill in some info", Toast.LENGTH_SHORT).show();

        } else {

            street = street_EditText.getText().toString();
            city = city_EditText.getText().toString();
            zipCode = zipCode_EditText.getText().toString();

            address = street + " " + city + " " + zipCode;

            saveData();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
    }

    public void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ONE_WAY_TRIP, oneWayTrip_EditText.getText().toString());
        editor.putString(STREET, street_EditText.getText().toString());
        editor.putString(CITY, city_EditText.getText().toString());
        editor.putString(ZIPCODE, zipCode_EditText.getText().toString());
        editor.putString(ADDRESS, address);

        editor.apply();

        Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        oneWayTrip = sharedPreferences.getString(ONE_WAY_TRIP, "");
        street = sharedPreferences.getString(STREET, "");
        city = sharedPreferences.getString(CITY, "");
        zipCode = sharedPreferences.getString(ZIPCODE, "");
        address = sharedPreferences.getString(ADDRESS, "");

    }

    public void updateViews() {

     oneWayTrip_EditText.setText(oneWayTrip);
     street_EditText.setText(street);
     city_EditText.setText(city);
     zipCode_EditText.setText(zipCode);

    }

    static public String getOneWayTrip(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        return prefs.getString(ONE_WAY_TRIP, "");
    }

    static public String getAddress(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        return prefs.getString(ADDRESS, "");
    }

}
