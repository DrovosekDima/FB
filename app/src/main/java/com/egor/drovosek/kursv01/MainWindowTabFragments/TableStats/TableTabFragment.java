package com.egor.drovosek.kursv01.MainWindowTabFragments.TableStats;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorAdapter;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerLoaderCallbacks;
import com.egor.drovosek.kursv01.Misc.Team;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;
import java.util.List;

import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_BESTPLAYER;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_STATISTICS;


public class TableTabFragment extends Fragment {

    FootballDBHelper mDB;
    public Context context;
    ListView lvData;
    public StatisticCursorAdapter scAdapter;
    public String TAG = "StatisticsTAB";
    String team;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TableStats::onCreate()");

        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(context);

        // формируем столбцы сопоставления

        String[] from = new String[] {
                "_id",
                "teamName",
                "teamLogo",
                "numberofgames",
                "wins",
                "draws",
                "losts",
                "goalsfor",
                "goalsagainst",
                "points"};

        int[] to = new int[] {
                R.id.colRank,
                R.id.colTeamName,
                R.id.colLogo,
                R.id.colPlays,
                R.id.colWons,
                R.id.colDraws,
                R.id.colLosts,
                R.id.colGoalsForAgainst,
                R.id.colGoalsDifferecnces,
                R.id.colPoints};

        // создаем адаптер и настраиваем список
        scAdapter = new StatisticCursorAdapter(context, R.layout.tab_frag_table, null, from, to);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_frag_table, container, false);

        lvData = (ListView) view.findViewById(R.id.lvStatsAll);
        lvData.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        Log.i(TAG, "Stats::создаем лоадер для чтения статистики из базы данных");
        getActivity().getSupportLoaderManager().initLoader(LOADER_STATISTICS, null,
                new StatisticLoaderCallbacks(context, scAdapter));
        return view;
    }

    /**
     * Called when the Fragment is visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "TableStats::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i(TAG, "TableStats::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "TableStats::onDestroy()");
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "TableStats::onDestroyView()");
    }

    /**
     * Called when the Fragment is no longer resumed.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "TableStats::onPause()");
    }

    /**
     * Called when the Fragment is no longer started.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "TableStats::onStop()");
    }
}
