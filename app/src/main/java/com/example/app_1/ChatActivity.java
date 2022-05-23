package com.example.app_1;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {
    protected String TAG = "firstActivity";
    //    private String ip="bigcat.tech";
    private String ip = "192.168.0.104";
    private int port = 5886;
    EditText socketInput;
    TextView socketReceive;
    Button send;
    Socket socket ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_chat);

        socketInput = (EditText)  findViewById(R.id.socketInput);
        socketReceive = (TextView) findViewById(R.id.socketReceive);
        socketReceive.setMovementMethod(ScrollingMovementMethod.getInstance());
        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketReceive.setText(socketReceive.getText()+"\nMe:"+socketInput.getText());
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(socketInput.getText().toString());
                            out.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                socketInput.setText("");
            }
        });

        new Thread(new SocketThread()).start();

//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                    out.writeUTF(socketInput.getText().toString());
//                    out.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        //        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        Intent intent = new Intent("socketSend");



//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                socketReceive.setText(intent.getStringExtra("info"));
//            }
//        };
//        localBroadcastManager.registerReceiver(receiver,new IntentFilter("receive"));
//
//        socketThread.start();
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.putExtra("send",socketInput.getText().toString());
//                localBroadcastManager.sendBroadcast(intent);
//            }
//        });





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

//        text1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    String temp = new String(s.toString());
//                    Log.w(TAG, temp );
//                    finalAlgorithm.setInfo(temp);
//                    String output = new String((byte[]) finalAlgorithm.encrypt());
//
//                    text2.setText(output);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    String de = new String((byte[]) finalAlgorithm.decrypt());
//                    Log.w(TAG, de);
//                    text2.setText(de);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("^^^^^^^^^^^^^^", "onStart: ");
    }

    class SocketThread implements Runnable{
        @Override
        public void run(){
            try {
                socket = new Socket(ip,port);




//                ChatActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                try {
//                                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                                    Log.w("&&&&&&&&&&", socketInput.getText().toString());
//                                    out.writeUTF(socketInput.getText().toString());
//                                    out.flush();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                });


                socket.setSoTimeout(200 * 1000);
                Log.w(TAG, "socket connected");


                new Thread(new Receive(socket)).start();
//                threadSend = new Thread(new Send(socket));
//                threadSend.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






    class Receive implements Runnable{
        private Socket socket;
        Receive(Socket socket) throws IOException {
            this.socket = socket;
        }
        @Override
        public void run(){
            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                while (true){
                    String receive = input.readUTF();
                    Log.w(TAG, receive );

                    if(!receive.equals(null)){
                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketReceive.setText(socketReceive.getText()+"\n"+receive);
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



//    class Send implements Runnable{
//        private Socket socket;
//        //        private LocalBroadcastManager broadcastManager;
////        BroadcastReceiver broadcastReceiver;
//        Send(Socket socket) throws IOException {
//            this.socket = socket;
////            broadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
////            broadcastManager.registerReceiver(broadcastReceiver,new IntentFilter("socketSend"));
//            Log.w(TAG, "SendThread : init" );
//        }
//
//        @Override
//        public void run() {
//            Log.w(TAG, "SendThread socket id run: " + socket.toString());
//            String str = socketInput.getText().toString();
//            Log.w("send thread run:", str);
//            try {
////                OutputStream outputStream = socket.getOutputStream();
////                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                out.writeUTF(str);
//                out.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////            broadcastReceiver = new BroadcastReceiver() {
////                @Override
////                public void onReceive(Context context, Intent intent) {
//////                    String str = intent.getStringExtra("send");
////                    String str = socketInput.getText().toString();
////                    Log.w(TAG, "str " + str);
////                    try {
////                        DataOutputStream out = null;
////                        out = new DataOutputStream(socket.getOutputStream());
////                        Log.w(TAG, "260: " + str);
////                        out.writeUTF(str);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////
////                }
////            };
//        }
//    }
}