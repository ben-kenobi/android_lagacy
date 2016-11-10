package fj.swsk.cn.eqapp.main.Common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.ResUtil;


public class Topbar extends RelativeLayout {

    public Button leftButton;
    public Button rightButton;
    public TextView titleTextView;

    private String leftButtonText;
    private String rightButtonText;
    private String titleTextViewText;

    private float leftButtonTextSize;
    private float rightButtonTextSize;
    private float titleTextViewTextSize;

    private Drawable leftButtonBackground;
    private Drawable rightButtonBackground;
    private int titleTextViewTextColor;

    private LayoutParams leftButtonLayoutParams;
    private LayoutParams rightButtonLayoutParams;
    private LayoutParams titleTextViewLayoutParams;

    public OnClickListener letfListener,rightListener;


    public Topbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

        leftButtonText = typedArray.getString(R.styleable.Topbar_leftButtonText);
        rightButtonText = typedArray.getString(R.styleable.Topbar_rightButtonText);
        titleTextViewText = typedArray.getString(R.styleable.Topbar_titleText);

        titleTextViewTextColor = typedArray.getColor(R.styleable.Topbar_titleColor, 0);
        leftButtonBackground = typedArray.getDrawable(R.styleable.Topbar_leftButtonBackground);
        rightButtonBackground = typedArray.getDrawable(R.styleable.Topbar_rightButtonBackground);

        titleTextViewTextSize = typedArray.getDimension(R.styleable.Topbar_titleSize, 14);
        leftButtonTextSize = typedArray.getDimension(R.styleable.Topbar_leftButtonSize, 14);
        rightButtonTextSize = typedArray.getDimension(R.styleable.Topbar_rightButtonSize, 14);
        typedArray.recycle();

        //setBackgroundColor(0x80000000); //设置标题栏背景透明
//        setBackgroundColor(Color.parseColor("#4C627D"));

        leftButton = new Button(context);
        rightButton = new Button(context);
        titleTextView = new TextView(context);

        leftButtonLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(leftButton, leftButtonLayoutParams);

        rightButtonLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(rightButton, rightButtonLayoutParams);

        titleTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        titleTextViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleTextView.setGravity(Gravity.CENTER);
        addView(titleTextView, titleTextViewLayoutParams);

        leftButton.setText(leftButtonText);
        leftButton.setBackgroundDrawable(leftButtonBackground);
        leftButton.setTextSize(leftButtonTextSize);
        leftButton.setTextColor(getResources().getColorStateList(R.color.topbar_btn_state));
        leftButton.setMinWidth(180);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (letfListener != null) {
                    letfListener.onClick(v);
                }
            }
        });

        rightButton.setText(rightButtonText);
        rightButton.setBackgroundDrawable(rightButtonBackground);
        rightButton.setTextSize(rightButtonTextSize);
        rightButton.setTextColor(getResources().getColorStateList(R.color.topbar_btn_state));
        rightButton.setMinWidth(180);
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });

        /*RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)rightButton.getLayoutParams();
        layoutParams.setMargins(10,0,10,0);
        rightButton.setLayoutParams(layoutParams);*/

        titleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        titleTextView.setSingleLine(true);
        titleTextView.setText(titleTextViewText);
        titleTextView.setTextSize(titleTextViewTextSize);
        titleTextView.setTextColor(titleTextViewTextColor);




        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ResUtil.dp2Intp(1));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        View line = new View(getContext());
        line.setBackgroundColor(0xaaaaaaaa);
        addView(line,lp);

    }


    public void setLeftButtonIsvisable(boolean flag) {
        if (leftButton == null) {
            return;
        }
        leftButton.setVisibility(flag?View.VISIBLE:View.GONE);

    }

    public void setRightButtonIsvisable(boolean flag) {
        if (rightButton == null) {
            return;
        }
        rightButton.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    public void setTitleTextViewIsvisable(boolean flag) {
        if (titleTextView == null) {
            return;
        }
        titleTextView.setVisibility(flag?View.VISIBLE:View.GONE);

    }

    public void setTitleText(String title) {
        if (titleTextView == null) {
            return;
        }
        titleTextView.setText(title);
    }

}
