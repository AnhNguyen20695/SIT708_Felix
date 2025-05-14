package com.example.chatbotv2.firebase;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
//        Log.d("MAIN_LOG", "TOKEN: "+token);
    }

    @Override
    public void onMessageReceived(@NonNull @org.jetbrains.annotations.NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("MAIN_LOG", "Message: "+remoteMessage.getNotification().getBody());
    }
}
