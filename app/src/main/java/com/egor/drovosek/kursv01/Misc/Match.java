package com.egor.drovosek.kursv01.Misc;

/**
 * Created by Drovosek on 30/01/2017.
 */

public class Match {

    int matchID;
    int homeTeamID;
    int guestTeamID;
    int scoreHome;
    int scoreGuest;
    int round;
    int season;
    String dateAndTime;
    String location;

    private String guestName;
    private String homeName;

    public String getGuestName()
    {
        return guestName;
    }

    public String getHomeName()
    {
        return homeName;
    }

    public void setGuestName(String inGuestName)
    {
        // имя команды находится в table teams.title
        // select title from teams where T_ID = this.guestTeamID
        this.guestName = inGuestName;
    }

    public void setHomeName(String inHomeName)
    {
        // имя команды находится в table teams.title
        // select title from teams where T_ID = this.homeTeamID
        this.guestName = inHomeName;
    }

    public String getDateAndTime()
    {
        return dateAndTime;
    }

    public String getScore()
    {
        return String.valueOf(scoreHome) + " : " + String.valueOf(scoreGuest);
    }

    public Match() {
        super();
    }

    Match(int _homeTeamID,
          int _guestTeamID,
          int _scoreHome,
          int _scoreGuest,
          int _season,
          String _dateAndTime,
          String _location)
    {
        super();
        homeTeamID = _homeTeamID;
        guestTeamID = _guestTeamID;
        scoreHome   = _scoreHome;
        scoreGuest  = _scoreGuest;
        season      = _season;
        dateAndTime = _dateAndTime;
        location      = _location;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
