package swsk.cn.rgyxtq.subs.security.C;

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

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.M.ViewPagerBean;
import swsk.cn.rgyxtq.subs.security.Common.SafeManagerFinal;

public class SafeManagerGridViewAdapter extends BaseAdapter {
    private List<ViewPagerBean> applist;
    private int holdPosition; //
    private boolean isChanged = false; //是否变化
    private boolean ShowItem = false; //显示item
    private LayoutInflater inflater;

    public SafeManagerGridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.applist = SafeManagerFinal.getbeans();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = ViewHolder.viewWith(applist.get(position),convertView,inflater);

        if (isChanged) {
            if (position == holdPosition) {
                if (!ShowItem) {
                    convertView.setVisibility(View.VISIBLE);
                }
            }
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

    static class ViewHolder{
        public  TextView tv ;
        public ImageView iv;

        public ViewHolder(View v){
            v.setTag(this);
            tv=(TextView)v.findViewById(R.id.main_gridview_item_name);
            iv=(ImageView)v.findViewById(R.id.main_gridview_src);
        }

        public static View viewWith(ViewPagerBean info,View view,LayoutInflater inflater){
            ViewHolder vh = null;
            if(view==null){
                view=inflater.inflate(R.layout.main_gridview_item,null);
                vh=new ViewHolder(view);
            }else{
                vh=(ViewHolder) view.getTag();
            }
            vh.setInfo(info);
            return view;
        }
        public void setInfo(ViewPagerBean info){
            tv.setText(info.getName());
            iv.setBackgroundResource(info.getId());
        }

    }
}