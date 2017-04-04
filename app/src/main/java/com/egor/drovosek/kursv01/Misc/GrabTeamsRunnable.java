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

public class GrabTeamsRunnable implements Runnable {

    private final String TAG = "GrabTeamsRunnable";
    Handler mUIHandler;
    Context mContext;
    private FootballDBHelper mDB;

    public GrabTeamsRunnable(Handler inHandler, Context inContext)
    {
        mContext = inContext;
        mUIHandler = inHandler;
    }

    @Override
    public void run()
    {
        Log.d(TAG, "new DataMiner ");
        mDB = new FootballDBHelper(mContext);
        List<Team> teams = mDB.getListTeams(MainActivity.gdSeason);
        mDB.close();

        //скачиваем список комманд, если они отсутствуют
        if(teams.isEmpty()) {
            Log.d(TAG, "grab team for " + MainActivity.gdSeason + " season");
            DataMiner dm = new DataMiner(mContext);
            //dm.grabTeam(MainActivity.gdSeason);
            dm.grabTeamFromFootballby(MainActivity.gdSeason);

            // сообщить MainActivity, что загрузка данных с сайта закончилась
            // и можно обновить список комманд в меню
            mUIHandler.sendEmptyMessage(MainActivity.GRAB_TEAM_COMPLETED);

            Log.d(TAG, "finished: grab team");
        }else
            Log.d(TAG, "Grabbing skipped, there are " + teams.size() + "teams in DB.");

    };
}
