package com.egor.drovosek.kursv01.Misc;

import android.app.Application;
import android.content.Context;

/**
 * Created by Drovosek on 31/01/2017.
 */

public class MyAppContext extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }

}
