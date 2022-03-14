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
        View view = findViewById(R.id.button_1);

        EditText socketInput = (EditText)  findViewById(R.id.socketInput);
        TextView socketReceive = (TextView) findViewById(R.id.socketReceive);
        Button send = (Button) findViewById(R.id.send);
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();
        Thread thread = new Thread(new SocketThread(writer,reader));
        thread.start();
        Log.w(TAG, "socket thread start ." );

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.w(TAG, "onClick: "+socketInput.getText().toString() );
                    writer.write("ok");
                    writer.flush();
                    Log.w(TAG, "onClick: flush" );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




//        startService(new Intent(this,ServerSocketService.class));
//        startService(new Intent(this,SocketService.class));


        Log.w(TAG, "onCreate: view test");
        Button button = (Button) view;
        Menu menu = findViewById(R.id.first_menu);
        EditText text1 = (EditText) findViewById(R.id.Text1);
        TextView text2 = (TextView) findViewById(R.id.Text2);



        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                socketReceive.setText(intent.getStringExtra("info"));
            }
        };
        localBroadcastManager.registerReceiver(receiver,new IntentFilter("receive"));




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
        PipedWriter writer;
        PipedReader reader;
        Thread threadSend;
        Thread threadRece;

        public SocketThread(PipedWriter w, PipedReader r){
            writer = w;
            reader = r;
        }

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


                threadRece = new Thread(new Receive(socket,"Client",reader));
                threadSend = new Thread(new Send(socket,"Client", writer));
                threadRece.start();
                threadSend.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    class Receive implements Runnable{
        private Socket socket;
        private String role;
        private PipedWriter w = new PipedWriter();
        Receive(Socket socket, String role , PipedReader r) throws IOException {
            System.out.println("here");
            this.socket = socket;
            this.role = role;
            this.w.connect(r);
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
        private String role;
        private PipedReader r = new PipedReader();
        Send(Socket socket, String role, PipedWriter w) throws IOException {
            this.socket = socket;
            this.role = role;
            this.r.connect(w);
            Log.w(TAG, "Send: bind" );
        }

        @Override
        public void run() {
            int i = 0;
            String str="";
            Log.w(TAG, "run: "+socket.toString() );
            try {
                while(true) {
                    if (r.ready()) {
                        r.read();
                        r.read();
                        str = ((EditText) findViewById(R.id.socketInput)).getText().toString();
                        Log.w(TAG, "str " + str);
                        DataOutputStream out = null;

                        out = new DataOutputStream(socket.getOutputStream());
                        Log.w(TAG, "260: " + str);
                        out.writeUTF(str);
                        continue;
                    }
                }
            } catch (IOException  e) {
                e.printStackTrace();
            }
        }
    }

}
