package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamMatchesAway;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egor.drovosek.kursv01.R;

/**
 * Created by Drovosek on 17/03/2017.
 */

public class TeamMatchesAwayListAdapter extends CursorAdapter {
    public TeamMatchesAwayListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvHome = (TextView) view.findViewById(R.id.homeTeamView);
        TextView tvGuest = (TextView) view.findViewById(R.id.guestTeamView);
        TextView tvScore = (TextView) view.findViewById(R.id.matchScoreView);
        TextView tvDateTime = (TextView) view.findViewById(R.id.timeDateView);
        ImageView ivHomeImage = (ImageView) view.findViewById(R.id.teamHomeImage);
        ImageView ivGuestImage = (ImageView) view.findViewById(R.id.teamGuestImage);

        // Extract properties from cursor
        String homeTeam = cursor.getString(cursor.getColumnIndex("home_title"));
        String guestTeam = cursor.getString(cursor.getColumnIndex("guest_title"));
        int round = cursor.getInt(cursor.getColumnIndex("round"));
        int scoreHome = cursor.getInt(cursor.getColumnIndex("score_home"));
        int scoreGuest = cursor.getInt(cursor.getColumnIndex("score_guest"));
        String dateAndTime = cursor.getString(cursor.getColumnIndex("datem"));

        byte[] homeLogoBlob  = cursor.getBlob(cursor.getColumnIndex("homeLogo"));
        Bitmap homeLogo    = BitmapFactory.decodeByteArray(homeLogoBlob, 0 ,homeLogoBlob.length);

        byte[] guestLogoBlob  = cursor.getBlob(cursor.getColumnIndex("guestLogo"));
        Bitmap guestLogo    = BitmapFactory.decodeByteArray(guestLogoBlob, 0 ,guestLogoBlob.length);

        // Populate fields with extracted properties
        tvHome.setText(homeTeam);
        tvGuest.setText(guestTeam);
        if(scoreHome == -1)
        {
            tvScore.setText("-:-"); //матч не начался
            tvScore.setVisibility(View.INVISIBLE);
        }
        else
            tvScore.setText(scoreHome + " : " + scoreGuest);

        tvDateTime.setText(dateAndTime);
        ivHomeImage.setImageBitmap(homeLogo);
        ivGuestImage.setImageBitmap(guestLogo);


    }
}
