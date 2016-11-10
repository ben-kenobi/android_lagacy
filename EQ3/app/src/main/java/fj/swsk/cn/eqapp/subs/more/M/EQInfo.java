package fj.swsk.cn.eqapp.subs.more.M;

import android.text.TextUtils;

import org.codehaus.jackson.type.TypeReference;

import java.util.Date;

import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.SharePrefUtil;

/**
 * Created by apple on 16/7/6.
 */
public class EQInfo {
    private static EQInfo ins;

    public double lat,lon;
    public String occurTime,focalDepth,magnitude,occurRegionName;
    public long obsTime;
    public boolean hasLayer=false;
    public static  EQInfo defaultIns(){
        EQInfo info = new EQInfo();
        info.lon=119.27;
        info.lat=27.88;
        info.occurTime="2015,09,16,03:37:33.6630";
        info.focalDepth="14.084013";
        info.magnitude="5.3(M)";
        info.occurRegionName="中国台湾";
        info.obsTime=new Date().getTime();
        return info;

    }

    public boolean hasLayer(){
        if(hasLayer)
            return true;
        return obsTime == SharePrefUtil.getObsTime();
    }
    public boolean isCurrentEQ(){
        return obsTime == SharePrefUtil.getObsTime();
    }


    public static EQInfo getIns(){
        if(ins==null){
            String js = SharePrefUtil.getEQInfo();
            if(TextUtils.isEmpty(js))
                return null;
            ins = JsonTools.getInstance(js, new TypeReference<EQInfo>() {}) ;
        }
        CommonUtils.log(ins.obsTime+"=======");
        return ins;
    }
    public static void setIns(String js){
        ins = JsonTools.getInstance(js, new TypeReference<EQInfo>() {}) ;
    }
    public static void setIns(EQInfo info ){
        ins = info;
    }
}
