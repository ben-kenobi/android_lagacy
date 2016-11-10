package cn.swsk.rgyxtqapp.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.utils.CommonUtils;

/**
 * Created by Administrator on 2016/3/11.
 */
public class SlideupCover extends FrameLayout {
    public ListView lv;
    public View bg;
    public SlideupCover(Activity context,int resid){
        this(context);
        this.setClickable(true);
        ViewGroup vg = (ViewGroup) context.findViewById(android.R.id.content);
        vg.addView(this, -1, -1);
        this.setBackgroundColor(0xaa000000);
        LayoutInflater.from(context).inflate(resid, this, true);
        bg=getChildAt(0);
        lv=(ListView)findViewById(R.id.lv);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            toggle(null,null);
        }
        return super.onTouchEvent(event);
    }

    public <T>void toggle(List<T> ary,BaseAdapter adpter){
        if (ary != null) {
            lv.setAdapter(adpter);
            CommonUtils.measure(bg);

            TranslateAnimation ta=new TranslateAnimation(0,0,bg.getMeasuredHeight(),0);
            ta.setInterpolator(new AccelerateDecelerateInterpolator());
            ta.setDuration(250);
            bg.startAnimation(ta);
            setVisibility(View.VISIBLE);
        }else{

            TranslateAnimation ta=new TranslateAnimation(0,0,0,bg.getMeasuredHeight());
            ta.setInterpolator(new AccelerateDecelerateInterpolator());
            ta.setFillBefore(true);
            ta.setDuration(250);
            bg.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }



    public SlideupCover(Context context) {
        this(context,null);
    }



    public SlideupCover(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideupCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
