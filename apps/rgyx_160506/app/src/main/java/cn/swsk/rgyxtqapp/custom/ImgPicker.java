package cn.swsk.rgyxtqapp.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.adapter.AddPicGVAdapter;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ImgPicker implements AdapterView.OnItemClickListener{
    public GridView gv;
    public AddPicGVAdapter adapter;
    public File photo = null;
    public List<File> tempFiles = new ArrayList<>();
    public Activity mContext;

    public ImgPicker(Activity con,GridView gridView){
        this.mContext=con;
        this.gv=gridView;
        adapter = new AddPicGVAdapter(con);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == adapter.list.size() - 1) {
            showImagePickDialog();
        } else {
            deleteIMG(position);
        }

    }

    public void showImagePickDialog() {
        String title = "获取图片方式";
        String[] choices = new String[]{"拍照", "从手机中选择"};
        AlertDialog.Builder dialog = DialogUtils.getItemDialogBuilder(mContext, choices, title,
                dialogListener);
        dialog.show();
    }

    protected void deleteIMG(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.list.remove(position);
                adapter.notifyDataSetChanged();
                tempFiles.remove(position);
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


    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface
            .OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            switch (which) {
                case 0:
                    startCamera();
                    break;
                case 1:
                    startPick();
                    break;
                default:
                    break;
            }
        }
    };

    // 调用系统相机
    protected void startCamera() {

        // 调用系统的拍照功能
        File file = null;
        try {
            file = newImgFile();
        } catch (Exception e) {
            CommonUtils.toast("调用摄像头失败", mContext);
        }

        if (file == null) return;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        photo = file;
        mContext.startActivityForResult(intent, 3);
    }

    // 调用系统相册
    protected void startPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
        mContext.startActivityForResult(intent, 4);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            photo=null;
            return;
        }
        switch (requestCode) {
            case 3:
                addBM(null,false);
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
                    photo = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    addBM(null,true);
                    cursor.close();
                }

                break;
            default:
                break;
        }


    }

    private void addBM(Uri uri,boolean copy) {
        try {

            File tofile =  !copy? photo : newImgFile();

            Bitmap addbmp = getImage(photo, tofile,uri);
            adapter.list.add(0, addbmp);
            tempFiles.add(0, tofile);
            photo = null;
            adapter.notifyDataSetChanged();

        }catch (Exception e){}
    }


    private Bitmap getImage(File photo,File to,Uri uri) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        if(uri==null)
            bitmap=BitmapFactory.decodeFile(photo.getPath(), newOpts);
        else{
            try {
                bitmap= BitmapFactory.decodeFileDescriptor(mContext.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor(),

                        null, newOpts);
            }catch (Exception e){}
        }


        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 800f;
        float ww = 480f;

        int be = 1;
        be = (int) Math.min(w / ww, h / hh);
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;

        if(uri==null)
            bitmap = BitmapFactory.decodeFile(photo.getPath(), newOpts);
        else{
            try {
                bitmap= BitmapFactory.decodeFileDescriptor(mContext.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor(),

                        null, newOpts);
            }catch (Exception e){}
        }


        FileOutputStream fos = null;
        try {
            CommonUtils.log(photo.length()+"||"+to.length());
            fos = new FileOutputStream(to);
            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            CommonUtils.log(photo.length()+"||"+to.length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    private File newImgFile() throws IOException {
        String timestamp = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss").format(new Date());
        return File.createTempFile(timestamp, ".png", Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }


}
