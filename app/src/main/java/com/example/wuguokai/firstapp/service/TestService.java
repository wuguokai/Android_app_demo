package com.example.wuguokai.firstapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by WUGUOKAI on 2017/9/26.
 */

public class TestService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreat", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        double i = Math.random();
        String msg = "随机数: "+String.valueOf(i);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
