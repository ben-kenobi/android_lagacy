package cn.swsk.rgyxtqapp.contants;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.ViewPagerBean;

/**
 * Created by tom on 2015/10/17.
 */
public class WorkStatusFinal {
    public static List<ViewPagerBean> list;
    // 定义整型数组 即图片源 main
    public static int[] mImageIds2 = {
            R.mipmap.begin_work,// 出发
            R.mipmap.arrive_work,// 到达
            R.mipmap.ready_work,// 准备就绪
            R.mipmap.complate_work,// 作业完毕
            R.mipmap.redo_work,// 再作业
            R.mipmap.cancle_work,// 取消作业
            R.mipmap.init_work,// 恢复初始状态
    };

    // 主界面2GridViewItem名
    public static String[] itemName = { "出发", "到达",
            "准备就绪", "作业完毕", "再作业",
            "取消作业", "恢复初始状态" };

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
