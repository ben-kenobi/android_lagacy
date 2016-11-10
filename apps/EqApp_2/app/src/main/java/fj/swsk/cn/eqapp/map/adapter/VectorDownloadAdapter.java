package fj.swsk.cn.eqapp.map.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.map.VectorInfo;

/**
 * Created by apple on 16/6/16.
 */
public class VectorDownloadAdapter extends BaseAdapter {

    public Context mContext;
    private List<VectorInfo> vectorData;

    public VectorDownloadAdapter(Context context, List<VectorInfo> vectorData) {
        this(context);
        setVecResInfo(vectorData);
    }

    public VectorDownloadAdapter(Context context) {
        mContext = context;
    }

    public void setVecResInfo(List<VectorInfo> vectorData) {
        this.vectorData = vectorData;
    }

    @Override
    public int getCount() {
        if (vectorData == null) return 0;
        return vectorData.size();
    }

    @Override
    public Object getItem(int position) {
        if (getCount() > 0) {
            return vectorData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        VectorInfo vectorInfo = (VectorInfo) getItem(position);
        if (vectorInfo == null) return null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.vector_item, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.name),
                    (TextView) convertView.findViewById(R.id.state),
                    (Button) convertView.findViewById(R.id.download_btn),
                    (Button) convertView.findViewById(R.id.delete_btn));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.update(vectorInfo);
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView state;
        Button download_btn;
        Button delete_btn;

        public ViewHolder(TextView name,
                          TextView state,
                          Button download_btn,
                          Button delete_btn) {
            this.name = name;
            this.state = state;
            this.download_btn = download_btn;
            this.delete_btn = delete_btn;
        }

        public void update(VectorInfo vectorInfo) {
            name.setText(vectorInfo.name);
            state.setText(vectorInfo.state);

            if (vectorInfo.state.equals(mContext.getResources().getString(
                    R.string.download_state1))) {
                download_btn.setText("更新");
                delete_btn.setVisibility(View.VISIBLE);
            } else {
                download_btn.setText("下载");
                delete_btn.setVisibility(View.GONE);
            }
            download_btn.setTag(vectorInfo.url);
            delete_btn.setTag(vectorInfo.filePath);
        }
    }
}
