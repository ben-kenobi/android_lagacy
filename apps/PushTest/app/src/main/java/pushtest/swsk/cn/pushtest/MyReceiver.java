package pushtest.swsk.cn.pushtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

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
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String mes = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            IUtil.log("message=" + mes);
//            processCustMes(context, bundle);
            if (mes.matches("^[0-9]{11}$")) {
               IUtil.unlockScr();
                IUtil.callIntent(mes);

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notiid = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String str = bundle.getString(JPushInterface.EXTRA_ALERT);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String str = bundle.getString(JPushInterface.EXTRA_ALERT);
            if (!str.matches("^[0-9]{11}$")) {
                Intent i = new Intent(context, TestActivity.class);
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

    private void processCustMes(Context con, Bundle bundle) {
        if (MainActivity.isForeground) {
            Intent msgintent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgintent.putExtra(MainActivity.MESSAGE, bundle.getString(JPushInterface.EXTRA_MESSAGE));
            msgintent.putExtra(MainActivity.EXTRAS, bundle.getString(JPushInterface.EXTRA_EXTRA));
            con.sendBroadcast(msgintent);

        }
    }
}
