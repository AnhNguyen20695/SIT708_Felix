package com.example.learningexpapp;

import com.example.learningexpapp.other.Utils;
import com.example.learningexpapp.other.Constants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningexpapp.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private List<String> questions;
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap;
    SharedPreferences sharedPref;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Retrieve preference
        sharedPref = getSharedPreferences("com.example.quiz",MODE_PRIVATE);
        String loggedin_username = sharedPref.getString("CURRENT_USERNAME","Unknown User");
        String loggedin_password = sharedPref.getString("CURRENT_PASSWORD","Unknown User");

        // Pull current user's information
        database = new DatabaseHelper(this);
        User currentUser = database.login(loggedin_username, loggedin_password);
        String rawQuestions = currentUser.getTaskQuestions();
        questionsAnswerMap = Utils.getQuestions(rawQuestions);

        Log.i("MAIN_LOG","Setting answer and question.");
        questions = new ArrayList<>(questionsAnswerMap.keySet());
        tvQuestion = findViewById(R.id.textView78);
        tvQuestionNumber = findViewById(R.id.textView18);
        btnNext = findViewById(R.id.btnNextQuestion);
        radioGroup = findViewById(R.id.radioGroup);

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        Log.i("MAIN_LOG","Resetting state.");
        resetState();


        findViewById(R.id.btnNextQuestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnNext.getText().equals(getString(R.string.next))){
                    currentQuestionIndex++;
                    displayNextQuestions();
                } else if (btnNext.getText().equals(getString(R.string.submit))){
                    int num_options = radioGroup.getChildCount();
                    for (int i=0; i<num_options; i++)
                    {
                        View o = radioGroup.getChildAt(i);
                        if (o instanceof RadioButton) {
                            RadioButton answer_button =(RadioButton)o;
                            if (questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(answer_button.getText())) {
                                answer_button.setBackgroundColor(Color.GREEN);
                            }

                        }
                    }

                    RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                    boolean selected_answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());
                    if (selected_answer){
                        correctQuestion++;
                    } else {radioButton.setBackgroundColor(Color.RED);}

                    if (currentQuestionIndex == Constants.QUESTION_SHOWING  - 1){
                        btnNext.setText(getText(R.string.finish));
                    } else {
                        btnNext.setText(R.string.next);
                    }
                } else{
                    Intent intentResult = new Intent(QuizActivity.this,ResultActivity.class);
                    intentResult.putExtra("TOPIC",currentUser.getRecommendedTopic());
                    intentResult.putExtra("CURRENT_USERNAME",currentUser.getUsername());
                    intentResult.putExtra("CURRENT_PASSWORD",currentUser.getPassword());
                    intentResult.putExtra(Constants.CORRECT,correctQuestion);
                    intentResult.putExtra(Constants.INCORRECT,Constants.QUESTION_SHOWING - correctQuestion);
                    startActivity(intentResult);
                }

            }
        });

        Log.i("MAIN_LOG","Displaying data.");
        displayData();
    }
    private void displayNextQuestions() {
        setAnswersToRadioButton();
        resetState();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1) + " / " + Constants.QUESTION_SHOWING);
    }

    private void displayData() {
        btnNext.setText(R.string.submit);
        Log.i("MAIN_LOG","Setting question: "+questions.get(currentQuestionIndex));
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1) + " / " + Constants.QUESTION_SHOWING);

        setAnswersToRadioButton();
    }

    private void setAnswersToRadioButton(){

        ArrayList<String> questionKey = new ArrayList(questionsAnswerMap.get(questions.get(currentQuestionIndex)).keySet());

        radioButton1.setText(questionKey.get(0));
        radioButton2.setText(questionKey.get(1));
        radioButton3.setText(questionKey.get(2));
        radioButton4.setText(questionKey.get(3));

    }

    private void resetState(){
        radioGroup.clearCheck();
        int num_options = radioGroup.getChildCount();
        for (int i=0; i<num_options; i++)
        {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                RadioButton answer_button =(RadioButton)o;
                answer_button.setBackgroundColor(Color.TRANSPARENT);

            }
        }
        btnNext.setText(R.string.submit);
    }
}