package com.example.civiladvocacyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";

    private final String libUrl = "https://democrats.org";
    private final String conUrl = "https://gop.com";
    ImageView profilePic, partyLogo;
    private Official official;
    private long start;
    private ConstraintLayout cl;

    private TextView location;
    private TextView personName;
    private TextView personRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        setTitle("Civil Advocacy");

        profilePic = findViewById(R.id.fullPhoto);
        partyLogo = findViewById(R.id.party);
        cl = findViewById(R.id.ConstraintLayout);
        personName = findViewById(R.id.pName);
        personRole = findViewById(R.id.pRole);
        location = findViewById(R.id.location_tv);

        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")){
            location.setText(intent.getStringExtra("LOCATION"));
        }
        personName.setText(official.getName());
        personRole.setText(official.getGovernmentTitle());

        noImage(); //if no connection then don't load a image. set to broken image

        donkeyElephant(official.getParty()); //using the party string it will determine the logo and background color

        if (official.getPhotoLink() != null){ //if link exists, use Glide to get it
            downloadImage();
        }
        else{ //glide error
            Glide.with(this)
                    .load(official.getPhotoLink())
                    .placeholder(R.drawable.missing)
                    .error(R.drawable.brokenimage)
                    .into(profilePic);
        }
    }

    public void donkeyElephant(String party){
        if (party.contains("Republican")){
            partyLogo.setImageResource(R.drawable.rep_logo);
            cl.setBackgroundColor(Color.RED);
        }
        else if (party.contains("Democrat")){
            partyLogo.setImageResource(R.drawable.dem_logo);
            cl.setBackgroundColor(Color.BLUE);
        }
        else{
            partyLogo.setVisibility(ImageView.INVISIBLE);
            cl.setBackgroundColor(Color.BLACK);
        }
    }

    public void noImage(){
        if (!hasNetworkConnection()){
            profilePic.setImageResource(R.drawable.brokenimage);
        }
    }

    public void doDownload(View v) {
        downloadImage();
    }

    private void downloadImage() {
        start = System.currentTimeMillis();
        Glide.with(this)
                .load(official.getPhotoLink())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        long time = System.currentTimeMillis() - start;
                        Log.d(TAG, "onResourceReady: " + time);
                        return false;
                    }
                })
                .into(profilePic);
    }

    public void clickLogo(View v){ //if they click the logo then it should direct user to party's website
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
