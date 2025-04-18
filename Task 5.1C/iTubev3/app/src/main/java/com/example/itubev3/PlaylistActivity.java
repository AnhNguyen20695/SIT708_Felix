package com.example.itubev3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView playlistRecycler;
    List<Playlist> playlist = new ArrayList<>();
    PlaylistRecyclerAdapter playlistRecyclerAdapter;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MAIN_LOG","In playlist activity");
        setContentView(R.layout.activity_playlist);
        Log.i("MAIN_LOG","set content view");
        database = new DatabaseHelper(this);
        Intent intent = getIntent();
        String currentUser = intent.getStringExtra("CURRENT_USER");

        playlistRecycler = findViewById(R.id.playlist_recycler_view);
        playlistRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(playlist);
        playlistRecycler.setAdapter(playlistRecyclerAdapter);

        playlist = database.retrievePlaylist(currentUser);
        Log.i("MAIN_LOG","Retrieved number of items in playlist: "+playlist.size());
        playlistRecyclerAdapter.updateData(playlist);
        playlistRecyclerAdapter.notifyDataSetChanged();
    }
}