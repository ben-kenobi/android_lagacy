package fj.swsk.cn.eqapp.main.Common;

import android.app.Dialog;
import android.content.Context;

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
                        if("1".equals(status)){
                            cb.cb(map);
                        }else if("2".equals(status)||"9999".equals(status)){
                            CommonUtils.toast(map.get("errmsg").toString());
                        }else{
                            CommonUtils.toast("未知错误");
                        }
                    }
                });
            }
        });

    }



    public static abstract  class RequestCB{
        public abstract void cb(Map<String,Object> map);

    }
}
