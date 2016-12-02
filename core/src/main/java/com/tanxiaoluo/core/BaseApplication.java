package com.tanxiaoluo.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.orm.SugarApp;
import com.orm.SugarContext;

import java.util.List;

import timber.log.Timber;

/**
 * Delivery
 * Created by Malei on 16/7/1.
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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

        SugarContext.init(this);
        Timber.plant(new StorePlantTree(BuildConfig.DEBUG));
        CrashHandler.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    /**
     * 判断主进程, 用于避免一些重复进程的注册, 比如推送
     *
     * @return boolean
     */
    protected boolean shouldInit() {
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
