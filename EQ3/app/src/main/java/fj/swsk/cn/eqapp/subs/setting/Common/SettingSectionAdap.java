package fj.swsk.cn.eqapp.subs.setting.Common;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.main.V.PinnedSectionListView;
import fj.swsk.cn.eqapp.subs.setting.M.SettingDatas;
import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/6/16.
 */
public class SettingSectionAdap extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {


    public static final int ITEM = 0;
    public static final int SECTION = 1;

    private boolean hasHeaderAndFooter;
    private boolean isFastScroll=false;
    private boolean addPadding;
    private boolean isShadowVisible = true;
    private int mDatasetUpdateCount;

    public Context mContext;
    public LayoutInflater mInflater;
    public int layoutId= R.layout.item4lv_common_menuitem;
    public int sectionLayoutId= R.layout.section4lv_common_menusection;
    public List<Integer> sections=new ArrayList<>();
    public int count=0;
    private Map<String,String> details=new HashMap<>();

    public SettingSectionAdap(Context context) {
        mContext = context;
        mInflater=LayoutInflater.from(context);
        for(String[] strary : SettingDatas.itemtitles){
            sections.add(count);
            count+=strary.length+1;
        }
    }


    public void setDetails(Map<String, String> details) {
        this.details = details;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return count;
    }

    @Override
    public Object getItem(int position) {
        int [] ary = pos2secNitem(position);
        if(ary[1]==-1){
            return "";
        }
        return SettingDatas.items[ary[0]][ary[1]];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int[] pos2secNitem(int pos){
        for(int i=0;i<sections.size()-1;i++){
            int sec = sections.get(i);
            if(sec <= pos && pos<sections.get(i+1)){
                return  new int[]{i,pos-sec-1};
            }
        }
        return new int[]{sections.size()-1,pos-sections.get(sections.size()-1)-1};
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder=null;
        if (getItemViewType(position)==SECTION){
             holder = ViewHolder.get(mContext, convertView, parent,
                     sectionLayoutId, null, position);
            String title = SettingDatas.sectionTitle[sections.indexOf(position)];
            holder.getView(R.id.textView1).getLayoutParams().height= ResUtil.dp2Intp(TextUtils.isEmpty(title)?0:25);
            holder.setText(R.id.textView1,title);
        }else{
            int ary[] = pos2secNitem(position);

             holder = ViewHolder.get(mContext, convertView, parent,
                    layoutId,null, position);
            String title = SettingDatas.itemtitles[ary[0]][ary[1]];
            holder.setText(R.id.text, title);
            holder.setImageResource(R.id.icon, SettingDatas.itemicons[ary[0]][ary[1]]);
            holder.setImageResource(R.id.accessory, SettingDatas.hasaccessory[ary[0]][ary[1]] ? R.mipmap.next : 0);
            holder.setVisible(R.id.myswitch, SettingDatas.hasswich[ary[0]][ary[1]]);


            String detail =null;
            detail = SettingDatas.items[ary[0]][ary[1]];
            holder.setText(R.id.detail,detail==null?"":detail);

            if(position==1){
                CheckBox sw=(CheckBox)holder.getView(R.id.myswitch);
                sw.setChecked(PushUtils.isautologin());
                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        PushUtils.setautologin(isChecked);
                    }
                });
            }
        }


        return holder.getConvertView();
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return sections.contains(position)?SECTION:ITEM;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SECTION;
    }
}
