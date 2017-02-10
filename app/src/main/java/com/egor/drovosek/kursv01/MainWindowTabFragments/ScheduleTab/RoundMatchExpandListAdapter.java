package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

/**
 * Created by Drovosek on 03/02/2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.egor.drovosek.kursv01.DB.DataMiner;
import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.R;

public class RoundMatchExpandListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<GroupRound> rounds;
    static public Activity mActivity;

    public RoundMatchExpandListAdapter(Context context, Activity inActivity, ArrayList<GroupRound> groups) {
        this.mContext = context;
        this.rounds = groups;
        this.mActivity = inActivity;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildMatch> chList = rounds.get(groupPosition).getMatches();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ChildMatch match = (ChildMatch) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.match_item, null);
        }
        TextView tvHome = (TextView) convertView.findViewById(R.id.homeTeamView);
        TextView tvGuest = (TextView) convertView.findViewById(R.id.guestTeamView);
        TextView tvScore = (TextView) convertView.findViewById(R.id.scoreView);
        TextView tvDateTime = (TextView) convertView.findViewById(R.id.timeDateView);

        tvHome.setText(match.gethomeName());
        tvGuest.setText(match.getGuestName());
        tvScore.setText(match.getScore());
        tvDateTime.setText(match.getDateAndTime());

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildMatch> matchesList = rounds.get(groupPosition).getMatches();
        if (matchesList == null)
           return 0;
        else
           return matchesList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return rounds.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return rounds.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupRound group = (GroupRound) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_round_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.group_round_name);
        tv.setText(group.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

        ArrayList<ChildMatch> match_list = rounds.get(groupPosition).getMatches();
        /*begin версия с ProgressBar*/
        if (match_list == null || match_list.isEmpty()) {

            FootballDBHelper db = new FootballDBHelper(mContext);
            Cursor temp = db.getMatchesSeasonRound(2016, groupPosition+1);

            if (temp.getCount() < 1) {
                temp.close();
                new GrabMatchesAndGoalsProgressTask(this.mContext).execute(String.valueOf(2016), String.valueOf(groupPosition + 1), "1");
                //"1" means go to site, parse data, put it into DB and finally get it from DB
            }
            else
                new GrabMatchesAndGoalsProgressTask(this.mContext).execute(String.valueOf(2016), String.valueOf(groupPosition + 1), "0");
                //"0" means just run QUERY
        }
        else
        {
            //nothing to do
        }
        /*end версия с ProgressBar*/

        /*работает, но отображает список матчей после последующих открытия/закрытия
        * причина: DataMiner.populateScheduleWithoutGoalsBG() запускает фоновый thread
         * и выходит. А db.getMatchesSeasonRound() не ждет окончания фоновой задачи DataMinera.
        * */
        /*if (match_list == null || match_list.isEmpty())
        {
            FootballDBHelper db = new FootballDBHelper(context);
            Cursor temp = db.getMatchesSeasonRound(2016, groupPosition+1);

            if (temp.getCount() < 1) {
                temp.close();
                DataMiner dm = new DataMiner(context);
                dm.populateScheduleWithoutGoalsBG(2016, groupPosition + 1);
                temp = db.getMatchesSeasonRound(2016, groupPosition+1);
            }

            temp.moveToFirst();
            match_list = new ArrayList<ChildMatch>();

            for (int i = 0; i < temp.getCount(); i++)
            {
                    String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
                    String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
                    int round = temp.getInt(temp.getColumnIndex("round"));
                    int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
                    int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
                    String dateAndTime = temp.getString(temp.getColumnIndex("datem"));

                    ChildMatch item = new ChildMatch();
                    item.sethomeName(homeTeam);
                    item.setHomeScore(String.valueOf(scoreHome));
                    item.setGuestName(guestTeam);
                    item.setGuestScore(String.valueOf(scoreGuest));
                    item.setDateAndTime(dateAndTime);

                    match_list.add(item);

                    temp.moveToNext();
            }

             temp.close();

            rounds.get(groupPosition).setMatches(match_list);
            this.notifyDataSetChanged();
        }*/
    }
    private class GrabMatchesProgressTask extends AsyncTask<String, String, Boolean> {
        private ProgressDialog dialog;
        private Context context;
        int season;
        int round;

        public GrabMatchesProgressTask(Context inContext) {
            context = inContext;
            dialog = new ProgressDialog(RoundMatchExpandListAdapter.mActivity);
        }



        /** progress dialog to show user that the backup is processing. */

        protected void onPreExecute() {
            this.dialog.setMessage("Загружаем список матчей...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (success) {
                Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        }

        protected Boolean doInBackground(final String... args) {
            try{

                season = Integer.valueOf(args[0].toString());
                round =  Integer.valueOf(args[1].toString());
                boolean grab = args[2].equals("1");

                FootballDBHelper db = new FootballDBHelper(context);
                if (grab) {
                    DataMiner dm = new DataMiner(mContext);
                    dm.populateScheduleWithoutGoalsFG(season, round);
                }
                Cursor temp = db.getMatchesSeasonRound(season, round);

                ArrayList<ChildMatch> match_list;

                temp.moveToFirst();

                match_list = new ArrayList<ChildMatch>();

                for (int i = 0; i < temp.getCount(); i++)
                {
                    String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
                    String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
                    int round = temp.getInt(temp.getColumnIndex("round"));
                    int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
                    int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
                    String dateAndTime = temp.getString(temp.getColumnIndex("datem"));

                    ChildMatch item = new ChildMatch();
                    item.sethomeName(homeTeam);
                    item.setHomeScore(String.valueOf(scoreHome));
                    item.setGuestName(guestTeam);
                    item.setGuestScore(String.valueOf(scoreGuest));
                    item.setDateAndTime(dateAndTime);

                    match_list.add(item);

                    temp.moveToNext();
                }

                temp.close();

                rounds.get(round-1).setMatches(match_list);

                return true;
            } catch (Exception e){
                Log.e("tag", "error", e);
                return false;
            }
        }


    }

    private class GrabMatchesAndGoalsProgressTask extends AsyncTask<String, String, Boolean> {
        private ProgressDialog dialog;
        private Context context;
        int season;
        int round;

        public GrabMatchesAndGoalsProgressTask(Context inContext) {
            context = inContext;
            dialog = new ProgressDialog(RoundMatchExpandListAdapter.mActivity);
        }



        /** progress dialog to show user that the backup is processing. */

        protected void onPreExecute() {
            this.dialog.setMessage("Загружаем список матчей...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (success) {
                Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        }

        protected Boolean doInBackground(final String... args) {
            try{

                season = Integer.valueOf(args[0].toString());
                round =  Integer.valueOf(args[1].toString());
                boolean grab = args[2].equals("1");

                FootballDBHelper db = new FootballDBHelper(context);
                if (grab) {
                    DataMiner dm = new DataMiner(mContext);
                    dm.populateScheduleWithGoalsAndPlayersFG(season, round);
                }
                Cursor temp = db.getMatchesSeasonRound(season, round);

                ArrayList<ChildMatch> match_list;

                temp.moveToFirst();

                match_list = new ArrayList<ChildMatch>();

                for (int i = 0; i < temp.getCount(); i++)
                {
                    String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
                    String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
                    int round = temp.getInt(temp.getColumnIndex("round"));
                    int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
                    int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
                    String dateAndTime = temp.getString(temp.getColumnIndex("datem"));

                    ChildMatch item = new ChildMatch();
                    item.sethomeName(homeTeam);
                    item.setHomeScore(String.valueOf(scoreHome));
                    item.setGuestName(guestTeam);
                    item.setGuestScore(String.valueOf(scoreGuest));
                    item.setDateAndTime(dateAndTime);

                    match_list.add(item);

                    temp.moveToNext();
                }

                temp.close();

                rounds.get(round-1).setMatches(match_list);

                return true;
            } catch (Exception e){
                Log.e("tag", "error", e);
                return false;
            }
        }


    }
}
