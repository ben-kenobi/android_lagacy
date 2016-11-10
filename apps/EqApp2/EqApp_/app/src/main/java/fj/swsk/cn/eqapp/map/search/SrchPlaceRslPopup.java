package fj.swsk.cn.eqapp.map.search;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.map.adapter.NeiPlaceSearchAdapter;
import fj.swsk.cn.eqapp.map.onPoiItemClick;

/**
 * Created by xul on 2016/6/22.
 */
public class SrchPlaceRslPopup extends PopupWindow {
    private View contentView;
    private ListView lvResults;

    //下拉列表项被单击的监听器
    private onPoiItemClick listener;
    private NeiPlaceSearchAdapter adapter;

    public SrchPlaceRslPopup(final Context context, LayoutInflater layoutInflater,
                             final ArrayList<LinkedHashMap> list, int width,
                             onPoiItemClick itemClickListener) {
        super(context);

        this.listener = itemClickListener;

        contentView = layoutInflater.inflate(R.layout.address_listview, null);
        lvResults = (ListView) contentView.findViewById(R.id.lvResults);
        adapter = new NeiPlaceSearchAdapter(list, layoutInflater);
        lvResults.setAdapter(adapter);


        //如果PopupWindow中的下拉列表项被单击了

        //则通知外部的下拉列表项单击监听器并传递当前单击项的数据
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                if (listener != null) {
                    LinkedHashMap item = (LinkedHashMap) adapter.getItem(index);
                    /**
                     * 删除不显示的字段
                     */
                    item.remove("ADDNAME");
                    item.remove("ABBER");
                    item.remove("SNAME");
                    item.remove("XNAME");
                    item.remove("ZNAME");
                    item.remove("IMPORTANCE");
                    item.remove("TYPE");
                    listener.onItemClick(arg0, view, item, arg3);
                    dismiss();
                }
            }
        });

        this.setContentView(contentView);
        this.setWidth(width - 5);
        //this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(AbsListView.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //this.setAnimationStyle(R.style.PopupAnimation);
        //this.setAnimationStyle(R.style.popwin_anim_style);
        this.setFocusable(false);
        this.setOutsideTouchable(true);
    }

    public void updateData(ArrayList<LinkedHashMap> data) {
        adapter.updateList(data);
    }

    public void clearData() {
        adapter.clearList();
    }
    public boolean isEmpty(){
       return adapter.getCount()>0?false:true;
    }
}
