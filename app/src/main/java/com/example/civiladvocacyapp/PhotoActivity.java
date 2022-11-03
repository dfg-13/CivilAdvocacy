package com.example.civiladvocacyapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    private final String libUrl = "https://democrats.org";
    private final String conUrl = "https://gop.com";
    ImageView profilePic, partyLogo;
    private Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        setTitle("Civil Advocacy");

        partyLogo = findViewById(R.id.party);

    }

    public void clickLogo(View v){
        Intent intent = null;
        Uri demLink = Uri.parse(libUrl);
        Uri conLink = Uri.parse(conUrl);
        if (official.getParty().equals("Republican Party")){
            intent = new Intent(Intent.ACTION_VIEW, conLink);
        }
        if (official.getParty().equals("Democratic Party")){
            intent = new Intent(Intent.ACTION_VIEW, demLink);
        }
        startActivity(intent);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
