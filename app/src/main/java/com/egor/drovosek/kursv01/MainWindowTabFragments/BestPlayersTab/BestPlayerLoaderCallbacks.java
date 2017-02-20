package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class BestPlayerLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;
    BestPlayerCursorAdapter mAdapter;

    public BestPlayerLoaderCallbacks(Context inContext, BestPlayerCursorAdapter inAdapater) {
        mContext = inContext;
        mAdapter = inAdapater;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("BestPlayerLoaderCall", "onCreateLoader id=" + id);
        return new BestPlayerCursorLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("BestPlayerLoaderCall", "onLoadFinished: loader id=" + loader.getId());
        Log.i("BestPlayerLoaderCall", "onLoadFinished - swap in a cursor with " + cursor.getCount() +
                " elements.");
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
