package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.utils.CommonUtils;

/**
 * Created by Administrator on 2016/3/29.
 */
public  class ItemStack extends LinearLayout{
    public String[] sary;
    public float textSize;
    public int textColor=0xff222222;
    public int lines = 1;
    public int dividerColor;
    public List<TextView> tvs=new ArrayList<>();
    public ItemStack(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        textSize=context.getResources().getDimension(R.dimen.tableitemtxsize);
        dividerColor=getContext().getResources().getColor(R.color.color_divider001);
    }

    public ItemStack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);
        textSize=context.getResources().getDimension(R.dimen.tableitemtxsize);
        dividerColor=getContext().getResources().getColor(R.color.color_divider001);
    }

    public ItemStack(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        textSize=context.getResources().getDimension(R.dimen.tableitemtxsize);
        dividerColor=getContext().getResources().getColor(R.color.color_divider001);
    }

    public void setSary(String[] sary){
        this.sary = sary;
        updateUI();
    }

    public void updateUI(){
        removeAllViews();
        tvs.clear();
        for(int i=0;i<sary.length;i++){
            if(i>0){
                View v = commonDivider();
                addView(v);
                v.getLayoutParams().height=-1;
                v.getLayoutParams().width=CommonUtils.getDensity(getContext())*1;
            }
            TextView tv = commonTV();
            tv.setText(sary[i]);
            addView(tv);
            tv.getLayoutParams().height=-1;
            tv.getLayoutParams().width=0;
            ((LayoutParams)tv.getLayoutParams()).weight=1;

        }
    }

    public View commonDivider(){
        View v= new View(this.getContext());
       v.setBackgroundColor(dividerColor);
        return v;
    }
    public TextView commonTV(){
        TextView tv= new MarqueeTextView(this.getContext());
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setMarqueeRepeatLimit(-1);
        tvs.add(tv);
        tv.setGravity(Gravity.CENTER);
        tv.setLines(lines);
        tv.setSingleLine(true);
        tv.setTextColor(textColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        return tv;
    }



}
