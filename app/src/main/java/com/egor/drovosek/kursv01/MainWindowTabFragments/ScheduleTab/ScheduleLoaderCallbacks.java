package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorAdapter;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorLoader;

import java.util.HashMap;

import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_MATCHES;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_ROUND;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class ScheduleLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;
    ScheduleCursorAdapter mAdapter;
    String TAG1 = getClass().getSimpleName().toString();

    public ScheduleLoaderCallbacks(Context inContext, ScheduleCursorAdapter inAdapater) {
        mContext = inContext;
        mAdapter = inAdapater;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG1, "onCreateLoader id=" + id);

        Loader<Cursor> cl;

        if (id == LOADER_SCHED_ROUND) {
            // group cursor
            Log.i(TAG1, "group. matches loader");
            cl = new ScheduleRoundCursorLoader(mContext);
        }
        else
        {
            // child cursor
            Log.i(TAG1, "child. rounds loader");
            HashMap<Integer, Integer> groupMap = mAdapter.getGroupMap();
            cl = new ScheduleMatchesCursorLoader(mContext, groupMap.get(id)); //TODO replace 1 with round #
        }

        return cl;

        //return new ScheduleRoundCursorLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG1, "onLoadFinished: loader id=" + loader.getId());

        // Setting the new cursor onLoadFinished. (Old cursor would be closed
        // automatically)
        int id = loader.getId();

        Log.d(TAG1, "onLoadFinished() for loader_id " + id);

        if (id == LOADER_SCHED_MATCHES) {
            // child cursor
            if (!data.isClosed()) {
                Log.d(TAG1, "data.getCount() " + data.getCount());

                HashMap<Integer, Integer> groupMap = mAdapter.getGroupMap();
                try {
                    int groupPos = groupMap.get(id);
                    Log.d(TAG1, "onLoadFinished() for groupPos " + groupPos);
                    mAdapter.setChildrenCursor(groupPos, data);
                } catch (NullPointerException e) {
                    Log.w(TAG1,
                            "Adapter expired, try again on the next query: "
                                    + e.getMessage());
                }
            }
        } else {
            mAdapter.setGroupCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called just before the cursor is about to be closed.
        int id = loader.getId();
        Log.d(TAG1, "onLoaderReset() for loader_id " + id);
        if (id != -1) {
            // child cursor
            try {
                mAdapter.setChildrenCursor(id, null);
            } catch (NullPointerException e) {
                Log.w(TAG1, "Adapter expired, try again on the next query: "
                        + e.getMessage());
            }
        } else {
            mAdapter.setGroupCursor(null);
        }
    }

}
