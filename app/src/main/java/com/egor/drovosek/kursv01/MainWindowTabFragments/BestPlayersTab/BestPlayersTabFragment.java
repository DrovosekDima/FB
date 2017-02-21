package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.support.v4.app.Fragment;


import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.R;


public class BestPlayersTabFragment extends Fragment
{

    FootballDBHelper mDB;
    public Context context;
    ListView lvData;
    public BestPlayerCursorAdapter scAdapter;
    public String TAG = "BestPlayer";
    public static final int LOADER_BESTPLAYER = 1;
    public static final int LOADER_STATISTICS = 2;

    public Context getContext()
    {
        return context;
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
        scAdapter = new BestPlayerCursorAdapter(context, R.layout.tab_frag_bestplayers, null, from, to);
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
        getActivity().getSupportLoaderManager().initLoader(LOADER_BESTPLAYER, null,
                 new BestPlayerLoaderCallbacks(context, scAdapter));

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
