package com.example.civiladvocacyapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Civil Advocacy");
        TextView link = findViewById(R.id.apiLink_tv);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setLinkTextColor(Color.WHITE);
    }
}