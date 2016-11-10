package fj.swsk.cn.eqapp.subs.setting.C;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import fj.swsk.cn.eqapp.R;

/**
 * Created by xul on 2016/8/6.
 */
public class SelectItemPopupWindow extends PopupWindow {
    private ListView mListView;
    public SelectItemPopupWindow(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.bottom_listview, null);
        mListView = (ListView)mMenuView.findViewById(R.id.lvResults);

        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
    public void setOnItemClickListener(ListView.OnItemClickListener itemOnClick){
        mListView.setOnItemClickListener(itemOnClick);
    }
    public void setListViewAdapter(BaseAdapter adapter){
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
