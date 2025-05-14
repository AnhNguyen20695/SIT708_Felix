package com.example.chatbotv2.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatbotv2.utilities.Constants;
import com.example.chatbotv2.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenerateResponseService extends IntentService {
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    public GenerateResponseService() {
        super("Generate Response from LLM server.");
    };

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("MAIN_LOG","Sending POST message to the server.");
        String promptLLM = intent.getStringExtra(Constants.PROMPT);
        Log.i("MAIN_LOG","Received prompt: "+promptLLM);

        // Init
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();

        // Make request to LLM server to generate a response based on an input prompt
        Log.i("MAIN_LOG","Sending POST message to the server.");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://10.0.2.2:5000/chat", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MAIN_LOG","onResponse: "+response);
                try {
                    // On successful response, send the message to the Firestore database
                    HashMap<String, Object> message = new HashMap<>();
                    message.put(Constants.KEY_SENDER_ID, Constants.BOT_FIREBASE_DOCUMENT_ID);
//        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
                    message.put(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                    message.put(Constants.KEY_MESSAGE, response);
                    message.put(Constants.KEY_TIMESTAMP, new Date());
                    database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
                } catch (Exception e) {
                    Log.i("MAIN_LOG","Error getting the response: "+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MAIN_LOG","onErrorResponse: " + error.toString());
            }
        }){
            // Add parameters to the request
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.LLM_REQUEST_MESSAGE_KEY, promptLLM);
                Log.i("MAIN_LOG", "Sending prompt: "+promptLLM);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
