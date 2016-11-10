package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.bean.Region;
import cn.swsk.rgyxtqapp.bean.TWareHouse;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by Administrator on 2016/3/9.
 */
public class Pop1Action extends BasePopAction {

    private CascadeLvAdapter<Region> adapter;
    private CommonAdapter<TWareHouse> sub2Adapter;
    public ListView lv, sub2Lv;
    private Context context;
    public HttpUtils.RequestCB cb;
    public int selidx;

    public Pop1Action( Context context, View trig) {
        super(R.layout.pop0_community,LayoutInflater.from(context), trig);
        this.context=context;
        execute();
    }

    public String getSelectedRegion(){
        if( selidx==-1){
            return adapter.selectedItem().getRegionName();
        }else{
            return sub2Adapter.getItem(selidx).getBelongRegion();
        }

    }
    public void execute()  {
        lv = (ListView)contentView.findViewById(R.id.listView1);
        sub2Lv = (ListView)contentView.findViewById(R.id.listView2);
        lv.setAdapter(adapter = new CascadeLvAdapter<Region>(context) {
            @Override
            public void convert(ViewHolder holder, Region o) {
                holder.setText(R.id.textView1, o.getRegionName());
                if (holder.getPosition() == selected)
                    holder.getConvertView().setBackgroundColor(selectedColor);
                else
                    holder.getConvertView().setBackgroundColor(Color.argb(0, 0, 0, 0));
                TextView tv = holder.getView(R.id.textView1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_arrow_small, 0);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View self,
                                    int position, long id) {
                if (adapter.selected == position) return;
                adapter.setSelected(position);
                refreshSubLvAdapter(position);
            }
        });

        sub2Lv.setAdapter(sub2Adapter = new CommonAdapter<TWareHouse>(context, null, R.layout.simple_list_item3) {
            @Override
            public void convert(ViewHolder holder, TWareHouse ware) {
                holder.setText(R.id.textView1, ware.getLoginName());
                if (holder.getPosition() == selidx) {
                    holder.setBackgroundColor(R.id.textView1, 0xffaaaaaa);
                } else {
                    holder.setBackgroundColor(R.id.textView1, 0);
                }
            }
        });

        sub2Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pop.dismiss();
                selidx = position;
                if (cb != null)
                    cb.cb(null, sub2Adapter.getItem(position).getLoginName(), 0);
            }
        });
    }



    @Override
    protected void onShow() {
        if(adapter.getCount()<1) {
            loadDatas("3a0c168d-6225-4ac5-86e5-117a81d7e842", adapter, false);
        }
    }

    private void refreshSubLvAdapter(final int position) {

        selidx = -1;
        loadDatas2(adapter.selectedItem().getRegionName(), sub2Adapter);


    }


    private void loadDatas(final String rootid,final CommonAdapter<Region> cd,boolean city) {
        String citypara = !city?"":"&LV=1";
        HttpUtils.commonRequest(PushUtils.getServerIP(context) + "rgyx/AppManageSystem/findChildRegionsByParentId?parentId=" + rootid + citypara, context,
                new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        resp = JsonTools.getJsonStr(map.get("list"));
                        List<Region> list = null;
                        if (resp != null && resp.startsWith("["))
                            list = JsonTools.getRegion(resp);
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (TextUtils.isEmpty(list.get(i).getRegionName().trim())) {

                                    list.remove(i);
                                    i--;
                                }
                            }

                            cd.setDatas(list);

                        } else {
                            CommonUtils.toast("获取数据失败", context);
                        }
                    }
                });


    }
    private void loadDatas2( String name,final CommonAdapter<TWareHouse> cd) {
        final String rootid=CommonUtils.UrlEnc(name);
        HttpUtils.commonRequest(PushUtils.getServerIP(context)+"rgyx/AppManageSystem/findWareHouse?regionName=" + rootid, context,
                new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        resp = JsonTools.getJsonStr(map.get("list"));
                        List<TWareHouse> list = JsonTools.getWareHouses(resp);

                        if (list != null) {
                            for(int i =0;i<list.size();i++){
                                String lname = list.get(i).getLoginName();
                                if(lname==null||lname.trim().equals("")){
                                    list.remove(i);
                                    i--;
                                }
                            }
                            cd.setDatas(list);
                        } else {
                            CommonUtils.toast("获取数据失败", context);
                        }
                    }
                });


    }

    public void resetTrigger() {
    }
}