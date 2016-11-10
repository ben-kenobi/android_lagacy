package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.swsk.rgyxtqapp.R;

/**
 * Created by tom on 2015/10/27.
 */
public class GridItem extends RelativeLayout implements Checkable {

    private Context mContext;
    private boolean mChecked;
    private ImageView mSecletImageView = null;

    public GridItem(Context context) {
        this(context, null, 0);
    }

    public GridItem(Context context,AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GridItem(Context context,AttributeSet attrs,int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.main_gridview_item, this);
        mSecletImageView=(ImageView) findViewById(R.id.select);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mSecletImageView.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
