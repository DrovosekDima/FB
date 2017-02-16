package com.egor.drovosek.kursv01.Misc;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class GrabTeamsRunnable implements Runnable {

    Handler mUIHandler;

    public GrabTeamsRunnable(Handler inHandler) {
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

        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        msg = mUIHandler.obtainMessage();
        bundle = new Bundle();
        bundle.putString("mydata", "Exit from GrabTeamsRunnable");
        msg.setData(bundle);
        mUIHandler.sendMessage(msg);
    };
}
