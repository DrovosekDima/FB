package com.egor.drovosek.kursv01.MainWindowTabFragments.TableStats;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egor.drovosek.kursv01.R;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class StatisticCursorAdapter extends SimpleCursorAdapter
{
    public Cursor cursor;
    private Context context;
    String TAG = "StatsCursorAdapter";
    String team;

    public StatisticCursorAdapter(Context inContext, int layout, Cursor inCursor, String[] from, int[] to, String inTeamName)
    {
        super(inContext, layout, inCursor, from, to);
        Log.i(TAG, "StatisticCursorAdapter constructor with empty cursor");
        cursor = inCursor;
        context = inContext;
        team = inTeamName;
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        cursor = c;
        return super.swapCursor(c);
    }

    public View getView(int pos, View inView, ViewGroup parent)
    {
        View row = inView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.table_stats_row, null);
        }


        if (cursor != null) {
            cursor.moveToPosition(pos);

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

            String teamName = cursor.getString(cursor.getColumnIndex("teamName"));
            String numOfGames = cursor.getString(cursor.getColumnIndex("numberofgames"));
            String wins = cursor.getString(cursor.getColumnIndex("wins"));
            String draws = cursor.getString(cursor.getColumnIndex("draws"));
            String losts = cursor.getString(cursor.getColumnIndex("losts"));

            String gF = cursor.getString(cursor.getColumnIndex("goalsfor"));
            String gA = cursor.getString(cursor.getColumnIndex("goalsagainst"));
            String goalsForAg = "(" + gF + "-" +  gA + ")";

            String points = cursor.getString(cursor.getColumnIndex("points"));

            columnRank.setText(String.valueOf(pos + 1));
            columnTeamName.setText(teamName);
            columnPlays.setText(numOfGames);
            columnWins.setText(wins);
            columnDraws.setText(draws);
            columnLosts.setText(losts);
            columnPoints.setText(points);
            columnGoalsForAgainst.setText(goalsForAg);
            columnDifference.setText(String.valueOf(Integer.valueOf(gF) - Integer.valueOf(gA)));

            byte[] byteLogo = cursor.getBlob(cursor.getColumnIndex("teamLogo"));
            Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0 ,byteLogo.length);
            columnLogo.setImageBitmap(logo);

            if ((pos & 1) != 0) //odd
            {
                row.setBackgroundColor(Color.LTGRAY);
            }
            if (teamName.compareTo(team) == 0)
                row.setBackgroundColor(Color.RED);
        }

        return (row);
    }
}
