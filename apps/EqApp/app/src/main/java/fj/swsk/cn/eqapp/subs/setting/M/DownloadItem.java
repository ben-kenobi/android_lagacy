package fj.swsk.cn.eqapp.subs.setting.M;

import android.view.View;

import java.io.Serializable;

import fj.swsk.cn.eqapp.util.CommonUtils;

public class DownloadItem implements Serializable {
    private String title;
    private long downloadId = -1;
    private long currentBytes = 0;
    private long totalBytes = -1;
    private int status = -1;
    private int progress = 0;
    private String layerName;
    private String fileName;
    private String url;
    private String filePath;
    //1 是layer 2是slice
    private int fileType;
    private String dataSize;
    private int position = -1;
    private View view;
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        double v1 = dataSize / 1024;
        if (v1 > 1024) {
            double v2 = v1 / 1024;
            if(v2 > 1024){
                double v3 = v2 / 1024;
                this.dataSize = CommonUtils.setScale2(v3) + "G";
            }else{
                this.dataSize = CommonUtils.setScale2(v2) + "M";
            }
        } else {
            this.dataSize = CommonUtils.setScale2(v1) + "K";
        }
    }
    public void setDataSize2(String dataSizeStr){
        dataSize = dataSizeStr;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void reset(){
        setStatus(-1);
        setDownloadId(-1);
        setProgress(0);
    }
}
