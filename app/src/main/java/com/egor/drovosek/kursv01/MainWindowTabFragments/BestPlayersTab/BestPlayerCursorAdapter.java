package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

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

public class BestPlayerCursorAdapter extends SimpleCursorAdapter
{
    public Cursor cursor;
    private Context context;

    public BestPlayerCursorAdapter(Context inContext, int layout, Cursor inCursor, String[] from, int[] to)
    {
        super(inContext, layout, inCursor, from, to);
        Log.i("BestPlayer", "BestPlayerCursorAdapter constructor with empty cursor");
        cursor = inCursor;
        context = inContext;
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
            row = inflater.inflate(R.layout.table_bestplayers_row, null);
        }


        if (cursor != null) {
            cursor.moveToPosition(pos);

            TextView columnFIO;
            ImageView columnLogo;
            TextView columnTeamName;
            TextView columnGoals;
            TextView columnRank;
            String data;

            columnFIO = (TextView) row.findViewById(R.id.colFIOBP);
            columnLogo = (ImageView) row.findViewById(R.id.colLogoBP);
            columnTeamName = (TextView) row.findViewById(R.id.colTeamNameBP);
            columnRank = (TextView) row.findViewById(R.id.colRankBP);
            columnGoals = (TextView) row.findViewById(R.id.colGoalsBP);

            data = String.valueOf(pos + 1);
            columnRank.setText(data);

            data = cursor.getString(cursor.getColumnIndex("teamName"));
            columnTeamName.setText(data);


            data = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                    cursor.getString(cursor.getColumnIndex("second_name"));
            columnFIO.setText(data);

            data = String.valueOf(cursor.getInt(cursor.getColumnIndex("numberOfGoals")));
            columnGoals.setText(data);

            byte[] byteLogo = cursor.getBlob(cursor.getColumnIndex("logo"));
            Bitmap logo = BitmapFactory.decodeByteArray(byteLogo, 0, byteLogo.length);
            columnLogo.setImageBitmap(logo);

            if ((pos & 1) != 0) //odd
            {
                row.setBackgroundColor(Color.LTGRAY);
            }
        }

        return (row);
    }
}
