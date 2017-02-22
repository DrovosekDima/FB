package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class ScheduleRoundCursorLoader extends CursorLoader {

    FootballDBHelper db;
    Context mContext;
    String TAG = getClass().getSimpleName().toString();

    public ScheduleRoundCursorLoader(Context context) {
        super(context);
        mContext = context;
        db = new FootballDBHelper(mContext);
    }

    @Override
    public Cursor loadInBackground() {
        Log.i(TAG, "loadInBackground - enter");
        Cursor cursor = db.getRounds(MainActivity.gdSeason);
        String numOfRounds = "null";
        if (cursor != null)
            numOfRounds = String.valueOf(cursor.getCount());

        Log.i(TAG, "loadInBackground: num of rounds = " + numOfRounds);
        return cursor;
    }

}