package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.egor.drovosek.kursv01.R;

import java.util.HashMap;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class ScheduleCursorAdapter extends SimpleCursorTreeAdapter
{
    public Cursor cursor;
    private Context context;
    private static boolean odd = true;
    String TAG = getClass().getCanonicalName().toString();
    protected final HashMap<Integer, Integer> mGroupMap;

    //public ScheduleCursorAdapter(Context inContext, int layout, Cursor inCursor, String[] from, int[] to)
    public ScheduleCursorAdapter(Context inContext, int groupLayout, int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom, int[] childrenTo)
    {
        super(inContext, null, groupLayout, groupFrom, groupTo, childLayout, childrenFrom, childrenTo);
        Log.i(TAG, "ScheduleCursorAdapter constructor with empty cursor");
        cursor = null;
        context = inContext;
        mGroupMap = new HashMap<Integer, Integer>();
    }

/*    public Cursor swapCursor(Cursor c) {
        cursor = c;
        return super.swapCursor(c);
    }*/

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Logic to get the child cursor on the basis of selected group.
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor.getColumnIndex("_id"));

        Log.d(TAG, "getChildrenCursor() for groupPos " + groupPos);
        Log.d(TAG, "getChildrenCursor() for groupId " + groupId);

        /*mGroupMap.put(groupId, groupPos);
        Loader<Cursor> loader = mActivity.getLoaderManager().getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            mActivity.getLoaderManager()
                    .restartLoader(groupId, null, mActivity);
        } else {
            mActivity.getLoaderManager().initLoader(groupId, null, mActivity);
        }*/

        return null;
    }

    public View getView(int pos, View inView, ViewGroup parent)
    {
        View row = inView;

/*        if (row == null)
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

            if (odd)
               row.setBackgroundColor(Color.LTGRAY);
            else
               row.setBackgroundColor(Color.WHITE);



            odd = !odd;
        }
*/
        return (row);
    }

    public HashMap<Integer, Integer> getGroupMap() {
        return mGroupMap;
    }
}
