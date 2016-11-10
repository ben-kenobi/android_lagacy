package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.TCodeCount;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.utils.DialogUtils;

/**
 * Created by apple on 16/2/19.
 */
public class AnquanItemsAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    public List<List<TEquip>> manualList=new ArrayList<>();
    public List<List<TEquip>> scanList=new ArrayList<>();

    private int resId = R.layout.item4lv_anquanitem;

    public List<TEquip> infolist;
    public int state=0;
    public boolean operatable=true;

    public Observable obsable = new Observable(){
        public void notifyChange(){
            setChanged();
            notifyObservers(infolist);
        }
    };

    private void notifyDataChange(){
        try {
            obsable.getClass().getMethod("notifyChange").invoke(obsable);
        }catch (Exception e){
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }


    public AnquanItemsAdapter(Context con, AdapterView av) {
        super();
        this.context = con;
        infolist = new ArrayList<TEquip>();
    }

    public boolean isStatusConsistence(List<TEquip> list){
       if(list.size()>0){
           String estatus = list.get(0).getEstatus();
           if(estatus==null||(!"2".equals(estatus)/*&&!"3".equals(estatus)*/)){
               return false;
           }
           for(int i=1;i<list.size();i++){
               if(!estatus.equals(list.get(i).getEstatus())){
                   return false;
               }
           }
       }
        return true;

    }

    public List<TEquip> getCheckedInfos(){
        List<TEquip> list=new ArrayList<TEquip>();
        for (TEquip equip :infolist){
            if(equip.getCheckstate()==1)
            list.add(equip);
        }
        return list;
    }

    public List<TCodeCount> getCheckedCodeCount(String savetype){
        List<TCodeCount> list = new ArrayList<>();
        TCodeCount tc = null;
        for (List<TEquip> li:manualList){
            tc = new TCodeCount(1,savetype,li);
            if (!TextUtils.isEmpty(tc.code))
                list.add(tc);
        }
        for (List<TEquip> li:scanList){
            tc = new TCodeCount(0,savetype,li);
            if (!TextUtils.isEmpty(tc.code))
                list.add(tc);
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
        manualList.clear();
        scanList.clear();
        notifyDataChange();

    }

    public void setInfos(List<TEquip> infos,int state) {
        infolist.clear();
        infolist.addAll(infos);
        this.state=state;

        notifyDataChange();
    }


    public void addInfos(List<TEquip> infos) {
        for(int i=0;i<infos.size();i++){
            TEquip eq=infos.get(i);
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
                    TEquip obj = infolist.remove(idx);
                    infolist.add(0, obj);
                    eq.setCheckstate(1);
                }else if(idx<0){
                    infolist.add(eq);
                    eq.setCheckstate(2);
                }
            }

        }

        notifyDataChange();

    }

    public void changeState(String nos[]){
        for(String str :nos){
            for(TEquip eq:infolist){
                if (str.equals(eq.getOriginalNo())){
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
    public void onClick(final View v) {
        DialogUtils.getMessageDialogBuilder(context, "删除提示", "确定删除该条数据？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                infolist.remove((int) v.getTag());
                notifyDataChange();
            }
        }).create().show();




    }

    public void setinfo(View convert, ViewHolder holder, int pos) {
        TEquip info = infolist.get(pos);
        String ary[] = {info.getOriginalNo(),  info.getBatch(), info.getProduceDate(),
                info.getManuFacturer(), "删除"};
        for (int i = 0; i < ary.length; i++) {
            holder.tvs[i].setText(ary[i]);
        }

        int color = info.getCheckstate()==1?0xff66ee66:
                info.getCheckstate()==2?0xffee7777:0xff666666;
        for (int i = 0; i < ary.length; i++) {
            if (i == ary.length-1) {
                holder.tvs[i].setTextColor(0xffff4444);
                holder.tvs[i].setTag(pos);
                holder.tvs[i].setOnClickListener(this);
                holder.tvs[i].setVisibility(operatable?View.VISIBLE:View.GONE);
            } else
                holder.tvs[i].setTextColor(color);
        }

        if (pos % 2 == 0) {
            convert.setBackgroundColor(0xffffffff);
        } else {
            convert.setBackgroundColor(0xfff5f5f5);
        }

    }

    public void clear(){
        infolist.clear();
        notifyDataChange();
    }

    public static class ViewHolder {
        public TextView tvs[] = new TextView[5];

        public ViewHolder(View v, Context context) {
            v.setTag(this);
            for (int i = 0; i < tvs.length; i++) {
                tvs[i] = (TextView) v.findViewById(context.getResources().getIdentifier
                        ("tv0" + (i + 1), "id", context.getPackageName()));
            }
        }
    }


}
