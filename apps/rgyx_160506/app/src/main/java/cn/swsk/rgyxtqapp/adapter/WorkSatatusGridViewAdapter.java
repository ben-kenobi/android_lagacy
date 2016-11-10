package cn.swsk.rgyxtqapp.adapter;

/**
 * Created by tom on 2015/10/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.ViewPagerBean;
import cn.swsk.rgyxtqapp.contants.WorkStatusFinal;
import cn.swsk.rgyxtqapp.utils.CommonUtils;

public class WorkSatatusGridViewAdapter extends BaseAdapter {
    public List<ViewPagerBean> applist;
    private int holdPosition; //
    private boolean isChanged = false; //是否变化
    private boolean ShowItem = false; //显示item
    private LayoutInflater inflater;

    private int selIdx;

    public int getSelIdx() {
        return selIdx;
    }

    public WorkSatatusGridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.applist = WorkStatusFinal.getbeans();
    }
    public String getCurName(){
        return applist.get(selIdx).getName();
    }

    @Override
    public int getCount() {
        return applist.size();
    }

    @Override
    public Object getItem(int position) {
        return applist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelIdx(int selIdx) {
        this.selIdx = selIdx;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.main_gridview_item, null);

        TextView tv = (TextView) convertView.findViewById(R.id.main_gridview_item_name);
        ImageView iv = (ImageView) convertView.findViewById(R.id.main_gridview_src);
        String text = applist.get(position).getName();
        tv.setText(text);
        iv.setBackgroundResource(applist.get(position).getId());
        if (isChanged) {
            if (position == holdPosition) {
                if (!ShowItem) {
                    convertView.setVisibility(View.VISIBLE);
                }
            }
        }
        if(position==selIdx){
            ImageView ivReady = (ImageView) convertView.findViewById(R.id.select);
            ivReady.setVisibility(View.VISIBLE);
        }else{
            ImageView ivReady = (ImageView) convertView.findViewById(R.id.select);
            ivReady.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void exchange(int startPosition, int endPosition) {
        Object start_obj = getItem(startPosition);
        holdPosition = endPosition;
        if (startPosition < endPosition) {
            applist.add(endPosition + 1, (ViewPagerBean) start_obj);
            applist.remove(startPosition);
        } else {
            applist.add(endPosition, (ViewPagerBean) start_obj);
            applist.remove(startPosition + 1);
        }
        isChanged = true;
        notifyDataSetChanged();
    }


    public void showDropItem(boolean b) {
        this.ShowItem = b;
    }
}