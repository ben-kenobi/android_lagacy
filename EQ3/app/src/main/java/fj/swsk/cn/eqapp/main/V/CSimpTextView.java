package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/6/13.
 */
public class CSimpTextView extends TextView {
    public CSimpTextView(Context context) {
        super(context);
    }

    public CSimpTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }


    public CSimpTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }
    private void initUI(){
        setTypeface(ResUtil.CSIMP);
    }
}
