package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamStaffTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egor.drovosek.kursv01.R;


public class TeamStaffFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "TeamStaffFragment::onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TeamStaffFragment::onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_team_staff, container, false);
        Context context = getActivity().getApplicationContext();

        return view;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

}
