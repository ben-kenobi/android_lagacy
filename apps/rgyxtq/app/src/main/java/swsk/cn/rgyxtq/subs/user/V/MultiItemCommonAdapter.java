package swsk.cn.rgyxtq.subs.user.V;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import swsk.cn.rgyxtq.subs.work.Common.CommonAdapter;
import swsk.cn.rgyxtq.subs.work.Common.ViewHolder;

/**
 * Created by apple on 16/3/11.
 */
public abstract  class MultiItemCommonAdapter<T> extends CommonAdapter<T> {
    protected MultiItemTypeSupport<T> mTMultiItemTypeSupport;
    public MultiItemCommonAdapter(Context context,List<T> datas,MultiItemTypeSupport<T> multiItemTypeSupport){
        super(context,datas,-1);
        mTMultiItemTypeSupport=multiItemTypeSupport;
    }

    @Override
    public int getViewTypeCount() {
        if(mTMultiItemTypeSupport!=null)
            return mTMultiItemTypeSupport.getViewTypeCount();
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(mTMultiItemTypeSupport!=null)
            return mTMultiItemTypeSupport.getItemViewType(position,mDatas.get(position));
        return super.getItemViewType(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mTMultiItemTypeSupport==null)
            return super.getView(position,convertView,parent);
        int layoutid = mTMultiItemTypeSupport.getLayoutId(position,getItem(position));
        ViewHolder holder = ViewHolder.get(convertView,layoutid,position,mInflater);
        convert(holder,getItem(position));
        return holder.mConvertView;
    }
}
