package fj.swsk.cn.eqapp.subs.collect.C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubHisAdapter;
import fj.swsk.cn.eqapp.subs.collect.Common.SceneService;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/27.
 */
public class SubmissionHisActivity2 extends BaseTopbarActivity implements AdapterView.OnItemClickListener,View.OnClickListener{

    public ListView lv;
    public PendingSubHisAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.submission_his_activity;
        super.onCreate(savedInstanceState);
        this.lv=(ListView)findViewById(R.id.lv01);
//        mTopbar.setRightButtonIsvisable(false);
        mTopbar.rightButton.setText("编辑");
        mTopbar.rightListener=this;
        adapter = new PendingSubHisAdapter(this);
        lv.setAdapter(adapter);
        adapter.setList(SceneService.ins.findAllBy(PushUtils.getLoginName(), "1"));
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonUtils.context.curscene=adapter.list.get(position);
        Intent intent = new Intent(this, EditSceneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v==mTopbar.rightButton){
            adapter.toggleEditMode();
            mTopbar.rightButton.setText(adapter.editMode ? "取消" : "编辑");
        }
    }
}
