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

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyDropBox()
                            .penaltyLog()
                            .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyDropBox()
                            .penaltyLog()
                            .build());
            Stetho.initializeWithDefaults(this);
        }

        mActivityLifecycle = new ActivityLifecycle();
        registerActivityLifecycleCallbacks(mActivityLifecycle);


        if (!isMainProcess()) {
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


    /**
     * 判断主进程, 用于避免一些重复进程的注册, 比如推送
     *
     * @return boolean
     */
    protected boolean isMainProcess() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            Timber.i("processinfo pid = %s, processName = %s", info.pid, info.processName);
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
