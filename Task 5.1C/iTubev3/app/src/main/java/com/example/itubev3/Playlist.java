package com.example.itubev3;

public class Playlist {
    private String username;
    private String url;

    public Playlist() {}
    public Playlist(String username, String url) {
        this.username = username;
        this.url = url;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
