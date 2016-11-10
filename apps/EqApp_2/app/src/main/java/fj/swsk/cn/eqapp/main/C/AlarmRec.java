package fj.swsk.cn.eqapp.main.C;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.Date;
import java.util.Map;

import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;

/**
 * Created by apple on 16/7/29.
 */
public class AlarmRec extends BroadcastReceiver implements Runnable {

    @Override
    public void onReceive(Context context, Intent intent)
    {
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
//        wl.acquire();
        //        wl.release();
        if(TextUtils.isEmpty(PushUtils.getToken())){
            AlarmRec.CancelAlarm(context);
            return ;
        }
        if(!HttpUtils.isNetworkAvailable(context)){
            return ;
        }
        if(MainActivity.curLoc==null){
            return ;
        }


        IConstants.THREAD_POOL.submit(this);
    }

    public static void SetAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmRec.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                0,
//                5*1000, pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*5, 1000 * 5, pi);
    }

    public static void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmRec.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    @Override
    public void run() {
        double lon=MainActivity.curLoc.getLongitude(),lat = MainActivity.curLoc.getLatitude() ;
        String path = IConstants.submitLocation+"?token="+PushUtils.getToken()+
                "&currDate="+IConstants.TIMESDF2.format(new Date())+"&lon="+lon+"&lat="+lat;
        NetUtil.commonRequestNoProgress(path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                CommonUtils.log(map.toString()+"---------------");
            }
        });

    }
}
