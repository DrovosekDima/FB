package com.egor.drovosek.kursv01.Misc;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

import java.util.List;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class GrabScheduleRunnable implements Runnable {

    private final String TAG = "GrabScheduleRunnable";
    Handler mUIHandler;
    Context mContext;
    private FootballDBHelper mDB;

    public GrabScheduleRunnable(Handler inHandler, Context inContext)
    {
        mContext = inContext;
        mUIHandler = inHandler;
    }

    @Override
    public void run()
    {
        Log.d(TAG, "new DataMiner");
        mDB = new FootballDBHelper(mContext);
        int numRounds = mDB.getNumberRounds(MainActivity.gdSeason);
        mDB.close();

        Log.d(TAG, "grab scheduler for season " + MainActivity.gdSeason);
        DataMiner dm = new DataMiner(mContext);

        // проверяем есть ли календарь(расписание) игр
        // если нет - то
        // идем на страничку
        // http://football.by/stat/belarus/2017/schedule.html
        // скачиваем все расписание, без голов

        if (numRounds == 0)
           dm.grabSchedule(MainActivity.gdSeason);

        // сообщить MainActivity, что загрузка данных с сайта закончилась
        // и можно обновить список комманд в меню
        mUIHandler.sendEmptyMessage(MainActivity.GRAB_SCHEDULE_COMPLETED);

        Log.d(TAG, "finished: grab schedule");
     };
}
