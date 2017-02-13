package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.egor.drovosek.kursv01.Misc.Team;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.egor.drovosek.kursv01.MainActivity.gdNumberOfRounds;
import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class ScheduleTabFragment extends Fragment {

    private RoundMatchExpandListAdapter ExpAdapter;
    private ArrayList<String> groupItems;
    private HashMap<String, List<ChildMatch>> matchItems;
    private ExpandableListView ExpandList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SetInitialRounds();
        Log.i("Debug", "Schedule::onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "Schedule::onCreateView()");
        View view = inflater.inflate(R.layout.tab_frag_schedule, container, false);

        ExpandList = (ExpandableListView)view.findViewById(R.id.exp_round_list);

        //SetInitialRounds();
        ExpAdapter = new RoundMatchExpandListAdapter(getActivity().getApplicationContext(),
                                                    getActivity(),
                                                    groupItems,
                                                    matchItems );
        ExpandList.setAdapter(ExpAdapter);

        //todo развернуть матчи определенного тура (последнего или первого)
        //ExpandList.expandGroup(0);

        /*todo: временно. очистка teams table*/
        //FootballDBHelper db = new FootballDBHelper(getActivity().getApplicationContext());
        //db.ClearTable(Schema.TABLE_TEAMS);

        /*todo: найти лучшее место для инициализации*/
//        DataMiner dm = new DataMiner(getActivity().getApplicationContext());
//        dm.populateTeam(2016);

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

    public void SetInitialRounds()
    {
        groupItems = new ArrayList<String>();
        matchItems = new HashMap<String, List<ChildMatch>>();
        ArrayList<ChildMatch> matchTemp;

        for (int i = 0; i < gdNumberOfRounds; i++)
        {
            matchTemp = new ArrayList<ChildMatch>();
            groupItems.add("Тур " + String.valueOf(i));
            matchItems.put(groupItems.get(i), matchTemp);
        }
        //todo: определить количество туров на данный момент,
        // если прошлый год, то генерируем все туры
        // текущий год - последний доступный тур на сегодняшний день
        // gdNumberOfRounds - общее количество туров в году

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Debug", "Schedule::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        Log.i("Debug", "Schedule::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Debug", "Schedule::onDestroy()");
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
        Log.i("Debug", "Schedule::onDestroyView()");
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Debug", "Schedule::onPause()");
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i("Debug", "Schedule::onStop()");
    }
}
