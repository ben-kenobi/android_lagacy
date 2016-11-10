package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.swsk.rgyxtqapp.R;

/**
 * Created by tom on 2015/10/27.
 */
public class topbar extends RelativeLayout {

    public Button leftButton;
    private Button rightButton;
    private TextView titleTextView;

    private String leftButtonText;
    private String rightButtonText;
    private String titleTextViewText;

    private float leftButtonTextSize;
    private float rightButtonTextSize;
    private float titleTextViewTextSize;

    private Drawable leftButtonBackground;
    private Drawable rightButtonBackground;
    private int titleTextViewTextColor;

    private RelativeLayout.LayoutParams leftButtonLayoutParams;
    private RelativeLayout.LayoutParams rightButtonLayoutParams;
    private RelativeLayout.LayoutParams titleTextViewLayoutParams;

    private topbarClickListener listener;

    public topbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.topbar);

        leftButtonText = typedArray.getString(R.styleable.topbar_leftButtonText);
        rightButtonText = typedArray.getString(R.styleable.topbar_rightButtonText);
        titleTextViewText = typedArray.getString(R.styleable.topbar_titleText);

        titleTextViewTextColor = typedArray.getColor(R.styleable.topbar_titleColor, 0);
        leftButtonBackground = typedArray.getDrawable(R.styleable.topbar_leftButtonBackground);
        rightButtonBackground = typedArray.getDrawable(R.styleable.topbar_rightButtonBackground);

        titleTextViewTextSize = typedArray.getDimension(R.styleable.topbar_titleSize, 14);
        leftButtonTextSize = typedArray.getDimension(R.styleable.topbar_leftButtonSize, 14);
        rightButtonTextSize = typedArray.getDimension(R.styleable.topbar_rightButtonSize, 14);
        typedArray.recycle();

        //setBackgroundColor(0x80000000); //设置标题栏背景透明
        setBackgroundColor(Color.parseColor("#4C627D"));

        leftButton = new Button(context);
        rightButton = new Button(context);
        titleTextView = new TextView(context);

        leftButtonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(leftButton, leftButtonLayoutParams);

        rightButtonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(rightButton, rightButtonLayoutParams);

        titleTextViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        titleTextViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleTextView.setGravity(Gravity.CENTER);
        addView(titleTextView, titleTextViewLayoutParams);

        leftButton.setText(leftButtonText);
        leftButton.setBackgroundDrawable(leftButtonBackground);
        leftButton.setTextSize(leftButtonTextSize);
        leftButton.setTextColor(Color.WHITE);
        leftButton.setMinWidth(200);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.leftClick();
                }
            }
        });

        rightButton.setText(rightButtonText);
        rightButton.setBackgroundDrawable(rightButtonBackground);
        rightButton.setTextSize(rightButtonTextSize);
        rightButton.setTextColor(Color.WHITE);
        rightButton.setMinWidth(200);
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.rightClick();
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
    }

    public void setOnTopbarClickListener(topbarClickListener listener) {
        this.listener = listener;
    }

    public void setLeftButtonIsvisable(boolean flag) {
        if (leftButton == null) {
            return;
        }
        if (flag) {
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }

    public void setRightButtonIsvisable(boolean flag) {
        if (rightButton == null) {
            return;
        }
        if (flag) {
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }

    public void setTitleTextViewIsvisable(boolean flag) {
        if (titleTextView == null) {
            return;
        }
        if (flag) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String title) {
        if (titleTextView == null) {
            return;
        }
        titleTextView.setText(title);
    }

    public interface topbarClickListener {
        public void leftClick();
        public void rightClick();
    }
}
