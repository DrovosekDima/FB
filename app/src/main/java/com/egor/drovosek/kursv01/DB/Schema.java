package com.egor.drovosek.kursv01.DB;

/**
 * Created by Drovosek on 30/01/2017.
 */

public class Schema {

    public static final String DATABASE_NAME = "football.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_MATCHES = "matches";
    public static final String TABLE_TEAMS = "teams";
    public static final String TABLE_PLAYERS = "players";
    public static final String TABLE_STAFF = "staff";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_VIOLATIONS = "violations";

    /*table matches*/
    public static final String MATCHES_M_ID            = "M_ID";
    public static final String MATCHES_HOME_TEAM_ID    = "home_team_id";
    public static final String MATCHES_GUEST_TEAM_ID   = "guest_team_id";
    public static final String MATCHES_SCORE_HOME      = "score_home";
    public static final String MATCHES_SCORE_GUEST     = "score_guest";
    public static final String MATCHES_ROUND           = "round";
    public static final String MATCHES_SEASON          = "season";
    public static final String MATCHES_DATEM           = "datem";
    public static final String MATCHES_LOCATION        = "location";

    // запрос на создание таблицы matches
    public static final String CREATE_TABLE_MATCHES = "create table "
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

    /*таблица teams*/
    public static final String TEAMS_M_ID          = "T_ID";
    public static final String TEAMS_TITLE         = "title";
    public static final String TEAMS_CITY          = "city";
    public static final String TEAMS_EMBLEM        = "emblem";
    public static final String TEAMS_PATH          = "path_to_logo";
    public static final String TEAMS_SEASON        = "season";
    public static final String TEAMS_WIN           = "win";
    public static final String TEAMS_DRAW          = "draw";
    public static final String TEAMS_LOST          = "lost";

    // запрос на создание таблицы teams
    public static final String CREATE_TABLE_TEAMS = "create table "
            + TABLE_TEAMS + "( "
            + TEAMS_M_ID + " integer primary key autoincrement, "
            + TEAMS_TITLE + " vchar, "
            + TEAMS_CITY + " vchar, "
            + TEAMS_EMBLEM + " blov, "
            + TEAMS_PATH + " vchar, "
            + TEAMS_SEASON + " integer, "
            + TEAMS_WIN + " integer, "
            + TEAMS_DRAW + " datetime, "
            + TEAMS_LOST + " vchar);";

}
