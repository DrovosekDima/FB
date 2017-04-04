package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamMatchesAway;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.R;

import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class TeamMatchesAwayFragment extends Fragment {

    FootballDBHelper mDB;
    String teamName;
    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "TeamMatchesAwayFragment::onCreate()");

        Bundle args = getArguments();
        teamName = args.getString("teamName");

        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TeamMatchesAwayFragment::onCreateView()");
        View view = inflater.inflate(R.layout.tab_team_matches, container, false);

        // Find ListView to populate
        ListView lvItems = (ListView) view.findViewById(R.id.team_matches_list);

        Cursor matchesCursor = mDB.getMatchesAwaySeason(gdSeason, teamName);

        // Setup cursor adapter using cursor from last step
        TeamMatchesAwayListAdapter matchAdapter = new TeamMatchesAwayListAdapter(context, matchesCursor);

        // Attach cursor adapter to the ListView
        lvItems.setAdapter(matchAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
