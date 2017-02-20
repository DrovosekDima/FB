package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

//import android.app.Fragment;
//import android.app.LoaderManager;
import android.content.Context;
//import android.content.CursorLoader;
//import android.content.Loader;
//import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
//import android.widget.SimpleCursorAdapter;
//import android.app.LoaderManager.LoaderCallbacks;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.Fragment;


import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.MainActivity;
import com.egor.drovosek.kursv01.R;

import static com.egor.drovosek.kursv01.MainActivity.gdSeason;
import static com.egor.drovosek.kursv01.R.id.container;


public class BestPlayersTabFragment extends Fragment implements LoaderCallbacks<Cursor>
{

    FootballDBHelper mDB;
    Context context;
    ListView lvData;
    CursorAdapterWithImage scAdapter;
    public String TAG = "BestPlayer";
    public static final int LOADER_BESTPLAYER = 1;

    static class MyCursorLoader extends CursorLoader {

        FootballDBHelper db;

        public MyCursorLoader(Context context, FootballDBHelper db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Log.i("BestPlayer", "loadInBackground - enter");
            Cursor cursor = db.getBestPlayers(MainActivity.gdSeason);
            Log.i("BestPlayer", "loadInBackground - Exit");
            return cursor;
        }

    }

    //=============================begin implementation of callback
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        Log.i("BestPlayer", "onCreateLoader: with id=" + id);
        return new MyCursorLoader(context, mDB);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("BestPlayer", "onLoadFinished: loader id=" + loader.getId());
        Log.i("BestPlayer", "onLoadFinished - swap in a cursor with " + cursor.getCount() +
                            "elements.");
        scAdapter.swapCursor(cursor);
        scAdapter.notifyDataSetChanged();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
    //=============================end  implementation of callback

    public class CursorAdapterWithImage extends SimpleCursorAdapter
    {
        public Cursor cursor;
        private Context context;

        public CursorAdapterWithImage(Context inContext, int layout, Cursor inCursor, String[] from, int[] to)
        {
            super(inContext, layout, inCursor, from, to);
            Log.i("BestPlayer", "CursorAdapterWithImage constructor with empty cursor");
            cursor = inCursor;
            context = inContext;
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            cursor = c;
            return super.swapCursor(c);
        }

        public View getView(int pos, View inView, ViewGroup parent)
        {
            View row = inView;

            if (row == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.table_bestplayers_row, null);
            }


            if (cursor != null) {
                cursor.moveToPosition(pos);

                TextView columnFIO;
                ImageView columnLogo;
                TextView columnTeamName;
                TextView columnGoals;
                TextView columnRank;
                String data;

                columnFIO = (TextView) row.findViewById(R.id.colFIOBP);
                columnLogo = (ImageView) row.findViewById(R.id.colLogoBP);
                columnTeamName = (TextView) row.findViewById(R.id.colTeamNameBP);
                columnRank = (TextView) row.findViewById(R.id.colRankBP);
                columnGoals = (TextView) row.findViewById(R.id.colGoalsBP);

                data = String.valueOf(pos + 1);
                columnRank.setText(data);

                data = cursor.getString(cursor.getColumnIndex("teamName"));
                columnTeamName.setText(data);


                data = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndex("second_name"));
                columnFIO.setText(data);

                data = String.valueOf(cursor.getInt(cursor.getColumnIndex("numberOfGoals")));
                columnGoals.setText(data);

                byte[] byteLogo = cursor.getBlob(cursor.getColumnIndex("logo"));
                Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0, byteLogo.length);
                columnLogo.setImageBitmap(logo);

                if ((pos & 1) != 0) //odd
                    row.setBackgroundColor(Color.LTGRAY);
            }

            return (row);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "BestPlayers::onCreate()");

        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(context);

        // формируем столбцы сопоставления

        String[] from = new String[] {  "_id", "first_name",
                                        "logo",
                                        "teamName",
                                        "numberOfGoals"};

        int[] to = new int[] {  R.id.colFIOBP,
                                R.id.colLogoBP,
                                R.id.colTeamNameBP,
                                R.id.colGoalsBP};

        // создаем адаптер и настраиваем список
        scAdapter = new CursorAdapterWithImage(context, R.layout.tab_frag_bestplayers, null, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "BestPlayers::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_frag_bestplayers, container, false);

        lvData = (ListView) view.findViewById(R.id.lvBestPlayer);
        lvData.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        Log.i("Debug", "BestPlayers::создаем лоадер для чтения данных");
        getActivity().getSupportLoaderManager().initLoader(LOADER_BESTPLAYER, null, this);

        /*TableLayout table = (TableLayout) view.findViewById(R.id.bestPlayersTable);
        View row;
        TextView columnFIO;
        ImageView columnLogo;
        TextView columnTeamName;
        TextView columnGoals;
        TextView columnRank;

        mDB = new FootballDBHelper(context);

        Cursor curs = mDB.getBestPlayers(gdSeason);


        if (curs != null && curs.getCount()>0)
        {
            int sizeCurs = curs.getCount();
            boolean odd = true;
            String data;

            curs.moveToFirst();
            for(int i =0; i< sizeCurs; i++)
            {
                row = getActivity().getLayoutInflater().inflate(R.layout.table_bestplayers_row, null);

                columnFIO = (TextView) row.findViewById(R.id.colFIOBP);
                columnLogo = (ImageView) row.findViewById(R.id.colLogoBP);
                columnTeamName = (TextView) row.findViewById(R.id.colTeamNameBP);
                columnRank = (TextView) row.findViewById(R.id.colRankBP);
                columnGoals = (TextView) row.findViewById(R.id.colGoalsBP);

                data = String.valueOf(i+1);
                columnRank.setText(data);

                data = curs.getString(curs.getColumnIndex("teamName"));
                columnTeamName.setText(data);


                data = curs.getString(curs.getColumnIndex("first_name")) + " " +
                        curs.getString(curs.getColumnIndex("second_name"));
                columnFIO.setText(data);

                data = String.valueOf(curs.getInt(curs.getColumnIndex("numberOfGoals")));
                columnGoals.setText(data);

                byte[] byteLogo = curs.getBlob(curs.getColumnIndex("logo"));
                Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0 ,byteLogo.length);
                columnLogo.setImageBitmap(logo);

                if (odd)
                {
                    row.setBackgroundColor(Color.LTGRAY);
                    odd = false;
                }
                else {
                    odd = true;
                }

                table.addView(row);

                curs.moveToNext();
            }

            curs.close();
        }

        mDB.close();*/

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Debug", "BestPlayers::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i("Debug", "BestPlayers::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDB.close();
        Log.i("Debug", "BestPlayers::onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Debug", "BestPlayers::onDestroyView()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Debug", "BestPlayers::onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Debug", "BestPlayers::onStop()");
    }
}

/*
* public class ExampleFragmen extends Fragment { // Don't implement LoaderCallbacks here.

    private static final int LOADER_A = 0;
    private static final int LOADER_B = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        getLoaderManager().restartLoader(LOADER_A, null, new LoaderACallbacks());
        getLoaderManager().restartLoader(LOADER_B, null, new LoaderBCallbacks());
        ...
    }

    public class LoaderACallbacks implements LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int loader, Bundle args) {
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            // Set up adapter A here...
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

    }

    public class LoaderBCallbacks implements LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int loader, Bundle args) {
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            // Set up adapter B here...
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

    }

}
* */