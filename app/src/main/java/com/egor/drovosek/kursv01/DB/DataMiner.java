package com.egor.drovosek.kursv01.DB;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;

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
       http://football.by/stat/belarus/2016/schedule.html
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
    --------------------------------------------------*/

    public int populateSchedule(int inSeason, int inRound) {
        int RetCode = 0;

        /*todo: добавить проверку есть ли такие данные в таблице*/
        GrabTeamsTask mt = new GrabTeamsTask();
        mt.execute(String.valueOf(inSeason), String.valueOf(inRound));

        return RetCode;
    }
    class GrabMatchesTask extends AsyncTask<String, String, Void>
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

}
