package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

//import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerCursorAdapter;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayerLoaderCallbacks;
import com.egor.drovosek.kursv01.R;

import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_BESTPLAYER;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_ROUND;


public class ScheduleFragment extends Fragment
{

    FootballDBHelper mDB;
    public Context context;
    private ExpandableListView ExpandList;
    public ScheduleCursorAdapter scAdapter;
    public String TAG = getClass().getSimpleName().toString();
    public static ScheduleLoaderCallbacks schedCallBack;
    public static LoaderManager mLM;

    public Context getContext()
    {
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "::onCreate()");

        context = getActivity().getApplicationContext();

        mDB = new FootballDBHelper(context);

        // формируем столбцы сопоставления

        String[] Groupfrom = new String[] {  "round"};
        int[] Groupto = new int[] {  R.id.group_round_name}; //android.R.id.text1};

        String[] Childfrom = new String[] {  "home_title",
                                             "guest_title"};
        int[] Childto = new int[] { android.R.id.text1,
                                    android.R.id.text2};

        mLM = getActivity().getSupportLoaderManager();
        // создаем адаптер и настраиваем список
        scAdapter = new ScheduleCursorAdapter(context,
                android.R.layout.simple_expandable_list_item_1,
                android.R.layout.simple_expandable_list_item_2,
                Groupfrom, Groupto,
                Childfrom, Childto);

        schedCallBack = new ScheduleLoaderCallbacks(context, scAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_frag_schedule, container, false);

        ExpandList = (ExpandableListView) view.findViewById(R.id.exp_round_list);
        ExpandList.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        Log.i(TAG, "создаем лоадер для чтения прошедших туров");
        mLM.initLoader(LOADER_SCHED_ROUND, null, schedCallBack);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i(TAG, "::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDB.close();
        Log.i(TAG, "::onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "::onDestroyView()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "::onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "::onStop()");
    }
}
