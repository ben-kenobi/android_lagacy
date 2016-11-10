package cn.swsk.rgyxtqapp.adapter;

/**
 * Created by tom on 2015/10/26.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    public Context mContext;
    public List<T> mDatas;
    public LayoutInflater mInflater;
    public int layoutId;
    public Class<? extends View> vclz;

    public CommonAdapter(Context context, List<T> datas, int layoutId)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }
    public CommonAdapter(Context context,List<T>datas,Class<? extends View> vclz){
        this(context,datas,-1);
        this.vclz=vclz;
    }

    @Override
    public int getCount()
    {

        return getDatas().size();
    }

    @Override
    public T getItem(int position)
    {

        return getDatas().get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId,vclz, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public void addData(List<T> data){
        getDatas().addAll(data);
        notifyDataSetChanged();
    }
    public void setDatas(List<T> datas){
        mDatas=datas;
        notifyDataSetChanged();
    }

    public abstract void convert(ViewHolder holder, T t);

    public List<T> getDatas(){
        if(mDatas==null)
            mDatas= new ArrayList<>();
        return mDatas;
    }

}
