package cn.swsk.rgyxtqapp.utils;

import java.util.Comparator;

import cn.swsk.rgyxtqapp.bean.GroupMemberBean;

/**
 * Created by tom on 2015/10/28.
 */
public class PinyinComparator implements Comparator<GroupMemberBean> {

    public int compare(GroupMemberBean o1, GroupMemberBean o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}