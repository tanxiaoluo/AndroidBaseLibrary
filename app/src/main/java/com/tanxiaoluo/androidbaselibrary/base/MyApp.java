package com.tanxiaoluo.androidbaselibrary.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.os.Process;

import com.facebook.stetho.Stetho;
import com.tanxiaoluo.androidbaselibrary.BuildConfig;
import com.tanxiaoluo.core.BaseApplication;

import java.util.List;

import timber.log.Timber;

public class MyApp extends BaseApplication {

    private static MyApp mApp;

    public static MyApp getApp() {
        return mApp;
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycle;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;

        mActivityLifecycle = new ActivityLifecycle();
        registerActivityLifecycleCallbacks(mActivityLifecycle);


        if (!shouldInit()) {
            Timber.d("this is no Main Process !");
            return;
        }

        // 注册一些只需要在主线程做的事情，比如推送等

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mActivityLifecycle);
    }
}
