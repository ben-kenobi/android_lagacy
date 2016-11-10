package fj.swsk.cn.eqapp.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.main.Common.PushUtils;

public class SharePrefUtil {
    public static final String RESERVED_DIMEN = "user_def";
    public static final String USER_INFO = "userInfo";
    public static final String USERS = "users";

    public static final String MAPLAYERS = "maplayers";
    public static final String EQINFO = "eqinfo";


    public static boolean put(String key, String value) {
        Editor editor = CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String get(String key, String defau) {
        return CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).getString(key, defau);
    }

//
//	public static boolean putLayer(String key,String value){
//		Editor editor = CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).edit();
//		editor.putString(key,value);
//		return editor.commit();
//	}

    public static long getObsTime() {
        return CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).getLong("obsTime", -1);
    }

    public static boolean putObsTime(long obsTime) {

        Editor editor = CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).edit();
        editor.putLong("obsTime", obsTime);
        return editor.commit();
    }

    public static long getObsTime_instant() {
        return CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).getLong("obsTime_instant", -1);
    }

    public static boolean putObsTime_instant(long obsTime) {

        Editor editor = CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).edit();
        editor.putLong("obsTime_instant", obsTime);
        return editor.commit();
    }

    //	public static String getLayer(String key,String defau){
//		return CommonUtils.context.getSharedPreferences(MAPLAYERS, 0).getString(key, defau);
//	}
//	public static boolean putEQInfo(String key,String value){
//		Editor editor = CommonUtils.context.getSharedPreferences(EQINFO, 0).edit();
//		editor.putString(key,value);
//		return editor.commit();
//	}
//
//	public static String getEQInfo(String key,String defau){
//		return CommonUtils.context.getSharedPreferences(EQINFO, 0).getString(key, defau);
//	}
    public static String getEQInfo() {
        return CommonUtils.context.getSharedPreferences(EQINFO, 0).getString("EQInfo", "");
    }

    public static boolean putEQInfo(String json) {

        Editor editor = CommonUtils.context.getSharedPreferences(EQINFO, 0).edit();
        editor.putString("EQInfo", json);
        return editor.commit();
    }


    public static boolean putByUser(String key, String value) {
        if (TextUtils.isEmpty(PushUtils.getLoginName())) {
            return false;
        }
        Editor editor = CommonUtils.context.getSharedPreferences(PushUtils.getLoginName(), 0).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getByUser(String key, String defau) {
        if (TextUtils.isEmpty(PushUtils.getLoginName()) && TextUtils.isEmpty(getUsername())) {
            return defau;
        }
        return CommonUtils.context.getSharedPreferences(TextUtils.isEmpty(PushUtils.getLoginName()) ?
                getUsername() : PushUtils.getLoginName(), 0).getString(key, defau);
    }

    public static boolean saveUser(String username, String pwd) {
        SharedPreferences.Editor editor = CommonUtils.context.getSharedPreferences(USER_INFO, 0).edit();
        editor.putString("USER_NAME", username);
        editor.putString("PASSWORD", pwd);
        saveInUsers(username, pwd);
        return editor.commit();
    }

    public static String getPwd() {
        if (PushUtils.isPwdExpire())
            return "";
        return CommonUtils.context.getSharedPreferences(USER_INFO, 0).getString("PASSWORD", "");
    }

    public static String getUsername() {
        return CommonUtils.context.getSharedPreferences(USER_INFO, 0).getString("USER_NAME", "");

    }

    public static boolean saveInUsers(String name, String pwd) {
        SharedPreferences.Editor editor = CommonUtils.context.getSharedPreferences(USERS, 0).edit();
        editor.putString(name, pwd);
        return editor.commit();
    }

    public static List<String[]> getUsers() {
        List<String[]> usernames = new ArrayList<>();
        Map<String, String> map = (Map<String, String>) CommonUtils.context.getSharedPreferences(USERS, 0).getAll();
        usernames = new ArrayList<>();
        for (String key : map.keySet()) {
            usernames.add(new String[]{key, map.get(key)});
        }
        return usernames;
    }

}
