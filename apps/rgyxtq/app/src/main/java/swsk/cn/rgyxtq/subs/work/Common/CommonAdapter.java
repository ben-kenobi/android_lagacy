package swsk.cn.rgyxtq.subs.work.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by apple on 16/3/1.
 */
public abstract  class CommonAdapter<T> extends BaseAdapter {
    public Context con;
    public List<T>mDatas;
    public LayoutInflater mInflater;
    public int resid;
    public CommonAdapter(Context con,List<T> datas,int resid){
        this.con=con;
        mInflater=LayoutInflater.from(con);
        this.mDatas=datas;
        this.resid=resid;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(convertView,resid,position,mInflater);
        convert(holder,getItem(position));
        return holder.mConvertView;
    }
    public abstract void convert(ViewHolder holder,T t);
}
