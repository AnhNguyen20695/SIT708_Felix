package com.example.lostandfound.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfound.DatabaseHelper;
import com.example.lostandfound.MainActivity;
import com.example.lostandfound.R;

public class CreateNewActivity extends AppCompatActivity {
    RadioButton lostButton, foundButton;
    RadioGroup postTypeGroup;
    EditText nameField, phoneField, descField, reportedDateField, locationField;
    String postType;
    Button saveButton;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        initWidgets();
        database = new DatabaseHelper(this);

        postType = "lost";

        postTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                String postTypeChecked = checkedRadioButton.getText().toString();
                Log.i("MAIN_LOG","Updated radio group.");

                if (postTypeChecked.equals("Lost")) {
                    Log.i("MAIN_LOG","Lost.");
                    postType = "lost";
                } else if (postTypeChecked.equals("Found")) {
                    Log.i("MAIN_LOG","Found.");
                    postType = "found";
                } else {postType = "not_implemented";}
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String phoneNumber = phoneField.getText().toString();
                String description = descField.getText().toString();
                String reportedDate = reportedDateField.getText().toString();
                String location = locationField.getText().toString();
                Log.i("MAIN_LOG","Adding items.");
                if (postType.equals("lost")) {
                    Log.i("MAIN_LOG","Adding lost items.");
                    String result = database.addToLostItems(name,
                            phoneNumber,
                            description,
                            reportedDate,
                            location);
                    Log.i("MAIN_LOG","Lost items result: "+result);
                } else if (postType.equals("found")) {
                    Log.i("MAIN_LOG","Adding found items.");
                    String result = database.addToFoundItems(name,
                            phoneNumber,
                            description,
                            reportedDate,
                            location);
                    Log.i("MAIN_LOG","Found items result: "+result);
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        lostButton = findViewById(R.id.lost_button);
        foundButton = findViewById(R.id.found_button);
        postTypeGroup = findViewById(R.id.post_type_group);
        saveButton = findViewById(R.id.save_button);
        nameField = findViewById(R.id.name_field);
        phoneField = findViewById(R.id.phone_field);
        descField = findViewById(R.id.description_field);
        reportedDateField = findViewById(R.id.date_field);
        locationField = findViewById(R.id.location_field);
    }

}