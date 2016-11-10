package cn.swsk.rgyxtqapp.contants;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.ViewPagerBean;

/**
 * Created by tom on 2015/10/17.
 */
public class SafeManagerFinal {
    public static List<ViewPagerBean> list;
    // 定义整型数组 即图片源 main
    public static int[] mImageIds2 = {
            R.mipmap.saft_rk,// 入库管理
            R.mipmap.saft_ck,// 出库管理
            R.mipmap.saft_ztgl,// 状态变更
            R.mipmap.saft_wtjl,// 问题记录
            R.mipmap.saft_pdgl,// 盘点管理
            R.mipmap.saft_tjcx,// 查询统计
            R.mipmap.saft_gqyj,// 库存过期预警
    };

    // 主界面2GridViewItem名
    public static String[] itemName = { "入库管理", "出库管理",
            "状态变更", "问题记录", "盘点管理","查询统计",
            "库存过期预警" };

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
