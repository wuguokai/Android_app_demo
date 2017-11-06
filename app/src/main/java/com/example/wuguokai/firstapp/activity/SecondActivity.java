package com.example.wuguokai.firstapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wuguokai.firstapp.R;

public class SecondActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final Activity activity = this;

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity instanceof SecondActivity){
                    Log.i("activity", "finish");
                    activity.finish();
                }
            }
        });


        dialog = new ProgressDialog(this);
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);

        new MyAsyncTask().execute();
    }


    class MyAsyncTask extends AsyncTask<Void,Integer,Boolean> {
        private int counter=0;

        //当我们调用了execute()，即开始执行异步任务时调用，做一些初始化操作
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        //在这里处理所有的耗时任务，会在子线程中执行，返回的结果会传递给onPostExecute()中的参数
        @Override
        protected Boolean doInBackground(Void... voids) {
            while (true){
                counter++;
                //获取当前任务进度，会执行onProgressUpdate，更新状态
                publishProgress(counter);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (counter>=100){
                    break;
                }
            }
            return true;
        }

        //处理来自publishProgress的信息
        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

        //对doInBackground返回的结果进行处理
        @Override
        protected void onPostExecute(Boolean b) {
            if (b){
                dialog.dismiss();
            }
        }
    }

}