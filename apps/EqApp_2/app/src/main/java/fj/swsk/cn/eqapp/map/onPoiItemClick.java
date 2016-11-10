package fj.swsk.cn.eqapp.map;

import android.view.View;
import android.widget.AdapterView;

import java.util.LinkedHashMap;

/**
 * Created by xul on 2016/6/23.
 * poi查询结果点击事件处理接口
 */
public interface onPoiItemClick {
    void onItemClick(AdapterView<?> parent, View view,LinkedHashMap attributes, long id);
}
