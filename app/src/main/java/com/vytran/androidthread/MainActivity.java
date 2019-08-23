package com.vytran.androidthread;

import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button button;
    private Handler mainHandler = new Handler(); //for the work can be done between different threads
    //For ex, the thread is running from 0 to 10, in 5th second, we want to modify the text of the button on UIThread
    //then, we can not do that directly because thread and UIThread are different
    //that why we need handler
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_start_thread);
    }

    public void startThread(View v){
        stopThread = false;
        ExampleThread thread = new ExampleThread(10);
        thread.start();

        //ExampleRunnable runnable = new ExampleRunnable(10);
        //new Thread(runnable).start();
    }

    public void stopThread(View v) {
        stopThread = true;
    }

    class ExampleThread extends Thread {

        int seconds;

        ExampleThread(int seconds) {
            this.seconds = seconds;
        }
        @Override
        public void run() {
            for (int i=0; i<seconds; i++) {
                Log.d(TAG, "startThread: " + i);
                //if (i == 5) {
                //button.setText("50%"); //we can not do that directly because conflict between thread and UIthread, need to use Handler

                if (stopThread)
                    return;

                if (i==5){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("50%");
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExampleRunnable implements Runnable{
        int seconds;
        ExampleRunnable(int seconds){
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i=0; i<seconds; i++) {
                Log.d(TAG, "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
