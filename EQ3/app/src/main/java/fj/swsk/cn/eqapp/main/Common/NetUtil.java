package fj.swsk.cn.eqapp.main.Common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.util.Map;

import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.HttpUtils;


/**
 * Created by apple on 16/3/1.
 */
public class NetUtil {

    public static void noNetWorkToast(){
        CommonUtils.toast("网络未打开，请检查网络。");
    }
    public static void connectFailToast(){
        CommonUtils.toast("无法连接到服务器!");
    }


    public static void  commonRequest(Context con,final String path,final RequestCB cb){
        final Dialog dialog= DialogUtil.progressD(con);
        dialog.show();
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<String,Object> map = JsonTools.getMap(HttpUtils.getJsonContent(path));
                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if(map==null){
                            connectFailToast();
                            return ;
                        }
                        if(cb==null) return ;
                        String status = map.containsKey("status")?map.get("status").toString():"";
                        if("200".equals(status)){
                            cb.cb(map,null,0);
                        }else if("400".equals(status)){
                            CommonUtils.toast(map.get("msg").toString());
                        }else{
                            CommonUtils.toast("未知错误");
                        }
                    }
                });
            }
        });

    }
    public static void commonRequest5( String path, final Context con, final Map<String,
            String> mapo, final Map<String, File> files,
                                      final RequestCB cb) {

        final ProgressDialog pd = DialogUtil.createProgressDialogNshow("提示", "正在提交中，请稍等...",
                false, con);
        path = HttpUtils.fullPath(path);
        com.lidroid.xutils.HttpUtils httpUtils = new com.lidroid.xutils.HttpUtils();

        RequestParams params = new RequestParams();
        for (String key : mapo.keySet()) {
            params.addBodyParameter(key, mapo.get(key));
        }


        for (String key : files.keySet()) {
            params.addBodyParameter(key, files.get(key));
        }

        try {
            httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>
                    () {
                @Override
                public void onFailure(HttpException e, String msg) {
                    CommonUtils.log(msg+"===="+e);
                    Toast.makeText(con, "提交失败", Toast.LENGTH_SHORT).show();
                    if (cb != null)
                        cb.cb(null, msg, 2);
                    pd.dismiss();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd.dismiss();

                    Map<String, Object> map = JsonTools.getMap(responseInfo.result);
                    CommonUtils.log("request5======"+map.toString());
                    if (map == null) {
                        CommonUtils.toast("操作失败");
                        if (cb != null)
                            cb.cb(map, null, 1);
                        return;
                    }
                    if (cb == null) return;
                    String status = map.containsKey("status") ? map.get("status")
                            .toString() : "";

                    if ("200".equals(status)) {
                        cb.cb(map, null, 0);
                    } else {
                        CommonUtils.toast(map.containsKey("msg") ? map.get("msg")
                                .toString() : "未知错误");
                        cb.cb(map, null, 1);
                    }

                }

            });
        } catch (Exception e) {
            Toast.makeText(con, "提交数据异常", Toast.LENGTH_SHORT).show();
            if (cb != null)
                cb.cb(null, e.getMessage(), 3);
        } finally {

        }
    }



    public static abstract  class RequestCB{
        public abstract void cb(Map<String,Object> map, String resp, int type);

    }
}
