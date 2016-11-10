package fj.swsk.cn.eqapp.subs.collect.C;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.subs.collect.Common.MediaService;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubAdapter;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubGVAdapter;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;

/**
 * Created by apple on 16/6/27.
 */
public class SubmissionHisActivity extends BaseTopbarActivity implements View.OnClickListener{

    public ListView lv;
    public PendingSubAdapter adapter;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.submission_his_activity;
        super.onCreate(savedInstanceState);
        this.lv=(ListView)findViewById(R.id.lv01);
        this.confirm=(Button)findViewById(R.id.confirm);
        mTopbar.rightButton.setText("选择");
        mTopbar.rightListener=this;
        adapter = new PendingSubAdapter(this);
        lv.setAdapter(adapter);
//        adapter.setList(MediaService.ins.findAllBy(PushUtils.getLoginName(),"1"));
        adapter.flag=1;
    }



    @Override
    public void onClick(View v) {
        if(v==mTopbar.rightButton){
            adapter.toggleMod();
            mTopbar.rightButton.setText(adapter.mulSelMode ? "取消" : "选择");
            confirm.setEnabled(adapter.mulSelMode);
        }else if (v==confirm){
            final List<T_Media> mediaList = new ArrayList<>();
            for(PendingSubGVAdapter ad:adapter.adapters){
//                for(T_Media media:ad.list){
//                    if(ad.selectedIds.contains(media._id)){
//                        mediaList.add(media);
//                    }
//                }
            }
//            if(mediaList.size()==0){
//                CommonUtils.toast("请选择要删除的数据");
//                return ;
//            }
//
//            delete(mediaList);
        }
    }

    private void delete(final List<T_Media> list){
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                for(T_Media media:list){
                    File file = new File(media.content_path);
                    if(file.exists()){
                        while(!file.delete()){}
                    }
                }
                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        afterDelete(list);
                    }
                });
            }
        });





    }

    private void afterDelete(List<T_Media> list){
        for(T_Media media:list) {
            MediaService.ins.delete(media._id);
//            for(EarthquateEvent vent:adapter.list){
//                vent.mediaList.remove(media);
//            }

        }
        adapter.refreshData();
    }

}
