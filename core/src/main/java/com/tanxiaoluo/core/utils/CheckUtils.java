package com.tanxiaoluo.core.utils;

import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

public class CheckUtils {

    private static final String format = "^[1][3,4,5,7,8][0-9]{9}$";

    private static final String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    private static final String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

    public static boolean isPhoneNum(String phoneNum) {
        return Pattern.compile(format).matcher(phoneNum).matches();
    }

    public static String IDCardValidate(String IDStr) {
        String errorInfo = "";
        // ================ 号码的长度18位 ================
        if (IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为18位。";
            return errorInfo;
        }
        // ================ 判断前17位是否全为数字 ================
        String Ai = IDStr.substring(0, 17);
        if (!isNumber(Ai)) {
            errorInfo = "身份证无效，不是合法的身份证号码";
            return errorInfo;
        }

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        IDStr = IDStr.toUpperCase();
        if (!Ai.equals(IDStr)) {
            errorInfo = "身份证无效，不是合法的身份证号码";
        }

        return errorInfo;
    }

    /**
     * 检测字符串是否全为number
     */
    private static boolean isNumber(String s) {
        try {
            long l = Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 检查相机是否可用
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
            mCamera.startPreview();//开始预览
        } catch (Exception e) {
            canUse = false;
        } finally {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
        return canUse;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
