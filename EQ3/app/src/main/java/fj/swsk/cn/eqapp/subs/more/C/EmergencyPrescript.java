package fj.swsk.cn.eqapp.subs.more.C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.subs.more.M.DocFile;
import fj.swsk.cn.eqapp.subs.more.V.EmergencyAdapter;
import fj.swsk.cn.eqapp.util.AppUtil;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/7/20.
 */
public class EmergencyPrescript extends BaseTopbarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv;
    private EmergencyAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.emergencypres_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        lv = (ListView) findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);

        List<DocFile> list = new ArrayList<>();
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());
        list.add(DocFile.getDefault());

        lv.setAdapter(adapt = new EmergencyAdapter(this, list));
        adapt.mListener = this;
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            int pos = (int) v.getTag();
            DocFile df = adapt.getItem(pos);
            if (df.path == null)
                return;
            Intent intent = AppUtil.openFile(df.path);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                CommonUtils.toast("无可打开此种类型文件的应用");
            }

        }

    }
}
