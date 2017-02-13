package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

/**
 * Created by Drovosek on 03/02/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
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
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.Misc.Match;
import com.egor.drovosek.kursv01.Misc.Team;
import com.egor.drovosek.kursv01.R;

public class RoundMatchExpandListAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<String> mGroup; // список туров (Тур 1 "1 апреля - 7 апреля")
    private HashMap<String, List<ChildMatch>> mChild;

    //private ArrayList<GroupRound> rounds;
    static public Activity mActivity;

    public RoundMatchExpandListAdapter(Context context, Activity inActivity, /*ArrayList<GroupRound> groups*/
                                       List<String> groupData,
                                       HashMap<String, List<ChildMatch>> childData) {
        this.mContext = context;
        //this.rounds = groups;
        this.mGroup = groupData;
        this.mChild = childData;

        this.mActivity = inActivity;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        /*ArrayList<ChildMatch> chList = rounds.get(groupPosition).getMatches();
        return chList.get(childPosition);*/
        return this.mChild.get(this.mGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildMatch element = (ChildMatch) getChild(groupPosition, childPosition);

        View row = convertView;
        if (row == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
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

        tvHome.setText(element.gethomeName());
        tvGuest.setText(element.getGuestName());
        tvScore.setText(element.getScore());
        tvDateTime.setText(element.getDateAndTime());
        ivHomeImage.setImageBitmap(element.getHomeLogo());
        ivGuestImage.setImageBitmap(element.getGuestLogo());

        return row;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String temp = this.mGroup.get(groupPosition);
        List<ChildMatch> tempSub = this.mChild.get(temp);
        if (tempSub != null)
            return tempSub.size();
        else
            return 0;
        /*ArrayList<ChildMatch> matchesList = rounds.get(groupPosition).getMatches();
        if (matchesList == null)
           return 0;
        else
           return matchesList.size();*/
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);

        View row = convertView;
        if (row == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
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

        return row;
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

        //ArrayList<ChildMatch> match_list = rounds.get(groupPosition).getMatches();
        String groupName = mGroup.get(groupPosition);
        List<ChildMatch> match_list =  mChild.get(groupName);

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

                List<ChildMatch> matches;

                temp.moveToFirst();

                matches = new ArrayList<ChildMatch>();

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

                    matches.add(item);

                    temp.moveToNext();
                }

                temp.close();

                //rounds.get(round-1).setMatches(match_list);
                mChild.put(mGroup.get(round-1), matches);
                db.close();

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
                    //Toast.makeText(context, "Grab from site", Toast.LENGTH_LONG).show();
                    DataMiner dm = new DataMiner(mContext);
                    dm.populateScheduleWithGoalsAndPlayersFG(season, round);
                }
                Cursor temp = db.getMatchesSeasonRound(season, round);

                ArrayList<ChildMatch> matches;

                temp.moveToFirst();

                matches = new ArrayList<ChildMatch>();

                for (int i = 0; i < temp.getCount(); i++)
                {
                    String homeTeam = temp.getString(temp.getColumnIndex("home_title"));
                    String guestTeam = temp.getString(temp.getColumnIndex("guest_title"));
                    int round = temp.getInt(temp.getColumnIndex("round"));
                    int scoreHome = temp.getInt(temp.getColumnIndex("score_home"));
                    int scoreGuest = temp.getInt(temp.getColumnIndex("score_guest"));
                    String dateAndTime = temp.getString(temp.getColumnIndex("datem"));

                    byte[] homeLogoBlob  = temp.getBlob(temp.getColumnIndex("homeLogo"));
                    Bitmap homeLogo    = BitmapFactory.decodeByteArray(homeLogoBlob, 0 ,homeLogoBlob.length);

                    byte[] guestLogoBlob  = temp.getBlob(temp.getColumnIndex("guestLogo"));
                    Bitmap guestLogo    = BitmapFactory.decodeByteArray(guestLogoBlob, 0 ,guestLogoBlob.length);

                    ChildMatch item = new ChildMatch();
                    item.sethomeName(homeTeam);
                    item.setHomeScore(String.valueOf(scoreHome));
                    item.setGuestName(guestTeam);
                    item.setGuestScore(String.valueOf(scoreGuest));
                    item.setDateAndTime(dateAndTime);
                    item.setHomeLogo(homeLogo);
                    item.setGuestLogo(guestLogo);

                    matches.add(item);

                    temp.moveToNext();
                }

                temp.close();

                //rounds.get(round-1).setMatches(match_list);
                mChild.put(mGroup.get(round-1), matches);

                return true;
            } catch (Exception e){
                Log.e("tag", "error", e);
                return false;
            }
        }


    }

    private class ViewHolder
    {
        private HashMap<Integer, View> storedViews = new HashMap<Integer, View>();

        public ViewHolder()
        {
        }

        /**
         *
         * @param view
         *            The view to add; to reference this view later, simply refer to its id.
         * @return This instance to allow for chaining.
         */
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
