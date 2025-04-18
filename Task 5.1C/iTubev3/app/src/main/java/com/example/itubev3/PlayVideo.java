package com.example.itubev3;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class PlayVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        WebView webView = findViewById(R.id.web_view);
//        String video = "<iframe width=\"100%\" height=\"100%\" src=\""+url+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }
}