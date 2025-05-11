package com.example.learningexpapp.other;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Utils {
    public static Map<String, Map<String,Boolean>> getQuestions(String rawQuestions){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();
        try {
            JSONObject jsonQuestions = new JSONObject(rawQuestions);
            // Loop in the list of questions
            Iterator<String> qnsKeys = jsonQuestions.keys();
            while(qnsKeys.hasNext()) {
                String qns = qnsKeys.next();
                HashMap<String,Boolean> answer = new HashMap<>();
                JSONObject jsonAnswers = jsonQuestions.getJSONObject(qns);
                Iterator<String> ansKeys = jsonAnswers.keys();
                while(ansKeys.hasNext()) {
                    String ans = ansKeys.next();
                    answer.put(ans,Boolean.valueOf(jsonAnswers.getString(ans)));
                }
                questions.put(qns,answer);
            }
            Log.i("MAIN_LOG", "Successfully parsed json questions into hash map.");
        } catch (JSONException e) {
            Log.i("MAIN_LOG", "Unable to parse the json. Raw questions: "+rawQuestions);
        }

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomQuestions(Context context, String rawQuestions, int SIZE){
        Map<String,Map<String,Boolean>> questionsMap = new HashMap<>();
        Map<String, Map<String, Boolean>> originalQuestion;
        originalQuestion = getQuestions(rawQuestions);

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }

    public static String generateRecommendedTopicURLParams(String currentRecords) {
        String recommendQuizParameters = "";
        try {
            JSONObject userRecords = new JSONObject(currentRecords);
            Log.i("MAIN_LOG","Retrieved user records: "+userRecords);

            // Loop in the list of user records to create parameters for the request to get the recommended topic
            Iterator<String> keys = userRecords.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                recommendQuizParameters = recommendQuizParameters + key.replace(" ","%20") + "=" + userRecords.get(key).toString();
                if (keys.hasNext()) {
                    recommendQuizParameters = recommendQuizParameters + "&";
                }
            }

        } catch (Exception e) {
            Log.i("MAIN_LOG","Can not convert String to json. Error: "+e);
        }
        return recommendQuizParameters;
    }
}
