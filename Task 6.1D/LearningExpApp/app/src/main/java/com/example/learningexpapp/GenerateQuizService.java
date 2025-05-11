package com.example.learningexpapp;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GenerateQuizService extends Service {
    private DatabaseHelper database;
    public GenerateQuizService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String recommendTopic = intent.getStringExtra("RECOMMENDED_TOPIC");
        String currentUsername = intent.getStringExtra("CURRENT_USERNAME");
        database = new DatabaseHelper(this);
        Log.i("MAIN_LOG","Started generating quiz on topic: "+recommendTopic);

        // Make request to LLM server to generate a quiz based on a recommended topic
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:5000/getQuiz?topic="+recommendTopic, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("MAIN_LOG","onResponse: "+response);
                try {
                    database.updateData(currentUsername, DatabaseHelper.COL_6_USERS, response.get("quiz").toString().replace("'",""));
                    Log.i("MAIN_LOG","Finished generating quiz on topic: "+recommendTopic);
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
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
