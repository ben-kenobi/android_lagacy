package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.util.CommonUtils;


/**
 * Created by Administrator on 2016/3/6.
 */
public class AddPicGVAdapter2 extends BaseUnstableCursorAdapter {
    public List<File> list=new ArrayList<>();
    private int resId=R.layout.add_picture_gridview_item2;

    public AddPicGVAdapter2(Context context) {
        super(context);
    }



    @Override
    public void bindView(View convertView, Context context, Cursor c) {
        convertView.setActivated(selectedIds.contains(c.getInt(c.getColumnIndex(IConstants.ID))));
        final ViewHolder holder=(ViewHolder)convertView.getTag();
        final String path = c.getString(c.getColumnIndex(ISQLite.T_MEDIA.CONTENTPATH));
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                if(path.endsWith(".mp4")){
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    IConstants.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.iv.setImageBitmap(thumb);
                            holder.videoflag.setVisibility(View.VISIBLE);
                        }
                    });


                }else{
                    final Bitmap thumb = CommonUtils.getImage(path);
                    IConstants.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.iv.setImageBitmap(thumb);
                            holder.videoflag.setVisibility(View.GONE);
                        }
                    });


                }
            }
        });

        holder.chosen.setVisibility(selectedIds.contains(c.getInt(c.getColumnIndex(IConstants.ID)))
        ?View.VISIBLE:View.GONE);


    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {

        View v=LayoutInflater.from(context).inflate(resId,parent,false);
        new ViewHolder(v);
        return v;
    }
    class ViewHolder{
        public ViewHolder(View v){
            iv=(ImageView)v.findViewById(R.id.imageView1);
            videoflag=(ImageView)v.findViewById(R.id.videoflag);
            chosen=(ImageView)v.findViewById(R.id.chosen);
            v.setTag(this);
        }
        public ImageView iv,videoflag,chosen;
    }

    public void onMultiModeItemClick(View view){
        int aid = getCursor().getInt(
                getCursor().getColumnIndex(
                        IConstants.ID));
        if (selectedIds.contains(aid)) {
            selectedIds.remove(aid);
        } else {
            selectedIds.add(aid);
        }
        final ViewHolder holder=(ViewHolder)view.getTag();

        holder.chosen.setVisibility(selectedIds.contains(aid)
                ?View.VISIBLE:View.GONE);

    }


}
