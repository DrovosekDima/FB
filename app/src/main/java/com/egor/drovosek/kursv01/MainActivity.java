package com.egor.drovosek.kursv01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.NewsTab.NewsTabFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ScheduleTab.ScheduleFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.TableStats.TableTabFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.TeamMatchesTab.TeamMatchesFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.TeamStaffTab.TeamStaffFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.TeamSummaryTab.TeamSummaryFragment;
import com.egor.drovosek.kursv01.MainWindowTabFragments.ViewPagerAdapter;
import com.egor.drovosek.kursv01.Misc.DataMinerWorkerThread;
import com.egor.drovosek.kursv01.Misc.ExpandableListAdapter;
import com.egor.drovosek.kursv01.Misc.GrabMatchesWithGoalsRunnable;
import com.egor.drovosek.kursv01.Misc.GrabTeamsRunnable;
import com.egor.drovosek.kursv01.Misc.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_BESTPLAYER;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_SCHED_ROUND;
import static com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab.BestPlayersTabFragment.LOADER_STATISTICS;
import static com.egor.drovosek.kursv01.SettingsActivity.KEY_PREF_SEASON;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GRAB_TEAM_COMPLETED = 1;
    public static final int GRAB_MATCHES_COMPLETED = 2;

    //todo: create global class
    public static int gdSeason = 2016;
    public static int gdNumberOfRounds = 0; //todo определить количество сезонов
    public static UIHandler mUIHandler;

    //private SectionsPagerAdapter mSectionsPagerAdapter;
    private ProgressDialog progressState;
    private ViewPager mViewPager;
    public ActionBarDrawerToggle toggle;

    View view_Group;
    private DrawerLayout mDrawerLayout;
    TabLayout tabLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<Team>> listDataChild;
    public String TAG = "MainActivity";
    public static DataMinerWorkerThread dataMinerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPrefs.getString(KEY_PREF_SEASON, "");
        gdSeason = Integer.valueOf(value);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        TextView mainTitle = (TextView) findViewById(R.id.main_title);
        mainTitle.setText(getString(R.string.title) + " " +gdSeason);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        prepareLeftMenuItems();

        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView,
                                        View view,
                                        int groupPosition,
                                        int childPosition, long id) {
                Log.d("DEBUG", "submenu item clicked");
                /*Toast.makeText(MainActivity.this,
                        "Header: "+String.valueOf(groupPosition) +
                                "\nItem: "+ String.valueOf(childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                view.setSelected(true);
                if (view_Group != null) {
                    view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                view_Group = view;
                view_Group.setBackgroundColor(Color.parseColor("#DDDDDD"));
                mDrawerLayout.closeDrawers();

                //* - отключить Drawer
                //mDrawerLayout.isDrawerVisible((DrawerLayout) findViewById(R.id.drawer_layout));
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
                toggle.setDrawerIndicatorEnabled(false);

                // получить имя комманды
                //ExpandableListAdapter tempMenuAdapter =  (ExpandableListAdapter)expandableList.getAdapter();
                Team element = (Team) mMenuAdapter.getChild(groupPosition, childPosition);

                // Поменять заголовок
                TextView mainTitle = (TextView) findViewById(R.id.main_title);
                mainTitle.setText(element.getTitle());

                /*Переключение на экран с информацией о комманде
                * - удалить предыдущие вкладки;
                * - создать новые вкладки с инфой о комманде
                * - уведомить, что адаптер изменился
                * - отключить Drawer
                * - добавить "Back" кнопку вместо Drawer*/

                ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();

                // 1. удалить предыдущие вкладки;

                int count = adapter.getCount();
                for (int i = (count-1) ; i > -1; i--) {
                    Log.d("DEBUG", "Del tab " + i);

                    Object obj = adapter.instantiateItem(mViewPager, i);

                    if (obj != null) {
                        adapter.destroyItem(mViewPager, i, obj);
                    }

                }

                adapter.notifyDataSetChanged();


                // 2. создать новые вкладки с инфой о комманде

                Bundle args = new Bundle();
                args.putString("teamName", element.getTitle());

                Fragment fTeamSummary = new TeamSummaryFragment();
                Fragment fTeamMatches = new TeamMatchesFragment();
                Fragment fTable = new TableTabFragment();
                Fragment fTeamStaff = new TeamStaffFragment();

                fTeamMatches.setArguments(args);
                fTeamSummary.setArguments(args);

                adapter.addFragment(fTeamSummary, "Обзор");
                adapter.addFragment(fTeamMatches, "Матчи");
                adapter.addFragment(fTable, "Таблица");
                adapter.addFragment(fTeamStaff, "Состав");
                //tabLayout.

                adapter.notifyDataSetChanged();

                 //* - добавить "Back" кнопку вместо Drawer*/
                return false;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id)
            {
                switch (groupPosition)
                {
                    case 0: break; //teams

                    case 1: //settings
                        Intent modifySettings=new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(modifySettings);
                        break;

                    case 2: break; //feedback

                    case 3: finish(); break; //exit

                    default: break;
                }
                Log.d("DEBUG", "heading clicked");
                return false;
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mUIHandler = new UIHandler();

        dataMinerThread = new DataMinerWorkerThread("DataMinerThread", mUIHandler);

        dataMinerThread.postTask(new GrabTeamsRunnable(mUIHandler, getApplicationContext()));

        dataMinerThread.postTask(new GrabMatchesWithGoalsRunnable(mUIHandler, getApplicationContext()));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TableTabFragment(), "ТАБЛИЦА");
        adapter.addFragment(new ScheduleFragment(), "РАСПИСАНИЕ");
        adapter.addFragment(new NewsTabFragment(), "НОВОСТИ");
        adapter.addFragment(new BestPlayersTabFragment(), "БОМБАРДИРЫ");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
            toggle.setDrawerIndicatorEnabled(true);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //нажали кнопку обновить данные (на первом шаге получаем данные)
        if (id == R.id.action_refresh) {
            Toast.makeText(MainActivity.this,"Refresh started!", Toast.LENGTH_SHORT).show();

            //progressState = ProgressDialog.show(MainActivity.this, "", "Воруем данные с football.by...");
            /*new Thread() {
                public void run() {
                    //grab data from football.by
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    mUIHandler.sendEmptyMessage(0);
                }
            }.start();*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //public Handler mUIHandler = new Handler()
    public class UIHandler extends Handler{
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch(msg.what) {
                case GRAB_TEAM_COMPLETED:
                    Toast.makeText(MainActivity.this, "Got GRAB_TEAM_COMPLETE", Toast.LENGTH_SHORT).show();

                    FootballDBHelper mDB = new FootballDBHelper(getApplicationContext());
                    List<Team> teamsList = mDB.getListTeams(gdSeason);
                    mDB.close();
                    listDataChild.remove(listDataHeader.get(0));
                    listDataChild.put(listDataHeader.get(0), teamsList);
                    mMenuAdapter.notifyDataSetChanged();

                    /*Loader lmLtemp = getSupportLoaderManager().getLoader(LOADER_STATISTICS);
                    if (lmLtemp !=null) {
                        Log.i(TAG, "mUIHandler: forceLoad with loader #" + LOADER_STATISTICS);
                        lmLtemp.forceLoad();
                    }
                    else
                        Log.i(TAG, "mUIHandler: loader #" + LOADER_STATISTICS + " is not init. Skip it.");*/

                    break;

                case GRAB_MATCHES_COMPLETED:
                    Toast.makeText(MainActivity.this, "Got GRAB_MATCHES_COMPLETED", Toast.LENGTH_SHORT).show();

                    // надо обновить данные на закладках
                     // -Stats
                     //  -BestPlayers
                    Log.i(TAG, "mUIHandler: got GRAB_MATCHES_COMPLETED");
                    Log.i(TAG, "mUIHandler: forceLoad with loader #" + LOADER_BESTPLAYER);
                    Loader lmL = getSupportLoaderManager().getLoader(LOADER_BESTPLAYER);
                    if (lmL !=null)
                       lmL.forceLoad();
                    else
                        Log.i(TAG, "mUIHandler: loader #" + LOADER_BESTPLAYER + " is not init. Skip it.");


                    Loader lmL2 = getSupportLoaderManager().getLoader(LOADER_STATISTICS);
                    if (lmL2 !=null) {
                        Log.i(TAG, "mUIHandler: forceLoad with loader #" + LOADER_STATISTICS);
                        lmL2.forceLoad();
                    }
                    else
                        Log.i(TAG, "mUIHandler: loader #" + LOADER_STATISTICS + " is not init. Skip it.");

                    Loader lmL3 = getSupportLoaderManager().getLoader(LOADER_SCHED_ROUND);
                    if (lmL3 !=null) {
                        Log.i(TAG, "mUIHandler: forceLoad with loader #" + LOADER_SCHED_ROUND);
                        lmL3.forceLoad();
                    }
                    else
                        Log.i(TAG, "mUIHandler: loader #" + LOADER_SCHED_ROUND + " is not init. Skip it.");

                    TextView mainTitle = (TextView) findViewById(R.id.main_title);
                    mainTitle.setText(getString(R.string.title) + " " +gdSeason);

                    break;

                default:
                    Bundle bundle = msg.getData();
                    String dataFromMSG = bundle.getString("mydata");

                    Log.i("MAIN", "mUIHandler got message " + dataFromMSG);
                    Toast.makeText(MainActivity.this, dataFromMSG, Toast.LENGTH_SHORT).show();

                    break;
            }
            //progressState.dismiss();
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_teams_) {
        } else if (id == R.id.nav_settings_) {

        } else if (id == R.id.nav_exit) {
            finish();
        } else if (id == R.id.nav_rate_) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareLeftMenuItems() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Team>>();

        // Adding data header
        listDataHeader.add(getString(R.string.nav_teams));
        listDataHeader.add(getString(R.string.nav_settings));
        listDataHeader.add(getString(R.string.nav_rate));
        listDataHeader.add(getString(R.string.nav_exit));

        FootballDBHelper mDB = new FootballDBHelper(getApplicationContext());

        List<Team> teamsList = mDB.getListTeams(gdSeason);

        mDB.close();

        listDataChild.put(listDataHeader.get(0), teamsList);// Header, Child data
    }

}
