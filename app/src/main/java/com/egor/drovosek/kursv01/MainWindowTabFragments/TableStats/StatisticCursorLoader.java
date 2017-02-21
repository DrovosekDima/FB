package com.egor.drovosek.kursv01.MainWindowTabFragments.TableStats;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class StatisticCursorLoader extends CursorLoader {

    FootballDBHelper db;
    Context mContext;
    String TAG = "Stats.Cursor.loader";

    public StatisticCursorLoader(Context context) {
        super(context);
        Log.i(TAG, "constructor");
        mContext = context;
        db = new FootballDBHelper(mContext);
    }

    @Override
    public Cursor loadInBackground() {
        Log.i(TAG, "loadInBackground - enter");
        Cursor cursor = db.getStats(MainActivity.gdSeason);
        Log.i(TAG, "loadInBackground - Exit");
        return cursor;
    }

}