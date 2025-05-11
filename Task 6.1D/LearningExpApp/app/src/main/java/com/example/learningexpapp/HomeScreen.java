package com.example.learningexpapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.learningexpapp.models.User;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeScreen extends AppCompatActivity {
    TextView loggedinUser;
    TextView taskName;
    TextView taskDescription;
    Button startTaskBtn;
    SharedPreferences sharedPref;
    private DatabaseHelper database;
    MaterialButton logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i("MAIN_LOG","In home screen");

        // Set greeting username
        loggedinUser = findViewById(R.id.loggedin_user);
        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.task_description);
        sharedPref = getSharedPreferences("com.example.quiz",MODE_PRIVATE);
        String loggedin_user = sharedPref.getString("CURRENT_USER","Unknown User");
        String loggedin_username = sharedPref.getString("CURRENT_USERNAME","Unknown User");
        String loggedin_password = sharedPref.getString("CURRENT_PASSWORD","Unknown User");
        loggedinUser.setText(loggedin_user);

        // Start task button
        startTaskBtn = findViewById(R.id.start_task_btn);
        startTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        // Log out button
        logoutBtn = findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPreferences();
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Pull current user's information
        database = new DatabaseHelper(this);
        User currentUser = database.login(loggedin_username, loggedin_password);
        taskName.setText(currentUser.getRecommendedTopic());
        taskDescription.setText("Based on your interests and previous records with TutorU, this task aims to improve your knowledge in topic: "+currentUser.getRecommendedTopic());
        Log.i("MAIN_LOG","Current records: "+currentUser.getRecords());

        // Generate quiz if not present
        Log.i("MAIN_LOG","Current generated questions: "+currentUser.getTaskQuestions());
        if (currentUser.getTaskQuestions().equals("no_generated_questions")) {
            Log.i("MAIN_LOG","No generated questions.");
        }
        Intent intent = new Intent(getApplicationContext(), GenerateQuizService.class);
        intent.putExtra("RECOMMENDED_TOPIC",currentUser.getRecommendedTopic());
        intent.putExtra("CURRENT_USERNAME",currentUser.getUsername());
        startService(intent);

    }

    public void clearSharedPreferences() {
        sharedPref.edit().clear().commit();
    }
}