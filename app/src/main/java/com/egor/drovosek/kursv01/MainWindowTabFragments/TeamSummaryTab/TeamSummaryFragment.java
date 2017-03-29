package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamSummaryTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.R;


public class TeamSummaryFragment extends Fragment {

    FootballDBHelper mDB;
    String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "TeamSummaryFragment::onCreate()");

        Bundle args = getArguments();
        teamName = args.getString("teamName");

        mDB = new FootballDBHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TeamSummaryFragment::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_team_summary, container, false);
        Context context = getActivity().getApplicationContext();

        // Поменять заголовок
        TextView urlTeam = (TextView) view.findViewById(R.id.siteView);
        urlTeam.setText(mDB.getTeamURL(teamName));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
