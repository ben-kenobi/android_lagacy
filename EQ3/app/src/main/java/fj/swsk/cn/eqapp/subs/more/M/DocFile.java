package fj.swsk.cn.eqapp.subs.more.M;

import java.io.File;
import java.util.Date;

/**
 * Created by apple on 16/7/21.
 */
public class DocFile {

    public String name, path;
    public long size;
    public Date genTime;

    public static DocFile getDefault(){
        DocFile df = new DocFile();
        int r = (int)(Math.random()*10);
        df.name="aa.doc"+r;
        if(r>5){
            df.path="/sdcard/aa.doc";
            df.size=new File(df.path).length();
        }else{
            df.size=0;
        }



        df.genTime=new Date();
        return df;
    }
}
