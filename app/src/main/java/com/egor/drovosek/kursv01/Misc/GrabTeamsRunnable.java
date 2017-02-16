package com.egor.drovosek.kursv01.Misc;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.DataMiner;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class GrabTeamsRunnable implements Runnable {

    private final String TAG = "GrabTeamsRunnable";
    Handler mUIHandler;
    Context mContext;

    public GrabTeamsRunnable(Handler inHandler, Context inContext)
    {
        mContext = inContext;
        mUIHandler = inHandler;
    }

    @Override
    public void run()
    {
        Message msg = mUIHandler.obtainMessage();
        Bundle bundle = new Bundle();

        bundle.putString("mydata", "enter in GrabTeamsRunnable");
        msg.setData(bundle);

        mUIHandler.sendMessage(msg);

        Log.d(TAG, "new DataMiner");

        DataMiner dm = new DataMiner(mContext);
        //todo add season;
        Log.d(TAG, "grab team for ");
        dm.grabTeam(2016);
        Log.d(TAG, "finished: grab team");

        msg = mUIHandler.obtainMessage();
        bundle = new Bundle();
        bundle.putString("mydata", "Exit from GrabTeamsRunnable");
        msg.setData(bundle);
        mUIHandler.sendMessage(msg);
    };
}
