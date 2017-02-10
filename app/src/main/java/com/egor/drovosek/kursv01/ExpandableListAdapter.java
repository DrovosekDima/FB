package com.egor.drovosek.kursv01;

/**
 * Created by Drovosek on 1/25/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.egor.drovosek.kursv01.Misc.Team;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, List<Team>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableListAdapter(Context context,
                                 List<String> listDataHeader,
                                 HashMap<String, List<Team>> listChildData )
    {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        //Log.d("GROUPCOUNT", String.valueOf(i));
        return i;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String temp = this.mListDataHeader.get(groupPosition);
        List<Team> tempSub = this.mListDataChild.get(temp);
        if (tempSub != null)
           return tempSub.size();
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Log.d("CHILD", mListDataChild.get(this.mListDataHeader.get(groupPosition))
        //        .get(childPosition).toString());
        return this.mListDataChild.get(
                this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.groupmenu);

        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);

        lblListHeader.setTypeface(null, Typeface.BOLD);

        lblListHeader.setText(headerTitle);

        //lblListHeader.setText(headerTitle.getIconName());
        //headerIcon.setImageResource(MainActivity.icon[groupPosition]);

        //Image view which you put in row_group_list.xml
         //View ind = convertView.findViewById(R.id.iconimage);

         if (headerIcon != null){
            //ImageView indicator = (ImageView) ind;
             int childrenCount = getChildrenCount(groupPosition);

            if (childrenCount == 0)
            {
                headerIcon.setVisibility(View.INVISIBLE);
            }
            else
            {
                headerIcon.setVisibility(View.VISIBLE);

                if (isExpanded)
                   headerIcon.setImageResource(R.drawable.expander_ic_maximized);
                else
                    headerIcon.setImageResource(R.drawable.expander_ic_minimized );
           }
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String childText = (String) getChild(groupPosition, childPosition);
        final Team element = (Team) getChild(groupPosition, childPosition);

        final Bitmap childImage = (Bitmap) element.getEmblem();
        final String childTeamName = (String) element.getTitle();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.teamName);
        ImageView imgListChild = (ImageView) convertView.findViewById(R.id.teamLogoImage);

        txtListChild.setText(childTeamName);
        imgListChild.setImageBitmap(childImage);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}