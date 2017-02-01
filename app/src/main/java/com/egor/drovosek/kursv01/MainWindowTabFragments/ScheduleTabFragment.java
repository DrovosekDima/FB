package com.egor.drovosek.kursv01.MainWindowTabFragments;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.R;


public class ScheduleTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*todo: временно. очистка teams table*/
        FootballDBHelper db = new FootballDBHelper(getActivity().getApplicationContext());
        //db.ClearTable(Schema.TABLE_TEAMS);

        /*todo: найти лучшее место для инициализации*/
        DataMiner dm = new DataMiner(getActivity().getApplicationContext());
        //dm.populateTeam(2016);

        //dm.populateScheduleWithGoals(2016, 2);
        Cursor temp = db.getAllMatches(2016);

        temp.moveToFirst();

        for (int i = 0; i < temp.getCount(); i++)
        {
            String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
            String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
            int round = temp.getInt(temp.getColumnIndex("round"));
            int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
            int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
            String dateAndTime = temp.getString(temp.getColumnIndex("datem"));
            temp.moveToNext();
        }

        temp.close();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_frag_schedule, container, false);
    }
}
