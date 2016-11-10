package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.WareHouseT;

/**
 * Created by apple on 16/2/19.
 */
public class AnquanItemsAdapter2 extends BaseAdapter  implements View.OnClickListener{


    private int resId = R.layout.item4lv_anquanitem_status2;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    protected Context context;


    public List<WareHouseT> infolist;
    public int state = 0;


    public AnquanItemsAdapter2(Context con) {
        super();
        this.context = con;
        infolist = new ArrayList<WareHouseT>();
    }


    public void setInfos(List<WareHouseT> infos) {
        for(WareHouseT eq:infos){
            eq.setCheckstate(1);
            eq.setReturnNo(eq.getDefNum());
        }

        infolist=infos;


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




    public void setinfo(View convert, ViewHolder holder, int pos) {
        WareHouseT info = infolist.get(pos);
        String ary[] = {info.getDefNum()+"", info.getUniqueCode(), sdf.format(info.getUpdateTime()),
                info.getReturnNo()+"","删除" };
        if (info.getDefNum()==1){
            holder.tvs[ary.length-2].setEnabled(false);
        }else{
            holder.tvs[ary.length-2].setEnabled(true);
        }
        int color = info.getCheckstate() == 1 ? 0xff66ee66 :
                info.getCheckstate() == 2 ? 0xffee7777 : 0xff666666;

        for (int i = 0; i < ary.length; i++) {
            holder.tvs[i].setText(ary[i]);
            if (i == ary.length-1) {
                holder.tvs[i].setTextColor(0xffff4444);
                holder.tvs[i].setTag(pos);
                holder.tvs[i].setOnClickListener(this);
            } else
                holder.tvs[i].setTextColor(color);
        }


        final EditText et=(EditText)holder.tvs[ary.length-2];
        et.setTag(R.id.tv01,pos);
        if(et.getTag()==null){
            TextWatcher tw;
            et.addTextChangedListener(tw=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString())){
                        et.setText("0");
                        et.setSelection(et.getText().length());
                        return ;
                    }
                    int count =Integer.valueOf(s.toString());
                    WareHouseT info = infolist.get((int) et.getTag(R.id.tv01));
                    int defc = info.getDefNum();
                    if(count>defc){
                        et.setText(defc+"");
                    }if(count<0){
                        et.setText("0");
                    }else{
                        info.setReturnNo(count);
                    }
                    et.setSelection(et.getText().length());

                }
            });
            et.setTag(tw);
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
            for (int i = 0; i < tvs.length; i++) {
                tvs[i] = (TextView)v.findViewById(context.getResources().getIdentifier
                        ("tv0" + (i + 1), "id", context.getPackageName()));
            }
        }
    }


    @Override
    public void onClick(View v) {
        infolist.remove((int) v.getTag());
        notifyDataSetChanged();


    }


}
