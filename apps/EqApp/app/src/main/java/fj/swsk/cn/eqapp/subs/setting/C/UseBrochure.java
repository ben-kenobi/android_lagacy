package fj.swsk.cn.eqapp.subs.setting.C;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.subs.more.M.DocFile;
import fj.swsk.cn.eqapp.subs.more.V.EmergencyAdapter;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.IOUtils;

/**
 * Created by apple on 16/7/20.
 */
public class UseBrochure extends BaseTopbarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv;
    private EmergencyAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.emergencypres_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setTitleText(getIntent().getStringExtra("title"));
        lv = (ListView) findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);
        lv.setVisibility(View.VISIBLE);
        List<DocFile> list = new ArrayList<>();

        DocFile dc = DocFile.getByPath("file:///android_asset/usebrochure.doc");
        dc.showname="辅助决策系统Android端APP用户操作手册v1.1.doc";
        dc.name="辅助决策系统Android端APP用户操作手册v1.1.doc";
        try {
            dc.size = getAssets().openFd("usebrochure.doc").getLength();
        }catch (Exception e){
            e.printStackTrace();
        }
        list.add(dc);
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
            File path = new File(getFilesDir(),new File(df.path).getName());

            if (!path.exists()) {
                try {
                    InputStream is = getAssets().open(new File(df.path).getName());
                    OutputStream os = new FileOutputStream(path);
                    IOUtils.copy(is, os);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile((path));
            intent.setDataAndType(uri, "application/msword");
            if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                CommonUtils.toast("无可打开此种类型文件的应用");
            }
        }
    }
}
