package com.egor.drovosek.kursv01.DB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

import static com.egor.drovosek.kursv01.DB.Schema.STATUS_COMPLETED;

/**
 * Created by Drovosek on 31/01/2017.
 */

public class DataMiner {
    public FootballDBHelper mDB;
    Context cont;

    public DataMiner(Context inContext)
    {

        cont = inContext;
        mDB = new FootballDBHelper(inContext);
    }

    /*-------------------------------------------------
    - Идем по адресу
       http://football.by/stat/belarus/2015/teams/
       http://football.by/stat/belarus/2016/teams/
       http://football.by/stat/belarus/2017/teams/
    - извлекаем информацию
    - заносим в базу teams
    --------------------------------------------------*/

    public int populateTeam(int inSeason) {
        int RetCode = 0;

        /*todo: добавить проверку есть ли такие данные в таблице*/
        GrabTeamsTask mt = new GrabTeamsTask();
        mt.execute(String.valueOf(inSeason));

        return RetCode;
    }

    class GrabTeamsTask extends AsyncTask<String, Void, Void>
    {

        String title;//Тут храним значение заголовка сайта
        List<Element> teams;
        String address; //адрес страницы http://football.by...
        Element item;
        int inSeason;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(cont, "Получение данных ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            inSeason = Integer.valueOf(params[0].toString());
            Document doc = null;//Здесь хранится будет разобранный html документ
            Element elem;
            try
            {
                //Считываем страницу http://football.by/stat/belarus/"inSeason"/teams/
                String test = "http://football.by/stat/belarus/" + params[0].toString() + "/teams";
                doc = Jsoup.connect(test).get();
            }
            catch (IOException e)
            {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (doc != null)
                teams = doc.select(".st-teams-team");
            else
                title = "Ошибка";

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            int i = 0;
            title = "";

            while (i < teams.size())
            {
                String teamName = teams.get(i).select(".alt").text();
                teamName = teams.get(i).text();

                ContentValues teamTemp = mDB.createTeamValue(
                        teamName,
                        teamName,
                        null,
                        "",
                        inSeason);
                mDB.addTeam(teamTemp);
                teamTemp.clear();

            /*item = teams.get(i);

            if (item != null)
            {
                List<Element> elem = item.select(".matchdesc");

                dateTime = elem.get(1).select(".md-wideleft").text();
                onSite = elem.get(1).select(".md-wideright").text();

                homeGoals = elem.get(2).select(".md-wideleft").text();
                guestGoals = elem.get(2).select(".md-wideright").text();
            }*/

                i++;
            }
        }
    }

    /*-------------------------------------------------
    - Идем по адресу
1.       http://football.by/stat/belarus/2016/schedule.html
        Данные представлены в виде:

        1 тур
            01.04.2016 18:30 Ислочь (Минский р-он) - Нафтан (Новополоцк) 2:0   Отчет Стат
            02.04.2016 14:00 Динамо (Брест) - БАТЭ (Борисов) 1:4     Стат
            02.04.2016 14:30 Белшина (Бобруйск) - Крумкачы (Минск) 1:1     Стат
            02.04.2016 16:00 Славия (Мозырь) - Слуцк 1:1     Стат
            02.04.2016 17:00 Торпедо-БелАЗ (Жодино) - Динамо (Минск) 0:2     Стат
            02.04.2016 17:00 Городея - Шахтер (Солигорск) 0:1     Стат
            02.04.2016 19:00 Минск - Витебск 1:0     Стат
            03.04.2016 16:00 Неман (Гродно) - Гранит (Микашевичи) 1:1     Стат
2. или по адресу
     http://football.by/stat/belarus/2016/1/
    - извлекаем информацию
        MATCHES_HOME_TEAM_ID    = "home_team_id";
        MATCHES_GUEST_TEAM_ID   = "guest_team_id";
        MATCHES_SCORE_HOME      = "score_home";
        MATCHES_SCORE_GUEST     = "score_guest";
        MATCHES_ROUND           = "round";
        MATCHES_SEASON          = "season";
        MATCHES_DATEM           = "datem";
        MATCHES_LOCATION        = "location";
    - заносим в базу matches
    - и заодно извлекаем инфо о голах
    --------------------------------------------------*/
    /*извлекаем данные использую AsyncTask, то есть в background*/
    public int populateScheduleWithoutGoalsBG(int inSeason, int inRound) {
        int RetCode = 0;

        /*todo: добавить проверку есть ли такие данные в таблице*/
        GrabMatchesTask mt = new GrabMatchesTask(cont);
        mt.execute(String.valueOf(inSeason), String.valueOf(inRound));

        return RetCode;
    }

