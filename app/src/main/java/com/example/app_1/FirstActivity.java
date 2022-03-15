package com.example.app_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FirstActivity extends AppCompatActivity {
    protected String TAG = "firstActivity";
//    private String ip="bigcat.tech";
    private String ip = "192.168.43.75";
    private int port = 5886;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        EditText socketInput = (EditText)  findViewById(R.id.socketInput);
        TextView socketReceive = (TextView) findViewById(R.id.socketReceive);
        Button send = (Button) findViewById(R.id.send);
        Button button = (Button) findViewById(R.id.button_1);
        Menu menu = findViewById(R.id.first_menu);
        EditText text1 = (EditText) findViewById(R.id.Text1);
        TextView text2 = (TextView) findViewById(R.id.Text2);

        Thread socketThread = new Thread(new SocketThread());
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("socketSend");



        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                socketReceive.setText(intent.getStringExtra("info"));
            }
        };
        localBroadcastManager.registerReceiver(receiver,new IntentFilter("receive"));

        socketThread.start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("send",socketInput.getText().toString());
                localBroadcastManager.sendBroadcast(intent);

            }
        });





        Algorithm algorithm = null;
        try {
            algorithm = (Algorithm) Algorithm.getAlgorithm("com.example.app_1.DES");
            algorithm.setNoise("testok");
            algorithm.setFunName("DES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DES des = new DES();

        Algorithm finalAlgorithm = algorithm;

        text1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String temp = new String(s.toString());
                    Log.w(TAG, temp );
                    finalAlgorithm.setInfo(temp);
                    String output = new String((byte[]) finalAlgorithm.encrypt());

                    text2.setText(output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String de = new String((byte[]) finalAlgorithm.decrypt());
                    Log.w(TAG, de);
                    text2.setText(de);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.app_bar_item:
                Toast.makeText(this,"finish",Toast.LENGTH_LONG);
                finish();
                break;
        }
        return true;
    }






    class SocketThread implements Runnable{
        Thread threadSend;
        Thread threadRece;


        @Override
        public void run(){
            Log.w(TAG, "run: " );
            Socket socket = null;
            try {
                socket = new Socket(ip,port);
                Log.w(TAG, "run: "+socket.toString() );
                if(socket == null)
                    Log.w(TAG, "run: no connect" );
                else {
                    Log.w(TAG, "run: connected" );
                }

                socket.setSoTimeout(200 * 1000);
                Log.w(TAG, "socket connected");


                threadRece = new Thread(new Receive(socket));
                threadSend = new Thread(new Send(socket));
                threadRece.start();
                threadSend.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    class Receive implements Runnable{
        private Socket socket;
        Receive(Socket socket) throws IOException {
            System.out.println("here");
            this.socket = socket;
        }

        @Override
        public void run(){
            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
//            DES des = new DES("pass");
                TextView display = (TextView) findViewById(R.id.socketReceive);
                while (true){
                    String receive = input.readUTF();

                    Intent intent = new Intent("receive");
                    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
                    intent.putExtra("info", receive);
                    broadcastManager.sendBroadcast(intent);


//                    if(!receive.isEmpty()){
//                        w.write(receive);
//                        display.setText(receive);
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    class Send implements Runnable{
        private Socket socket;
        private LocalBroadcastManager broadcastManager;
        BroadcastReceiver broadcastReceiver;
        Send(Socket socket) throws IOException {
            this.socket = socket;
            broadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
            broadcastManager.registerReceiver(broadcastReceiver,new IntentFilter("socketSend"));
            Log.w(TAG, "SendThread : init" );
        }

        @Override
        public void run() {
            Log.w(TAG, "SendThread socket id run: " + socket.toString());

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String str = intent.getStringExtra("send");
                    Log.w(TAG, "str " + str);
                    try {
                        DataOutputStream out = null;
                        out = new DataOutputStream(socket.getOutputStream());
                        Log.w(TAG, "260: " + str);
                        out.writeUTF(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
        }
    }
}