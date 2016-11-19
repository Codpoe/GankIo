package me.codpoe.gankio.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.tencent.bugly.crashreport.CrashReport;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class GankIoApplication extends Application{

    private static final String DB_NAME = "gank.realm";
    private static final String DAY_NIGHT_MODE = "day_night_mode";
    private static final String NIGHT_MODE = "night_mode";
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    private static GankIoApplication instance;

    public static synchronized GankIoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Bugly
        CrashReport.initCrashReport(getApplicationContext(), "900057697", true);

        SharedPreferences preferences = getSharedPreferences(DAY_NIGHT_MODE, MODE_PRIVATE);
        changeDayNightMode(preferences.getBoolean(NIGHT_MODE, false));

        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().name(DB_NAME).build());

        getScreenSize();
    }


    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    public void changeDayNightMode(boolean yes) {
        if (yes) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
