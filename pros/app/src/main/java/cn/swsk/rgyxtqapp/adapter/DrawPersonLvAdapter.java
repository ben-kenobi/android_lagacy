package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.DrawPerson;

/**
 * Created by apple on 16/2/23.
 */
public class DrawPersonLvAdapter  extends BaseAdapter implements View.OnClickListener{

    private Context context;

    private int resId = R.layout.item4lv_drawpersonlv;

    private List<DrawPerson> infolist;



    public DrawPersonLvAdapter(Context con){
        super();
        this.context = con;
        infolist = new ArrayList<DrawPerson>();
        infolist.add(DrawPerson.getDefault());
    }
    public void addInfos(List<DrawPerson> infos){
        infolist.addAll(infos);
        notifyDataSetChanged();
    }
    public void addInfo(DrawPerson info){
        infolist.add(info);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (infolist == null)
            return 0;
        return infolist.size();
    }

    @Override
    public Object getItem(int position) {
        if (infolist == null)
            return null;
        return infolist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder(convertView = LayoutInflater.from(context)
                    .inflate(resId, parent, false),context);
        } else
            holder = (ViewHolder) convertView.getTag();



        setinfo(convertView, holder, position);
        return convertView;
    }


    @Override
    public void onClick(View v) {
        infolist.remove((int) v.getTag());
        notifyDataSetChanged();
    }

    public void setinfo(View convert,ViewHolder holder,int pos){
        DrawPerson info = infolist.get(pos);
        String ary[]={info.getName(),info.getIdentity(),info.getPhone()/*,pos==0?"编辑":"删除"*/};
        int len = ary.length;
        for(int i=0;i<len;i++){
            holder.tvs[i].setText(ary[i]);
        }
        if(pos==0){
            for (int i=0;i<len;i++){
                holder.tvs[i].setTextColor(0xff333333);

            }
        }else{
            for (int i=0;i<len;i++){
                holder.tvs[i].setTextColor(0xff888888);
            }
        }
        if(pos%2==0){
            convert.setBackgroundColor(0xfff5f5f5);
        }else{
            convert.setBackgroundColor(0xffffffff);
        }

    }




    public static class ViewHolder {
        public TextView tvs[]=new TextView[3];
        public ViewHolder(View v,Context context) {
            v.setTag(this);
            for(int i=0;i<3;i++){
                tvs[i]=(TextView)v.findViewById(context.getResources().getIdentifier
                        ("tv0"+(i+1),"id",context.getPackageName()));
            }
        }
    }

}
