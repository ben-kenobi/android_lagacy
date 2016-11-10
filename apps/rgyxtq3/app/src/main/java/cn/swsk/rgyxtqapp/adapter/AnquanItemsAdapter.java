package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.BaseEquip;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.bean.WareHouseT;

/**
 * Created by apple on 16/2/19.
 */
public class AnquanItemsAdapter extends BaseAdapter implements View.OnClickListener {

    protected Context context;

    private int resId = R.layout.item4lv_anquanitem;

    public List infolist;
    public int state=0;

    public Observable obsable = new Observable(){
        public void notifyChange(){
            setChanged();
            notifyObservers(infolist);
        }
    };

    private void notifyDataChange(){
        try {
            obsable.getClass().getMethod("notifyChange").invoke(obsable);
        }catch (Exception e){}

    }


    public AnquanItemsAdapter(Context con) {
        super();
        this.context = con;
        infolist = new ArrayList();
    }

//    public boolean isStatusConsistence(List<Object> list){
//       if(list.size()>0){
//           String estatus = list.get(0).getEstatus();
//           if(estatus==null||(!"2".equals(estatus)&&!"3".equals(estatus))){
//               return false;
//           }
//           for(int i=1;i<list.size();i++){
//               if(!estatus.equals(list.get(i).getEstatus())){
//                   return false;
//               }
//           }
//       }
//        return true;
//
//    }

    public List getCheckedInfos(){
        List list=new ArrayList();
        for (BaseEquip equip :(List<BaseEquip>)infolist){
            if(equip.getCheckstate()==1)
            list.add(equip);
        }
        return list;
    }
    public void removeCheckedInfos(){
        for (int i=0;i<infolist.size(); i++){
            if(((BaseEquip)infolist.get(i)).getCheckstate()==1) {
                infolist.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
        notifyDataChange();
    }

    public void setInfos(List infos,int state) {
        infolist.clear();
        infolist.addAll(infos);
        this.state=state;
        notifyDataSetChanged();
        notifyDataChange();
    }


    public void addInfos(List infos) {
        for(int i=0;i<infos.size();i++){
            BaseEquip eq=(BaseEquip)infos.get(i);
            int idx=infolist.indexOf(eq);
            if(state==0){
                if(idx>=0){
                }else{
                    infolist.add(eq);
                    eq.setCheckstate(1);
                }
            }else{
                if(idx>=0&&((BaseEquip)infolist.get(idx)).getCheckstate()!=2){
                    ((BaseEquip)infolist.get(idx)).setCheckstate(1);
                }else if(idx<0){
                    infolist.add(eq);
                    eq.setCheckstate(1);
                }
            }

        }


        notifyDataSetChanged();
        notifyDataChange();

    }

    public void changeState(String nos[]){
        for(String str :nos){
            for(BaseEquip eq:(List<BaseEquip>)infolist){
                if (str.equals(eq.getCode())){
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
        notifyDataChange();


    }

    public void setinfo(View convert, ViewHolder holder, int pos) {
        BaseEquip info = (BaseEquip)infolist.get(pos);
        String num = "";
        String batch = "";
        String date = "";
        String manu = "";
        if(info instanceof TEquip){
            TEquip eq = (TEquip)info;
            num = eq.getEquipNum();
            batch=eq.getUniqueCode();
            date=eq.getProduceDate();
            manu = eq.getEquipCategroy();

        }else if(info instanceof WareHouseT){
            WareHouseT eq = (WareHouseT)info;
            num = Integer.parseInt(eq.getEquipNum())-eq.getReturnNum()+"";
            batch=eq.getUniqueCode();
            date=eq.getProduceDate();
//            manu = eq.get();
        }

        String ary[] = { num, batch,date,
                manu, "删除"};


        int color = info.getCheckstate()==1?0xff66ee66:
                info.getCheckstate()==2?0xffee7777:0xff666666;
        for (int i = 0; i < ary.length; i++) {
            holder.tvs[i].setText(ary[i]);
            if (i == ary.length-1) {
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
        public TextView tvs[] = new TextView[5];

        public ViewHolder(View v, Context context) {
            v.setTag(this);
            for (int i = 0; i < 5; i++) {
                tvs[i] = (TextView) v.findViewById(context.getResources().getIdentifier
                        ("tv0" + (i + 1), "id", context.getPackageName()));
            }
        }
    }


}
