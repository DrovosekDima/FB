package com.egor.drovosek.kursv01.Misc;

/**
 * Created by Drovosek on 03/02/2017.

GroupRound - номер тура в ExpandebleListView (расписание).
 ---------------------------------------
 |  Тур 1 <-- GoupRound                |
 ---------------------------------------
 |   -----------------------------      |
 |   |   БАТЭ    5:0    Городея  |      |
 |   |     вс 27 ноя 16:00       |      |
 |   -----------------------------      |
 | . . .                                |
 ----------------------------------------
 */

import com.egor.drovosek.kursv01.Misc.Match;

import java.util.ArrayList;

public class GroupRound {

    private String round;
    private ArrayList<Match> matches;

    public GroupRound() {
        this.round = "";
        matches = null;
    }

    public String getName() {
        return round;
    }
    public void setName(String in) {
        this.round = in;
    }
    public ArrayList<Match> getMatches() {
        return matches;
    }
    public void setMatches(ArrayList<Match> Items) {
        this.matches = Items;
    }

}
