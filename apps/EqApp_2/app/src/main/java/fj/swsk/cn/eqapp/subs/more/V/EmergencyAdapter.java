package fj.swsk.cn.eqapp.subs.more.V;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.subs.more.M.DocFile;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.FileUtils;

/**
 * Created by apple on 16/7/21.
 */
public class EmergencyAdapter extends CommonAdapter<DocFile> implements View.OnClickListener {
    public View.OnClickListener mListener;

    public EmergencyAdapter(Context context, List<DocFile> datas) {
        super(context, datas,R.layout.item4lv_emergencylv);
    }



    @Override
    public void convert(ViewHolder holder, final DocFile docFile) {
        holder.setText(R.id.title,docFile.showname);
        holder.setText(R.id.size, FileUtils.formatFileSize(docFile.size));
        holder.setText(R.id.time, IConstants.TIMESDF.format(docFile.genTime));
        holder.setText(R.id.btn,!new File(docFile.path).exists()?"下载":"查看");
        holder.setVisible(R.id.btn2, new File(docFile.path).exists());
        holder.setTag(R.id.btn, holder.getPosition());
        holder.setTag(R.id.btn2, holder.getPosition());
        holder.setOnClickListener(R.id.btn,mListener);
        holder.setOnClickListener(R.id.btn2,this);
        if(docFile.size<=0){

            NetUtil.getFileLen(mContext, docFile.url, new NetUtil.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    long len = (Long)map.get("len");
                    docFile.size=len;
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onClick(final View v) {
        if(v.getId()==R.id.btn2){
            DialogUtil.getMessageDialogBuilder(mContext, "确认删除该文件？", "提示", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int pos = (int) v.getTag();
                    DocFile df = getItem(pos);
                    File f = new File(df.path);
                    if (f.exists())
                        f.delete();
                    notifyDataSetChanged();
                }
            }).show();


        }
    }
}
