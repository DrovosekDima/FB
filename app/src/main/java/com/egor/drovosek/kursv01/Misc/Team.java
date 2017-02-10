package com.egor.drovosek.kursv01.Misc;

import android.graphics.Bitmap;

import java.sql.Blob;

/**
 * Created by Drovosek on 29/01/2017.
 */

public class Team {
    int     T_ID;
    String  title;
    String  city;
    Bitmap  emblem;
    int     season;
    int     win;
    int     draw;
    int     lost;

    public Team(String inTitle, String inCity, Bitmap inEmblem, int inSeason) {
        title = inTitle;
        city = inCity;
        emblem = inEmblem;
        season = inSeason;
    }
    public Bitmap getEmblem()
    {
        return this.emblem;
    }

    public String getTitle(){
        return this.title;
    }
}
