package cn.swsk.rgyxtqapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.bean.TWareHouse;

/**
 * Created by tom on 2015/10/17.
 */
public class PushUtils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    public static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    public static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    public static String sysTitle = ""; //系统标题

    public static String userName = ""; //用户名称

    public static String userUnit = ""; //用户单位

    public static String userId = ""; //用户ID

    public static String token = ""; //登录令牌

    public static String channelId = ""; //推送渠道ID

    public static String terminalPhone = ""; //GPS电话卡号码

    public static String workGroupId = ""; //作业信息组标识

    public static TWareHouse sWareHouse;
    public static void setWareHouse(Object map){
        String json = JsonTools.getJsonStr(map);
        sWareHouse=JsonTools.getInstance(json, new TypeReference<TWareHouse>() {});
    }

    // 获取MetaValue
    public static String getUserName(){
        if(sWareHouse==null)
            return userName;
        return sWareHouse.getLoginName();
    }


    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }


    public static String getServerIPText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("server_ip", "");
    }

    public static void setServerIPText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("server_ip", text);
        editor.commit();
    }
    public static String getServerIP(Context con){
        return "http://"+getServerIPText(con)+ File.separator;

    }
}