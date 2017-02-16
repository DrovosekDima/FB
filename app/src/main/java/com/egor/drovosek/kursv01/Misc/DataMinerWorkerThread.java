package com.egor.drovosek.kursv01.Misc;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class DataMinerWorkerThread{

    private static int MSG_START_HELLO = 0;
    private static int MSG_HELLO_COMPLETE = 1;

    private Handler mWorkerHandler;
    private Handler mUIHandler;
    private HandlerThread ht;
    private String TAG = "DataMinerWorkerThread";

    public DataMinerWorkerThread(String name, Handler inUIhandler)
    {
        mUIHandler = inUIhandler;

        ht = new HandlerThread(name);
        ht.start();
        Log.i(TAG, " started");

        mWorkerHandler = new Handler(ht.getLooper()){
            public void handleMessage (Message msg){
                Log.i(TAG, " handleMessage entered");
                if (msg.what == MSG_START_HELLO){
                    Message test = mUIHandler.obtainMessage();
                    Bundle bundle = new Bundle();

                    bundle.putString("mydata", "enter in run");
                    test.setData(bundle);

                    mUIHandler.sendMessage(test);

                }
                Log.i(TAG, " handleMessage exited");
            }
        };
    }

    public void postTask(Runnable task){
        Log.i(TAG, " start TASK");
        mWorkerHandler.post(task);
    }

    public void sendHello()
    {
        Log.i(TAG, " sendHello");
        mWorkerHandler.sendEmptyMessage(MSG_START_HELLO);
    }

}
