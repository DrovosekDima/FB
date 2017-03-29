package com.egor.drovosek.kursv01.DB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.egor.drovosek.kursv01.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.*;

import static com.egor.drovosek.kursv01.DB.Schema.STATUS_COMPLETED;
import static com.egor.drovosek.kursv01.DB.Schema.STATUS_FUTURE;
import static com.egor.drovosek.kursv01.MainActivity.gdNumberOfRounds;

/**
 * Created by Drovosek on 31/01/2017.
 */

public class DataMiner {
    public FootballDBHelper mDB;
    Context cont;
    String TAG ="DataMiner()";

    public DataMiner(Context inContext)
    {

        cont = inContext;
        mDB = new FootballDBHelper(inContext);
    }

    public void grabTeam(int inSeason)
    {

        String title;//Тут храним значение заголовка сайта
        List<Element> teams;
        String address; //адрес страницы http://football.by...
        Element item;

        Elements tables;

        Document doc = null;//Здесь хранится будет разобранный html документ
        Element elem;
        try
        {
            //Считываем страницу http://football.by/stat/belarus/"inSeason"/teams/
            //String test = "http://football.by/stat/belarus/" + params[0].toString() + "/teams";
            String test = "http://fbdata.ucoz.net/";
            doc = Jsoup.connect(test).get();
        }
        catch (IOException e)
        {
            //Если не получилось считать
            e.printStackTrace();
        }

        //Если всё считалось, что вытаскиваем из считанного html документа заголовок
        if (doc != null)
            //teams = doc.select(".st-teams-team");
            tables = doc.select("table");
        else {
            title = "Ошибка";
            return;
        }

            /*вариант для http://fbdata.ucoz.net*/
        //todo обработка номера сезона

        Element tblOneSeason = tables.get(0); //первая таблица содержит комманды за 2016
        Elements rows = tblOneSeason.select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            String teamName = columns.get(0).text();
            String city = columns.get(1).text();
            Element image = columns.get(2).select("img").first();
            String imgUrl = image.absUrl("src");

            Bitmap teamLogo = getImageBitmap(imgUrl);

            ContentValues teamTemp = mDB.createTeamValue(
                    teamName,
                    city,
                    teamLogo,
                    "",
                    inSeason);

            mDB.addTeam(teamTemp);
        }

