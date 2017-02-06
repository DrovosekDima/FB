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
import android.widget.ExpandableListView;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.MainActivity;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.ChildMatch;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.GroupRound;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.RoundMatchExpandListAdapter;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;

import static com.egor.drovosek.kursv01.MainActivity.gdNumberOfRounds;
import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class ScheduleTabFragment extends Fragment {

    private RoundMatchExpandListAdapter ExpAdapter;
    private ArrayList<GroupRound> ExpListItems;
    private ExpandableListView ExpandList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_frag_schedule, container, false);

        ExpandList = (ExpandableListView)view.findViewById(R.id.exp_round_list);

        ExpListItems = SetInitialRounds();
        ExpAdapter = new RoundMatchExpandListAdapter(getActivity().getApplicationContext(), getActivity(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);

        //todo развернуть матчи определенного тура (последнего или первого)
        //ExpandList.expandGroup(0);

        /*todo: временно. очистка teams table*/
        //FootballDBHelper db = new FootballDBHelper(getActivity().getApplicationContext());
        //db.ClearTable(Schema.TABLE_TEAMS);

        /*todo: найти лучшее место для инициализации*/
        //DataMiner dm = new DataMiner(getActivity().getApplicationContext());
        //dm.populateTeam(2016);

        //dm.populateScheduleWithoutGoalsBG(2016, 2);
        /*Cursor temp = db.getMatchesSeason(2016);

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

        temp.close();*/

        // Inflate the layout for this fragment
        return view;
    }

    public ArrayList<GroupRound> SetInitialRounds()
    {
        ArrayList<GroupRound> listRounds = new ArrayList<GroupRound>();
        ArrayList<ChildMatch> ch_list;
        FootballDBHelper db = new FootballDBHelper(getActivity().getApplicationContext());

        //todo: определить количество туров на данный момент,
        // если прошлый год, то генерируем все туры
        // текущий год - последний доступный тур на сегодняшний день
        // gdNumberOfRounds - общее количество туров в году

        GroupRound gr;

        for (int i = 1; i < (gdNumberOfRounds + 1); i++) {
            gr = new GroupRound();
            gr.setName("Тур " + String.valueOf(i));
            listRounds.add(gr);
        }

        return listRounds;
    }
}
