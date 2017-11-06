package com.example.wuguokai.firstapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.wuguokai.firstapp.activity.ThreadRandomServiceDemoActivity;

/**
 * Created by WUGUOKAI on 2017/9/26.
 */

public class RandomService extends Service {

    private Thread workThread ;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("click", "onCreat");
        Toast.makeText(this, "onCreat", Toast.LENGTH_SHORT).show();
        workThread = new Thread(null, backgroundWork, "WorkThread");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        if (!workThread.isAlive()){
            workThread.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        workThread.interrupt();
    }

    private Runnable backgroundWork = new Runnable() {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()){
                    double randomDouble = Math.random();
                    ThreadRandomServiceDemoActivity.UpdateGUI(randomDouble);
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
