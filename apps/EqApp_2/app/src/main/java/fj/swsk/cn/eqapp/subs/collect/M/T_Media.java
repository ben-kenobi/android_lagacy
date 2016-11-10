package fj.swsk.cn.eqapp.subs.collect.M;

import java.io.File;
import java.util.Map;

/**
 * Created by apple on 16/6/28.
 */
public class T_Media {
    public long _id,sceneid;
    public String content_name,content_path;
    public int flag;



    public  void getFile(Map<String,File> map){
        map.put(content_name,new File(content_path));
    }

}
