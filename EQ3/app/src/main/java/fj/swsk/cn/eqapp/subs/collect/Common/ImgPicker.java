package fj.swsk.cn.eqapp.subs.collect.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ImgPicker{

    public Activity mContext;
    public File mFile;

    public ImgPicker(Activity con){
        this.mContext=con;

    }




    public void showImagePickDialog() {
        String title = "拍摄方式";
        String[] choices = new String[]{"拍照", "摄像"};
        AlertDialog.Builder dialog = DialogUtil.getItemDialogBuilder(mContext, choices, title,
                dialogListener);
        dialog.show();
    }



    private DialogInterface.OnClickListener dialogListener = new DialogInterface
            .OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            switch (which) {
                case 0:
                    startCamera(MediaStore.ACTION_IMAGE_CAPTURE,newImgFile(),3);
                    break;
                case 1:
                    startCamera(MediaStore.ACTION_VIDEO_CAPTURE,newVideoFile(),4);
                    break;
                default:
                    break;
            }
        }
    };

    private DialogInterface.OnClickListener dialogListener2 = new DialogInterface
            .OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    startCamera(MediaStore.ACTION_IMAGE_CAPTURE,newImgFile(),3);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    startCamera(MediaStore.ACTION_VIDEO_CAPTURE,newVideoFile(),4);
                    break;
                default:
                    break;
            }
        }
    };

    // 调用系统相机
    protected void startCamera(String type,File output,int requestCode) {
        // 调用系统的拍照功能
        if (output == null) return;
        Intent intent = new Intent(type);
        intent.putExtra("camerasensortype", 2); // 调用摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径

        intent.putExtra(MediaStore.EXTRA_OUTPUT, 0);
        mContext.startActivityForResult(intent, requestCode);
        CommonUtils.log(output.getAbsolutePath()+"======");
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data,Location loc) {
        if (resultCode == 0||loc==null) {
            mFile.delete();
            mFile=null;
            return;
        }
        switch (requestCode) {
            case 3:


            case 4:

//                ContentValues cv = new ContentValues();
//                cv.put(ISQLite.T_MEDIA.LOGINNAME, PushUtils.getLoginName());
//                cv.put(ISQLite.T_MEDIA.CONTENTNAME,mFile.getName());
//                cv.put(ISQLite.T_MEDIA.CONTENTPATH,mFile.getAbsolutePath());
//                cv.put(ISQLite.T_MEDIA.LOC_LAT, loc.getLatitude());
//                cv.put(ISQLite.T_MEDIA.LOC_LON, loc.getLongitude());
//                cv.put(ISQLite.T_MEDIA.EVENTID, "1");
//                cv.put(ISQLite.T_MEDIA.TITLE, "");
//                cv.put(ISQLite.T_MEDIA.DETAIL, "");
//                cv.put(ISQLite.T_MEDIA.REMARK, "");
//                MediaService.ins.insert(cv);
                mFile=null;
                AlertDialog dialog= new AlertDialog.Builder(mContext)
                        .setTitle("提示").setMessage("是否继续拍摄？")
                        .setPositiveButton("结束", dialogListener2)
                        .setNegativeButton("继续拍照", dialogListener2).
                        setNeutralButton("继续摄影", dialogListener2).create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(0xffff8888);

                break;
            default:
                break;
        }


    }



    private File newImgFile() {
        String timestamp = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss'.png'").format(new Date());
        try {
            File file =  new File(mContext.getFilesDir(),timestamp);
            if(!file.exists())
                file.createNewFile();
            file.setWritable(true,false);
            file.setReadable(true, false);
            mFile=file;
            return file;
        }catch (Exception e){
            return null;
        }
    }
    private File newVideoFile() {
        String timestamp = new SimpleDateFormat("'MP4'_yyyyMMdd_HHmmss'.mp4'").format(new Date());
        try {
            File file =  new File(mContext.getFilesDir(),timestamp);
            if(!file.exists())
                file.createNewFile();
            file.setWritable(true,false);
            file.setReadable(true, false);
            mFile=file;
            return file;
        }catch (Exception e){
            return null;
        }
    }






}
