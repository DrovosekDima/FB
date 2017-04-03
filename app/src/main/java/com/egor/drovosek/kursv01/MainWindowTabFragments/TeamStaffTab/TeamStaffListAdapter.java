package com.egor.drovosek.kursv01.MainWindowTabFragments.TeamStaffTab;

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

public class TeamStaffListAdapter extends CursorAdapter {
    public TeamStaffListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.staff_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        // Find fields to populate in inflated template
        TextView tvPoz = (TextView) view.findViewById(R.id.colPositionStaff);
        TextView tvFIO = (TextView) view.findViewById(R.id.colFIOstaff);

        // Extract properties from cursor
        String poz = cursor.getString(cursor.getColumnIndex("position"));

        String fio = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                     cursor.getString(cursor.getColumnIndex("second_name"));

        tvFIO.setText(fio);
        tvPoz.setText(poz);
    }
}
