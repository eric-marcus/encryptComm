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

import java.io.UnsupportedEncodingException;

public class FirstActivity extends AppCompatActivity {
    protected String TAG = "firstActivity";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_layout);
        View view = findViewById(R.id.button_1);

        startService(new Intent(getBaseContext(),SocketService.class));

//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");
//        Log.e(TAG, data );

        Log.d(TAG, "onCreate: view test");
        Button button = (Button) view;
        Menu menu = findViewById(R.id.first_menu);
        EditText text1 = (EditText) findViewById(R.id.Text1);
        TextView text2 = (TextView) findViewById(R.id.Text2);


        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver receiver = new BroadcastReceiver();
        localBroadcastManager.registerReceiver(receiver,new IntentFilter("service"));




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

//    protected View rewrite(View view){
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FirstActivity.this,"ss",Toast.LENGTH_LONG).show();
//                DES des = new DES();
//
//            }
//        });
//        return view;
//    }


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

//    public void startService(View view){
//        startService(new Intent(getBaseContext(),SocketService.class));
//    }
//
//    public void stopService(View view){
//        stopService(new Intent(getBaseContext(),SocketService.class));
//    }


    private class BroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "onReceive: " );
            String info = intent.getStringExtra("info");
            Log.w(TAG, info );
        }
    }
}
