package com.egor.drovosek.kursv01.Misc;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class GrabMatchesWithGoalsRunnable implements Runnable {

    private final String TAG = "GrabMatchesWithGoals";
    Handler mUIHandler;
    Context mContext;
    private FootballDBHelper mDB;

    public GrabMatchesWithGoalsRunnable(Handler inHandler, Context inContext)
    {
        mContext = inContext;
        mUIHandler = inHandler;
    }

    @Override
    public void run()
    {
        Log.d(TAG, "new DataMiner");
        mDB = new FootballDBHelper(mContext);
        mDB.close();

        Log.d(TAG, "grab team for ");
        DataMiner dm = new DataMiner(mContext);
        dm.grabAllMatches(MainActivity.gdSeason);

        // сообщить MainActivity, что загрузка данных с сайта закончилась
        // и можно обновить список комманд в меню
        mUIHandler.sendEmptyMessage(MainActivity.GRAB_MATCHES_COMPLETED);

        Log.d(TAG, "finished: grab matches");
     };
}
