package com.example.learningexpapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RecommendTopicService extends IntentService {
    private DatabaseHelper database;
    public RecommendTopicService() {
        super("Recommend topic intent service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String recommendQuizParameters = intent.getStringExtra("RECOMMENDED_QUIZ_PARAMETERS");
        String currentUsername = intent.getStringExtra("CURRENT_USERNAME");
        database = new DatabaseHelper(this);
        Log.i("MAIN_LOG","Started recommend topic intent service. Parameters: "+recommendQuizParameters);

        // Make request to LLM server to generate a recommended topic
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:5000/recommendTopic?"+recommendQuizParameters, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("MAIN_LOG","onResponse: "+response);
                try {
                    database.updateData(currentUsername, DatabaseHelper.COL_5_USERS, response.get("topic").toString());
                    Log.i("MAIN_LOG","Recommend topic intent service finished.");
                } catch (JSONException e) {
                    Log.i("MAIN_LOG","Can not process recommended topic.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MAIN_LOG","onErrorResponse: " + error.toString());
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

}
