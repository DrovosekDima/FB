package com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab;

/**
 * Created by Drovosek on 03/02/2017.
 */
/*
ChildMatch - элемент который отображается в ExpandebleListView (расписание).
Информация о матче:
-----------------------------
|   БАТЭ    5:0    Городея  |
|     вс 27 ноя 16:00       |
-----------------------------
todo: добавить эмблемы клубов

 */

public class ChildMatch {

    private String homeName;
    private String guestName;
    private String dateAndTime;
    private String homeScore;
    private String guestScore;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String Name) {
        this.guestName = Name;
    }

    public String gethomeName() {
        return homeName;
    }

    public void sethomeName(String Name) {
        this.homeName = Name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String Name) {
        this.dateAndTime = Name;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String score) {
        this.homeScore = score;
    }

    public String getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(String score) {
        this.guestScore = score;
    }

    public String getScore() {
        String score = homeScore + " : " + guestScore;
        return score ;
    }

}

