package swsk.cn.rgyxtq.subs.work.V;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.subs.work.Common.CommonAdapter;
import swsk.cn.rgyxtq.subs.work.Common.ViewHolder;
import swsk.cn.rgyxtq.subs.work.M.WorkPlan;
import swsk.cn.rgyxtq.util.DeviceInfoUtil;

/**
 * Created by apple on 16/3/1.
 */
public class PlanDetailPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<WorkPlan> mDatas;


    public PlanDetailPopupWindow(Context con,List<WorkPlan> datas){
        Point p= DeviceInfoUtil.getScreenSize2();
        mWidth=p.x;
        mHeight=p.y;

        mConvertView= LayoutInflater.from(con).inflate(R.layout.activity_plan_detail,null);
        mDatas=datas;
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
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initView(con);
        initEvents();

    }
    private void initView(Context con){
        mListView= (ListView)mConvertView.findViewById(R.id.lv_plan_detail);
        mListView.setAdapter(new CommonAdapter<WorkPlan>(con,mDatas,R.layout.activity_plan_detail_item) {
            @Override
            public void convert(ViewHolder holder, WorkPlan wp) {
                holder.setText(R.id.tv_famcV,wp.getFamc())
                        .setText(R.id.tv_fazzsjV,wp.getFazzsj())
                        .setText(R.id.tv_zjyjV,wp.getZjyj())
                        .setText(R.id.tv_zjfwjV,wp.getZjfwj())
                        .setText(R.id.tv_aqsjV,wp.getAqsj());
            }
        });
    }

    private  void initEvents(){

    }



}
