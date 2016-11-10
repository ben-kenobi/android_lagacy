package cn.swsk.rgyxtqapp.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

/**
 * Created by tom on 2015/10/31.
 */
public class DialogUtils {
    public static AlertDialog.Builder getItemDialogBuilder(Context context, String[] items, String title, DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, clickListener);
    }

    public static AlertDialog.Builder getMessageDialogBuilder(Context context, String message, String title, DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message)
                .setPositiveButton("确定", clickListener)
                .setNegativeButton("取消", null);
    }

    public static AlertDialog.Builder getMessageDialogBuilder(Context context, String message, String title, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener cancleClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message)
                .setPositiveButton("确定", clickListener)
                .setNegativeButton("取消", cancleClickListener);
    }

    public static AlertDialog.Builder getCustDialog(Context con,String title,int  res){
        return new AlertDialog.Builder(con).setTitle(title)
//                .setIcon(R.mipmap.ic_launcher)
                .setView(LayoutInflater.from(con).inflate(res, null));
    }
    public static ProgressDialog createProgressDialogNshow(String title,
                                                           String message, boolean cancelOnTouchOutside, Context context) {
        ProgressDialog pd = new ProgressDialog(context,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCanceledOnTouchOutside(cancelOnTouchOutside);
        pd.show();
        return pd;
    }
}
