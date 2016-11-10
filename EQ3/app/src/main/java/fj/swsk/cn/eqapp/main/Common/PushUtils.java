package fj.swsk.cn.eqapp.main.Common;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fj.swsk.cn.eqapp.main.C.LoginActivity;
import fj.swsk.cn.eqapp.main.M.UserInfo;
import fj.swsk.cn.eqapp.util.SharePrefUtil;


/**
 * Created by tom on 2015/10/17.
 */
public class
        PushUtils {
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



    public static String channelId = ""; //推送渠道ID

    public static UserInfo loginUser =null;

    public static String getLoginName(){
        if(loginUser==null)return null;
        return loginUser.getName();
    }

    public static String getToken(){
        if(loginUser==null)return null;
        return loginUser.getToken();
    }

    public static void setLoginUser(UserInfo user){
        loginUser=user;
        SharePrefUtil.putByUser("haslogin", "1");
        if(!shouldAutoLogin())
            SharePrefUtil.putByUser("logintime",new Date().getTime()+"");

    }

    public static void logout(Context context) {

        SharePrefUtil.putByUser("haslogin", "0");
        PushUtils.loginUser = null;
        context.startActivity(new Intent(context,
                LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    public static  boolean shouldAutoLogin(){
        return "1".equals(SharePrefUtil.getByUser("haslogin", "0"))
        && isautologin();
    }

    public static  void setautologin(boolean b ){

         SharePrefUtil.putByUser("autologin", b ? "1" : "0");
    }
    public static boolean isautologin(){
        return "1".equals(SharePrefUtil.getByUser("autologin", "1"));
    }
    public static void setPwdReservedDays(int days){
        SharePrefUtil.putByUser("PwdReservedDays", days + "");
    }
    public static int getPwdReservedDays(){
        return Integer.valueOf(SharePrefUtil.getByUser("PwdReservedDays","30"));
    }


    public static boolean isPwdExpire(){
        long maxspan = getPwdReservedDays()*24*60*60*1000l;
        long span= new Date().getTime()-Long.valueOf(SharePrefUtil.getByUser
                ("logintime",new Date().getTime()+""));
//        CommonUtils.log(maxspan +  "--------------"+span);

        return  span > maxspan;
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



}
