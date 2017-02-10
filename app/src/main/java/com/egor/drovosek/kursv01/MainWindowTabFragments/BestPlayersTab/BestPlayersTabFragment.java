package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

/**
 * Created by Drovosek on 27/01/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
            String data;

            curs.moveToFirst();
            for(int i =0; i< sizeCurs; i++)
            {
                row = getActivity().getLayoutInflater().inflate(R.layout.table_bestplayers_row, null);

                columnFIO = (TextView) row.findViewById(R.id.colFIOBP);
                columnLogo = (ImageView) row.findViewById(R.id.colLogoBP);
                columnTeamName = (TextView) row.findViewById(R.id.colTeamNameBP);
                columnRank = (TextView) row.findViewById(R.id.colRankBP);
                columnGoals = (TextView) row.findViewById(R.id.colGoalsBP);

                data = String.valueOf(i+1);
                columnRank.setText(data);

                data = curs.getString(curs.getColumnIndex("teamName"));
                columnTeamName.setText(data);


                data = curs.getString(curs.getColumnIndex("first_name")) + " " +
                        curs.getString(curs.getColumnIndex("second_name"));
                columnFIO.setText(data);

                data = String.valueOf(curs.getInt(curs.getColumnIndex("numberOfGoals")));
                columnGoals.setText(data);

                byte[] byteLogo = curs.getBlob(curs.getColumnIndex("logo"));
                Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0 ,byteLogo.length);
                columnLogo.setImageBitmap(logo);

                if (odd)
                {
                    row.setBackgroundColor(Color.LTGRAY);
                    odd = false;
                }
                else {
                    odd = true;
                }

                table.addView(row);

                curs.moveToNext();
            }

            curs.close();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
