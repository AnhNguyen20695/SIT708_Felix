package com.example.itubev3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistRecyclerAdapter.PlaylistViewHolder> {
    List<Playlist> playlist;
    @NonNull
    @Override
    public PlaylistRecyclerAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_row, parent, false);
        return new PlaylistViewHolder(view);
    }

    PlaylistRecyclerAdapter(List<Playlist> playlist) {
        this.playlist = playlist;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistRecyclerAdapter.PlaylistViewHolder holder, int position) {
        Playlist playlist_item = playlist.get(position);
        Log.i("MAIN_LOG","Displaying url:"+playlist_item.getURL()+" | position: "+position);
        holder.urlView.setText(playlist_item.getURL());
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    void updateData(List<Playlist> data) {
        playlist.clear();
        playlist.addAll(data);
        Log.i("MAIN_LOG","Current num items:"+playlist.size());
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder{
        TextView urlView;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            urlView = itemView.findViewById(R.id.url_row);
        }
    }
}
