package fj.swsk.cn.eqapp.subs.more.C;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;

/**
 * Created by apple on 16/7/6.
 */
public class EQHisActivity extends BaseTopbarActivity implements AdapterView.OnItemClickListener {
    public static int ReturnFlag = 1011;


    private ListView lv;
    private CommonAdapter<EQInfo> adapt;
    private List<EQInfo> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.eqhis_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        lv = (ListView)findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);

        String path = IConstants.eqHisUrl+"";
        NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
//                String js ="{\"status\":200,\"serviceId\":\"\",\"msg\":\"\",\"data\":[{\"tid\":\"b855256c-22d3-48d2-8c50-a292d8a6537a\",\"obsTime\":1469414389658,\"occurTime\":\"2015,09,16,03:37:33.6630\",\"occurRegionName\":\"中国台湾\",\"lat\":24.25563,\"lon\":121.86916,\"focalDepth\":14.084013,\"magnitude\":\"5.3(M)\",\"remark\":null,\"createTime\":\"2016-07-2510:39:49\",\"creatorId\":\"sys\",\"modifyTime\":null,\"modifierId\":null},{\"tid\":\"b855256c-22d3-48d2-8c50-a292d8a6537a\",\"obsTime\":1469414389658,\"occurTime\":\"2015,09,16,03:37:33.6630\",\"occurRegionName\":\"中国台湾\",\"lat\":24.25563,\"lon\":121.86916,\"focalDepth\":14.084013,\"magnitude\":\"5.3(M)\",\"remark\":null,\"createTime\":\"2016-07-2510:39:49\",\"creatorId\":\"sys\",\"modifyTime\":null,\"modifierId\":null},{\"tid\":\"b855256c-22d3-48d2-8c50-a292d8a6537a\",\"obsTime\":1469414389658,\"occurTime\":\"2015,09,16,03:37:33.6630\",\"occurRegionName\":\"中国台湾\",\"lat\":24.25563,\"lon\":121.86916,\"focalDepth\":14.084013,\"magnitude\":\"5.3(M)\",\"remark\":null,\"createTime\":\"2016-07-2510:39:49\",\"creatorId\":\"sys\",\"modifyTime\":null,\"modifierId\":null},{\"tid\":\"b855256c-22d3-48d2-8c50-a292d8a6537a\",\"obsTime\":1469414389658,\"occurTime\":\"2015,09,16,03:37:33.6630\",\"occurRegionName\":\"中国台湾\",\"lat\":24.25563,\"lon\":121.86916,\"focalDepth\":14.084013,\"magnitude\":\"5.3(M)\",\"remark\":null,\"createTime\":\"2016-07-2510:39:49\",\"creatorId\":\"sys\",\"modifyTime\":null,\"modifierId\":null},{\"tid\":\"b855256c-22d3-48d2-8c50-a292d8a6537a\",\"obsTime\":1469414389658,\"occurTime\":\"2015,09,16,03:37:33.6630\",\"occurRegionName\":\"中国台湾\",\"lat\":24.25563,\"lon\":121.86916,\"focalDepth\":14.084013,\"magnitude\":\"5.3(M)\",\"remark\":null,\"createTime\":\"2016-07-2510:39:49\",\"creatorId\":\"sys\",\"modifyTime\":null,\"modifierId\":null}]}";
//                map = JsonTools.getMap(js);
                List<Map<String,Object>> li =(List<Map<String,Object>>) map.get("data");
                list=new ArrayList<>();
                for(Map<String,Object> m:li){
                    EQInfo ei=JsonTools.getInstance(JsonTools.getJsonString(m),
                            new TypeReference<EQInfo>() {
                            });
                    ei.hasLayer=true;
                    list.add(ei);
                }


                lv.setAdapter(adapt = new CommonAdapter<EQInfo>(EQHisActivity.this, list, R.layout.item4lv_eqhislv) {
                    @Override
                    public void convert(ViewHolder holder, EQInfo o) {
                        holder.setText(R.id.title, o.occurRegionName + "    " + o.magnitude+"  "+o.obsTime);
                        holder.setText(R.id.time, o.occurTime);

                    }
                });
            }
        });


        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EQInfo.setIns(list.get(position));
        setResult(ReturnFlag);
        finish();
    }
}
