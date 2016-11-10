package fj.swsk.cn.eqapp.subs.collect.M;


import com.lidroid.xutils.http.HttpHandler;

/**
 * Created by apple on 16/7/11.
 */
public class TsceneProg {

    public long _id,total,progress;
    public boolean compressing=true;
    public int state=0;// 0 uploading  1 complete  2 failure
    public String describe;
    public HttpHandler<String> uploadhandler;


}
