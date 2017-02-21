package com.egor.drovosek.kursv01.MainWindowTabFragments.TableStats;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorAdapter;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorLoader;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class StatisticLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;
    StatisticCursorAdapter mAdapter;
    String TAG = "StatsLoaderCallbacks";

    public StatisticLoaderCallbacks(Context inContext, StatisticCursorAdapter inAdapater) {
        mContext = inContext;
        mAdapter = inAdapater;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader id=" + id);
        return new StatisticCursorLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "onLoadFinished: loader id=" + loader.getId());
        Log.i(TAG, "onLoadFinished - swap in a cursor with " + cursor.getCount() +
                " elements.");
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
