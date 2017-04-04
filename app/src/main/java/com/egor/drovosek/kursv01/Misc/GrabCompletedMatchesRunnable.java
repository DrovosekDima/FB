package com.egor.drovosek.kursv01.Misc;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

/**
 * Created by Drovosek on 16/02/2017.
 */

public class GrabCompletedMatchesRunnable implements Runnable {

    private final String TAG = "GrabMatchesWithGoals";
    Handler mUIHandler;
    Context mContext;
    private FootballDBHelper mDB;

    public GrabCompletedMatchesRunnable(Handler inHandler, Context inContext)
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

        //grabCompletedMatches - скачивает данные с football.by и кладет их в базу
        // перед скачиванием проверяет есть ли загруженные матчи и скачивает только новые
        dm.grabCompletedMatches(MainActivity.gdSeason);

        // сообщить MainActivity, что загрузка данных с сайта закончилась
        // и можно обновить список комманд в меню
        mUIHandler.sendEmptyMessage(MainActivity.GRAB_MATCHES_COMPLETED);

        Log.d(TAG, "finished: grab matches");
     };
}
