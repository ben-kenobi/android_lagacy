package fj.swsk.cn.eqapp.map.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fj.swsk.cn.eqapp.R;

/**
 * Created by xul on 2016/6/22.
 */
public class NeiPlaceSearchAdapter extends BaseAdapter {
    private ArrayList<LinkedHashMap> list = new ArrayList<>();
    private LayoutInflater inflater;

    public NeiPlaceSearchAdapter(ArrayList<LinkedHashMap> pList, LayoutInflater layoutInflater) {
        this.list = pList;
        this.inflater = layoutInflater;
    }

    /**
     * 更新列表数据
     * @param mList
     */
    public void updateList(ArrayList<LinkedHashMap> mList) {
        this.list = mList;
        this.notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearList() {
        if (!list.isEmpty()) {
            this.list = new ArrayList<>();
            this.notifyDataSetChanged();
        }
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    class viewHolder {
        public TextView txtView;
        public String place;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        viewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.address_listview_item, null);
            holder = new viewHolder();
            holder.txtView = (TextView) view.findViewById(R.id.address_name);

            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        holder.place = list.get(index).get("NAME").toString();
        holder.txtView.setText(holder.place);

        return view;
    }
}
