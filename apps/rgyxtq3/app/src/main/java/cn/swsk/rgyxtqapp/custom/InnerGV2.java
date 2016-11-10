package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ScrollView;

public class InnerGV2 extends GridView{
    public InnerGV2(Context context) {
        super(context);
    }

    public InnerGV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InnerGV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec =MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
