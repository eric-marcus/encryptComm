package com.example.app_1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.nio.charset.StandardCharsets;

public class ChatActivity extends AppCompatActivity {
    protected String TAG = "firstActivity";
    //    private String ip="bigcat.tech";
    private String ip = "192.168.0.104";
    private int port = 5886;
    EditText socketInput;
    TextView socketReceive;
    Button send;
    Socket socket ;
    Algorithm algorithm;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_chat);

        Intent intent = getIntent();
        try {
            algorithm = Common.getAlgorithm(intent.getStringExtra("sEncrypt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(ChatActivity.this, intent.getStringExtra("sEncrypt"), Toast.LENGTH_SHORT).show();
//        Toast.makeText(ChatActivity.this, intent.getStringExtra("asEncrypt"), Toast.LENGTH_SHORT).show();
//        Toast.makeText(ChatActivity.this, intent.getStringExtra("encode"), Toast.LENGTH_SHORT).show();

        socketInput = (EditText)  findViewById(R.id.socketInput);
        socketReceive = (TextView) findViewById(R.id.socketReceive);
        socketReceive.setMovementMethod(ScrollingMovementMethod.getInstance());
        send = (Button) findViewById(R.id.send);



        socketInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(""))
                    send.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(""))
                    send.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals(""))
                    send.setEnabled(false);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketReceive.setText(socketReceive.getText()+"\nMe:"+socketInput.getText());
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            if(token == null) {
                                token = socketInput.getText().toString();
                                algorithm.setNoise(token);
                                Log.w("token&&&&", token);
                            }
//                            Common.enBase64((byte[]) algorithm.encrypt(socketInput.getText().toString()));
                            out.writeUTF(Common.enBase64((byte[]) algorithm.encrypt(socketInput.getText().toString())));
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                socketInput.setText("");
            }
        });

        new Thread(new SocketThread()).start();





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
                socket.setSoTimeout(120 * 1000);
                Log.w(TAG, "socket connected");


                new Thread(new Receive(socket)).start();
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
                    String receive = new String((byte[]) algorithm.decrypt(Common.deBase64(input.readUTF().getBytes(StandardCharsets.UTF_8))));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}