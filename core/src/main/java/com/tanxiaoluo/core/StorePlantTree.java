package com.tanxiaoluo.core;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * StorePlantTree
 * Created by Malei on 16/3/15.
 */
public class StorePlantTree extends Timber.DebugTree {

    private static final String TAG = StoreWorker.class.getSimpleName();
    private static final String PATTERN_NAME = "yyyy-MM-dd-HH";
    private static final String PATTERN_LOG = "mm:ss.SSS";
    private final ExecutorService mThreadPool = Executors.newSingleThreadExecutor();
    private boolean mDebug;

    public StorePlantTree(boolean debug) {
        mDebug = debug;
    }

    @Override
    protected boolean isLoggable(int priority) {
        return mDebug;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, tag, message, t);
        mThreadPool.execute(new StoreWorker(priority, tag, message, t));
    }

    private class StoreWorker implements Runnable {

        private final int priority;
        private final String tag;
        private final String message;
        private final Throwable t;

        public StoreWorker(int priority, String tag, String message, Throwable t) {
            this.priority = priority;
            this.tag = tag;
            this.message = message;
            this.t = t;
        }

        @Override
        public void run() {
            File logFile = getCurrentLogFile();
            if (logFile == null) {
                return;
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(logFile, true));
                DateFormat format = new SimpleDateFormat(PATTERN_LOG, Locale.CHINA);
                writer.write(format.format(new Date()) + " | "
                        + priority + " | "
                        + tag + " | "
                        + message + " | "
                        + Log.getStackTraceString(t));
                writer.newLine();
            } catch (IOException e) {
                Log.e(TAG, "日志存储异常: " + e.getMessage());
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

        private File getCurrentLogFile() {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Log.e(TAG, "无外部存储设备");
                return null;
            }
            File file = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
            if (!file.exists() && !file.mkdirs()) {
                Log.e(TAG, "创建目录错误");
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_NAME, Locale.CHINA);
            return new File(file, dateFormat.format(new Date()) + ".log");
        }
    }
}
