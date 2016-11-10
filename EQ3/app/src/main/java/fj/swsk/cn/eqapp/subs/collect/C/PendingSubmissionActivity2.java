package fj.swsk.cn.eqapp.subs.collect.C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubAdapter;
import fj.swsk.cn.eqapp.subs.collect.Common.SceneService;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/27.
 */
public class PendingSubmissionActivity2 extends BaseTopbarActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    public ListView lv;
    public PendingSubAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.pending_submission_activity;
        super.onCreate(savedInstanceState);
        this.lv=(ListView)findViewById(R.id.lv01);
//        mTopbar.setRightButtonIsvisable(false);
        mTopbar.rightListener=this;
        adapter = new PendingSubAdapter(this);
        lv.setAdapter(adapter);
        adapter.setList(SceneService.ins.findAllBy(PushUtils.getLoginName(), "0"));
        lv.setOnItemClickListener(this);
        mTopbar.rightButton.setText("编辑");
        mTopbar.rightListener=this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateDatas();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(adapter.editMode)
            return ;
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
