package com.example.civiladvocacyapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private String loc;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private List<Official> officials = new ArrayList<>();
    private OfficialAdapter oAdapter;
    private TextView locationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Civil Advocacy");
        //RecyclerView stuff
        recyclerView = findViewById(R.id.person_rv);
        oAdapter = new OfficialAdapter(officials, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        determineLocation();

        locationTV = findViewById(R.id.location_tv);

        if (!hasNetworkConnection()) {
            recyclerView.setVisibility(View.GONE);
            locationTV.setText("No data for location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded  without a network connection");
            builder.setPositiveButton("OK", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.searchLocation:
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enter Address");
                    EditText locationInput = new EditText(this);
                    locationInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    locationInput.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(locationInput);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO: set the location -> do API stuff
                            loc = locationInput.getText().toString();
                            officials.clear();
                            OfficialRunnable oRun = new OfficialRunnable(MainActivity.this, locationInput.getText().toString());
                            locationTV.setText(locationInput.getText().toString());
                            Toast.makeText(MainActivity.this, "Searched for location", Toast.LENGTH_SHORT).show();
                            new Thread(oRun).start();
                        }
                    });

                    builder.setNegativeButton("Cancel", (dialog, id) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> { //TODO: location is giving null. FIX THIS
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        loc = getPlace(location);
                        locationTV.setText(loc);
                        officials.clear();
                        OfficialRunnable or = new OfficialRunnable(this, loc);
                        new Thread(or).start();
                    }
                    else{
                        locationTV.setText("Null Location");
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    locationTV.setText("No location");
                }
            }
        }
    }

    private String getPlace(Location loc) {
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String subThoroughFare = addresses.get(0).getSubThoroughfare(); //building number
            String thoroughFare = addresses.get(0).getThoroughfare(); // direction + street EXAMPLE: "S State St"
            String city = addresses.get(0).getLocality(); //city name
            String state = addresses.get(0).getAdminArea(); //state name
            String zipcode = addresses.get(0).getPostalCode(); //zipcode
            sb.append(String.format(Locale.getDefault(),
                    "%s %s, %s, %s %s",
                    subThoroughFare, thoroughFare, city, state, zipcode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official o = officials.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OFFICIAL_INFO", o);
        intent.putExtra("LOCATION", loc);
        startActivity(intent);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public void addNewOff(Official o){
        officials.add(o);
        oAdapter.notifyDataSetChanged();
    }

}