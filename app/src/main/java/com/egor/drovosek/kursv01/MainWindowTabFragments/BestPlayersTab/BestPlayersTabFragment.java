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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.DB.Schema;
import com.egor.drovosek.kursv01.R;

import static com.egor.drovosek.kursv01.MainActivity.gdSeason;


public class BestPlayersTabFragment extends Fragment {

    public class CursorAdapterWithImage extends SimpleCursorAdapter
    {
        private Cursor c;
        private Context context;

        public CursorAdapterWithImage(Context context, int layout, Cursor c, String[] from, int[] to)
        {
            super(context, layout, c, from, to);
            this.c = c;
            this.context = context;
        }

        public View getView(int pos, View inView, ViewGroup parent)
        {
            View v = inView;

            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.table_bestplayers_row, null);
            }
            this.c.moveToPosition(pos);

            String exerciseName = this.c.getString(this.c.getColumnIndex("firstName"));
            String exerciseDescr = this.c.getString(this.c.getColumnIndex("secondName"));
            byte[] image = this.c.getBlob(this.c.getColumnIndex("logo"));

            ImageView iv = (ImageView) v.findViewById(R.id.colLogoBP);
            if (image != null)
            {
                // If there is no image in the database "NA" is stored instead of a blob
                // test if there more than 3 chars "NA" + a terminating char if more than
                // there is an image otherwise load the default
                if (image.length > 3)
                {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                }
            /*else
            {
                iv.setImageResource(R.drawable.icon); // todo: create default icon
            }*/
            }
            TextView fname = (TextView) v.findViewById(R.id.colTeamNameBP);
            fname.setText(exerciseName);

            TextView fDescr = (TextView) v.findViewById(R.id.colFIOBP);
            fDescr.setText(exerciseDescr);
            return (v);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Debug", "BestPlayers::onCreate()");
/*
---------------------------- on create------------------------------------------
        setContentView(R.layout.activity_exercises_list);
        ListView lvExList = (ListView) findViewById(R.id.listExercises);
        db = new WorkoutDataBase(this);
        mCursor = db.getAllExercises();

        startManagingCursor(mCursor);
        // Now create a new list adapter bound to the cursor.
        BaseAdapter adapter = new CursorAdapterWithImage(this, // Context.
                R.layout.exercise_item, // Specify the row template
                // to use (here, two
                // columns bound to the
                // two retrieved cursor
                // rows).
                mCursor, // Pass in the cursor to bind to.
                // Array of cursor columns to bind to.
                new String [] {WorkoutDataBase.TE_IMAGE, WorkoutDataBase.TE_EX_NAME, WorkoutDataBase.TE_DESCRIPTION},
                // Parallel array of which template objects to bind to those
                // columns.
                new int[] { R.id.tvItemExerciseImage, R.id.tvItemExcersizeName, R.id.tvItemExcersizeDescription
                });


        // Bind to our new adapter.
        lvExList.setAdapter(adapter);
----------------------------------------------------------------------
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Debug", "BestPlayers::onCreateView()");
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

        mDB.close();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Debug", "BestPlayers::onStart()");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        Log.i("Debug", "BestPlayers::onResume()");
        super.onResume();
    }


    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Debug", "BestPlayers::onDestroy()");
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
        Log.i("Debug", "BestPlayers::onDestroyView()");
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to { Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Debug", "BestPlayers::onPause()");
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i("Debug", "BestPlayers::onStop()");
    }
}
