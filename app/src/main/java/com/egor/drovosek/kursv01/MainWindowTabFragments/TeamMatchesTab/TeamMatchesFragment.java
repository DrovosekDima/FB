package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamMatchesTab;

/**
 * Created by Drovosek on 27/01/2017.
 * Расписание игр
 * В формате ТУР -> список игр
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.RoundMatchExpandListAdapter;
import com.egor.drovosek.kursv01.Misc.Match;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.egor.drovosek.kursv01.MainActivity.gdNumberOfRounds;
import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class TeamMatchesFragment extends Fragment {

    FootballDBHelper mDB;
    public Context context;
    String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("Debug", "TeamMatchesFragment::onCreate()");
        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(context);
        Bundle args = getArguments();
        teamName = args.getString("teamName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TeamMatchesFragment::onCreateView()");
        View view = inflater.inflate(R.layout.tab_team_matches, container, false);

        // Find ListView to populate
        ListView lvItems = (ListView) view.findViewById(R.id.team_matches_list);

        Cursor matchesCursor = mDB.getMatchesSeason(gdSeason, teamName);

        // Setup cursor adapter using cursor from last step
        TeamMatchesListAdapter matchAdapter = new TeamMatchesListAdapter(context, matchesCursor);

        // Attach cursor adapter to the ListView
        lvItems.setAdapter(matchAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Debug", "TeamMatchesFragment::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i("Debug", "TeamMatchesFragment::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Debug", "TeamMatchesFragment::onDestroy()");
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
        Log.i("Debug", "TeamMatchesFragment::onDestroyView()");
    }

    /**
     * Called when the Fragment is no longer resumed.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Debug", "TeamMatchesFragment::onPause()");
    }

    /**
     * Called when the Fragment is no longer started.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i("Debug", "TeamMatchesFragment::onStop()");
    }
}
