package swsk.cn.rgyxtq.main.Common;

import java.util.ArrayList;
import java.util.List;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.M.ViewPagerBean;

/**
 * Created by apple on 16/2/29.
 */
public class MainFinal {
    public static List<ViewPagerBean> list;
    public static int[] mImageIds2={
            R.mipmap.disk_zyztsb,
            R.mipmap.disk_fack,
            R.mipmap.disk_xxsb,
            R.mipmap.disk_dy,
            R.mipmap.user_manage
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
