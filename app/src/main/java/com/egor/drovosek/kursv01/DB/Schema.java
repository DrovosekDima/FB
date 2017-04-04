package com.egor.drovosek.kursv01.DB;

/**
 * Created by Drovosek on 30/01/2017.
 */

public class Schema {

    public static final String DATABASE_NAME = "football.db";
    public static final int DATABASE_VERSION = 58;

    public static final String TABLE_MATCHES = "matches";
    public static final String TABLE_TEAMS = "teams";
    public static final String TABLE_PLAYERS = "players";
    public static final String TABLE_STAFF = "staff";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_VIOLATIONS = "violations";

    /*=================================================================================*/
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
    public static final String MATCHES_STATUS          = "status"; //COMPLETED, INPROGRESS, FUTURE

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
            + MATCHES_LOCATION + " vchar, "
            + MATCHES_STATUS + " vchar);";

    public static final String STATUS_COMPLETED       = "COMPLETED";
    public static final String STATUS_INPROGRESS      = "INPROGRESS";
    public static final String STATUS_FUTURE          = "FUTURE";

    /*=================================================================================*/
    /*таблица teams*/
    public static final String TEAMS_M_ID          = "T_ID";
    public static final String TEAMS_TITLE         = "title";
    public static final String TEAMS_CITY          = "city";
    public static final String TEAMS_EMBLEM        = "emblem";
    public static final String TEAMS_SITE          = "site";
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
            + TEAMS_EMBLEM + " blob, "
            + TEAMS_SITE + " vchar, "
            + TEAMS_SEASON + " integer, "
            + TEAMS_WIN + " integer, "
            + TEAMS_DRAW + " datetime, "
            + TEAMS_LOST + " vchar);";

    /*=================================================================================*/
    /*таблица goals*/
    public static final String GOALS_G_ID          = "G_ID";
    public static final String GOALS_MINUTE        = "minute";
    public static final String GOALS_PLAYER_ID     = "player_id";
    public static final String GOALS_MATCH_ID      = "match_id";
    public static final String GOALS_TYPE          = "type";

    // запрос на создание таблицы goals
    public static final String CREATE_TABLE_GOALS = "create table "
            + TABLE_GOALS + "( "
            + GOALS_G_ID + " integer primary key autoincrement, "
            + GOALS_MINUTE + " time, "
            + GOALS_PLAYER_ID + " integer, "
            + GOALS_MATCH_ID + " integer, "
            + GOALS_TYPE + " vchar);";

    /*CREATE TABLE goals (
            G_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            minute time,
            player_id INTEGER,
            match_id INTEGER,
            type vchar);*/
    /*=================================================================================*/
    public static final String PLAYERS_P_ID         = "P_ID";
    public static final String PLAYERS_FIRST_NAME   = "first_name";
    public static final String PLAYERS_SECOND_NAME  = "second_name";
    public static final String PLAYERS_BIRTH        = "birth";
    public static final String PLAYERS_COUNTRY      = "country";
    public static final String PLAYERS_HEIGHT       = "height";
    public static final String PLAYERS_WEIGHT       = "weight";
    public static final String PLAYERS_PHOTO        = "photo";
    public static final String PLAYERS_TEAM_ID      = "team_id";

    // запрос на создание таблицы players
    public static final String CREATE_TABLE_PLAYERS = "create table "
            + TABLE_PLAYERS + "( "
            + PLAYERS_P_ID + " integer primary key autoincrement, "
            + PLAYERS_FIRST_NAME + " vchar, "
            + PLAYERS_SECOND_NAME + " vchar, "
            + PLAYERS_BIRTH + " date, "
            + PLAYERS_COUNTRY + " vchar, "
            + PLAYERS_HEIGHT + " integer, "
            + PLAYERS_WEIGHT + " integer, "
            + PLAYERS_PHOTO + " blob, "
            + PLAYERS_TEAM_ID + " integer);";
    /*
    * CREATE TABLE players (
		P_ID INTEGER PRIMARY KEY AUTOINCREMENT,
	    first_name varchar,
    	last_name varchar,
		birth date,
    	country varchar,
	    height INTEGER,
    	weight INTEGER,
	    photo blob,
	    team_id INTEGER);*/

    /*=================================================================================*/
    /*таблица staff*/
    public static final String STAFF_ID          = "S_ID";
    public static final String STAFF_PLAYER_ID   = "player_id";
    public static final String STAFF_POSITION    = "position";
    public static final String STAFF_SEASON      = "season";
    public static final String STAFF_TEAM_ID     = "team_id";

    // запрос на создание таблицы goals
    public static final String CREATE_TABLE_STAFF = "create table "
            + TABLE_STAFF + "( "
            + STAFF_ID + " integer primary key autoincrement, "
            + STAFF_PLAYER_ID + " integer, "
            + STAFF_POSITION + " vchar, "
            + STAFF_SEASON + " integer, "
            + STAFF_TEAM_ID + " integer);";

    /*CREATE TABLE staff (
            S_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            player_id INTEGER,
            position vchar,
            season INTEGER,
            team_id integer);*/
    /*=================================================================================*/
}

