package fj.swsk.cn.eqapp.subs.setting.C;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.Downloads;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.conf.MyApplication;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.subs.setting.M.DownloadItem;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.SystemUtil;
import fj.swsk.cn.eqapp.util.ZipUtil;

public class OfflineMapDownload extends BaseTopbarActivity implements CompoundButton.OnCheckedChangeListener {
    /**
     * 下载器
     */
    private DownloadManager mDownloadManager;
    /**
     * 下载状态监听
     */
    private MyContentObserver mContentObserver = new MyContentObserver();
    /**
     * 下载存放路径
     */
    private String DOWNLOAD_DIR_NAME;
    /**
     * 未下载列表
     */
    List<DownloadItem> notDownloadList;
    /**
     * 未下载容器
     */
    LinearLayout notDownloadLayout;
    /**
     * 已下载列表
     */
    List<DownloadItem> downloadedList;
    /**
     * 已下载容器
     */
    LinearLayout downloadedLayout;
    /**
     * 下载中队列
     */
    Queue<DownloadItem> taskQueue;
    /**
     * 下载中容器
     */
    LinearLayout downloadingLayout;
    /**
     * 下载中列表
     */
    List<DownloadItem> downloadingList;
    /**
     * 当前下载项
     */
    DownloadItem currentDownloadItem = null;

