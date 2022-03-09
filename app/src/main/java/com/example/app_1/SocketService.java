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

public class SocketService extends Service {
    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.w("sdfsdf", "onStartCommand:" );
        Intent intent1 = new Intent("service");
        intent1.putExtra("info","hi from service");

//        this.sendBroadcast(intent1);
//        this.getBaseContext().sendBroadcast(intent1);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent1);


//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent2 = new Intent("service");
//        intent2.putExtra("info","delay info");
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME, (long)(SystemClock.elapsedRealtime()+2000), PendingIntent.getBroadcast(this,0,intent2,0));

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}