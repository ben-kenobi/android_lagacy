package swsk.cn.rgyxtq.main.C;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.MainFinal;
import swsk.cn.rgyxtq.main.M.ViewPagerBean;

/**
 * Created by apple on 16/2/29.
 */
public class MenuGVAdapter extends BaseAdapter {
    private List<ViewPagerBean> applist;
    private int holdPosition;
    private boolean change = false;
    private boolean  showitem=false;
    private LayoutInflater inflater;

    public MenuGVAdapter(Context c){
        inflater=LayoutInflater.from(c);
        this.applist= MainFinal.getbeans();
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
        View view =  ViewHolder.viewWith(applist.get(position),convertView,inflater);
        if(change){
            if(position==holdPosition){
                if(!showitem){
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
        return view;
    }

    public void exchange(int start,int end){
        ViewPagerBean start_obj = applist.get(start);
        holdPosition=end;
        if(start<end){
            applist.add(end+1,start_obj);
            applist.remove(start);
        }else{
            applist.add(end,start_obj);
            applist.remove(start+1);
        }
        change=true;
        notifyDataSetChanged();
    }

    public void showDropItem(boolean b){this.showitem=b;}






    static  class ViewHolder{

        public TextView tv;
        public ImageView iv;
        public ViewHolder(View v){
            tv=(TextView)v.findViewById(R.id.menu_gridview_item_name);
            iv = (ImageView)v.findViewById(R.id.menu_gridview_src);
            v.setTag(this);
        }
         public static View viewWith(ViewPagerBean info,View view,LayoutInflater inflater){
             ViewHolder vh=null;
             if(view == null){
                 view = inflater.inflate(R.layout.menu_gridview_item,null);
                 vh = new ViewHolder(view);
             }else{
                 vh=(ViewHolder)view.getTag();
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
