package cn.swsk.rgyxtqapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import cn.swsk.rgyxtqapp.R;

/**
 * Created by conkis-tp on 2015/12/10.
 */
public class ProgressDialog {
    public Dialog progressDialog;

    public ProgressDialog(Context context) {
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);//设置进度条是否可以按退回键取消
        progressDialog.setCanceledOnTouchOutside(false); //设置点击进度对话框外的区域对话框不消失
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("正在加载中");
    }

    public void showDialog() {
        progressDialog.show();
    }

    public void cancelDialog() {
        progressDialog.dismiss();
    }
}
