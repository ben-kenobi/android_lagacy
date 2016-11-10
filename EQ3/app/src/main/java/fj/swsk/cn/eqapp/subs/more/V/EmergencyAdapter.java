package fj.swsk.cn.eqapp.subs.more.V;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.subs.more.M.DocFile;
import fj.swsk.cn.eqapp.util.FileUtils;

/**
 * Created by apple on 16/7/21.
 */
public class EmergencyAdapter extends CommonAdapter<DocFile> {
    public View.OnClickListener mListener;

    public EmergencyAdapter(Context context, List<DocFile> datas) {
        super(context, datas,R.layout.item4lv_emergencylv);
    }



    @Override
    public void convert(ViewHolder holder, DocFile docFile) {
        holder.setText(R.id.title,docFile.name);
        holder.setText(R.id.size, FileUtils.formatFileSize(docFile.size));
        holder.setText(R.id.time, IConstants.TIMESDF.format(docFile.genTime));
        holder.setText(R.id.btn, TextUtils.isEmpty(docFile.path)?"下载":"查看");
        holder.setTag(R.id.btn, holder.getPosition());
        holder.setOnClickListener(R.id.btn,mListener);

    }
}
