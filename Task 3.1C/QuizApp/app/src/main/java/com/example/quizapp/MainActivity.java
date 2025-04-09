package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.other.Constants;

public class MainActivity extends AppCompatActivity {
    Button startButton, clearButton;
    EditText inputName;
    TextView welcomeText;
    SharedPreferences sharedPreferences;
    String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        clearButton = findViewById(R.id.clearButton);
        inputName = findViewById(R.id.inputName);
        welcomeText = findViewById(R.id.welcomeText);

        sharedPreferences = getSharedPreferences("com.example.quizapp", MODE_PRIVATE);
        checkSharedPreferences();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_NAME, inputName.getText().toString());
                editor.apply();

                Intent intent = new Intent(MainActivity.this, Quiz.class);
                intent.putExtra(Constants.SUBJECT,getString(R.string.literature));
                startActivity(intent);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPreferences();
                inputName.setText("");
                inputName.setBackgroundResource(R.drawable.text_input_border);
                welcomeText.setText(R.string.input_field);
            }
        });
    }

    public void checkSharedPreferences() {
        String username = sharedPreferences.getString(USER_NAME, "");
        if (!username.isEmpty()) {
            inputName.setText(username);
            inputName.setBackgroundColor(Color.TRANSPARENT);
            welcomeText.setText("Hi, ");
        }
    }

    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}