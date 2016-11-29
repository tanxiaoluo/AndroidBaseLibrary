package com.tanxiaoluo.core;

import android.app.Application;

import com.orm.SugarApp;
import com.orm.SugarContext;

import timber.log.Timber;

/**
 * Delivery
 * Created by Malei on 16/7/1.
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Timber.plant(new StorePlantTree(BuildConfig.DEBUG));
        CrashHandler.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
