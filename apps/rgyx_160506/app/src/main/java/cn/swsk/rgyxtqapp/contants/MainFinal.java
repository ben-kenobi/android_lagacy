package cn.swsk.rgyxtqapp.contants;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.ViewPagerBean;

/**
 * Created by tom on 2015/10/17.
 */
public class MainFinal {
    public static List<ViewPagerBean> list;
    // 定义整型数组 即图片源 main
    public static int[] mImageIds2 = {
            R.mipmap.disk_zyztsb,// 作业状态上报
            R.mipmap.disk_fack,// 作业方案查看
            R.mipmap.disk_xxsb,// 作业信息上报
            R.mipmap.disk_dy,// 安全管理
            R.mipmap.user_manage,// 用户管理
    };

    // 主界面2GridViewItem名
    public static String[] itemName = { "作业状态上报", "作业方案查看", "作业信息上报", "安全管理", "用户管理"};

    public static List<ViewPagerBean> getbeans(){
        list=new ArrayList<ViewPagerBean>();
        for (int i = 0; i < itemName.length; i++) {
            ViewPagerBean bean=new ViewPagerBean();
            bean.setName(itemName[i]);
            bean.setId(mImageIds2[i]);
            list.add(bean);
        }
        return list;

    }

}
