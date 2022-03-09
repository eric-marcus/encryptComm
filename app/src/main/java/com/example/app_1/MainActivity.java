package com.example.app_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int NO_1 =5321 ;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        View view = findViewById(R.id.main);

        Button buttonGoFir = findViewById(R.id.button_main_goFir);
        buttonGoFir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("head", "do button ");
                Intent intent = new Intent("dadada");
//                intent.setData(Uri.parse("https://www.baidu.com"));
                intent.putExtra("data","data test");
                startActivity(intent);
            }
        });



        if (!Settings.canDrawOverlays(this)) {
            Log.w("root", "onCreate: no right");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }

        Timer timer = new Timer("15");
        TimerTask timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                if(Looper.myLooper() == null)
                    Looper.prepare();
                onCount();
                Log.w("15", "notice");
            }
        };

        Button button = findViewById(R.id.button_main_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(timerTask.scheduledExecutionTime() == 0)
//                    timer.schedule(timerTask,3 *1000,3 *1000);
                timer.schedule(timerTask,3 *1000,3 *1000);
            }
        });
        findViewById(R.id.button_main_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int it =  timer.purge();
                Log.w("15", String.valueOf(it));
            }
        });




//        //λ表达式
//        view.setOnClickListener(v -> {
//
//            Notification.Builder builder = new Notification.Builder(this);
//            builder.setContentTitle("REMIND");
//            builder.setContentText("another 15 mins.");
//            builder.setSmallIcon(R.drawable.painting2);
//            builder.setChannelId("hw");
//            builder.setDefaults(Notification.DEFAULT_ALL);
//            Notification noti = builder.build();
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//            manager.notify("test",934,noti);
//
////            Intent intent = new Intent(MainActivity.this,FirstActivity.class);
////            Intent browser = new Intent(Intent.ACTION_VIEW);
////            browser.setData(Uri.parse("http://47.114.48.8/wordpress"));
////
//////            startActivity(geo);
//////            startActivity(intent);
////            Toast.makeText(MainActivity.this,"ss",Toast.LENGTH_LONG).show();
//        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,FirstActivity.class);
//                Intent browser = new Intent(Intent.ACTION_VIEW);
//                browser.setData(Uri.parse("http://47.114.48.8/wordpress"));
//                startActivity(browser);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this,"ss",Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onCount(){
        Log.w("noti", "onCreate: in");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int count = 4682;

        NotificationChannel channel = new NotificationChannel(String.valueOf(1), "name",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(MainActivity.this ,String.valueOf(1));

        builder.setContentTitle("REMIND");
        builder.setContentText("another 15 mins.");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.painting2);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification noti = builder.build();

        manager.notify(count,noti);

        Log.w("noti", "onCreate: done");
        Toast.makeText(MainActivity.this,"count"+count,Toast.LENGTH_SHORT).show();
    }
}