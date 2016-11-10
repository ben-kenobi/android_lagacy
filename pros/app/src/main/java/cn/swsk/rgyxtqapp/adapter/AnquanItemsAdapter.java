package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.TEquip;

/**
 * Created by apple on 16/2/19.
 */
public class AnquanItemsAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;

    private int resId = R.layout.item4lv_anquanitem;

    private List<TEquip> infolist;
    private int state=0;


    public AnquanItemsAdapter(Context con, AdapterView av) {
        super();
        this.context = con;
        infolist = new ArrayList<TEquip>();
    }

    public List<TEquip> getCheckedInfos(){
        List<TEquip> list=new ArrayList<TEquip>();
        for (TEquip equip :infolist){
            if(equip.getCheckstate()==1)
            list.add(equip);
        }
        return list;
    }
    public void removeCheckedInfos(){
        for (int i=0;i<infolist.size(); i++){
            if(infolist.get(i).getCheckstate()==1) {
                infolist.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
    }

    public void setInfos(List<TEquip> infos,int state) {
        infolist.clear();
        infolist.addAll(infos);
        this.state=state;
        notifyDataSetChanged();

    }


    public void addInfos(List<TEquip> infos) {
        for(TEquip eq:infos){
            int idx=infolist.indexOf(eq);
            if(state==0){
                if(idx>=0){
                }else{
                    infolist.add(eq);
                    eq.setCheckstate(1);
                }
            }else{
                if(idx>=0&&infolist.get(idx).getCheckstate()!=2){
                    infolist.get(idx).setCheckstate(1);
                }else if(idx<0){
                    infolist.add(eq);
                    eq.setCheckstate(2);
                }
            }

        }


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
                    .inflate(resId, parent, false), context);
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

    public void setinfo(View convert, ViewHolder holder, int pos) {
        TEquip info = infolist.get(pos);
        String ary[] = {info.getOriginalNo(), info.getEquipCategroy(), info.getBatch(), info.getProduceDate(),
                info.getManuFacturer(), "删除"};
        for (int i = 0; i < 6; i++) {
            holder.tvs[i].setText(ary[i]);
        }

        int color = info.getCheckstate()==1?0xff66ee66:
                info.getCheckstate()==2?0xffee7777:0xff666666;
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                holder.tvs[i].setTextColor(0xffff4444);
                holder.tvs[i].setTag(pos);
                holder.tvs[i].setOnClickListener(this);
            } else
                holder.tvs[i].setTextColor(color);
        }

        if (pos % 2 == 0) {
            convert.setBackgroundColor(0xffffffff);
        } else {
            convert.setBackgroundColor(0xfff5f5f5);
        }

    }


    public static class ViewHolder {
        public TextView tvs[] = new TextView[6];

        public ViewHolder(View v, Context context) {
            v.setTag(this);
            for (int i = 0; i < 6; i++) {
                tvs[i] = (TextView) v.findViewById(context.getResources().getIdentifier
                        ("tv0" + (i + 1), "id", context.getPackageName()));
            }
        }
    }


}
