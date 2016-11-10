package fj.swsk.cn.eqapp.subs.setting.M;

import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/6/16.
 */
public interface SettingDatas {

    String sectionTitle[]={"","",""};
    String itemtitles[][]={{"系统自动登录","密码保留天数"},{"离线地图"},{"通讯录","版本更新"}};
    String items[][]={{"","32天"},{""},{"","未发现新版本"}};
    int itemicons[][]={{R.mipmap.system,R.mipmap.password},{R.mipmap.contacts
            },{R.mipmap.contacts,R.mipmap.version}};
    boolean hasaccessory[][]={{false,true},{true},{true,true}};
    boolean hasswich[][]={{true,false},{false},{false,false}};



}
