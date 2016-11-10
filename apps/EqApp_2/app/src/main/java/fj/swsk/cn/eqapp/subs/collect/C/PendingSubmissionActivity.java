package fj.swsk.cn.eqapp.subs.collect.C;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.collect.Common.MediaService;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubAdapter;
import fj.swsk.cn.eqapp.subs.collect.Common.PendingSubGVAdapter;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/27.
 */
public class PendingSubmissionActivity extends BaseTopbarActivity implements View.OnClickListener{

    public ListView lv;
    public PendingSubAdapter adapter;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.pending_submission_activity;
        super.onCreate(savedInstanceState);
        this.lv=(ListView)findViewById(R.id.lv01);
        this.confirm=(Button)findViewById(R.id.confirm);
        mTopbar.rightButton.setText("选择");
        mTopbar.rightListener=this;
        adapter = new PendingSubAdapter(this);
        lv.setAdapter(adapter);
//        adapter.setList(MediaService.ins.findAllBy(PushUtils.getLoginName(),"0"));
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
            if(mediaList.size()==0){
                CommonUtils.toast("请选择要上传的数据");
                return ;
            }
            for(T_Media media:mediaList){
//                if(TextUtils.isEmpty(media.detail)){
//                    DialogUtil.getMessageDialogBuilder(this, "有数据未添加描述，是否继续上传", "提醒",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    upload(mediaList);
//                                }
//                            }).show();
//
//                    return ;
//                }
            }
            upload(mediaList);
        }
    }

    private void upload(final List<T_Media> list){

        String name = PushUtils.getToken();
        Map<String,Map<String,Object>> infolist = new HashMap<>();
        Map<String,File> files = new HashMap<>();
        for(T_Media media:list){
//            infolist.put(media.content_name,media.genUploadData());
            media.getFile(files);
        }
        String json = JsonTools.getJsonString(infolist);
        Map<String, String> map1 = new HashMap<>();
        map1.put("infolist", json);

        String path = IConstants.uploadUrl + "?token=" + CommonUtils.UrlEnc(name);



        NetUtil.commonRequest5(path, this, map1, files, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if (type != 0) {
                    CommonUtils.toast("上传失败，请重新上传");
                } else {
                    new AlertDialog.Builder(PendingSubmissionActivity.this).setTitle("上传成功！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    afterUpload(list);
                                }
                            }).show();
                }
            }
        });



    }

    private void afterUpload(List<T_Media> list){
        for(T_Media media:list) {
            ContentValues cv = new ContentValues();
            cv.put(ISQLite.T_MEDIA.FLAG, 1);
            MediaService.ins.update(cv, media._id);
//            for(EarthquateEvent vent:adapter.list){
//                vent.mediaList.remove(media);
//            }

        }
        adapter.refreshData();
    }

}