    CheckBox checkbox;
    LayoutInflater inflate;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //获取历史下载列表
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                //下载中
                if (downloadingList.size() > 0) {
                    downloadingLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < downloadingList.size(); i++) {
                        DownloadItem item = downloadingList.get(i);
                        if (item.getStatus() == DownloadManager.STATUS_RUNNING) {
                            taskQueue.offer(item);
                        }
                        downloadingLayout.addView(getDownloadingItem(item));
                    }
                }
                //未下载
                if (notDownloadList.size() > 0) {
                    notDownloadLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < notDownloadList.size(); i++) {
                        notDownloadLayout.addView(getNotDownloadItem(notDownloadList.get(i)));
                    }
                }
                //下载完成
                if (downloadedList.size() > 0) {
                    downloadedLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < downloadedList.size(); i++) {
                        downloadedLayout.addView(getDownloadedItem(downloadedList.get(i)));
                    }
                }
            }
            //启动下载
            else if (msg.what == 0) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                downloadingLayout.setVisibility(View.VISIBLE);
                notDownloadLayout.removeView(item.getView());
                if (notDownloadLayout.getChildCount() == 3) {
                    notDownloadLayout.setVisibility(View.GONE);
                }
                downloadingLayout.addView(getDownloadingItem(item));
            }
            //删除下载
            else if (msg.what == 2) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                downloadingLayout.removeView(item.getView());
                if (downloadingLayout.getChildCount() == 3) {
                    downloadingLayout.setVisibility(View.GONE);
                }
                item.reset();
                /*int index = item.getPosition();
                if (item.getPosition() > notDownloadLayout.getChildCount() - 1) {
                    index = notDownloadLayout.getChildCount();
                }*/
                notDownloadLayout.addView(getNotDownloadItem(item));
            } else if (msg.what == 4 || msg.what == 5 || msg.what == 9 || msg.what == 11) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                updateDownloadingProgress(item.getView(), item);
            } else if (msg.what == 6) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                updateUnzipView(item);
            } else if (msg.what == 7) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                downloadingLayout.removeView(item.getView());
                if (downloadingLayout.getChildCount() == 3) {
                    downloadingLayout.setVisibility(View.GONE);
                }
                downloadedLayout.setVisibility(View.VISIBLE);
                downloadedLayout.addView(getDownloadedItem(item));
            } else if (msg.what == 8) {
                Toast.makeText(OfflineMapDownload.this, "解压缩失败！", Toast.LENGTH_SHORT).show();
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                updateDownloadingProgress(item.getView(), item);
            } else if (msg.what == 10) {
                DownloadItem item = (DownloadItem) msg.getData().getSerializable("data");
                Toast.makeText(OfflineMapDownload.this, "下载失败", Toast.LENGTH_SHORT).show();
                updateDownloadingProgress(item.getView(), item);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_offline_map_download;

        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);

        buildComponents();

        initView();

        if (!MyApplication.isStartDownloadService) {
            CommonUtils.context.startDownloadService();
        }
    }

    private void initView() {
        SharedPreferences mySharedPreferences = getSharedPreferences("eqApp",
                Activity.MODE_PRIVATE);
        boolean checked = mySharedPreferences.getBoolean("isWifiDownload", false);
        checkbox.setChecked(checked);

        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                //获得下载历史信息
                List<String> titleListList = getHistoryDownloadsInfo();

                boolean isNetworkAvailable = HttpUtils.isNetworkAvailable(OfflineMapDownload.this);
                if (isNetworkAvailable) {
                    initNetDownloadList(titleListList);
                    myHandler.sendMessage(getMessage(1, null));
                }
            }
        });
    }

    private void buildComponents() {
        notDownloadLayout = (LinearLayout) findViewById(R.id.notDownloadLayout);
        downloadedLayout = (LinearLayout) findViewById(R.id.downloadedLayout);
        downloadingLayout = (LinearLayout) findViewById(R.id.downloadingLayout);

        notDownloadList = new ArrayList<>();
        downloadedList = new ArrayList<>();
        downloadingList = new ArrayList<>();
        taskQueue = new LinkedList<>();
        checkbox = (CheckBox) findViewById(R.id.myswitch);
        checkbox.setOnCheckedChangeListener(this);

        DOWNLOAD_DIR_NAME = getResources().getString(R.string.temp_dir);

        //下载器初始化
        mDownloadManager = new DownloadManager(getContentResolver(),
                getPackageName());
        mDownloadManager.setAccessAllDownloads(true);

        inflate = getLayoutInflater();
    }

    /**
     * 增加一个下载中的项
     *
     * @param item
     */
    private View getDownloadingItem(final DownloadItem item) {
        View view = inflate.inflate(R.layout.vector_item_downloading, downloadedLayout, false);
        ((TextView) view.findViewById(R.id.itemName)).setText(item.getTitle());
        ImageButton download = (ImageButton) view.findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseOrResumeClick(item);
            }
        });
        updateDownloadingProgress(view, item);

        item.setView(view);
        return view;
    }

    /**
     * 更新下载项界面
     *
     * @param item
     */
    private void updateDownloadingProgress(View view, DownloadItem item) {
        TextView downloadProgressNum = (TextView) view.findViewById(R.id.downloadProgressNum);
        ImageButton download = (ImageButton) view.findViewById(R.id.download);
        switch (item.getStatus()) {
            case DownloadManager.STATUS_PENDING:
                downloadProgressNum.setText("等待下载," + "已下载 " + item.getProgress() + "%");
                download.setImageResource(R.mipmap.emotionstore_progresscancelbtn);
                break;
            case DownloadManager.STATUS_FAILED:
                downloadProgressNum.setText("下载出错," + "已下载 " + item.getProgress() + "%");
                download.setImageResource(R.mipmap.emotionstore_progresscancelbtn);
                break;
            case DownloadManager.STATUS_PAUSED:
                downloadProgressNum.setText("停止下载," + "已下载 " + item.getProgress() + "%");
                download.setImageResource(android.R.drawable.ic_media_play);
                break;
            case DownloadManager.STATUS_RUNNING:
                downloadProgressNum.setText("正在下载 " + item.getProgress() + "%");
                download.setImageResource(R.mipmap.emotionstore_progresscancelbtn);
                break;
        }
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(item.getProgress());
    }

    /**
     * 更新解压状态
     *
     * @param item
     */
    private void updateUnzipView(DownloadItem item) {
        View view = item.getView();
        TextView downloadProgressNum = (TextView) view.findViewById(R.id.downloadProgressNum);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        downloadProgressNum.setText("正在解压中");
    }

    /**
     * 增加一个未下载的项
     *
     * @param item
     * @return
     */
    private View getNotDownloadItem(final DownloadItem item) {
        View view = inflate.inflate(R.layout.vector_item_notdownload, notDownloadLayout, false);
        ((TextView) view.findViewById(R.id.itemName)).setText(item.getTitle());
        ((TextView) view.findViewById(R.id.dataSize)).setText(item.getDataSize());
        ImageButton download = (ImageButton) view.findViewById(R.id.download);
        item.setView(view);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownloadClick(item);
            }
        });
        return view;
    }

    /**
     * 增加一个已下载的项
     *
     * @param item
     * @return
     */
    private View getDownloadedItem(DownloadItem item) {
        View view = inflate.inflate(R.layout.vector_item_downloaded, downloadedLayout, false);
        ((TextView) view.findViewById(R.id.itemName)).setText(item.getTitle());
        item.setView(view);
        return view;
    }

    private ArrayList<LinkedHashMap> getLayerJson(String url) {
        /*String jsonString;
        if (url.equals(IConstants.lifelineLayerInfoUrl)) {
            jsonString = "{\"status\":200,\"msg\":\"\",\"serviceId\":\"\",\"data\":[{\"size\":34900000,\"name\":\"微信\",\"layerName\":\"weixin\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信2\",\"layerName\":\"weixin2\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信3\",\"layerName\":\"weixin3\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信4\",\"layerName\":\"weixin4\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信5\",\"layerName\":\"weixin5\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信6\",\"layerName\":\"weixin6\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"},{\"size\":34900000,\"name\":\"微信7\",\"layerName\":\"weixin7\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"}]}";
        } else {
            jsonString = "{\"status\":200,\"msg\":\"\",\"serviceId\":\"\",\"data\":[{\"size\":34900000,\"name\":\"矢量地图\",\"layerName\":\"vec\",\"url\":\"http://115.231.86.8:9780/W06/360_S_J_Z_S.apk\"}]}";
        }*/
        Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(url));//JsonTools.getMap(jsonString);
        if (map != null) {
            int status = (int) map.get("status");
            if (status == 400) {
                String msg = (String) map.get("msg");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            } else if (status == 200) {
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
                    notDownloadList.add(item);
                }
            }
        }
        //地图切片
        LinkedHashMap mapJson = mapSliceResult.get(0);
        if (mapJson != null) {
            DownloadItem item = getDownloadItem(mapJson, 2);
            if (titleList != null && !titleList.contains(item.getTitle())) {
                item.setPosition(position++);
                notDownloadList.add(item);
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
        String filePath = DOWNLOAD_DIR_NAME + "/" + item.getFileName();
        item.setTotalBytes(-1);
        item.setFilePath(filePath);
        item.setLayerName(layerName);
        item.setUrl(url);
        item.setFileType(fileType);
        item.setDataSize(Long.valueOf(hashMap.get("size") + ""));
        return item;
    }

    public void onDownloadClick(DownloadItem item) {
        if (taskQueue.size() == 0) {
            if (startDownload(item) == -1) {
                Toast.makeText(this, "启动下载任务失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (taskQueue.offer(item)) {
            item.setStatus(DownloadManager.STATUS_PENDING);
            myHandler.sendMessage(getMessage(0, item));
        }
    }

    private Message getMessage(int what, Serializable serial) {
        Message msg = new Message();
        msg.what = what;
        if (serial != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", serial);
            msg.setData(bundle);
        }
        return msg;
    }

    /**
     * 暂停或恢复下载
     *
     * @param item
     */
    public void onPauseOrResumeClick(final DownloadItem item) {
        if (item.getStatus() == DownloadManager.STATUS_PENDING) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("是否要移除该下载？").setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (taskQueue.remove(item)) {
                                mDownloadManager.remove(item.getDownloadId());
                                myHandler.sendMessage(getMessage(2, item));
                            } else {
                                Toast.makeText(OfflineMapDownload.this, "移除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).create().show();
        } else if (item.getStatus() == DownloadManager.STATUS_RUNNING) {
            if (taskQueue.remove(item)) {
                item.setStatus(DownloadManager.STATUS_PAUSED);
                mDownloadManager.pauseDownload(item.getDownloadId());
                myHandler.sendMessage(getMessage(4, item));
                startNextDownload();
            }
        } else if (item.getStatus() == DownloadManager.STATUS_PAUSED) {
            if (taskQueue.offer(item)) {
                if (taskQueue.size() == 1 || currentDownloadItem == null) {
                    mDownloadManager.resumeDownload(item.getDownloadId());
                    currentDownloadItem = item;
                }
                item.setStatus(DownloadManager.STATUS_PENDING);
                myHandler.sendMessage(getMessage(4, item));
            }
        } else if (item.getStatus() == DownloadManager.STATUS_FAILED) {
            if (taskQueue.offer(item)) {
                if (taskQueue.size() == 1 || currentDownloadItem == null) {
                    if (item.getDownloadId() != -1) {
                        mDownloadManager.remove(item.getDownloadId());
                    }
                    if (startDownload(item) == -1) {
                        Toast.makeText(this, "启动下载任务失败", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                item.setStatus(DownloadManager.STATUS_PENDING);
                myHandler.sendMessage(getMessage(4, item));
            }
        }
    }

    /*******************************************************************************/
    private class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(myHandler);
        }

        @Override
        public void onChange(boolean selfChange) {
            final DownloadItem item = getCurrentDownloadInfo(currentDownloadItem.getDownloadId());
            switch (item.getStatus()) {
                case DownloadManager.STATUS_PENDING:
                    myHandler.sendMessage(getMessage(4, item));
                    break;
                case DownloadManager.STATUS_RUNNING:
                    myHandler.sendMessage(getMessage(5, item));
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    IConstants.THREAD_POOL.submit(new Runnable() {
                        public void run() {
                            boolean isUncompressSucc = false;
                            String outDir;
                            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .getAbsolutePath() + "/" + item.getFileName();
                            item.setProgress(101);
                            myHandler.sendMessage(getMessage(6, item));
                            try {
                                if (item.getFileType() == 1) {
                                    outDir = FileUtils.getSDPath() + "/eqAppData/" + item.getLayerName();
                                } else {
                                    outDir = FileUtils.getSDPath() + "/eqAppData/mapLayer/";
                                }
                                ZipUtil.unLayerZip(filePath, outDir);
                                myHandler.sendMessage(getMessage(7, item));
                                isUncompressSucc = true;
                            } catch (IOException e) {
                                myHandler.sendMessage(getMessage(8, item));
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
                    if (taskQueue.remove(item)) {
                        startNextDownload();
                        myHandler.sendMessage(getMessage(10, item));
                    }
                    break;
                case DownloadManager.STATUS_PAUSED:
                    if (taskQueue.remove(item)) {
                        myHandler.sendMessage(getMessage(11, item));
                    }
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        getContentResolver().registerContentObserver(Downloads.CONTENT_URI,
                true, mContentObserver);
        super.onStart();
    }

    @Override
    protected void onStop() {
        getContentResolver().unregisterContentObserver(mContentObserver);
        super.onStop();
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
        int downloadingPos = 3;
        int downloadedPos = 3;
        try {
            Cursor cursor = mDownloadManager.query(baseQuery);
            //下载项唯一ID
            int mDownloadIdColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
            //下载状态
            int mStatusColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            //下载文件总字节
            int mTotalBytesColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            //已下载字节
            int mCurrentBytesColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            //文件名不带后缀
            int mTitleColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
            //文件名带后缀
            int mDescColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_DESCRIPTION);
            int mUri = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI);

            int localUri = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
            while (cursor.moveToNext()) {
                long downloadId = cursor.getLong(mDownloadIdColumnId);
                long totalBytes = cursor.getLong(mTotalBytesColumnId);
                long currentBytes = cursor.getLong(mCurrentBytesColumnId);
                int status = cursor.getInt(mStatusColumnId);
                String url = cursor.getString(mUri);
                String localPath = cursor.getString(localUri);
                String desc = cursor.getString(mDescColumnId);
                int progress = getProgressValue(totalBytes, currentBytes);
                String title = cursor.getString(mTitleColumnId);
                DownloadItem di = new DownloadItem();

                di.setDownloadId(downloadId);
                di.setTotalBytes(totalBytes);
                di.setCurrentBytes(currentBytes);
                di.setStatus(status);
                di.setProgress(progress);
                di.setTitle(title);
                di.setUrl(url);
                String[] arr = desc.split(",");
                di.setFileName(arr[1]);
                di.setLayerName(arr[0]);
                di.setDataSize2(arr[2]);
                di.setFilePath(localPath);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    di.setPosition(downloadedPos++);
                    downloadedList.add(di);
                } else {
                    di.setPosition(downloadingPos++);
                    downloadingList.add(di);
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
     * 获取当前正在下载的任务信息
     *
     * @param downloadId
     * @return
     */
    private DownloadItem getCurrentDownloadInfo(long downloadId) {
        DownloadManager.Query baseQuery = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(baseQuery);
        if (cursor.moveToFirst()) {
            //下载状态
            int mStatusColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            //下载文件总字节
            int mTotalBytesColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            //已下载字节
            int mCurrentBytesColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

            long totalBytes = cursor.getLong(mTotalBytesColumnId);
            long currentBytes = cursor.getLong(mCurrentBytesColumnId);
            int status = cursor.getInt(mStatusColumnId);
            int progress = getProgressValue(totalBytes, currentBytes);

            currentDownloadItem.setTotalBytes(totalBytes);
            currentDownloadItem.setCurrentBytes(currentBytes);
            currentDownloadItem.setStatus(status);
            currentDownloadItem.setProgress(progress);
            return currentDownloadItem;
        }
        cursor.close();
        return null;
    }

    /**
     * 计算下载%进度
     *
     * @param totalBytes
     * @param currentBytes
     * @return
     */
    public int getProgressValue(long totalBytes, long currentBytes) {
        if (totalBytes == -1) {
            return 0;
        }
        return (int) (currentBytes * 100 / totalBytes);
    }

    /**
     * 开始下载
     *
     * @param di
     * @return
     */
    private long startDownload(DownloadItem di) {
        if (!SystemUtil.checkSDCardIsAvailable()) {
            Toast.makeText(getApplicationContext(), "外部存储空间不可用，请检查后再试", Toast.LENGTH_SHORT).show();
            return -1;
        }
        try {
            String url = di.getUrl();
            Uri srcUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(srcUri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/")
                    .setShowRunningNotification(true)
                    .setTitle(di.getTitle())
                    .setDescription(di.getLayerName() + "," + di.getFileName() + "," + di.getDataSize())
                    .setAllowedOverRoaming(true);
            long id = mDownloadManager.enqueue(request);
            di.setDownloadId(id);
            di.setStatus(DownloadManager.STATUS_PENDING);
            currentDownloadItem = di;
            return id;

        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 从下载队列取出下一个任务下载
     */
    private void startNextDownload() {
        DownloadItem item = taskQueue.peek();
        if (item != null && item.getStatus() == DownloadManager.STATUS_PENDING) {
            if (item.getDownloadId() == -1) {
                item.setDownloadId(startDownload(item));
            } else {
                mDownloadManager.resumeDownload(item.getDownloadId());
                currentDownloadItem = item;
            }
            myHandler.sendMessage(getMessage(9, item));
        } else {
            currentDownloadItem = null;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences mySharedPreferences = getSharedPreferences("eqApp",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("isWifiDownload", isChecked);
        editor.commit();
    }
}
