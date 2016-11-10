package fj.swsk.cn.eqapp.subs.setting.C;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.Downloads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.subs.setting.M.DownloadItem;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.SystemUtil;
import fj.swsk.cn.eqapp.util.ZipUtil;

public class autoOfflineMapDownload {
    /**
     * 下载器
     */
    private DownloadManager mDownloadManager;
    /**
     * 下载中队列
     */
    Queue<DownloadItem> taskQueue;

    Context mContext;
    MyContentObserver mContentObserver = new MyContentObserver();
    DownloadItem curDownloadItem;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startNextDownload();
        }
    };

    public autoOfflineMapDownload(Context mContext) {
        this.mContext = mContext;
        mDownloadManager = new DownloadManager(mContext.getContentResolver(),
                mContext.getPackageName());
        mDownloadManager.setAccessAllDownloads(true);
    }

    private void initView() {
        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                //获得下载历史信息
                List<String> titleListList = getHistoryDownloadsInfo();

                boolean isNetworkAvailable = HttpUtils.isNetworkAvailable(mContext);
                if (isNetworkAvailable) {
                    initNetDownloadList(titleListList);
                }
                Message msg = new Message();
                myHandler.sendMessage(msg);
            }
        });
    }

    private ArrayList<LinkedHashMap> getLayerJson(String url) {
        Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(url));
        if (map != null) {
            int status = (int) map.get("status");
            if (status == 200) {
                ArrayList<LinkedHashMap> result = (ArrayList<LinkedHashMap>) map.get("data");
                return result;
            }
        }
        return null;
    }

    /**
     * 从网络上获取下载列表
     *
     * @return
     */
    public void initNetDownloadList(List<String> titleList) {
        ArrayList<LinkedHashMap> VectorResult = getLayerJson(IConstants.lifelineLayerInfoUrl);
        if (VectorResult == null) {
            return;
        }
        ArrayList<LinkedHashMap> mapSliceResult = getLayerJson(IConstants.map_slice_url);
        if (mapSliceResult == null) {
            return;
        }
        int position = 3;
        //生命线
        for (int i = 0; i < VectorResult.size(); i++) {
            LinkedHashMap hashMap = VectorResult.get(i);
            if (hashMap != null) {
                DownloadItem item = getDownloadItem(hashMap, 1);
                if (titleList != null && !titleList.contains(item.getTitle())) {
                    item.setPosition(position++);
                    taskQueue.add(item);
                }
            }
        }
        //地图切片
        LinkedHashMap mapJson = mapSliceResult.get(0);
        if (mapJson != null) {
            DownloadItem item = getDownloadItem(mapJson, 2);
            if (titleList != null && !titleList.contains(item.getLayerName())) {
                item.setPosition(position++);
                taskQueue.add(item);
            }
        }
    }

    private DownloadItem getDownloadItem(LinkedHashMap hashMap, int fileType) {
        DownloadItem item = new DownloadItem();
        String title = hashMap.get("name").toString();
        String layerName = hashMap.get("layerName").toString();
        String url = hashMap.get("url").toString();
        item.setTitle(title);
        File file = new File(url);
        item.setFileName(file.getName());
        item.setLayerName(layerName);
        item.setUrl(url);
        item.setFileType(fileType);
        item.setStatus(DownloadManager.STATUS_PENDING);
        return item;
    }

    /**
     * 获取历史下载列表
     *
     * @return
     */
    public List<String> getHistoryDownloadsInfo() {
        DownloadManager.Query baseQuery = new DownloadManager.Query().setOnlyIncludeVisibleInDownloadsUi(true);
        if (baseQuery == null) return null;
        List<String> titleListList = new ArrayList<>();
        try {
            Cursor cursor = mDownloadManager.query(baseQuery);
            //下载项唯一ID
            int mDownloadIdColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
            //下载状态
            int mStatusColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            //文件名不带后缀
            int mTitleColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
            //文件名带后缀
            int mDescColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_DESCRIPTION);
            int mUri = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI);
            while (cursor.moveToNext()) {
                long downloadId = cursor.getLong(mDownloadIdColumnId);
                int status = cursor.getInt(mStatusColumnId);
                String url = cursor.getString(mUri);
                String desc = cursor.getString(mDescColumnId);
                String title = cursor.getString(mTitleColumnId);
                DownloadItem di = new DownloadItem();

                di.setDownloadId(downloadId);
                di.setStatus(status);
                di.setTitle(title);
                di.setUrl(url);
                String[] arr = desc.split(",");
                di.setFileName(arr[1]);
                di.setLayerName(arr[0]);
                if (status != DownloadManager.STATUS_SUCCESSFUL) {
                    taskQueue.add(di);
                }
                titleListList.add(title);
            }
            cursor.close();
            return titleListList;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 开始下载
     *
     * @param di
     * @return
     */
    private long startDownload(DownloadItem di) {
        if (!SystemUtil.checkSDCardIsAvailable()) {
            Toast.makeText(mContext, "外部存储空间不可用，无法下载", Toast.LENGTH_SHORT).show();
            return -1;
        }
        try {
            String url = di.getUrl();
            Uri srcUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(srcUri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/")
                    .setShowRunningNotification(false)
                    .setTitle(di.getTitle())
                    .setDescription(di.getLayerName() + "," + di.getFileName() + "," + di.getDataSize())
                    .setAllowedOverRoaming(true);
            long id = mDownloadManager.enqueue(request);
            di.setDownloadId(id);
            di.setStatus(DownloadManager.STATUS_PENDING);
            return id;

        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 从下载队列取出下一个任务下载
     */
    private void startNextDownload() {
        curDownloadItem = taskQueue.peek();
        if (curDownloadItem != null) {
            if (curDownloadItem.getStatus() == DownloadManager.STATUS_PENDING) {
                if (curDownloadItem.getDownloadId() == -1) {
                    curDownloadItem.setDownloadId(startDownload(curDownloadItem));
                } else {
                    mDownloadManager.resumeDownload(curDownloadItem.getDownloadId());
                }
            } else if (curDownloadItem.getStatus() == DownloadManager.STATUS_FAILED) {
                if (curDownloadItem.getDownloadId() == -1) {
                    startDownload(curDownloadItem);
                } else {
                    mDownloadManager.restartDownload(curDownloadItem.getDownloadId());
                }

            } else if (curDownloadItem.getStatus() == DownloadManager.STATUS_PAUSED) {
                mDownloadManager.resumeDownload(curDownloadItem.getDownloadId());
            }
            curDownloadItem.setStatus(DownloadManager.STATUS_PENDING);
        }
    }

    private int getCurrentDownloadStatus(long downloadId) {
        DownloadManager.Query baseQuery = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(baseQuery);
        if (cursor.moveToFirst()) {
            //下载状态
            int mStatusColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            curDownloadItem.setStatus(cursor.getInt(mStatusColumnId));
            return curDownloadItem.getStatus();
        }
        cursor.close();
        return -1;
    }

    private class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            switch (getCurrentDownloadStatus(curDownloadItem.getDownloadId())) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    IConstants.THREAD_POOL.submit(new Runnable() {
                        public void run() {
                            boolean isUncompressSucc = false;
                            String outDir;
                            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .getAbsolutePath() + curDownloadItem.getFileName();

                            try {
                                if (curDownloadItem.getFileType() == 1) {
                                    outDir = FileUtils.getSDPath() + "/eqAppData/" + curDownloadItem.getLayerName();
                                } else {
                                    outDir = FileUtils.getSDPath() + "/eqAppData/mapLayer/";
                                }
                                ZipUtil.unLayerZip(filePath, outDir);

                                isUncompressSucc = true;
                            } catch (IOException e) {

                            } finally {
                                if (isUncompressSucc) {
                                    File file = new File(filePath);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                }
                            }
                            taskQueue.poll();
                            startNextDownload();
                        }
                    });

                    break;
                case DownloadManager.STATUS_FAILED:
                    startNextDownload();
                    break;
            }
        }
    }

    public void startDownloading() {
        mContext.getContentResolver().registerContentObserver(Downloads.CONTENT_URI,
                true, mContentObserver);
        initView();

    }

    public void stopDownloading() {
        mContext.getContentResolver().unregisterContentObserver(mContentObserver);
    }
}
