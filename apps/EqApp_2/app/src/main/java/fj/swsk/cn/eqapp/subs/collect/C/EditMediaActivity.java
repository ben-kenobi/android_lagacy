package fj.swsk.cn.eqapp.subs.collect.C;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/29.
 */
public class EditMediaActivity extends BaseTopbarActivity implements View.OnClickListener {

    TextView location;
    EditText detail;
    Button edit,cancel;
    ImageView mImageView,videoflag;
    Boolean editMode=false;
    T_Media mT_media= CommonUtils.context.curEdit;
    int flag=0;


    protected void onCreate(Bundle savedInstanceState) {
//        layoutRes= R.layout.edit_media_activity;
//        super.onCreate(savedInstanceState);
//        flag = getIntent().getIntExtra("flag",0);
//        mTopbar.setRightButtonIsvisable(false);
//        mTopbar.setTitleText("");
//        mTopbar.leftButton.setTextColor(getResources().getColorStateList(R.color.tv_state_list));
//        initUI();
//        Drawable ld=getResources().getDrawable(R.mipmap.location);
//        ld.setBounds(0,0, ResUtil.dp2Intp(20),ResUtil.dp2Intp(20));
//        location.setCompoundDrawables(ld,null,null,null);
//        location.setText("地点:" + String.format("%.2f", mT_media.loc_lon) +
//                " , " + String.format("%.2f", mT_media.loc_lat) + "   " + IConstants.TIMESDF.format(mT_media.addtime));
//        detail.setText(mT_media.detail);
//
//        final String path = mT_media.content_path;
//
//        IConstants.THREAD_POOL.submit(new Runnable() {
//            @Override
//            public void run() {
//                if (path.endsWith(".mp4")) {
//                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
//                            MediaStore.Images.Thumbnails.MINI_KIND);
//                    IConstants.MAIN_HANDLER.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mImageView.setImageBitmap(thumb);
//                           videoflag.setVisibility(View.VISIBLE);
//                        }
//                    });
//
//
//                } else {
//                    final Bitmap thumb = CommonUtils.getImage(path, DeviceInfoUtil.getScreenWidth(),DeviceInfoUtil.getScreenHeight());
//                    IConstants.MAIN_HANDLER.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mImageView.setImageBitmap(thumb);
//                           videoflag.setVisibility(View.GONE);
//                        }
//                    });
//
//
//                }
//            }
//        });
//
//
    }
//
//    private void initUI(){
//        location=(TextView)findViewById(R.id.location);
//        detail=(EditText)findViewById(R.id.et01);
//        edit=(Button)findViewById(R.id.edit);
//        cancel=(Button)findViewById(R.id.cancel);
//        mImageView=(ImageView)findViewById(R.id.iv01);
//        videoflag=(ImageView)findViewById(R.id.videoflag);
//        findViewById(R.id.operation).setVisibility(flag==0?View.VISIBLE:View.GONE);
//    }
//
//
//    @Override
    public void onClick(View v) {
//        if(v==cancel){
//            editMode=false;
//            detail.setText(mT_media.detail);
//            updateUI();
//        }else if (v==edit){
//            editMode=!editMode;
//            if(!editMode) {
//                mT_media.detail=detail.getText().toString();
//                ContentValues cv = new ContentValues();
//                cv.put(ISQLite.T_MEDIA.DETAIL,mT_media.detail);
//                MediaService.ins.update(cv, mT_media._id);
//            }
//            updateUI();
//
//        }
    }
//
//   private void  updateUI(){
//       edit.setText(editMode?"保存":"编辑");
//       cancel.setVisibility(editMode ? View.VISIBLE : View.GONE);
//       detail.setEnabled(editMode);
//    }


}
