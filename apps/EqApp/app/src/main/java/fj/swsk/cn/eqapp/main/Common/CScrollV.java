package fj.swsk.cn.eqapp.main.Common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/3/4.
 */
public class CScrollV extends ScrollView {
    public CScrollV(Context context) {
        super(context);
    }

    public CScrollV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CScrollV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);

    }
}
