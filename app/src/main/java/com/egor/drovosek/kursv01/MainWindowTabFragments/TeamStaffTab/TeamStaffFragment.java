package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamStaffTab;

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
import android.widget.TextView;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainWindowTabFragments.TeamMatchesTab.TeamMatchesListAdapter;
import com.egor.drovosek.kursv01.R;

import org.w3c.dom.Text;

import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class TeamStaffFragment extends Fragment {

    FootballDBHelper mDB;
    public Context context;
    String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "TeamStaffFragment::onCreate()");
        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(context);
        Bundle args = getArguments();
        teamName = args.getString("teamName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TeamStaffFragment::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_team_summary, container, false);
        Context context = getActivity().getApplicationContext();

        String couchName = mDB.getCouchOfTeam(gdSeason, teamName);

        TextView couchView = (TextView) view.findViewById(R.id.couchView);
        couchView.setText(couchName);

        // Find ListView to populate
        ListView lvItems = (ListView) view.findViewById(R.id.staffList);

        Cursor staffCursor = mDB.getMembersOfTeam(gdSeason, teamName);

        // Setup cursor adapter using cursor from last step
        TeamStaffListAdapter staffAdapter = new TeamStaffListAdapter(context, staffCursor);

        // Attach cursor adapter to the ListView
        lvItems.setAdapter(staffAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
