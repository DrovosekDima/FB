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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.Misc.Team;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;
import java.util.List;


public class TableTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "TableStats::onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "TableStats::onCreateView()");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.tab_frag_table, container, false);

        Context context = getActivity().getApplicationContext();

        TableLayout table = (TableLayout) view.findViewById(R.id.statsTable);
        View row;

        TextView columnRank;
        ImageView columnLogo;
        TextView columnTeamName;
        TextView columnPlays;
        TextView columnWins;
        TextView columnDraws;
        TextView columnLosts;
        TextView columnPoints;
        TextView columnGoalsForAgainst;
        TextView columnDifference;

        FootballDBHelper mDB = new FootballDBHelper(context);
        Cursor curs = mDB.getStats(2016);
                /*teamName, " +
                " SUM(NOM) as numberofgames, " +
                " SUM(Win) as wins, " +
                " SUM(Draw) as draws, " +
                " SUM(Lost) as losts, " +
                "         SUM(score_in) as goalsfor, " +
                " SUM(score_out) as goalsagainst, " +
                " SUM(Win*3 + Draw) as points " +*/

        if (curs != null && curs.getCount()>0)
        {
            int sizeCurs = curs.getCount();
            boolean odd = true;

            curs.moveToFirst();
            for(int i =0; i< sizeCurs; i++)
            {
                row = getActivity().getLayoutInflater().inflate(R.layout.table_stats_row, null);

                columnRank = (TextView) row.findViewById(R.id.colRank);
                columnLogo = (ImageView) row.findViewById(R.id.colLogo);
                columnTeamName = (TextView) row.findViewById(R.id.colTeamName);
                columnPlays= (TextView) row.findViewById(R.id.colPlays);
                columnWins= (TextView) row.findViewById(R.id.colWons);
                columnDraws = (TextView) row.findViewById(R.id.colDraws);
                columnLosts= (TextView) row.findViewById(R.id.colLosts);
                columnPoints= (TextView) row.findViewById(R.id.colPoints);
                columnGoalsForAgainst= (TextView) row.findViewById(R.id.colGoalsForAgainst);
                columnDifference= (TextView) row.findViewById(R.id.colGoalsDifferecnces);

                String teamName = curs.getString(curs.getColumnIndex("teamName"));
                String numOfGames = curs.getString(curs.getColumnIndex("numberofgames"));
                String wins = curs.getString(curs.getColumnIndex("wins"));
                String draws = curs.getString(curs.getColumnIndex("draws"));
                String losts = curs.getString(curs.getColumnIndex("losts"));

                String gF = curs.getString(curs.getColumnIndex("goalsfor"));
                String gA = curs.getString(curs.getColumnIndex("goalsagainst"));
                String goalsForAg = "(" + gF + "-" +  gA + ")";

                String points = curs.getString(curs.getColumnIndex("points"));

                columnRank.setText(String.valueOf(i+1));
                columnTeamName.setText(teamName);
                columnPlays.setText(numOfGames);
                columnWins.setText(wins);
                columnDraws.setText(draws);
                columnLosts.setText(losts);
                columnPoints.setText(points);
                columnGoalsForAgainst.setText(goalsForAg);
                columnDifference.setText(String.valueOf(Integer.valueOf(gF) - Integer.valueOf(gA)));

                byte[] byteLogo = curs.getBlob(curs.getColumnIndex("teamLogo"));
                Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0 ,byteLogo.length);
                columnLogo.setImageBitmap(logo);

                if (odd)
                    row.setBackgroundColor(Color.LTGRAY);

                odd = !odd;

                table.addView(row);

                curs.moveToNext();
            }
            curs.close();
        }

        mDB.close();


        /*row = getActivity().getLayoutInflater().inflate(R.layout.table_stats_row, null);

        columnRank = (TextView) row.findViewById(R.id.colRank);
        columnLogo = (ImageView) row.findViewById(R.id.colLogo);
        columnTeamName = (TextView) row.findViewById(R.id.colTeamName);

        columnRank.setText("2");
        columnLogo.setImageResource(R.drawable.bate);
        columnTeamName.setText("БАТЭ");
        table.addView(row);*/

        return view;
    }

    /**
     * Called when the Fragment is visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i("Debug", "TableStats::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i("Debug", "TableStats::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Debug", "TableStats::onDestroy()");
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
        Log.i("Debug", "TableStats::onDestroyView()");
    }

    /**
     * Called when the Fragment is no longer resumed.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Debug", "TableStats::onPause()");
    }

    /**
     * Called when the Fragment is no longer started.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i("Debug", "TableStats::onStop()");
    }
}
