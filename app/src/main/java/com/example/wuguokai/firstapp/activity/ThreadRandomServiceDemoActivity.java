package com.example.wuguokai.firstapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuguokai.firstapp.R;
import com.example.wuguokai.firstapp.service.RandomService;

/**
 * Created by WUGUOKAI on 2017/9/26.
 */

public class ThreadRandomServiceDemoActivity extends Activity {
    private static Handler handler = new Handler();
    private static TextView labelView = null;
    private static double randomDouble;

    public static void UpdateGUI(double refreshDouble){
        randomDouble = refreshDouble;
        handler.post(RefreshLable);
    }

    private static Runnable RefreshLable = new Runnable() {
        @Override
        public void run() {
            labelView.setText(String.valueOf(randomDouble));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        labelView = findViewById(R.id.label);
        Button startBuntton = findViewById(R.id.btn_start);
        final Button stopBuntton = findViewById(R.id.btn_stop);
        final Intent serviceIntent = new Intent(this, RandomService.class);

        startBuntton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                startService(serviceIntent);
//                Toast.makeText("click", Toast.LENGTH_SHORT).show();
                Log.i("click", "click");
            }
        });

        stopBuntton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopService(serviceIntent);
            }
        });
    }
}
