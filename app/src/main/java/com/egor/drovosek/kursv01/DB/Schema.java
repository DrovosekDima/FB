package com.egor.drovosek.kursv01.DB;

/**
 * Created by Drovosek on 30/01/2017.
 */

public class Schema {

    private static final String DATABASE_NAME = "football.db";
    private static final int DATABASE_VERSION = 0;

    private static final String TABLE_MATCHES = "matches";
    private static final String TABLE_TEAMS = "teams";
    private static final String TABLE_PLAYERS = "players";
    private static final String TABLE_STAFF = "staff";
    private static final String TABLE_GOALS = "goals";
    private static final String TABLE_VIOLATIONS = "violations";

    /*table matches*/
    private static final String MATCHES_M_ID            = "M_ID";
    private static final String MATCHES_HOME_TEAM_ID    = "home_team_id";
    private static final String MATCHES_GUEST_TEAM_ID   = "guest_team_id";
    private static final String MATCHES_SCORE_HOME      = "score_home";
    private static final String MATCHES_SCORE_GUEST     = "score_guest";
    private static final String MATCHES_ROUND           = "round";
    private static final String MATCHES_SEASON          = "season";
    private static final String MATCHES_DATEM           = "datem";
    private static final String MATCHES_LOCATION        = "location";

    // запрос на создание таблицы matches
    private static final String CREATE_TABLE_MATCHES = "create table "
            + TABLE_MATCHES + "( "
            + MATCHES_M_ID + " integer primary key autoincrement, "
            + MATCHES_HOME_TEAM_ID + " integer, "
            + MATCHES_GUEST_TEAM_ID + " integer, "
            + MATCHES_SCORE_HOME + " integer, "
            + MATCHES_SCORE_GUEST + " integer, "
            + MATCHES_ROUND + " integer, "
            + MATCHES_SEASON + " integer, "
            + MATCHES_DATEM + " datetime, "
            + MATCHES_LOCATION + " vchar);";
}
