package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/3/10.
 */
public class InnerLV2 extends ListView {
    public InnerLV2(Context context) {
        super(context);
    }

    public InnerLV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerLV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec =MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
