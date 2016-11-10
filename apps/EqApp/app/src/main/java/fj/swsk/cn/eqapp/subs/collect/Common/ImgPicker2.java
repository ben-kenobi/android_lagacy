package fj.swsk.cn.eqapp.subs.collect.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ImgPicker2 implements AdapterView.OnItemClickListener{
    public GridView gv;
    public AddPicGVAdapter adapter;
    public File mFile;
    public Activity mContext;
    public ImgPicker2(Activity con, GridView gridView){
        this.mContext=con;
        this.gv=gridView;
        adapter = new AddPicGVAdapter(con);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        deleteIMG(position);
    }


    protected void deleteIMG(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确认移除已添加的内容吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CommonUtils.context.curscene.list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            mFile=null;
            return;
        }
        switch (requestCode) {
            case 3:
                addBM();
                break;
            case 4:

                Uri uri = data.getData();
                String pat=uri.getPath();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = mContext.getContentResolver().query(
                            uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    //光标移动至开头 获取图片路径
                    cursor.moveToFirst();
//                   String names[]= cursor.getColumnNames();
                    mFile = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    cursor.close();
                    addBM();
                }

                break;
            case 5:
                addBM();
                break;
            default:
                break;
        }


    }


    public void addBM(){
        CommonUtils.context.curscene.list.add(mFile);
        adapter.notifyDataSetChanged();
    }




    // 调用系统相册
    public void startPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
        mContext.startActivityForResult(intent, 4);
    }

    // 调用系统相机
    public void startCamera() {
        File output = null;
        try {
            output = newImgFile();
        } catch (Exception e) {
            CommonUtils.toast("调用摄像头失败");
        }

        if (output == null) return;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);

        // 指定调用相机拍照后照片的存储路径

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        mContext.startActivityForResult(intent, 3);
        CommonUtils.log(output.getAbsolutePath() + "======");
        mFile=output;

    }
    // 调用系统相机
    public void startRecorder( ) {
        File output = null;
        try {
            output = newVideoFile();
        } catch (Exception e) {
            CommonUtils.toast("调用摄像头失败");
        }
        if (output == null) return;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // 指定调用相机拍照后照片的存储路径

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        mContext.startActivityForResult(intent, 5);
        CommonUtils.log(output.getAbsolutePath()+"======");
        mFile=output;
    }

    private File newImgFile() {
        String timestamp = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss'.png'").format(new Date());
        try {
            File file =  new File(mContext.getFilesDir(),timestamp);
            if(!file.exists())
                file.createNewFile();
            file.setWritable(true,false);
            file.setReadable(true, false);

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
            return file;
        }catch (Exception e){
            return null;
        }
    }



}
