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
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;

/**
 * Created by apple on 16/11/9.
 */
public class EqBrochureActivity extends BaseTopbarActivity implements AdapterView.OnItemClickListener {


    private ListView lv;
    private CommonAdapter<String> adapt;
    private List<String> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.eqhis_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        lv = (ListView)findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);
        mTopbar.setTitleText(getIntent().getStringExtra("title"));
        list.add("福建省地震局机关及事业单位地震应急预案");
        list.add("福建省地震系统地震应急预案");

        lv.setAdapter(adapt = new CommonAdapter<String>(EqBrochureActivity.this, list, R.layout.item4lv_eqhislv) {
            @Override
            public void convert(ViewHolder holder, String o) {
                holder.setText(R.id.title, o);
                holder.setText(R.id.time, "");

            }
        });

        lv.setOnItemClickListener(this);

}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, LoacalWebActivity.class);
        intent.putExtra("title", getIntent().getStringExtra("title"));
        if (position==0) {
            intent.putExtra("path", "file:///android_asset/yjya.html");
        }else{
            intent.putExtra("path", "file:///android_asset/qebrochure.html");
        }
        this.startActivity(intent);
    }
}