    /*извлекаем данные  без AsyncTask, то есть в foreground*/
    public int populateScheduleWithoutGoalsFG(int inSeason, int inRound)
    {
        int RetCode = 0;
        String title;//Тут храним значение заголовка сайта
        List<Element> matches;
        String address; //адрес страницы http://football.by...
        Element item;
        Elements tables;
        /*todo: добавить проверку есть ли такие данные в таблице*/

        Document doc = null;//Здесь хранится будет разобранный html документ
        Element elem;
        try
        {
            //Считываем страницу http://football.by/stat/belarus/"inSeason"/schedule.html
            String test = "http://football.by/stat/belarus/" + String.valueOf(inSeason) + "/schedule.html";

            // Информация о каждом туре хранится на след. страницах:
            // http://football.by/stat/belarus/2016/1/
            // http://football.by/stat/belarus/"inSeason"/"inRound"/
            //String test = "http://football.by/stat/belarus/" + params[0].toString() + "/" + params[1].toString();

            doc = Jsoup.connect(test).get();
        }
        catch (IOException e)
        {
            //Если не получилось считать
            e.printStackTrace();
        }

        //Если всё считалось, что вытаскиваем из считанного html документа заголовок
        if (doc != null)
            tables = doc.select("table");
        else {
            title = "Ошибка";
            return -1;
        }

          /* обработка страницы (без информации о голах)
            http://football.by/stat/belarus/"inSeason"/schedule.html*/

        int i = 0;
        String roundStr;
        int currentRound = 1;
        String currentRoundStr = "0";

        Element tableMatch = tables.get(1); //вторая таблица содержит всю информацию
        Elements rows = tableMatch.select("tr");

        for (i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            if (columns.size()==1)
            {
                    /*deal with 0. номер тура "sch-title"
                                1.          "colspan"*/
                roundStr = columns.get(0).text();
                if (roundStr.endsWith("тур"))
                {
                    String test = roundStr.substring(0, 2);
                    String strRound = test.trim();
                    currentRoundStr = strRound;
                    currentRound = Integer.valueOf(strRound);
                }
            }
            else
            {
                // должно быть 8 матчей
                    /*1. sch-date
                         sch-time
                         sch-teams
                         sch-score
                         sch-status*/
                if (currentRound == inRound)
                {
                    String date = columns.get(0).text(); //sch-date
                    String time = columns.get(1).text(); //sch-time

                    date = date + " " + time;

                    //teams
                    Element teams = columns.get(2);
                    String homeTeam = teams.child(0).text();
                    String guestTeam = teams.child(1).text();
                    //end teams

                    String score = columns.get(3).text(); //2:0

                    String retval[] = score.split(":");
                    String homeScore = retval[0];
                    String guestScore = retval[1];

                    //todo: add check for status, if current date > match.date then complete
                    ContentValues matchTemp = mDB.createMatchValue(
                            homeTeam,
                            guestTeam,
                            homeScore,
                            guestScore,
                            String.valueOf(inSeason),
                            currentRoundStr,
                            date,
                            "unknown",
                            STATUS_COMPLETED
                    );

                    mDB.addMatch(matchTemp);
                    matchTemp.clear();

                }
                else if (currentRound > inRound)
                {
                    break;
                }

            }
        }
        return RetCode;
    }

    class GrabMatchesTask extends AsyncTask<String, String, Void>
    {

        String title;//Тут храним значение заголовка сайта
        List<Element> matches;
        String address; //адрес страницы http://football.by...
        Element item;
        Elements tables;
        int inSeason;
        int inRound;

        private Activity activity;
        private ProgressDialog dialog;
        private Context context;

        public GrabMatchesTask(Context inContext) {
            this.context = inContext;
            this.dialog = new ProgressDialog(context);
            this.dialog.setTitle("Загрузка");
            this.dialog.setMessage("Информации о туре...");

            /*if(!this.dialog.isShowing()){
                this.dialog.show();
            }*/
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //Toast.makeText(cont, "Получение данных ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            inSeason = Integer.valueOf(params[0].toString());
            inRound =  Integer.valueOf(params[1].toString());
            Document doc = null;//Здесь хранится будет разобранный html документ
            Element elem;
            try
            {
                //Считываем страницу http://football.by/stat/belarus/"inSeason"/schedule.html
                String test = "http://football.by/stat/belarus/" + params[0].toString() + "/schedule.html";

                // Информация о каждом туре хранится на след. страницах:
                // http://football.by/stat/belarus/2016/1/
                // http://football.by/stat/belarus/"inSeason"/"inRound"/
                //String test = "http://football.by/stat/belarus/" + params[0].toString() + "/" + params[1].toString();

                doc = Jsoup.connect(test).get();
            }
            catch (IOException e)
            {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (doc != null)
                tables = doc.select("table");
            else
                title = "Ошибка";

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

          /* обработка страницы (без информации о голах)
            http://football.by/stat/belarus/"inSeason"/schedule.html*/

            int i = 0;
            String roundStr;
            int currentRound = 1;
            String currentRoundStr = "0";

            Element tableMatch = tables.get(1); //вторая таблица содержит всю информацию
            Elements rows = tableMatch.select("tr");

            for (i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements columns = row.select("td");
                if (columns.size()==1)
                {
                    /*deal with 0. номер тура "sch-title"
                                1.          "colspan"*/
                     roundStr = columns.get(0).text();
                     if (roundStr.endsWith("тур"))
                     {
                         String test = roundStr.substring(0, 2);
                         String strRound = test.trim();
                         currentRoundStr = strRound;
                         currentRound = Integer.valueOf(strRound);
                     }
                }
                else
                {
                    // должно быть 8 матчей
                    /*1. sch-date
                         sch-time
                         sch-teams
                         sch-score
                         sch-status*/
                    if (currentRound == inRound)
                    {
                        String date = columns.get(0).text(); //sch-date
                        String time = columns.get(1).text(); //sch-time

                        date = date + " " + time;

                        //teams
                        Element teams = columns.get(2);
                        String homeTeam = teams.child(0).text();
                        String guestTeam = teams.child(1).text();
                        //end teams

                        String score = columns.get(3).text(); //2:0

                        String retval[] = score.split(":");
                        String homeScore = retval[0];
                        String guestScore = retval[1];

                        //todo: add check for status, if current date > match.date then complete
                        ContentValues matchTemp = mDB.createMatchValue(
                                homeTeam,
                                guestTeam,
                                homeScore,
                                guestScore,
                                String.valueOf(inSeason),
                                currentRoundStr,
                                date,
                                "unknown",
                                STATUS_COMPLETED
                                );

                        mDB.addMatch(matchTemp);
                        matchTemp.clear();

                    }
                    else if (currentRound > inRound)
                    {
                        break;
                    }

                }
            }

            //this.dialog.dismiss();

        }
    }

}
