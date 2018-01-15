package com.example.wuguokai.firstapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wuguokai.firstapp.R;
import com.example.wuguokai.firstapp.database.DatabaseHelper;
import com.example.wuguokai.firstapp.service.TestService;
import com.example.wuguokai.firstapp.util.PhoneInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {
    private EditText editText ;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE;
    public static Activity currentActivity = null;
//    Toast toast = new Toast(this);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限请求成功的操作
                    PhoneInfo siminfo = new PhoneInfo(MainActivity.this);
                    System.out.println("getPhoneInfo:"+siminfo.getPhoneInfo());
                    Toast.makeText(currentActivity,"读取成功", Toast.LENGTH_SHORT).show();
                } else {

                    // 权限请求失败的操作
                    System.out.println("权限请求失败");
                }
                return;
            }

            // case其他权限结果。。
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        currentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText =(EditText) findViewById(R.id.edit_text);
        final Intent intent = new Intent(this, TestService.class);

        //解决在主线程使用网络的问题
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
//        intent.setAction("com.example.wuguokai.firstapp.service.TestService");

        //第一步：获取状态通知栏管理
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //第二步：实例化通知栏构造器NotificationCompat.Builder
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //第三步：对Builder进行配置
        builder//.setLargeIcon(R.drawable.icon1)
                .setSmallIcon(R.drawable.icon1)
                .setContentTitle("测试标题")//设置通知栏标题
                .setContentText("测试内容") //<span style="font-family:Arial;">/设置通知栏显示内容</span>
    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                .setDefaults(Notification.DEFAULT_VIBRATE)//添加默认震动提醒
//                .setVibrate(new long[] {0,300,500,700})//设置震动方式,延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms。
//  .setNumber(number) //设置通知集合的数量
                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
//        .setOngoing(true) // 设置为ture，表示它为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        //max:进度条最大数值  、progress:当前进度、indeterminate:表示进度是否不确定，true为不确定，如下第3幅图所示  ，false为确定下第1幅图所示
        //        功能：设置带进度条的通知，可以在下载中使用
//        .setProgress(int max, int progress,boolean indeterminate)
        ;

        //第四步：设置通知栏PendingIntent（点击动作事件等都包含在这里）

        //第五步，最简单的一部，发送通知请求
        final int mId = 1;
//        notificationManager.notify(mId, builder.build());

        Button activityButton = (Button) findViewById(R.id.btn_activity);
        Button serviceButton = (Button) findViewById(R.id.btn_service);
        Button intentButton = (Button) findViewById(R.id.btn_intent);
        Button broadcastButton = (Button) findViewById(R.id.btn_broadcast);
        Button connectButton= (Button) findViewById(R.id.btn_connect);
        Button notificationButton = (Button) findViewById(R.id.btn_notification);
        Button phoneInfoButton = (Button) findViewById(R.id.btn_phoneinfo);
        Button sqliteButton = (Button) findViewById(R.id.btn_sqlite);

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        sqliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取数据库版本
//                String query = "select sqlite_version() AS sqlite_version";
//                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
//                Cursor cursor = db.rawQuery(query, null);
//                String sqliteVersion = "";
//                if (cursor.moveToNext()) {
//                    sqliteVersion = cursor.getString(0);
//                    System.out.println("sqliteVersion:  "+sqliteVersion);
//                }
//                db.close();

                SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
                sqLiteDatabase.execSQL("delete from person");
                sqLiteDatabase.execSQL("insert into person(name, age) values('wuguokai',23)");
                Cursor cursor1 = sqLiteDatabase.rawQuery("select * from person",null);
                while (cursor1.moveToNext()){
                    System.out.println(cursor1.getInt(0)+" : "+cursor1.getString(1)+" : "+cursor1.getInt(2));
                    editText.setText(cursor1.getInt(0)+" : "+cursor1.getString(1)+" : "+cursor1.getInt(2));
                }
                cursor1.close();
                sqLiteDatabase.close();
            }
        });

//        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String osVersion = android.os.Build.MODEL + ", "
                        + Build.VERSION.SDK_INT + ", "
                        + android.os.Build.VERSION.RELEASE;
                System.out.println(osVersion);

                DisplayMetrics metrics=new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels=metrics.widthPixels;
                int heightPixels=metrics.heightPixels;
                String screenData = widthPixels+" x " +heightPixels;
                System.out.println(screenData);

                editText.setText(osVersion+"  " +screenData);

                //判断sdk是否支持
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //判断是否有该权限
                    if (checkSelfPermission(READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                        // Should we show an explanation?
                        if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                            // 显示给用户的解释
                            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                            builder.setTitle("测试提示用户");
                            builder.setMessage("是否打开权限？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{READ_PHONE_STATE}, 0);
                                    }
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        } else {
                            //请求权限,传入一个0作为requestCode，在onRequestPermissionsResult中就可以根据这个标记写请求权限后的方法
                            requestPermissions(new String[]{READ_PHONE_STATE}, 0);
                        }
                    }else {
                        PhoneInfo siminfo = new PhoneInfo(MainActivity.this);
                        System.out.println("getPhoneInfo:"+siminfo.getPhoneInfo());
                        Toast.makeText(currentActivity,"读取成功", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    PhoneInfo siminfo = new PhoneInfo(MainActivity.this);
                    System.out.println("getPhoneInfo:"+siminfo.getPhoneInfo());
                    Toast.makeText(currentActivity,"读取成功", Toast.LENGTH_SHORT).show();
//                    TelephonyManager telephonyManager = (TelephonyManager) currentActivity.getSystemService(Context.TELEPHONY_SERVICE);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                        System.out.println(telephonyManager.getAllCellInfo());
//                        System.out.println("deviceId: "+telephonyManager.getDeviceId());
//                        System.out.println(telephonyManager.getPhoneType());
//                        System.out.println(telephonyManager.getNetworkType());
//                    }
                }
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager.notify(mId, builder.build());
            }
        });
        activityButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                跳转到第二个activity
                final Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                startActivity(intent);

            }
        });

        intentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //打开浏览器并访问网页
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

        broadcastButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //发送广播消息
                final Intent intent = new Intent("com.example.wuguokai.firstapp.activity.MainActivity");
                intent.putExtra("message", "test Broadcast");
                sendBroadcast(intent);
            }
        });

        serviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //使用service
                final Intent intent = new Intent(MainActivity.this, ThreadRandomServiceDemoActivity.class);
                startActivity(intent);
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    String urlstr = "http://10.211.98.188:8081/gethello";
                    URL url = new URL(urlstr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的
//                    connection.setRequestProperty("Accept-Charset", "utf-8");  //设置编码语言
                    connection.connect();

                    if(200 == connection.getResponseCode()){
                        //得到输入流
                        InputStream is =connection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while(-1 != (len = is.read(buffer))){
                            baos.write(buffer,0,len);
                            baos.flush();
                        }
                         Toast toast = Toast.makeText(getApplicationContext(), baos.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        loadSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        saveSharedPreferences();
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
        String name = sharedPreferences.getString("Name", "WUGUOKAI");
        editText.setText(name);
    }

    private void saveSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Name", editText.getText().toString());
        editor.commit();
    }

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }
}
