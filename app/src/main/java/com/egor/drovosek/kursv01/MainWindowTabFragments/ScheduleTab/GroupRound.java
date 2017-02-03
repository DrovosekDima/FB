package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

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

import java.util.ArrayList;

public class GroupRound {

    private String round;
    private ArrayList<ChildMatch> matches;

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
    public ArrayList<ChildMatch> getMatches() {
        return matches;
    }
    public void setMatches(ArrayList<ChildMatch> Items) {
        this.matches = Items;
    }

}
