package com.carrus.carrusshipper.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static String TAG = MyService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;
    private String bookingId = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (!isRunning) {
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        bookingId = intent.getStringExtra("bookingId");
        Log.d(TAG, "onStart With id " + bookingId);
        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage() {
        Log.d(TAG, "I AM EXCUTED");
    }

    class MyThread extends Thread {
        static final long DELAY = 5000;

        @Override
        public void run() {
            while (isRunning) {
                Log.d(TAG, "Running");
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

}