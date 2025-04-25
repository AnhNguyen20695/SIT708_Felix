package com.example.itubev3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
    Button playButton;
    Button addToPlaylistButton;
    Button myPlaylistButton;
    Button logoutButton;
    EditText urlView;
    SharedPreferences sharedPref;
    String CURRENT_USER;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        playButton = findViewById(R.id.play_button);
        addToPlaylistButton = findViewById(R.id.add_to_playlist_button);
        myPlaylistButton = findViewById(R.id.my_playlist_button);
        logoutButton = findViewById(R.id.logout_button);
        urlView = findViewById(R.id.url);

        database = new DatabaseHelper(this);

        sharedPref = getSharedPreferences("com.example.itubev3",MODE_PRIVATE);
        String currentUser = sharedPref.getString("CURRENT_USER", "");
        Log.i("MAIN_LOG","home screen current user: "+currentUser);
        if (currentUser.equals("")) {
            Intent intent = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(intent);
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlView.getText().toString();
                Intent intent = new Intent(HomeScreen.this, PlayVideo.class);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });

        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlView.getText().toString();
                String result = database.addToPlaylist(currentUser, url);
                if (result.equals("success")) {
                    Toast.makeText(HomeScreen.this, "Added " + url, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeScreen.this, "Failed to add " + url, Toast.LENGTH_SHORT).show();
                }
            }
        });

        myPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, PlaylistActivity.class);
                intent.putExtra("CURRENT_USER",currentUser);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("IS_LOGGED_IN", false);
                editor.apply();
                Toast.makeText(HomeScreen.this, "Logged out. See you again "+currentUser, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}