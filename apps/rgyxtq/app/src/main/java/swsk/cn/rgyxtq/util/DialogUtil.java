package swsk.cn.rgyxtq.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import swsk.cn.rgyxtq.R;

/**
 * Created by apple on 16/2/26.
 */
public class DialogUtil {


    public static Dialog progressD(Context con) {
        Dialog pro = new Dialog(con, R.style.progress_dialog);
        pro.setContentView(R.layout.dialog);
        pro.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pro.setCancelable(false);
        pro.setCanceledOnTouchOutside(false);
        ((TextView) pro.findViewById(R.id.id_tv_loadingmsg)).setText("正在加载中。。");
        return pro;
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

    public static AlertDialog.Builder getDefault(Context con,String title,String mes){
        return new AlertDialog.Builder(con).setTitle(title).setIcon(R.mipmap.ic_launcher)
                .setMessage(mes);
    }

    public static AlertDialog.Builder getCustDialog(Context con,String title,int  res){
        return new AlertDialog.Builder(con).setTitle(title).setIcon(R.mipmap.ic_launcher)
                .setView(LayoutInflater.from(con).inflate(res,null));
    }


    public static void setDialogsWindowSoftInputModeToHideAlways(Dialog dialog) {
        dialog.getWindow().getAttributes().softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
    }


}
