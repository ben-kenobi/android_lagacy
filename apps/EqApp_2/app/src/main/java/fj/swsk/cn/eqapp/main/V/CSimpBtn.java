package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/6/13.
 */
public class CSimpBtn extends Button{

    public CSimpBtn(Context context) {
        super(context);
    }

    public CSimpBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CSimpBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }
    private void initUI(){
        setTypeface(ResUtil.CSIMP);

    }

}
