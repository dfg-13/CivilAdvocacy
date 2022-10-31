package com.example.civiladvocacyapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private RecyclerView recyclerView;
    //private OfficialAdapter oAdapter;
    private static final String TAG = "MainActivity";
    private List<Official> officials = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Civil Advocacy");
        //RecyclerView stuff
        recyclerView = findViewById(R.id.person_rv);
        OfficialAdapter oAdapter = new OfficialAdapter(officials, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i <= 10; i++) {
            officials.add(new Official("Joe Biden", "President of the United States", "Democratic Party"));
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
        switch (item.getItemId()){
            case R.id.info:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.searchLocation:
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
                        Toast.makeText(MainActivity.this, "Searched for location", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, id) -> {});
                AlertDialog dialog = builder.create();
                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        //int pos = recyclerView.getChildLayoutPosition(view);
        //Official o = officials.get(pos);
        Toast.makeText(this, "Clicked on an official's profile", Toast.LENGTH_SHORT).show();
    }
}