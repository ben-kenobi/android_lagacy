package fj.swsk.cn.eqapp.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.InstructionsListActivity;
import fj.swsk.cn.eqapp.main.C.MainActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;
import fj.swsk.cn.eqapp.util.SharePrefUtil;

/**
 * Created by apple on 16/3/30.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        IUtil.log(intent.getAction() + "--------------\n-----------------" +
                printBundle(bundle));
        IUtil.log("regid=" + JPushInterface.getRegistrationID(context));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regid = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            //send id to server
            PushUtils.pushChannelId(context);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String mes = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            handleLayerNoti(context,mes);

//            processCustMes(context, bundle);
//            if (mes.matches("^[0-9]{11}$")) {
//               IUtil.unlockScr();
//                IUtil.callIntent(mes);
//
//            }else{
//                processCustomMessage(context, bundle);
//
//            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notiid = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String str = bundle.getString(JPushInterface.EXTRA_ALERT);
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            if("地震系统".equals(title)){
                handleLayerNoti(context,str);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String str = bundle.getString(JPushInterface.EXTRA_ALERT);
            if (!str.matches("^[0-9]{11}$")) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            String str = bundle.getString(JPushInterface.EXTRA_EXTRA);
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        } else {
            IUtil.log(intent.getAction() + " -----unhandled intent");
        }
    }


    private void handleLayerNoti(Context context,String json){
        Map<String,Object> map = JsonTools.getMap(json);
        String serverId = map.get("serviceId")+"";


        if("ha000001".equals(serverId)){
            buildActivityNoti(context, "灾损专题图开始生成");
        }else if ("ha000002".equals(serverId)){
            Map<String,Object> result = (Map<String,Object>)map.get("data");
            long obsTime = (Long)result.get("obsTime");
            SharePrefUtil.putObsTime(obsTime);
//        CommonUtils.log("```````````````````````"+result);
            Map<String,String>  layerNames = (Map<String,String>)result.get("layerNames");
//            CommonUtils.log("-------------------------"+layerNames);

//            for(String key :layerNames.keySet()){
//                SharePrefUtil.putLayer(key,layerNames.get(key));
//            }
            Map<String,String>  resport = (Map<String,String>)result.get("reports");
//            String hbr=resport.get("hbr");
//            String adm = resport.get("adm");
            buildActivityNoti(context, "灾损专题图生成完毕");

        }else if("ha000003".equals(serverId)){
//            for(String key :map.keySet()){
//                SharePrefUtil.putEQInfo(key,map.get(key)+"");
//            }
            Map<String,Object> result = (Map<String,Object>)map.get("data");
            String js=JsonTools.getJsonString(result);
            SharePrefUtil.putEQInfo(js);
            EQInfo.setIns(js);
            long obsTime = (Long)result.get("obsTime");

            buildActivityNoti(context, result.get("occurRegionName")+" 发生 "+
                    result.get("magnitude")+"级地震,震源深度 "+result.get("focalDepth"));
            SharePrefUtil.putObsTime_instant(obsTime);
            sendEqInfoNoti(context);
        }else if("cm000001".equals(serverId)){
            Map m = (Map<String,String>)map.get("data");
            buildInstructionNoti(context, "收到一条指令，点击查看",m.get("msg")+"");
        }

    }
    public void buildInstructionNoti(Context context,String title,String mes){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(mes).setDefaults(Notification.DEFAULT_SOUND);
// Creates an explicit intent for an Activity in your app

        Intent i = new Intent(context, InstructionsListActivity.class);
        i.putExtra("mes",mes);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1,i,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti=mBuilder.build();
        noti.flags|=Notification.FLAG_AUTO_CANCEL;

// mId allows you to update the notification later on.
        mNotificationManager.notify((int)(Math.random()*10000),noti );
    }


    public void buildBroadNoti(Context context,String mes,String content){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("地震项目")
                        .setContentText(mes).setDefaults(Notification.DEFAULT_SOUND);
// Creates an explicit intent for an Activity in your app


        Intent msgIntent = new Intent(JPushInterface.ACTION_NOTIFICATION_OPENED);
        msgIntent.putExtra(JPushInterface.EXTRA_ALERT, content);


        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context,1,msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti=mBuilder.build();
        noti.flags|=Notification.FLAG_AUTO_CANCEL;

// mId allows you to update the notification later on.
        mNotificationManager.notify((int)(Math.random()*10000),noti );
    }

    public void buildActivityNoti(Context context,String mes){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("地震项目")
                        .setContentText(mes).setDefaults(Notification.DEFAULT_SOUND);
// Creates an explicit intent for an Activity in your app

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("mes",mes);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1,i,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti=mBuilder.build();
        noti.flags|=Notification.FLAG_AUTO_CANCEL;

// mId allows you to update the notification later on.
        mNotificationManager.notify((int)(Math.random()*10000),noti );
    }

    private static String printBundle(Bundle bundle) {
        StringBuffer sb = new StringBuffer("{");
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ",val:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ",val:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(key).isEmpty()) {
                    sb.append("\nthis message has no extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(key));
                    Iterator<String> it = null;
                    while ((it = json.keys()).hasNext()) {
                        String jk = it.next();
                        sb.append("\nkey:" + key + ",val:[" + jk + " - " + json.optString(jk) + "]");
                    }
                } catch (Exception e) {
                    sb.append("\nthis message is not json format");
                }

            } else {
                sb.append("\nkey:" + key + ",val:" + bundle.getString(key));
            }
        }
        sb.append("\n}");
        return sb.toString();
    }


    //send msg to MainActivity
    private void sendEqInfoNoti(Context context) {
        if (MainActivity.isForeground) {
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);

            context.sendBroadcast(msgIntent);
        }
    }


}
