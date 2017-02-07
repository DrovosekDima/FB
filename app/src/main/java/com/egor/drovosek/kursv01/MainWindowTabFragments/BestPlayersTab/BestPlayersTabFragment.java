package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.R;

import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class BestPlayersTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_frag_bestplayers, container, false);
        Context context = getActivity().getApplicationContext();

        TableLayout table = (TableLayout) view.findViewById(R.id.bestPlayersTable);
        View row;
        TextView columnFIO;
        ImageView columnLogo;
        TextView columnTeamName;
        TextView columnGoals;
        TextView columnRank;

        FootballDBHelper mDB = new FootballDBHelper(context);

        Cursor curs = mDB.getBestPlayers(gdSeason);


        if (curs != null && curs.getCount()>0)
        {
            int sizeCurs = curs.getCount();
            boolean odd = true;

            curs.moveToFirst();
            for(int i =0; i< sizeCurs; i++)
            {
                String tmpWName = curs.getString(curs.getColumnIndex("player_id"));

                row = getActivity().getLayoutInflater().inflate(R.layout.table_bestplayers_row, null);

                columnFIO = (TextView) row.findViewById(R.id.colFIOBP);
                columnLogo = (ImageView) row.findViewById(R.id.colLogoBP);
                columnTeamName = (TextView) row.findViewById(R.id.colTeamNameBP);
                columnRank = (TextView) row.findViewById(R.id.colRankBP);
                columnGoals = (TextView) row.findViewById(R.id.colGoalsBP);

                columnRank.setText(String.valueOf(i+1));
                columnTeamName.setText(tmpWName);
                columnGoals.setText("0");
                columnFIO.setText("Drovosek #" + String.valueOf(i+1));
                if (odd)
                {
                    columnLogo.setImageResource(R.drawable.dinamominsk);
                    row.setBackgroundColor(Color.LTGRAY);
                    odd = false;
                }
                else {
                    columnLogo.setImageResource(R.drawable.bate);
                    odd = true;
                }

                table.addView(row);

                curs.moveToNext();
            }
        }

        return view;
    }
}
