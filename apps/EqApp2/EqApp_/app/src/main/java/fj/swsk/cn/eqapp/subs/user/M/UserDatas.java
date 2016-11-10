package fj.swsk.cn.eqapp.subs.user.M;

import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/6/16.
 */
public interface UserDatas {

    String sectionTitle[]={"基本信息设置","","",""};
    String itemtitles[][]={{"手机号码","邮箱"},{"所属市","所属县","所属乡镇"},{"部门","职务"},
            {"修改密码"}};
    String itemkeys[][]={{"cellphone","mailbox"},{"city","county","town"},{"department","duty"},
            {"modipwd"}};
    int itemicons[][]={{R.mipmap.cellphone,R.mipmap.mailbox18},{R.mipmap.city,
            R.mipmap.county,R.mipmap.town},{R.mipmap.department,R.mipmap.duties},
            {R.mipmap.password}};
    boolean hasaccessory[][]={{true,true},{false,false,false},{false,false},
            {true}};


}
