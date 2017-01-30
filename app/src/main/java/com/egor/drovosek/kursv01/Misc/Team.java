package com.egor.drovosek.kursv01.Misc;

import java.sql.Blob;

/**
 * Created by Drovosek on 29/01/2017.
 */

public class Team {
    int     T_ID;
    String  title;
    String  city;
    Blob    emblem;
    int     season;
    int     win;
    int     draw;
    int     lost;

    public Team() {
        super();
    }
}
