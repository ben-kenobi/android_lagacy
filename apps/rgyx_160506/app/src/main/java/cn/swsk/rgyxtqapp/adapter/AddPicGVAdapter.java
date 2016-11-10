package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;

/**
 * Created by Administrator on 2016/3/6.
 */
public class AddPicGVAdapter extends BaseAdapter {
    public List<Bitmap> list=new ArrayList<>();
    private Context con;
    public AddPicGVAdapter(Context con){
        this.con=con;
        Bitmap addImage = BitmapFactory.decodeResource(con.getResources(), R.mipmap.add_image);
        list.add(addImage);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(con).inflate(R.layout.add_picture_gridview_item2,null,false);

        }
        ImageView iv=(ImageView)convertView.findViewById(R.id.imageView1);
        iv.setImageBitmap(list.get(position));
        return convertView;
    }
}
