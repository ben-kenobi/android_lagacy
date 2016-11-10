package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fj.swsk.cn.eqapp.R;


/**
 * Created by Administrator on 2016/3/6.
 */
public class PendingSubGVAdapter extends BaseMulSelAdapter implements AdapterView.OnItemClickListener {
    public List<File> list = new ArrayList<>();
    private int resId = R.layout.add_picture_gridview_item2;
    private HashSet<ViewHolder> vhs = new HashSet<>();
    public int flag = 0;

    public PendingSubGVAdapter(Context context, List<File> list) {
        super(context);
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resId, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File media = list.get(position);
        final String path = media.getAbsolutePath();
        final ViewHolder holder1 = holder;
        ImageUtil.asyncDecodeImageAndShow(holder1.iv, path, context, false);

        if (path.endsWith(".mp4")) {
            holder1.videoflag.setVisibility(View.VISIBLE);
        } else {
            holder1.videoflag.setVisibility(View.GONE);
        }


//        holder.chosen.setVisibility(selectedIds.contains(media._id)
//                ? View.VISIBLE : View.GONE);
        vhs.add(holder);
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

    public void onMultiModeItemClick(View view, int pos) {
//        long aid = list.get(pos)._id;
//        if (selectedIds.contains(aid)) {
//            selectedIds.remove(aid);
//        } else {
//            selectedIds.add(aid);
//        }
//        final ViewHolder holder=(ViewHolder)view.getTag();
//
//        holder.chosen.setVisibility(selectedIds.contains(aid)
//                ?View.VISIBLE:View.GONE);

    }

    public void onItemClick(View view, int pos) {
//        CommonUtils.context.curEdit=list.get(pos);
//        Intent intent = new Intent(context,EditMediaActivity.class);
//        intent.putExtra("flag",flag);
//        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isMultiChoiceMode)
            onMultiModeItemClick(view, position);
        else {
            onItemClick(view, position);
        }
    }

    @Override
    public void onModeChange() {
        if (!isMultiChoiceMode) {
            for (ViewHolder holder : vhs) {
                holder.chosen.setVisibility(View.GONE);
            }
        }
    }
}
