package com.example.app_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.ServerSocket;


public class ServerSocketService extends Service {
    public ServerSocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServerSocket
        return START_STICKY;
    }
}