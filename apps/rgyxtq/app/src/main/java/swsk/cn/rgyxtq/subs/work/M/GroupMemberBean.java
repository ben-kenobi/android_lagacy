package swsk.cn.rgyxtq.subs.work.M;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2015/10/28.
 */
public class GroupMemberBean {

    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String workTypeId; //显示的数据ID

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(String name) {
        this.workTypeId = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
    public static List<GroupMemberBean> testList(){
        List<GroupMemberBean> list = new ArrayList<>();
        for(int i=0;i<25;i++){
            GroupMemberBean bean = new GroupMemberBean();
            list.add(bean);
            bean.name=(char)('A'+i)+"123123";
            bean.sortLetters=String.valueOf((char)('A'+i))+"123";
            bean.workTypeId=i+"";
        }
        return list;
    }
}
