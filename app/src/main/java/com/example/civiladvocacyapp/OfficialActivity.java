package com.example.civiladvocacyapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private String loc;
    private Official official;
    private ImageView profilePic, partyLogo;
    private TextView address, phoneNum, email, website;
    private String ytLink, tLink, fbLink;
    private TextView name, role, party;
    private TextView location;
    private ImageView yt, twitter, fb;
    private TextView phoneTV, addressTV, websiteTV, emailTV;
    private long start;
    private ConstraintLayout cl; //needed to change the color of the background

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setTitle("Civil Advocacy");

        location = findViewById(R.id.location_tv);
        name = findViewById(R.id.official_name_tv);
        role = findViewById(R.id.office_tv);
        party = findViewById(R.id.partyName_tv);
        profilePic = findViewById(R.id.photo_iv);
        partyLogo = findViewById(R.id.partyLogo);
        address = findViewById(R.id.addressResult);
        phoneNum = findViewById(R.id.phoneResult);
        email = findViewById(R.id.emailResult);
        website = findViewById(R.id.siteResult);

        //placeholders. will change visibility depending on if exists
        phoneTV = findViewById(R.id.phoneTV);
        addressTV = findViewById(R.id.addressTV);
        websiteTV = findViewById(R.id.websiteTV);
        emailTV = findViewById(R.id.emailTV);

        //social media icons
        yt = findViewById(R.id.ytLogo);
        twitter = findViewById(R.id.twitterLogo);
        fb = findViewById(R.id.fbLogo);

        cl = findViewById(R.id.cl);

        Intent intent = getIntent();
        ///////////////////
        if (intent.hasExtra("OFFICIAL_INFO")){
            official = (Official) intent.getSerializableExtra("OFFICIAL_INFO");
        }
        if (intent.hasExtra("LOCATION")){
            location.setText(intent.getStringExtra("LOCATION"));
        }
        if (official.getPhotoLink() != null){
            downloadImage(official.getPhotoLink());
        }
        else{ //glide error
            Glide.with(this)
                    .load(official.getPhotoLink())
                    .placeholder(R.drawable.missing)
                    .error(R.drawable.missing)
                    .into(profilePic);
        }
        noImage();
        //////////////
        name.setText(official.getName());
        role.setText(official.getGovernmentTitle());
        //////////////
        socialLinkers();
        //////////////
        extraInfo();
        //////////////
        partySetter();
    }

    public void noImage(){
        if (!hasNetworkConnection()){
            profilePic.setImageResource(R.drawable.brokenimage);
        }
    }

    private void downloadImage(String link) {
        start = System.currentTimeMillis();
        Glide.with(this)
                .load(link)
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

    public void extraInfo(){
        if (official.getPhoneNum() == null){
            phoneTV.setVisibility(View.GONE);
            phoneNum.setVisibility(View.GONE);
        }
        else{
            phoneNum.setText(official.getPhoneNum());
        }
        /////////////
        if (official.getOfficeAddress() == null){
            addressTV.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }
        else{
            address.setText(official.getOfficeAddress());
        }
        /////////////
        if (official.getWebsite() == null){
            website.setVisibility(View.GONE);
            websiteTV.setVisibility(View.GONE);
        }
        else{
            website.setText(official.getWebsite());
        }
        /////////////
        if (official.getEmail() == null){
            email.setVisibility(View.GONE);
            emailTV.setVisibility(View.GONE);
        }
        else{
            email.setText(official.getEmail());
        }
    } //address, website, email, phoneNum checker

    public void socialLinkers(){
        //check for each text view if it is present otherwise set it to invisible or gone
        if (official.getYtLink() == null){ //YOUTUBE LINK
            yt.setVisibility(View.INVISIBLE);
        }
        else{
            ytLink = official.getYtLink();
        }
        if (official.getTwitLink() == null){
            twitter.setVisibility(View.INVISIBLE);
        }
        else{
            tLink = official.getTwitLink();
        }
        if (official.getFbLink() == null){
            fb.setVisibility(View.INVISIBLE);
        }
        else{
            fbLink = official.getFbLink();
        }
    } //checks for social media links

    public void partySetter(){
        if (official.getParty() != null){
            String partyName = official.getParty();
            party.setText(String.format("(%s)", partyName));
            if (partyName.contains("Republican")){
                partyLogo.setImageResource(R.drawable.rep_logo);
                cl.setBackgroundColor(Color.RED);
            }
            else if (partyName.contains("Democrat")){
                partyLogo.setImageResource(R.drawable.dem_logo);
                cl.setBackgroundColor(Color.BLUE);
            }
            else if (!partyName.contains("Democrat") || !partyName.contains("Republican")){
                partyLogo.setVisibility(View.INVISIBLE);
                cl.setBackgroundColor(Color.BLACK);
            }
        }
    } //checks for the party and sets up the activity background color accordingly

    public void clickYT(View v){
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + ytLink));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + ytLink)));
        }
    }

    //TODO: fix each click method with their respective link
    public void clickTwitter(View v){
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + tLink));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + tLink)));
        }
    }

    public void clickFb(View v){
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + fbLink));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + fbLink)));
        }
    }

    public void clickPhoto(View v){
        if(official.getPhotoLink() != null){
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("LOCATION", address.getText().toString());
            intent.putExtra("OFFICIAL_INFO", official);
            startActivity(intent);
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}