package fj.swsk.cn.eqapp.subs.more.C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.subs.more.M.DocFile;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;
import fj.swsk.cn.eqapp.subs.more.V.EmergencyAdapter;
import fj.swsk.cn.eqapp.util.AppUtil;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/7/20.
 */
public class EmergencyPrescript extends BaseTopbarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv;
    private EmergencyAdapter adapt;
    String prefixes[] = {"hbr_","adm_"};

    String prefixenames[] = {"灾情简报_","辅助决策报告_"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.emergencypres_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setTitleText(getIntent().getStringExtra("title"));
        lv = (ListView) findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);
        EQInfo info = EQInfo.getIns();
        if(info==null||!info.hasLayer()){
            lv.setVisibility(View.GONE);
        }else {
            int idx = getIntent().getIntExtra("type", 0);
            lv.setVisibility(View.VISIBLE);
            List<DocFile> list = new ArrayList<>();
            list.add(DocFile.getByPrefix(prefixes[idx], prefixenames[idx], info));

            lv.setAdapter(adapt = new EmergencyAdapter(this, list));
            adapt.mListener = this;
            lv.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            int pos = (int) v.getTag();
            DocFile df = adapt.getItem(pos);
            if (!new File(df.path).exists()){

                NetUtil.dl2F(this, df.url, df.path, new NetUtil.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        if(new File(resp).exists()){
                            CommonUtils.toast("下载成功");
                            adapt.notifyDataSetChanged();
                        }else{
                            CommonUtils.toast("下载失败");
                        }
                    }
                });
                return;
            }

            Intent intent = AppUtil.openFile(df.path);
            if (intent!=null&&intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                CommonUtils.toast("无可打开此种类型文件的应用");
            }

        }

    }
}
