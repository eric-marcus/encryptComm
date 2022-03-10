package com.example.app_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class ServerSocketService extends Service {
    private static int port = 7500;
    private ServerSocket server = null;
    protected String TAG = "ServerSocket";

    public ServerSocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new ServerSocketThread());
        thread.start();
        return START_STICKY;
    }


    class ServerSocketThread implements Runnable{
        @Override
        public void run(){
//            try {
//                server = new ServerSocket(port);
//                Log.w(TAG, "server done");
//                Socket socket = null;
//                socket = server.accept();
//                Log.w(TAG, "accept");
//                socket.setSoTimeout(200 * 1000);
//                InetAddress ip = socket.getInetAddress();
//                Log.w(TAG, "ip"+ip);
//
//                Runnable runnable = Receive(socket,"Server");
//                Thread thread = new Thread(runnable);
//                Thread thread1 = new Thread(new Send(socket,"Server"));
//                thread.start();
//                thread1.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }}
}
