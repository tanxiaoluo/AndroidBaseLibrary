package com.tanxiaoluo.core;

import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CrashHandler
 * Created by Malei on 16/3/26.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String PATH = "/" + BuildConfig.APPLICATION_ID + "/crash";
    private static final String TAG = CrashHandler.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static void init() {
        sInstance.mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(sInstance);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveToSDCard(ex);
        if (mDefaultExceptionHandler != null) {
            if (DEBUG) {
                Log.d(TAG, "交给系统去结束我们的程序");
            }
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        } else {
            if (DEBUG) {
                Log.d(TAG, "自己结束程序");
            }
            Process.killProcess(Process.myPid());
        }
    }

    private void saveToSDCard(Throwable ex) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (DEBUG) {
                Log.d(TAG, "SD卡无法使用");
            }
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + PATH);
        if (!file.exists() && !file.mkdirs()) {
            if (DEBUG) {
                Log.d(TAG, "创建crash目录错误");
            }
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            file = new File(file, "MainThread_" + System.currentTimeMillis() + ".crash");
        } else {
            file = new File(file, "ChildThread_" + System.currentTimeMillis() + ".crash");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));

            // 版本
            writer.write("Version: " + BuildConfig.VERSION_NAME);
            writer.newLine();
            writer.write("Build: " + BuildConfig.VERSION_CODE);
            writer.newLine();

            // 硬件
            writer.write("Android: " + Build.VERSION.RELEASE);
            writer.newLine();
            writer.write("CPU ABI: " + Build.CPU_ABI);
            writer.newLine();
            writer.write("Vendor: " + Build.MANUFACTURER);
            writer.newLine();
            writer.write("MODEL: " + Build.MODEL);
            writer.newLine();

            // 时间
            DateFormat format = new SimpleDateFormat(PATTERN, Locale.CHINA);
            writer.write("Date: " + format.format(new Date()));
            writer.newLine();
            writer.newLine();

            // 堆栈信息
            writer.write(Log.getStackTraceString(ex));
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
