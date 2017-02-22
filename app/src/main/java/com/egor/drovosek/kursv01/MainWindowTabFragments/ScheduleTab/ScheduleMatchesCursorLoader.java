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

public class ScheduleMatchesCursorLoader extends CursorLoader {

    FootballDBHelper db;
    Context mContext;
    String TAG = getClass().getSimpleName().toString();
    int mRound;

    public ScheduleMatchesCursorLoader(Context context, int inRound) {
        super(context);
        mContext = context;
        db = new FootballDBHelper(mContext);
        mRound = inRound;
    }

    @Override
    public Cursor loadInBackground() {
        Log.i(TAG, "loadInBackground - enter");
        Cursor cursor = db.getMatchesSeasonRound(MainActivity.gdSeason, mRound);
        String numOfMatches = "null";
        if (cursor != null)
            numOfMatches = String.valueOf(cursor.getCount());

        Log.i(TAG, "loadInBackground: num of matches = " + numOfMatches);
        return cursor;
    }

}