package fj.swsk.cn.eqapp.map.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fj.swsk.cn.eqapp.R;

/**
 * Created by xul on 2016/6/22.
 */
public class distinctListAdapter extends BaseAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public distinctListAdapter(ArrayList<String> pList, LayoutInflater layoutInflater) {
        this.list = pList;
        this.inflater = layoutInflater;
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
        holder.txtView.setText(getItem(index).toString());

        return view;
    }
}
