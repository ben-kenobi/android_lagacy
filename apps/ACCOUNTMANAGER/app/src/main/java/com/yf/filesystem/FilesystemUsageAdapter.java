package com.yf.filesystem;

import android.content.Context;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.util.DeviceInfoUtil;
import com.yf.accountmanager.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/2/17.
 */
public class FilesystemUsageAdapter  extends BaseAdapter{

    private Context context;

    private int resId = R.layout.item4lv_usage;

    private List<File> fileList;


    public FilesystemUsageAdapter(Context con){
        super();
        this.context = con;
        fileList = new ArrayList<File>();
        if (DeviceInfoUtil.isSDCardMounted()) {
            fileList.add(new File(File.separator + "sdcard" + File.separator));
        }
    }

    @Override
    public int getCount() {
        if (fileList == null)
            return 0;
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        if (fileList == null)
            return null;
        return fileList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder(convertView = LayoutInflater.from(context)
                    .inflate(resId, parent, false));
        } else
            holder = (ViewHolder) convertView.getTag();

        File file = fileList.get(position);
        FileUtils.setFileIconByType(file, holder.icon, holder.label1);

        getStorageUsage(file, holder);

        return convertView;
    }

    private  void getStorageUsage(File storage,ViewHolder holder){
        long avai=getAvailableSize(storage);
        long total=getTotalSize(storage);
        double percent = avai/(double)total;
        StringBuffer sb = new StringBuffer("“— π”√£∫");
        sb.append(FileUtils.formatFileSize(avai) + "  /  " +
                FileUtils.formatFileSize(total) + "       ");
        sb.append(String.format("%.2f", percent * 100) + "%");
        holder.label2.setText(sb.toString());
        holder.pb.setProgress((int) (percent * 100));
    }

    public static class ViewHolder {
        public ImageView icon;
        public TextView label1,label2;
        public ProgressBar pb;
        public ViewHolder(View v) {
            icon = (ImageView) v.findViewById(R.id.imageView1);
            label1 = (TextView) v.findViewById(R.id.textView1);
            label2 = (TextView) v.findViewById(R.id.textView2);
            pb =  (ProgressBar)v.findViewById(R.id.progressBar1);
            v.setTag(this);
        }
    }
    public static long  getAvailableSize(File file){
        StatFs fs=new StatFs(file.getPath());
        return fs.getAvailableBlocks() * fs.getBlockSize();
    }
    public static long getTotalSize(File file){
        StatFs fs = new StatFs(file.getPath());
        return fs.getBlockSizeLong()*fs.getBlockCountLong();
    }

}
