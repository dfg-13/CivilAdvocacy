package com.example.civiladvocacyapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OfficialActivity extends AppCompatActivity {

    private Official official;
    private ImageView profilePic, partyLogo;
    private TextView address, phoneNum, email, website;
    private String ytLink, tLink, fbLink;
    private TextView name, role, party;
    private TextView location;

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

        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL_INFO")){
            official = (Official) intent.getSerializableExtra("OFFICIAL_INFO");
            if (official != null){

            }
        }

    }


    //TODO: fix each click method with their respective link
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
}