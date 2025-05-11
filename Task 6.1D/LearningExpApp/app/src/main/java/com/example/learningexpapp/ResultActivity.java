package com.example.learningexpapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.learningexpapp.models.User;
import com.example.learningexpapp.other.Constants;
import com.example.learningexpapp.other.Utils;

import org.json.JSONObject;


public class ResultActivity extends AppCompatActivity {
    private TextView tvSubject, tvCorrect, tvIncorrect, tvDate;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        int correctAnswer = intent.getIntExtra(Constants.CORRECT, 0);
        int incorrectAnswer = intent.getIntExtra(Constants.INCORRECT, 0);
        String topic = intent.getStringExtra("TOPIC");


        tvSubject = findViewById(R.id.textView16);
        tvCorrect = findViewById(R.id.textView19);
        tvIncorrect = findViewById(R.id.textView27);
        tvDate = findViewById(R.id.textView30);

        tvSubject.setText(topic);
        tvCorrect.setText(""+correctAnswer);
        tvIncorrect.setText(""+incorrectAnswer);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        tvDate.setText(formatter.format(date));

        findViewById(R.id.imageViewFinalResultQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this,HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.btnFinishQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this,HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        // Update user performance into database
//        Intent intentService = new Intent(getApplicationContext(), UpdateDatabaseService.class);
//        intentService.putExtra("TYPE","update_records");
//        intentService.putExtra("TOPIC",topic);
//        intentService.putExtra("CORRECT_ANSWER",correctAnswer);
//        intentService.putExtra("INCORRECT_ANSWER",incorrectAnswer);
//        startService(intentService);
//
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            Log.i("MAIN_LOG", "Can not sleep.");
//        }
        String currentUsername = intent.getStringExtra("CURRENT_USERNAME");
        String currentPassword = intent.getStringExtra("CURRENT_PASSWORD");
        database = new DatabaseHelper(this);
        User currentUser = database.login(currentUsername, currentPassword);
        String currentRecords = currentUser.getRecords();
        try {
            JSONObject jsonCurrentRecords = new JSONObject(currentRecords);
            Log.i("MAIN_LOG","Getting score of topic: "+topic);
            Double currentScore = jsonCurrentRecords.getDouble(topic);
            Log.i("MAIN_LOG","Current score of topic: "+currentScore);
            Double newScore = currentScore + (10*correctAnswer / (correctAnswer+incorrectAnswer));
            Log.i("MAIN_LOG","Correct answer: "+correctAnswer);
            Log.i("MAIN_LOG","Incorrect answer: "+incorrectAnswer);
            Log.i("MAIN_LOG","New score of topic: "+newScore);
            jsonCurrentRecords.put(topic,newScore);
            currentUser.setRecords(jsonCurrentRecords.toString());
            database.updateData(currentUser.getUsername(), DatabaseHelper.COL_4_USERS, jsonCurrentRecords.toString());
        } catch (Exception e) {
            Log.i("MAIN_LOG","Can not parse current records into JSON. Error: "+e);
        }

        Log.i("MAIN_LOG","Parsing records: "+currentUser.getRecords());
        String recommendQuizParameters = Utils.generateRecommendedTopicURLParams(currentUser.getRecords());
        Log.i("MAIN_LOG","Parsed records: "+recommendQuizParameters);
        Intent intentRecommendTopicService = new Intent(getApplicationContext(), RecommendTopicService.class);
        intentRecommendTopicService.putExtra("RECOMMENDED_QUIZ_PARAMETERS",recommendQuizParameters);
        intentRecommendTopicService.putExtra("CURRENT_USERNAME",currentUsername);
        startService(intentRecommendTopicService);
    }
}