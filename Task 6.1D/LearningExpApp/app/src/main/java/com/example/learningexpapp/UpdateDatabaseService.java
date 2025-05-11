package com.example.learningexpapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.learningexpapp.models.User;

import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateDatabaseService extends IntentService {

    private DatabaseHelper database;
    SharedPreferences sharedPref;

    public UpdateDatabaseService() {
        super("UpdateDatabaseService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        String updateType = intent.getStringExtra("TYPE");
        if (updateType.equals("update_records")) {
            String topic = intent.getStringExtra("TOPIC");
            Integer correctAnswer = intent.getIntExtra("CORRECT_ANSWER",0);
            Integer incorrectAnswer = intent.getIntExtra("INCORRECT_ANSWER",0);
            database = new DatabaseHelper(this);
            // Record user progress into databased
            sharedPref = getSharedPreferences("com.example.quiz",MODE_PRIVATE);
            String loggedin_username = sharedPref.getString("CURRENT_USERNAME","Unknown User");
            String loggedin_password = sharedPref.getString("CURRENT_PASSWORD","Unknown User");
            User currentUser = database.login(loggedin_username, loggedin_password);
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
                database.updateData(currentUser.getUsername(), DatabaseHelper.COL_5_USERS, jsonCurrentRecords.toString());
            } catch (Exception e) {
                Log.i("MAIN_LOG","Can not parse current records into JSON. Error: "+e);
            }
        }
    }

}