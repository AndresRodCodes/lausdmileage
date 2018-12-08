package com.example.andres.lausdmileage;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView start_textView;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView2;
    AutoCompleteTextView autoCompleteTextView3;
    AutoCompleteTextView autoCompleteTextView4;
    AutoCompleteTextView autoCompleteTextView5;
    AutoCompleteTextView autoCompleteTextView6;
    AutoCompleteTextView autoCompleteTextView7;
    AutoCompleteTextView autoCompleteTextView8;
    AutoCompleteTextView autoCompleteTextView9;

    AutoCompleteTextView end_textView;
    AutoCompleteTextView end_textView2;
    AutoCompleteTextView end_textView3;
    AutoCompleteTextView end_textView4;
    AutoCompleteTextView end_textView5;
    AutoCompleteTextView end_textView6;
    AutoCompleteTextView end_textView7;
    AutoCompleteTextView end_textView8;
    AutoCompleteTextView end_textView9;
    AutoCompleteTextView end_textView10;

    MaterialButton button_clear;

    TextView textView_showRoadDistance;
    TextView textView_showRoadDistance2;
    TextView textView_showRoadDistance3;
    TextView textView_showRoadDistance4;
    TextView textView_showRoadDistance5;
    TextView textView_showRoadDistance6;
    TextView textView_showRoadDistance7;
    TextView textView_showRoadDistance8;
    TextView textView_showRoadDistance9;
    TextView textView_showRoadDistance10;

    int startTextViewAddressIndex;
    int startTextViewAddressIndex2;
    int startTextViewAddressIndex3;
    int startTextViewAddressIndex4;
    int startTextViewAddressIndex5;
    int startTextViewAddressIndex6;
    int startTextViewAddressIndex7;
    int startTextViewAddressIndex8;
    int startTextViewAddressIndex9;
    int startTextViewAddressIndex10;

    int endTextViewAddressIndex;
    int endTextViewAddressIndex2;
    int endTextViewAddressIndex3;
    int endTextViewAddressIndex4;
    int endTextViewAddressIndex5;
    int endTextViewAddressIndex6;
    int endTextViewAddressIndex7;
    int endTextViewAddressIndex8;
    int endTextViewAddressIndex9;
    int endTextViewAddressIndex10;

    Double oneWayTripDouble = 0.0;
    String address = "";
    String oneWayTrip;

    ArrayList<String> SCHOOLNAMES = new ArrayList<>();

    ArrayList<String> ADDRESSES = new ArrayList<>();

    //Read CVS file of school names and addresses
    private void readSchoolData() {

        InputStream inputStream = getResources().openRawResource(R.raw.lausdinfo);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );

        String line = "";

        try {
            //Step over headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                //split by ","
                String[] tokens = line.split(",");

                //read the data
                SCHOOLNAMES.add(tokens[0]);
                ADDRESSES.add(tokens[1]);

            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);
            e.printStackTrace();

        }
    }

    public String calculateRoadDistance (final String start_address,final String end_address) {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];

        if ((start_address.equals("18230 KITTRIDGE ST RESEDA 91335") & end_address.equals("18230 KITTRIDGE ST RESEDA 91335"))) {

            Log.i("index", "Nothing to show here");

        } else{

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            URL url = new URL("https:maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + start_address + "&destinations=" + end_address + ",NY&key=AIzaSyC_mTRR4Nm2Jg8vlkVrDPN8gokEFQRvPWs");

                            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            conn.setRequestMethod("POST");

                            InputStream in = new BufferedInputStream(conn.getInputStream());

                            response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                            JSONObject reader = new JSONObject(response[0]);

                            JSONArray rowsArray = reader.getJSONArray("rows");

                            JSONObject elementObject = rowsArray.getJSONObject(0);

                            JSONArray elementsArray = elementObject.getJSONArray("elements");

                            JSONObject obj3 = elementsArray.getJSONObject(0);

                            JSONObject distanceObj = obj3.getJSONObject("distance");

                            parsedDistance[0] = distanceObj.getString("text");

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

                try {

                    thread.join();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }
        }

            return parsedDistance[0];
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String checkIfHomeIsSelected(AutoCompleteTextView start_textView, AutoCompleteTextView end_textView, String roadDistance, Double oneWayTrip) {

        String roadDistance1 = roadDistance;

        if ((start_textView.getText().toString().equals("Home")) || (end_textView.getText().toString().equals("Home"))) {

            String[] split = roadDistance.split(" ");

            String number = split[0];
            String miles = split[1];

            Double numberDouble = Double.parseDouble(number);
            numberDouble = numberDouble - oneWayTrip;
            Double roundedNumber = round(numberDouble, 1);
            number = roundedNumber.toString();

            roadDistance1 = number + " " + miles;

        }
        return roadDistance1;
    }

    public void checkIfNull(TextView TextView) {

        if (TextView.getText().toString().equals("null")) {

            TextView.setText("-");

        }
    }

    public void showDistance() {

        oneWayTripDouble = Double.parseDouble(oneWayTrip);

        String roadDistance = calculateRoadDistance(((ADDRESSES.get(startTextViewAddressIndex))), (ADDRESSES.get(endTextViewAddressIndex)));
        String roadDistance2 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex2), ADDRESSES.get(endTextViewAddressIndex2));
        String roadDistance3 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex3), ADDRESSES.get(endTextViewAddressIndex3));
        String roadDistance4 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex4), ADDRESSES.get(endTextViewAddressIndex4));
        String roadDistance5 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex5), ADDRESSES.get(endTextViewAddressIndex5));
        String roadDistance6 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex6), ADDRESSES.get(endTextViewAddressIndex6));
        String roadDistance7 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex7), ADDRESSES.get(endTextViewAddressIndex7));
        String roadDistance8 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex8), ADDRESSES.get(endTextViewAddressIndex8));
        String roadDistance9 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex9), ADDRESSES.get(endTextViewAddressIndex9));
        String roadDistance10 = calculateRoadDistance(ADDRESSES.get(startTextViewAddressIndex10), ADDRESSES.get(endTextViewAddressIndex10));

        roadDistance = checkIfHomeIsSelected(start_textView, end_textView, roadDistance, oneWayTripDouble);
        roadDistance2 = checkIfHomeIsSelected(autoCompleteTextView, end_textView2, roadDistance2, oneWayTripDouble);
        roadDistance3 = checkIfHomeIsSelected(autoCompleteTextView2, end_textView3, roadDistance3, oneWayTripDouble);
        roadDistance4 = checkIfHomeIsSelected(autoCompleteTextView3, end_textView4, roadDistance4, oneWayTripDouble);
        roadDistance5 = checkIfHomeIsSelected(autoCompleteTextView4, end_textView5, roadDistance5, oneWayTripDouble);
        roadDistance6 = checkIfHomeIsSelected(autoCompleteTextView5, end_textView6, roadDistance6, oneWayTripDouble);
        roadDistance7 = checkIfHomeIsSelected(autoCompleteTextView6, end_textView7, roadDistance7, oneWayTripDouble);
        roadDistance8 = checkIfHomeIsSelected(autoCompleteTextView7, end_textView8, roadDistance8, oneWayTripDouble);
        roadDistance9 = checkIfHomeIsSelected(autoCompleteTextView8, end_textView9, roadDistance9, oneWayTripDouble);
        roadDistance10 = checkIfHomeIsSelected(autoCompleteTextView9, end_textView10, roadDistance10, oneWayTripDouble);

        textView_showRoadDistance.setText(String.valueOf(roadDistance));
        textView_showRoadDistance2.setText(String.valueOf(roadDistance2));
        textView_showRoadDistance3.setText(String.valueOf(roadDistance3));
        textView_showRoadDistance4.setText(String.valueOf(roadDistance4));
        textView_showRoadDistance5.setText(String.valueOf(roadDistance5));
        textView_showRoadDistance6.setText(String.valueOf(roadDistance6));
        textView_showRoadDistance7.setText(String.valueOf(roadDistance7));
        textView_showRoadDistance8.setText(String.valueOf(roadDistance8));
        textView_showRoadDistance9.setText(String.valueOf(roadDistance9));
        textView_showRoadDistance10.setText(String.valueOf(roadDistance10));

        checkIfNull(textView_showRoadDistance);
        checkIfNull(textView_showRoadDistance2);
        checkIfNull(textView_showRoadDistance3);
        checkIfNull(textView_showRoadDistance4);
        checkIfNull(textView_showRoadDistance5);
        checkIfNull(textView_showRoadDistance6);
        checkIfNull(textView_showRoadDistance7);
        checkIfNull(textView_showRoadDistance8);
        checkIfNull(textView_showRoadDistance9);
        checkIfNull(textView_showRoadDistance10);

    }

    public void clear(View view) {

        start_textView.setText("");
        autoCompleteTextView.setText("");
        autoCompleteTextView2.setText("");
        autoCompleteTextView3.setText("");
        autoCompleteTextView4.setText("");
        autoCompleteTextView5.setText("");
        autoCompleteTextView6.setText("");
        autoCompleteTextView7.setText("");
        autoCompleteTextView8.setText("");
        autoCompleteTextView9.setText("");

        end_textView.setText("");
        end_textView2.setText("");
        end_textView3.setText("");
        end_textView4.setText("");
        end_textView5.setText("");
        end_textView6.setText("");
        end_textView7.setText("");
        end_textView8.setText("");
        end_textView9.setText("");
        end_textView10.setText("");

        textView_showRoadDistance.setText("-");
        textView_showRoadDistance2.setText("-");
        textView_showRoadDistance3.setText("-");
        textView_showRoadDistance4.setText("-");
        textView_showRoadDistance5.setText("-");
        textView_showRoadDistance6.setText("-");
        textView_showRoadDistance7.setText("-");
        textView_showRoadDistance8.setText("-");
        textView_showRoadDistance9.setText("-");
        textView_showRoadDistance10.setText("-");

        startTextViewAddressIndex = 0;
        startTextViewAddressIndex2 = 0;
        startTextViewAddressIndex3 = 0;
        startTextViewAddressIndex4 = 0;
        startTextViewAddressIndex5 = 0;
        startTextViewAddressIndex6 = 0;
        startTextViewAddressIndex7 = 0;
        startTextViewAddressIndex8 = 0;
        startTextViewAddressIndex9 = 0;
        startTextViewAddressIndex10 = 0;

        endTextViewAddressIndex = 0;
        endTextViewAddressIndex2 = 0;
        endTextViewAddressIndex3 = 0;
        endTextViewAddressIndex4 = 0;
        endTextViewAddressIndex5 = 0;
        endTextViewAddressIndex6 = 0;
        endTextViewAddressIndex7 = 0;
        endTextViewAddressIndex8 = 0;
        endTextViewAddressIndex9 = 0;
        endTextViewAddressIndex10 = 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readSchoolData();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, SCHOOLNAMES);

        MaterialButton getDistance = findViewById(R.id.btn_getDistance);
        button_clear = findViewById(R.id.button_clear);
        textView_showRoadDistance = findViewById(R.id.textView_showRoadDistance);
        textView_showRoadDistance2 = findViewById(R.id.textView_showRoadDistance2);
        textView_showRoadDistance3 = findViewById(R.id.textView_showRoadDistance3);
        textView_showRoadDistance4 = findViewById(R.id.textView_showRoadDistance4);
        textView_showRoadDistance5 = findViewById(R.id.textView_showRoadDistance5);
        textView_showRoadDistance6 = findViewById(R.id.textView_showRoadDistance6);
        textView_showRoadDistance7 = findViewById(R.id.textView_showRoadDistance7);
        textView_showRoadDistance8 = findViewById(R.id.textView_showRoadDistance8);
        textView_showRoadDistance9 = findViewById(R.id.textView_showRoadDistance9);
        textView_showRoadDistance10 = findViewById(R.id.textView_showRoadDistance10);

        start_textView = findViewById(R.id.start_textView);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView3 = findViewById(R.id.autoCompleteTextView3);
        autoCompleteTextView4 = findViewById(R.id.autoCompleteTextView4);
        autoCompleteTextView5 = findViewById(R.id.autoCompleteTextView5);
        autoCompleteTextView6 = findViewById(R.id.autoCompleteTextView6);
        autoCompleteTextView7 = findViewById(R.id.autoCompleteTextView7);
        autoCompleteTextView8 = findViewById(R.id.autoCompleteTextView8);
        autoCompleteTextView9 = findViewById(R.id.autoCompleteTextView9);

        end_textView = findViewById(R.id.end_textView);
        end_textView2 = findViewById(R.id.end_textView2);
        end_textView3 = findViewById(R.id.end_textView3);
        end_textView4 = findViewById(R.id.end_textView4);
        end_textView5 = findViewById(R.id.end_textView5);
        end_textView6 = findViewById(R.id.end_textView6);
        end_textView7 = findViewById(R.id.end_textView7);
        end_textView8 = findViewById(R.id.end_textView8);
        end_textView9 = findViewById(R.id.end_textView9);
        end_textView10 = findViewById(R.id.end_textView10);

        start_textView.setAdapter(arrayAdapter);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView2.setAdapter(arrayAdapter);
        autoCompleteTextView3.setAdapter(arrayAdapter);
        autoCompleteTextView4.setAdapter(arrayAdapter);
        autoCompleteTextView5.setAdapter(arrayAdapter);
        autoCompleteTextView6.setAdapter(arrayAdapter);
        autoCompleteTextView7.setAdapter(arrayAdapter);
        autoCompleteTextView8.setAdapter(arrayAdapter);
        autoCompleteTextView9.setAdapter(arrayAdapter);

        end_textView.setAdapter(arrayAdapter);
        end_textView2.setAdapter(arrayAdapter);
        end_textView3.setAdapter(arrayAdapter);
        end_textView4.setAdapter(arrayAdapter);
        end_textView5.setAdapter(arrayAdapter);
        end_textView6.setAdapter(arrayAdapter);
        end_textView7.setAdapter(arrayAdapter);
        end_textView8.setAdapter(arrayAdapter);
        end_textView9.setAdapter(arrayAdapter);
        end_textView10.setAdapter(arrayAdapter);

        oneWayTrip = Settings.getOneWayTrip(this);
        address = Settings.getAddress(this);
        Log.i("settings", oneWayTrip);
        Log.i("settings", address);

        SCHOOLNAMES.add("Home");
        ADDRESSES.add(address);

        if (oneWayTripDouble == 0.0 && address.equals("")) {

            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);

        }

        getDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDistance();

            }
        });

        start_textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(start_textView.getText());
                startTextViewAddressIndex = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();

            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView.getText());
                startTextViewAddressIndex2 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex2);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView2.getText());
                startTextViewAddressIndex3 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex3);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView3.getText());
                startTextViewAddressIndex4 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex4);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView4.getText());
                startTextViewAddressIndex5 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex5);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView5.getText());
                startTextViewAddressIndex6 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex6);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView6.getText());
                startTextViewAddressIndex7 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex7);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView7.getText());
                startTextViewAddressIndex8 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex8);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView8.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView8.getText());
                startTextViewAddressIndex9 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex9);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView9.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(autoCompleteTextView9.getText());
                startTextViewAddressIndex10 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(startTextViewAddressIndex10);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView.getText());
                endTextViewAddressIndex = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView2.getText());
                endTextViewAddressIndex2 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex2);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView3.getText());
                endTextViewAddressIndex3 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex3);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView4.getText());
                endTextViewAddressIndex4 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex4);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView5.getText());
                endTextViewAddressIndex5 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex5);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView6.getText());
                endTextViewAddressIndex6 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex6);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView7.getText());
                endTextViewAddressIndex7 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex7);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView8.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView8.getText());
                endTextViewAddressIndex8 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex8);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView9.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView9.getText());
                endTextViewAddressIndex9 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex9);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        end_textView10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String schoolSelected;
                schoolSelected = String.valueOf(end_textView10.getText());
                endTextViewAddressIndex10 = SCHOOLNAMES.indexOf(schoolSelected);

                String addressOfSchoolSelected = ADDRESSES.get(endTextViewAddressIndex10);

                Toast.makeText(MainActivity.this, addressOfSchoolSelected , Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu );
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
            Intent intent1 = new Intent(this, Settings.class);
            this.startActivity(intent1);

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}




