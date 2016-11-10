package fj.swsk.cn.eqapp.util;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.conf.MyApplication;


/**
 * Created by apple on 16/2/25.
 */
public class CommonUtils {
    public static MyApplication context;

    public static void log(String str){
        Log.e("itag", str);
    }
    public static void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void toastOnMainHandler(final String message){
        IConstants.MAIN_HANDLER.post(new Runnable() {
            public void run() {
                toast(message);
            }
        });
    }


    public static int str2id(String str,String type){
        return context.getResources().getIdentifier(str,type,context.getPackageName());
    }

    public static Uri id2Uri(int id){
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
    }
    public static Uri indexingUriFromPath(String path){
        StringBuffer sb = new StringBuffer("android-app://");
        sb.append(context.getPackageName());
        if(!path.startsWith("/"))
            sb.append("/");
        sb.append(path);
        return Uri.parse(sb.toString());
    }

    public static void toggleFocusStatus(ViewGroup container,View[] ableGroup,View[] visibleGroup,TextView[] ableGroup2){
        if(container.getDescendantFocusability()==ViewGroup.FOCUS_BLOCK_DESCENDANTS)
            requetFocus(container,ableGroup,ableGroup2);
        else
            blockFocus(container, ableGroup,visibleGroup,ableGroup2);

    }
    public static void blockFocus(ViewGroup container,View[] ableGroup,View[] visibleGroup,TextView[] ableGroup2){
        container.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        container.clearFocus();
        for(View v:ableGroup)
            v.setVisibility(View.GONE);
        for(View v:visibleGroup)
            v.setVisibility(View.GONE);
        for(TextView v:ableGroup2)
            v.setEnabled(false);
    }
    public static void requetFocus(ViewGroup container,View[] ableGroup,TextView[] ableGroup2){
        container.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        container.requestFocus();
        for(View v:ableGroup)
            v.setVisibility(View.VISIBLE);
        for(TextView v:ableGroup2)
            v.setEnabled(true);
    }
    public static ContentValues encapsulateCursorAsContentValues(Cursor cursor){
        ContentValues cv=new ContentValues();
        for(int i=0;i<cursor.getColumnCount();i++){
            String coulumnName=cursor.getColumnName(i);
            if(IConstants.ID.equalsIgnoreCase(coulumnName))
                cv.put(coulumnName, cursor.getInt(i));
            else
                cv.put(coulumnName, cursor.getString(i));
        }
        return cv;
    }

    public static List<ContentValues> encapsulateCursorAsContentValuesList(Cursor cursor){
        if(cursor==null||cursor.getCount()==0) return null;
        List<ContentValues> entries=new ArrayList<ContentValues>();
        if(cursor.moveToFirst()){
            do{
                entries.add(encapsulateCursorAsContentValues(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return entries;
    }


    public static JSONObject encapsulateCursorAsJSONObject(Cursor cursor) throws JSONException {
        JSONObject jo = new JSONObject();
        for(int i=0;i<cursor.getColumnCount();i++){
            String coulumnName=cursor.getColumnName(i);
            if(IConstants.ID.equalsIgnoreCase(coulumnName))
                jo.put(coulumnName, cursor.getInt(i));
            else
                jo.put(coulumnName, cursor.getString(i));
        }
        return jo;
    }

    public static JSONArray encapsulateCursorAsJSONArray(Cursor cursor) throws JSONException {
        if(cursor==null||cursor.getCount()==0) return null;
        JSONArray ja=new JSONArray();
        if(cursor.moveToFirst()){
            do{
                ja.put(encapsulateCursorAsJSONObject(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return ja;
    }

    public static JSONObject convertContentValuesToJSONObject(ContentValues cv) throws JSONException{
        JSONObject jo = new JSONObject();
        for(Iterator<String> it=cv.keySet().iterator();it.hasNext();){
            String key=it.next();
            jo.put(key, cv.get(key));
        }
        return jo;
    }


    public static ContentValues convertJSONObjectToContentValues(JSONObject jo) throws JSONException{
        ContentValues cv=new ContentValues();
        for(Iterator<String> it=jo.keys();it.hasNext();){
            String key=it.next();
            if(!IConstants.ID.equalsIgnoreCase(key))
                cv.put(key, jo.getString(key));
        }
        return cv;
    }

    public static List<ContentValues> convertJSONArrayToContentValuesList(JSONArray ja) throws JSONException{
        if(ja==null||ja.length()==0) return null;
        List<ContentValues> list=new ArrayList<ContentValues>();
        for(int i=0;i<ja.length();i++){
            JSONObject jo=ja.getJSONObject(i);
            list.add(convertJSONObjectToContentValues(jo));
        }
        return list;
    }

    public static void setOnClickListenerForCheckedTextView(
            final CheckedTextView ctv) {
        ctv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (ctv.isChecked())
                    ctv.setChecked(false);
                else
                    ctv.setChecked(true);
            }
        });
    }




    public static void dismissDialogs(Dialog...dialogs){
        if(dialogs!=null){
            for(int i=0;i<dialogs.length;i++){
                Dialog dialog=dialogs[i];
                if(dialog!=null)
                    dialog.dismiss();
            }
        }
    }

    public static int versionCode(){
        try {
            return pkgInfo().versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static PackageInfo pkgInfo()throws Exception{
        return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName,0);
    }
    // 获取MetaValue
    public static String getMetaValue( String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static String UrlEnc(String str){
        try{
            return URLEncoder.encode(str, "UTF-8");
        }catch (Exception e){}
        return "";
    }

    /**
     * 获取根目录路径
     * @return
     */
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }



    public static Bitmap getImage(String photopath) {
        File photo = new File(photopath);
        if(!photo.exists()) return null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        bitmap=BitmapFactory.decodeFile(photo.getPath(), newOpts);


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


        bitmap = BitmapFactory.decodeFile(photo.getPath(), newOpts);


        return bitmap;
    }

    public static Bitmap getImage(String photopath,float ww,float hh) {
        File photo = new File(photopath);
        if(!photo.exists()) return null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        bitmap=BitmapFactory.decodeFile(photo.getPath(), newOpts);


        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        int be = 1;
        be = (int) Math.min(w / ww, h / hh);
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;


        bitmap = BitmapFactory.decodeFile(photo.getPath(), newOpts);

        return bitmap;
    }


    public static Bitmap getImage(File photo,File to,Uri uri) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        if(uri==null)
            bitmap=BitmapFactory.decodeFile(photo.getPath(), newOpts);
        else{
            try {
                bitmap= BitmapFactory.decodeFileDescriptor(context.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor(),

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
                bitmap= BitmapFactory.decodeFileDescriptor(context.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor(),

                        null, newOpts);
            }catch (Exception e){}
        }


        FileOutputStream fos = null;
        try {
            CommonUtils.log(photo.length()+"||"+to.length());
            fos = new FileOutputStream(to);
            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            CommonUtils.log(photo.length() + "||" + to.length());
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



    public static Bitmap resizeBitmap(Bitmap bitmap,int wid) {
        if(bitmap.getWidth()<=wid)
            return bitmap;
        Matrix matrix = new Matrix();
        float fx = wid/ (float) bitmap.getWidth();
        matrix.setScale(fx, fx);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        if(bitmap!=bm)
            bitmap.recycle();
        return bm;
    }

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static String setScale2(double d) {
        DecimalFormat df = new java.text.DecimalFormat("#.00");
        return df.format(d);
    }
}
