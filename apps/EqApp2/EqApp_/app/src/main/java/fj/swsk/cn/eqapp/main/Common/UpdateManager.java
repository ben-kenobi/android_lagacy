package fj.swsk.cn.eqapp.main.Common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DeviceInfoUtil;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.ResUtil;
import fj.swsk.cn.eqapp.util.SystemUtil;


/**
 * Created by apple on 16/2/26.
 */
public class UpdateManager implements Runnable{
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    HashMap<String, String> mHashMap;
    private File mFile;
    private int progress;
    private boolean cancelUpdate = false;
    private Context mContext;
    private ProgressBar mProgressBar;
    private Dialog mDialog;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLOAD:
                    mProgressBar.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    install();
                    break;
            }
        }
    };

    public UpdateManager(Context context) {
        mContext = context;
    }


    public void checkUpdate(boolean isShowToast) {
        if (isUpdate()) {
            showNoticeDialog();
        } else {
            if (isShowToast)
                CommonUtils.toast(ResUtil.getString(R.string.soft_update_no));
        }
    }

    private boolean isUpdate() {
        int versionCode = CommonUtils.versionCode();
        InputStream io = HttpUtils.get("version.xml");
        if (io != null) {
            mHashMap = ParseXmlService.parseXml(io);
            String version = mHashMap.get("version");
            return (version != null && Integer.valueOf(version) > versionCode);
        }
        return false;

    }

    private void showNoticeDialog() {
        DialogUtil.getDefault(mContext, ResUtil.getString(R.string.soft_update_title),
                ResUtil.getString(R.string.soft_update_info)).
                setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDLDialog();
                    }
                }).setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void showDLDialog(){
        mDialog=DialogUtil.getCustDialog(mContext,ResUtil.getString(R.string.soft_updating),
                R.layout.softupdate_progress).setNegativeButton(R.string.soft_update_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancelUpdate=true;
                    }
                }).create();
        mDialog.show();
        mProgressBar=(ProgressBar)mDialog.getWindow().findViewById(R.id.update_progress);
        dlApk();
    }
    private void dlApk(){
        IConstants.THREAD_POOL.submit(this);
    }

    @Override
    public void run() {
        InputStream is =null;
        FileOutputStream os=null;
        try {
            if (DeviceInfoUtil.isSDCardMounted()) {
                mFile= FileUtils.newFile(Environment.getExternalStorageDirectory() + "/download/",
                        mHashMap.get("name"));
                HttpURLConnection connection = HttpUtils.connect(mHashMap.get("url"));
                long len = connection.getContentLength();
                 is = connection.getInputStream();
                 os = new FileOutputStream(mFile);
                int count=0;
                long readed=0;
                byte buf[]=new byte[1024*8];
                while(!cancelUpdate&&(count = is.read(buf))!=-1){
                    os.write(buf,0,count);
                    readed+=count;
                    progress=(int)(((float)readed/len)*100);
                    mHandler.sendEmptyMessage(DOWNLOAD);
                }
                if(!cancelUpdate)
                    mHandler.sendEmptyMessage(DOWNLOAD_FINISH);


            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            }catch (Exception e){

            }
            mDialog.dismiss();
        }
    }

    private void install(){
        SystemUtil.openAsInstallation(mFile, mContext);
    }
}
