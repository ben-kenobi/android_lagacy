package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import cn.swsk.rgyxtqapp.R;
import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.WorkPlan;

/**
 * Created by tom on 2015/11/1.
 */
public class PlanDetailPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<WorkPlan> mDatas;

    public PlanDetailPopupWindow(Context context, List<WorkPlan> datas) {
        calcWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.activity_plan_detail, null);

        mDatas = datas;
        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews(context);
        initEvents();
    }


    private void initViews(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.lv_plan_detail);
        mListView.setAdapter(new CommonAdapter<WorkPlan>(context, mDatas, R.layout.activity_plan_detail_item) {
            @Override
            public void convert(ViewHolder holder, WorkPlan workPlan) {
                holder.setText(R.id.tv_famcV, workPlan.getFamc())
                        .setText(R.id.tv_fazzsjV, workPlan.getFazzsj())
                        .setText(R.id.tv_zjyjV, workPlan.getZjyj())
                        .setText(R.id.tv_zjfwjV, workPlan.getZjfwj())
                        .setText(R.id.tv_aqsjV, workPlan.getAqsj());
            }
        });
    }

    private void initEvents() {

    }

    /**
     * 计算popupWindow宽度和高度
     *
     * @param context
     */
    private void calcWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.5);
    }
}