        return;
    }

    public void grabTeamFromFootballby(int inSeason)
    {

        String title;//Тут храним значение заголовка сайта
        List<Element> teams;
        String address; //адрес страницы http://football.by...
        Element item;

        Elements tables;

        Document doc = null;//Здесь хранится будет разобранный html документ
        Element elem;
        try
        {
            //Считываем страницу http://football.by/stat/belarus/"inSeason"/teams/
            String test = "http://football.by/stat/belarus/" + String.valueOf(inSeason) + "/teams";
            doc = Jsoup.connect(test).get();
        }
        catch (IOException e)
        {
            //Если не получилось считать
            e.printStackTrace();
        }

        //Если всё считалось, что вытаскиваем из считанного html документа заголовок
        if (doc != null)
            teams = doc.select(".st-teams-logo");
        else {
            title = "Ошибка";
            return;
        }


        for (int i = 0; i < teams.size(); i++) {

            Element itemTeam = teams.get(i);

            Element image = itemTeam.select("img").first();
            String imgUrl = image.absUrl("src");
            Bitmap teamLogo = getImageBitmap(imgUrl);

            String teamName = image.attr("title");

            Element link = itemTeam.select("a").first();
            String absUrl = link.absUrl("href");

            ContentValues teamTemp = mDB.createTeamValue(
                    teamName,
                    "",
                    teamLogo,
                    "",
                    inSeason);

            mDB.addTeam(teamTemp);

             /*
             Для Городеи
             absUrl = http://football.by/stat/belarus/2016/teams/39/

             идем по этому адресу и скачиваем следующую информацию
             - Сайт: www.fcgorodeya.by
             - Домашний стадион: Стадион "Городея" (Городея, вместимость: 1020)
             - ГЛАВНЫЙ ТРЕНЕР
                Сергей Валерьевич Яромко
             - ИГРОКИ
                вра		Скиндерис Симас
                защ		Колпачук Илья
                пзщ		Воловик Юрий
                нап		Ковб Дмитрий
              */

            grabStaffFromFootballby(inSeason, teamName, absUrl);

            //todo обновить URL site комманды
        }

        return;
    }

    public void grabStaffFromFootballby(int inSeason, String team, String urlSite)
    {
        Elements teamFullInfo; //<div class="st-teams-full-info"><div class="st-teams-full-name">Городея</div><b>Сайт:</b> <noindex><a href="http://www.fcgorodeya.by/" target="_blank" rel="external nofollow">www.fcgorodeya.by</a></noindex><br><br><b>Домашний стадион: </b><br>Стадион "Городея" (Городея, вместимость: 1020)<br></div>
        Elements teamCoach; //<div class="st-teams-full-coach"><div class="st-teams-full-coach-name"><img src="/stat/getimage.php?flagid=1" alt="" title="Беларусь"> Сергей Валерьевич Яромко</div><div class="st-teams-full-coach-data">Дата рождения: 07.04.1967<br>Период работы в команде: 14.05.2014 - по настоящее время</div>
        Elements tables;

        Document doc = null;//Здесь хранится будет разобранный html документ
        Element elem;
        try
        {
            //Считываем страницу http://football.by/stat/belarus/2016/teams/39/
            doc = Jsoup.connect(urlSite).get();
        }
        catch (IOException e)
        {
            //Если не получилось считать
            e.printStackTrace();
        }

        //Если всё считалось, что вытаскиваем из считанного html документа заголовок
        if (doc != null) {
            teamFullInfo = doc.select(".st-teams-full-info");
            teamCoach = doc.select(".st-teams-full-coach");
            tables    = doc.select("table");
        }
        else {
            return;
        }

        Element stadium = teamFullInfo.first();
        String stadAndUrl = stadium.text();

        Elements coach = teamCoach.select(".st-teams-full-coach-name");
        String coachStr = coach.first().text();

        Element tableMatch = tables.get(1); //вторая таблица содержит всю информацию
        Elements rows = tableMatch.select("tr");

             /*
             Для Городеи
             absUrl = http://football.by/stat/belarus/2016/teams/39/

             идем по этому адресу и скачиваем следующую информацию
             - Сайт: www.fcgorodeya.by
             - Домашний стадион: Стадион "Городея" (Городея, вместимость: 1020)
             - ГЛАВНЫЙ ТРЕНЕР
                Сергей Валерьевич Яромко
             - ИГРОКИ
                вра		Скиндерис Симас
                защ		Колпачук Илья
                пзщ		Воловик Юрий
                нап		Ковб Дмитрий
              */

            //todo обновить URL site комманды
            /*
               ContentValues teamTemp = mDB.createTeamValue(
                    teamName,
                    "",
                    teamLogo,
                    "",
                    inSeason);

              mDB.addTeam(teamTemp);
            */

        return;
    }

    // populateTeam - это реализация grabTeam with AsyncTask
    public int populateTeam(int inSeason) {
    /*-------------------------------------------------
    - Идем по адресу
       http://football.by/stat/belarus/2015/teams/
       http://football.by/stat/belarus/2016/teams/
       http://football.by/stat/belarus/2017/teams/
    - извлекаем информацию
    - заносим в базу teams
    --------------------------------------------------*/
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
        Elements tables;

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
                //String test = "http://football.by/stat/belarus/" + params[0].toString() + "/teams";
                String test = "http://fbdata.ucoz.net/";
                doc = Jsoup.connect(test).get();
            }
            catch (IOException e)
            {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (doc != null)
                //teams = doc.select(".st-teams-team");
                tables = doc.select("table");
            else
                title = "Ошибка";

            /*вариант для http://fbdata.ucoz.net*/
            //todo обработка номера сезона

            Element tblOneSeason = tables.get(0); //первая таблица содержит комманды за 2016
            Elements rows = tblOneSeason.select("tr");

            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements columns = row.select("td");
                String teamName = columns.get(0).text();
                String city = columns.get(1).text();
                Element image = columns.get(2).select("img").first();
                String imgUrl = image.absUrl("src");

                Bitmap teamLogo = getImageBitmap(imgUrl);

                ContentValues teamTemp = mDB.createTeamValue(
                        teamName,
                        city,
                        teamLogo,
                        "",
                        inSeason);

                mDB.addTeam(teamTemp);
            }
            //end processing fbdata.ucoz.net

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            /* рабочий вариант для http://football.by
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

                i++;
            }*/
        }
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            // See what we are getting
            Log.i(TAG, "" + url);

            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);

            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
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

    private class GrabMatchesTask extends AsyncTask<String, String, Void> {

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


    public void grabAllMatches(int inSeason)
    {
        int lastRound = mDB.getLastCompleteRound(inSeason);

        Log.i(TAG, "grabAllMatches: last round in season " + inSeason +" is " + lastRound);

        int ret = 0;
        int i = 0;
        //for(int i = lastRound + 1; i < MainActivity.gdNumberOfRounds+1; i++) {
        for(i = lastRound + 1; ret != -1; i++) {
            ret = populateScheduleWithGoalsAndPlayersFG(inSeason, i);
            Log.i(TAG, "grabAllMatches: round #" + i +" loaded.");
        }
        gdNumberOfRounds = i-1;

        return;
    }
    /*извлекаем данные  без AsyncTask, то есть в foreground
    * только для одного матча*/
    public int populateScheduleWithGoalsAndPlayersFG(int inSeason, int inRound)
    {
     /*==================================================================================
       - Идем по адресу
             http://football.by/stat/belarus/2016/1/
             http://football.by/stat/belarus/inSeason/inRound/
      Данные представлены в виде:
       ---------------------------------------------------------
        Белшина (Бобруйск) 1:1 Крумкачы (Минск)
        02.04.2016. 14:30  Бобруйск, Стадион им. А.Прокопенко
        + 4'
        Александр Юшин
                                36' +
                                Евгений Шикавка
       ---------------------------------------------------------

        - извлекаем информацию
           о матче (комманды, время, счет, место)
           о голах (игрок и время)
        - добавляем в таблицы
            1. matches
            2. goals
            3. players (если игрока нет в таблице)
        ==================================================================================*/

        int RetCode = 0;
        String title;//Тут храним значение заголовка сайта
        List<Element> matches;
        String address; //адрес страницы http://football.by...
        Element item;
        Elements teams = null;

        //todo: добавить проверку есть ли такие данные в таблице
        //

        Document doc = null;//Здесь хранится будет разобранный html документ
        Element elem;
        try
        {
            //Считываем страницу http://football.by/stat/belarus/"inSeason"/"inRound"/
            address = "http://football.by/stat/belarus/" +
                       String.valueOf(inSeason) + "/" +
                       String.valueOf(inRound) + "/";

            doc = Jsoup.connect(address).get();
        }
        catch (IOException e)
        {
            //Если не получилось считать
            e.printStackTrace();
            return -1;
        }

        //Если всё считалось, что вытаскиваем из считанного html документа заголовок
/*
<div class="matchdesc2"><div class="matchdesc md-status3" style="border-bottom: 1px solid #eee;">
    <div class="md-left">
          <img src="/stat/getimage.php?mode=thumb&teamid=22" alt="" style="float: left; padding-right: 5px; " />
          <a href="../teams/22/" title="Белшина (Бобруйск)">Белшина (Бобруйск)</a>
    </div>

    <div class="md-center"><a href="3/" class="md-scorelink" title="Подробно о матче">1:1</a><br /><span class="md-livestatus"></span>
    </div>

    <div class="md-right"><img src="/stat/getimage.php?mode=thumb&teamid=995" alt="" style="float: right; padding-left: 5px; " />
                          <a href="../teams/995/" title="Крумкачы (Минск)">Крумкачы (Минск)</a>
    </div></div>

    <div class="matchdesc" style="font-size: 0.9em;">
          <div class="md-wideleft"><span class="md-color1">02.04.2016. 14:30</span></div>
          <div class="md-wideright"><span class="md-color1">&nbsp;Бобруйск, Стадион им. А.Прокопенко</span></div>
    </div>

    <div class="matchdesc">
          <div class="md-wideleft">
          <div style="overflow:auto;"><div style="float:left; width:50px; height:18px;"><img src="/stat/getimage.php?iconid=1" alt="+" />
          <span class="md-color2"> 4'</span></div><div>Александр Юшин</div>
          </div>
          </div>

          <div class="md-wideright"><div style="overflow:auto;">
          <div style="float:right; width:50px; height:18px;">
          <span class="md-color2">36'</span> <img src="/stat/getimage.php?iconid=1" alt="+" /></div>
          <div>Евгений Шикавка</div></div></div></div>
          <div class="matchdata"><a href="3/" title="протокол">&bull; протокол</a></div>
</div>
*/
        if (doc != null)
            teams = doc.select(".matchdesc2");
        else {
            title = "Ошибка";
            return -1;
        }

        int i = 0;
        title = "";
        String dateTime = "";
        String onSite = "";
        String homeGoals = "";
        String guestGoals = "";
        List<String> homeGoalsPlayers = new LinkedList<String>();
        List<String> guestGoalsPlayers = new LinkedList<String>();
        String home;
        String guest;
        int matchID;
        int playerID;
        int homeTeamID;
        int guestTeamID;
        String status = STATUS_COMPLETED;

        if(teams == null || teams.size() < 1)
            return -1;

        while (i < teams.size())
        {
            status = STATUS_COMPLETED;

            String homeAndGuestgoals;

            home = teams.get(i).select(".md-left").text();
            guest = teams.get(i).select(".md-right").text();
            homeAndGuestgoals = teams.get(i).select(".md-center").text();
            if(homeAndGuestgoals.compareTo("-:-") == 0)
            {
                /*матч не начался
                    Днепр (Могилев) -:- Динамо (Минск)
                 */
                homeGoals = "-1";
                guestGoals = "-1";
                status = STATUS_FUTURE;
            }
            else {
                // счет может выглядеть  "0:3 тех.пор."
                String hgGoals[] = homeAndGuestgoals.split(":");
                //todo: добавить проверку на счет, если матч не начался то будет crash?
                String goals[] = hgGoals[0].split(" ");
                homeGoals = goals[0];
                goals = hgGoals[1].split(" ");
                guestGoals = goals[0];
            }

            item = teams.get(i);

            if (item != null)
            {
                List<Element> innerElem = item.select(".matchdesc");

                if (innerElem.size() < 3)
                    return -1; //форма на сайте не полная, просто выходим

                dateTime = innerElem.get(1).select(".md-wideleft").text();
                onSite = innerElem.get(1).select(".md-wideright").text();

                guestGoalsPlayers.clear();
                homeGoalsPlayers.clear();

                //есть голы
                if(innerElem.size()>2) {
                    Elements homeGoalsList = innerElem.get(2).select(".md-wideleft");

                    Elements playersName = homeGoalsList.get(0).children();

                    for (int cPl = 0; cPl < playersName.size(); cPl++)
                        homeGoalsPlayers.add(playersName.get(cPl).text());

                    Elements guestGoalsList = innerElem.get(2).select(".md-wideright");

                    playersName = guestGoalsList.get(0).children();

                    for (int cPl = 0; cPl < playersName.size(); cPl++)
                        guestGoalsPlayers.add(playersName.get(cPl).text()); // выглядит как: "4' Александр Юшин"
                    // надо выделить время, firstName and secondName
                }

            }

            /*Step 1: добавить матч в таблицу matches*/
            {
                //todo: add check for status, if current date > match.date then complete
                ContentValues matchTemp = mDB.createMatchValue(
                        home,
                        guest,
                        homeGoals,
                        guestGoals,
                        String.valueOf(inSeason),
                        String.valueOf(inRound),
                        dateTime,
                        onSite,
                        status);

                mDB.addMatch(matchTemp);
                // получить матч ID, который будет использоваться при добавлении гола в таблицу
                homeTeamID = mDB.getTeamID(home);
                guestTeamID = mDB.getTeamID(guest);
                matchID = mDB.getMatchID(homeTeamID, dateTime);

                matchTemp.clear();
            }
            /*Step 2: добавить голы в таблицу goals и игроков в таблицу players*/
            {
                // homeGoalsPlayers  [0] "4' Александр Юшин"
                //                   [1] "36' Евгений Шикавка" (п)

                // guestGoalsPlayers [0] "Дмитрий Н. Игнатенко (щ) 90+2'"
                //                   [1] "36' Евгений Шикавка"

                for (int j = 0; j < homeGoalsPlayers.size(); j++) {
                    String chunks[] = homeGoalsPlayers.get(j).split(" ");
                    String timeG = chunks[0];
                    String firstName = chunks[1];
                    String secondName = chunks[2];

                    if (chunks.length > 3) {
                        // проверить на (ш) - штрафной
                        //              (п) - пенальти
                        //              (аг) - автогол
                        if (chunks[3].equals("(ш)") || chunks[3].equals("(п)") || chunks[3].equals("(аг)"))
                        {
                            //skip
                        }
                        else
                           secondName = secondName + " " + chunks[3];
                    }


                    playerID = mDB.getPlayerID(firstName, secondName);

                    if (playerID < 0 ){
                        // такого игрока не существует и его надо добавить в таблицу
                        ContentValues player = mDB.createPlayerValue(firstName, secondName, 0, 0, null, null, null, homeTeamID);

                        mDB.addPlayer(player);

                        playerID = mDB.getPlayerID(firstName, secondName);
                    }

                    ContentValues goal = mDB.createGoalValue(timeG, playerID, matchID, "game");

                    mDB.addGoal(goal);
                }

                for (int j = 0; j < guestGoalsPlayers.size(); j++) {

                    String chunks[] = guestGoalsPlayers.get(j).split(" ");
                    String timeG = chunks[0];
                    String firstName = chunks[1];
                    String secondName = chunks[2];

                    if (chunks.length > 3) {
                        // проверить на (ш) - штрафной
                        //               (п) - пенальти
                        if (chunks[3].equals("(ш)") || chunks[3].equals("(п)"))
                        {
                            //skip
                        }
                        else
                            secondName = secondName + " " + chunks[3];
                    }

                    playerID = mDB.getPlayerID(firstName, secondName);

                    if (playerID < 0 ){
                        // такого игрока не существует и его надо добавить в таблицу
                        ContentValues player = mDB.createPlayerValue(firstName,
                                                                    secondName, 0, 0, null, null, null,
                                                                    guestTeamID);

                        mDB.addPlayer(player);

                        playerID = mDB.getPlayerID(firstName, secondName);
                    }

                    ContentValues goal = mDB.createGoalValue(timeG, playerID, matchID, "game");

                    mDB.addGoal(goal);
                }
            }

            i++;
        }

        return RetCode;
    }

}
