package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.egor.drovosek.kursv01.Misc.Match;
import com.egor.drovosek.kursv01.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_MATCHES;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_ROUND;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.ScheduleFragment.mLM;

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
        // пара <"номер тура", "позиция группы" >
    }

/*    public Cursor swapCursor(Cursor c) {
        cursor = c;
        return super.swapCursor(c);
    }*/

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Logic to get the child cursor on the basis of selected group.
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor.getColumnIndex("round"));

        Log.d(TAG, "getChildrenCursor() for groupPos " + groupPos);
        Log.d(TAG, "getChildrenCursor() for groupId " + groupId);

        mGroupMap.put(groupId, groupPos);

        Loader<Cursor> loader = mLM.getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            mLM.restartLoader(groupId, null, ScheduleFragment.schedCallBack);
        } else {
            mLM.initLoader(groupId, null, ScheduleFragment.schedCallBack);
        }

        return null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupTitle = "Тур " + (groupPosition+1);

        View row = convertView;
        if (row == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.group_round_item, parent, false);

            ViewHolder holder = new ViewHolder();
            View myView = row.findViewById(R.id.group_round_name);
            holder.addView(myView);

            row.setTag(holder);
        }

        // Get the stored ViewHolder that also contains our views
        ViewHolder holder = (ViewHolder) row.getTag();

        TextView groupRoundName = (TextView)holder.getView(R.id.group_round_name);
        groupRoundName.setText(groupTitle);
        groupRoundName.setTextColor(Color.WHITE);

        return row;
        //return super.getGroupView(groupPosition, isExpanded, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Cursor temp = (Cursor) getChild(groupPosition, childPosition);

        View row = convertView;

        temp.moveToPosition(childPosition);

        String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
        String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
        int round = temp.getInt(temp.getColumnIndex("round"));
        int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
        int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
        String dateAndTime = temp.getString(temp.getColumnIndex("datem"));

        byte[] homeLogoBlob = temp.getBlob(temp.getColumnIndex("homeLogo"));
        Bitmap homeLogo = BitmapFactory.decodeByteArray(homeLogoBlob, 0, homeLogoBlob.length);

        byte[] guestLogoBlob = temp.getBlob(temp.getColumnIndex("guestLogo"));
        Bitmap guestLogo = BitmapFactory.decodeByteArray(guestLogoBlob, 0, guestLogoBlob.length);

        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = infalInflater.inflate(R.layout.match_item, parent, false);

            ViewHolder holder = new ViewHolder();
            View myView = row.findViewById(R.id.homeTeamView);
            holder.addView(myView);

            myView = row.findViewById(R.id.guestTeamView);
            holder.addView(myView);

            myView = row.findViewById(R.id.matchScoreView);
            holder.addView(myView);

            myView = row.findViewById(R.id.timeDateView);
            holder.addView(myView);

            myView = row.findViewById(R.id.teamHomeImage);
            holder.addView(myView);

            myView = row.findViewById(R.id.teamGuestImage);
            holder.addView(myView);

            row.setTag(holder);
        }

        // Get the stored ViewHolder that also contains our views
        ViewHolder holder = (ViewHolder) row.getTag();

        TextView tvHome = (TextView) holder.getView(R.id.homeTeamView);
        TextView tvGuest = (TextView) holder.getView(R.id.guestTeamView);
        TextView tvScore = (TextView) holder.getView(R.id.matchScoreView);
        TextView tvDateTime = (TextView) holder.getView(R.id.timeDateView);
        ImageView ivHomeImage = (ImageView) holder.getView(R.id.teamHomeImage);
        ImageView ivGuestImage = (ImageView) holder.getView(R.id.teamGuestImage);

        tvHome.setText(homeTeam);
        tvGuest.setText(guestTeam);
        if (scoreHome == -1)
        {
            tvScore.setText("-:-"); //матч не начался
            tvScore.setVisibility(View.INVISIBLE);
        }
        else
            tvScore.setText(scoreHome + " : " + scoreGuest);

        tvDateTime.setText(dateAndTime);
        ivHomeImage.setImageBitmap(homeLogo);
        ivGuestImage.setImageBitmap(guestLogo);

        return row;
        //return super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
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

    private class ViewHolder
    {
        private HashMap<Integer, View> storedViews = new HashMap<Integer, View>();

        public ViewHolder()
        {
        }


        public ViewHolder addView(View view)
        {
            int id = view.getId();
            storedViews.put(id, view);
            return this;
        }

        public View getView(int id)
        {
            return storedViews.get(id);
        }
    }
}
