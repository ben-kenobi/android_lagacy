package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.CommonUtils;


/**
 * Created by Administrator on 2016/3/6.
 */
public class AddPicGVAdapter extends BaseAdapter {
    private int resId = R.layout.add_picture_gridview_item2;
    private Context con;

    public AddPicGVAdapter(Context con) {
        this.con = con;
    }

    @Override
    public int getCount() {
        return CommonUtils.context.curscene.list.size();
    }

    @Override
    public Object getItem(int position) {
        return CommonUtils.context.curscene.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(con).inflate(resId, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File media = CommonUtils.context.curscene.list.get(position);
        final String path = media.getAbsolutePath();
        final ViewHolder holder1 = holder;
        ImageUtil.asyncDecodeImageAndShow(holder1.iv, path, con, false);
        if (path.endsWith(".mp4")) {
            holder1.videoflag.setVisibility(View.VISIBLE);
        } else {
            holder1.videoflag.setVisibility(View.GONE);
        }

        return convertView;
    }


    class ViewHolder {
        public ViewHolder(View v) {
            iv = (ImageView) v.findViewById(R.id.imageView1);
            videoflag = (ImageView) v.findViewById(R.id.videoflag);
            chosen = (ImageView) v.findViewById(R.id.chosen);
            v.setTag(this);
        }

        public ImageView iv, videoflag, chosen;
    }

}
