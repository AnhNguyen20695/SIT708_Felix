package com.example.lostandfoundv2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundv2.DatabaseHelper;
import com.example.lostandfoundv2.MainActivity;
import com.example.lostandfoundv2.R;

public class RemoveItemActivity extends AppCompatActivity {
    TextView nameView, phoneView, descView, reportedDateView, locationView;
    Button removeButton;
    String postType, name, phoneNumber, description, reportedDate, location;
    Integer itemID;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        initWidgets();
        database = new DatabaseHelper(this);

        Intent intent = getIntent();
        itemID = intent.getIntExtra("ID",0);
        postType = intent.getStringExtra("PostType");
        name = intent.getStringExtra("Name");
        phoneNumber = intent.getStringExtra("PhoneNumber");
        description = intent.getStringExtra("Description");
        reportedDate = intent.getStringExtra("ReportedDate");
        location = intent.getStringExtra("Location");

        phoneView.setText(phoneNumber);
        descView.setText(description);
        reportedDateView.setText(reportedDate);
        locationView.setText(location);

        if (postType.equals("lost")) {
            nameView.setText("Lost "+name);
        } else if (postType.equals("found")) {
            nameView.setText("Found "+name);
        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postType.equals("lost")) {
                    database.deleteLostItem(itemID);
                } else if (postType.equals("found")) {
                    database.deleteFoundItem(itemID);
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initWidgets() {
        nameView = findViewById(R.id.name_field_remove);
        phoneView = findViewById(R.id.phone_field_remove);
        descView = findViewById(R.id.description_field_remove);
        reportedDateView = findViewById(R.id.date_field_remove);
        locationView = findViewById(R.id.location_field_remove);
        removeButton = findViewById(R.id.remove_button);
    }
}