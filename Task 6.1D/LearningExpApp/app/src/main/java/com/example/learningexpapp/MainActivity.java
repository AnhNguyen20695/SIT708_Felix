package com.example.learningexpapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningexpapp.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MAIN_LOG","Displaying main layout");
        sharedPref = getSharedPreferences("com.example.quiz",MODE_PRIVATE);
        Log.i("MAIN_LOG","Checking shared preferences.");
//        clearSharedPreferences();
        checkSharedPreferences();
    }

    public void clearSharedPreferences() {
        sharedPref.edit().clear().commit();
    }
    public void checkSharedPreferences() {
        Log.i("MAIN_LOG","Checking if logged in.");
        boolean isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false);
        Log.i("MAIN_LOG","is logged in? "+isLoggedIn);
        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(intent);
        } else {
            Log.i("MAIN_LOG","Not signed in, switching to login fragment.");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.signin_fragment, LoginFragment.class, null)
                    .commit();
            Log.i("MAIN_LOG","Not signed in, switched to login fragment.");
        }
    }


}