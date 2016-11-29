package com.tanxiaoluo.core.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String CLIENT_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String TODAY_FORMAT = "HH:mm";

    /**
     * 服务器日期格式转为客户端日期格式
     *
     * @param serverDate
     * @return
     */
    public static String fromServerTime(String serverDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        try {
            format.applyPattern(SERVER_FORMAT);
            Date parse = format.parse(serverDate);
            format.applyPattern(CLIENT_FORMAT);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFormatDate(long time, String formatStr){
        String content = "";
        SimpleDateFormat format = new SimpleDateFormat();
        if (TextUtils.isEmpty(formatStr)){
            formatStr = CLIENT_FORMAT ;
        }
        Date date = new Date(time);
        format.applyPattern(formatStr);
        content = format.format(date);
        return content;
    }
}
