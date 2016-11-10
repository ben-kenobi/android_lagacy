package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.WorkUnit;

/**
 * Created by apple on 16/2/23.
 */
public class DrawPersonLvAdapter  extends BaseAdapter implements View.OnClickListener{

    private Context context;

    private int resId = R.layout.item4lv_drawpersonlv;

    public List<WorkUnit> infolist;



    public DrawPersonLvAdapter(Context con){
        super();
        this.context = con;
        infolist = new ArrayList<WorkUnit>();

    }
    public void addInfos(List<WorkUnit> infos){
        infolist.addAll(infos);
        notifyDataSetChanged();
    }
    public void addInfo(WorkUnit info){
        infolist.add(info);
        notifyDataSetChanged();
    }

    public List<Map<String,String>> idenList(){
        Set<String> set =new HashSet<>();
        for(WorkUnit unit:infolist){
            set.add(unit.getIden());
        }
        List<Map<String,String>> list = new ArrayList<>();
        for(String str:set){
            Map<String,String> m= new HashMap<>();
            m.put("name",str);
            list.add(m);
        }
        return list;
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
        WorkUnit info = infolist.get(pos);
        String ary[]={info.getWuName(),info.getIden(),info.getTel()/*,pos==0?"编辑":"删除"*/};
        int len = ary.length;
        for(int i=0;i<len;i++){
            holder.tvs[i].setText(ary[i]);
        }
        if(pos==0){
            for (int i=0;i<len;i++){
                holder.tvs[i].setTextColor(0xff888888);

            }
        }else{
            for (int i=0;i<len;i++){
                holder.tvs[i].setTextColor(0xff888888);
            }
        }
        if(pos%2!=0){
            convert.setBackgroundResource(R.drawable.selector_lvitem_lightgray);
        }else{
            convert.setBackgroundResource(R.drawable.selector_lvitem_white);
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
