package fj.swsk.cn.eqapp.subs.more.M;

import android.os.Environment;

import java.io.File;
import java.util.Date;

import fj.swsk.cn.eqapp.conf.IConstants;

/**
 * Created by apple on 16/7/21.
 */
public class DocFile {

    public String name, path,url,showname;
    public long size;
    public Date genTime;


    public static DocFile getByPrefix(String pref,String showPref,EQInfo info){
        DocFile dc = new DocFile();
        dc.showname=showPref+info.obsTime+".doc";
        dc.name=pref+info.obsTime+".doc";
        String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"eqappDLDir";
        File dirf = new File(dir);
        dirf.mkdirs();

        dc.path= dirf.getAbsolutePath()+ File.separator+dc.name;
        dc.genTime=new Date(info.obsTime);
        dc.url= IConstants.eqFilePrefix+dc.name;
        File f = new File(dc.path);
        if(f.exists()){
            dc.size=f.length();
        }
        return dc;
    }
}
