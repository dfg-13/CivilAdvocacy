package com.example.civiladvocacyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OfficialActivity extends AppCompatActivity {

    private Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setTitle("Civil Advocacy");

        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL_INFO")){
            official = (Official) intent.getSerializableExtra("OFFICIAL_INFO");
            if (official != null){

            }
        }

    }

    public void clickYT(View v){

    }

    public void clickTwitter(View v){

    }

    public void clickFb(View v){

    }
}