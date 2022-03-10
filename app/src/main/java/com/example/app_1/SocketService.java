package com.example.app_1;

import static android.app.AlarmManager.RTC;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketService extends Service {

    private static String ip="47.118.48.8";
    //    private static String ip = "bigcat.tech";
    private static int port=7501;
    protected String TAG = "SocketService";

    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.w(TAG, "onStartCommand: start" );
        Thread thread = new Thread(new SocketThread());
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    class SocketThread implements Runnable{
        public void run(){
//            Socket socket = null;
//            try {
//                socket = new Socket(ip,port);
//                socket.setSoTimeout(200 * 1000);
//                Log.w(TAG, "socket connected");
//                Runnable runnable = new Receive(socket,"Client");
//                Runnable runnable1 = new Send(socket,"Client");
//                Thread thread = new Thread(runnable);
//                Thread thread1 = new Thread(runnable1);
//                thread.start();
//                thread1.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}