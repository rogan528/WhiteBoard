package com.zhangbin.paint.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName Util
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/20 下午1:31
 */
public class Util {
    public static Integer toInteger(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof Integer) {
            return (Integer) object;
        }
        try {
            return Integer.valueOf(toString(object));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String toString(Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof String) {
            return object.toString();
        }

        if (object instanceof Double || object instanceof Float) {
            return ((Double) object).longValue() + "";
        }
        return String.valueOf(object);
    }

    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
//            Log.e(TAG, ex.getMessage(), ex);
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
//                Log.e(TAG, "close IO ERROR...", e);
            }
        }
    }

    public static String readFile(String filePath) {
        StringBuilder resultSb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
        }
        return inputStream2String(is);
    }

    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {

        }
        return inputStream2String(is);

    }


}
