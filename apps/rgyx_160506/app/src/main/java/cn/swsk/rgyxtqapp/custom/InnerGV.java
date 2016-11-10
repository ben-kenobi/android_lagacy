package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerGV extends GridView{
    private ScrollView parentScrollView;
    private int currentY;  int mTop = 10; private int lastScrollDelta = 0;
    public InnerGV(Context context) {
        super(context);
    }

    public InnerGV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InnerGV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parentScrollView.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                parentScrollView.requestDisallowInterceptTouchEvent(false);
                break;
//
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //    public void resume() {
//        overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
//        lastScrollDelta = 0;
//    }
//
//    /**
//     * 将targetView滚到最顶端
//     */
//    public void scrollTo(View targetView) {
//        int oldScrollY = getScrollY();
//        int top = targetView.getTop() - mTop;
//        int delatY = top - oldScrollY;
//        lastScrollDelta = delatY;
//        overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
//    }
//
//    private int getScrollRange() {
//        int scrollRange = 0;
//        if (getChildCount() > 0) {
//            View child = getChildAt(0);
//            scrollRange = Math.max(0, child.getHeight() - (getHeight()));
//        }
//        return scrollRange;
//    }
//
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d("touchEvent",ev.getAction()+"   @InnerListView");
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                parentScrollView.requestDisallowInterceptTouchEvent(true);
//                currentY = (int)ev.getY();
//                Log.d("touchEvent","onInterceptTouchEvent down  @InnerListView");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d("touchEvent","onInterceptTouchEvent move   @InnerListView");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d("touchEvent","onInterceptTouchEvent up    @InnerListView");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d("touchEvent","onInterceptTouchEvent cancel    @InnerListView");
//                break;
////	            case MotionEvent.ACTION_HOVER_ENTER:
////	                Log.d("touchEvent","onInterceptTouchEvent hover enter    @InnerListView");
////	                break;
////	            case MotionEvent.ACTION_HOVER_EXIT:
////	                Log.d("touchEvent","onInterceptTouchEvent hover exit    @InnerListView");
////	                break;
////	            case MotionEvent.ACTION_HOVER_MOVE:
////	                Log.d("touchEvent","onInterceptTouchEvent hover move    @InnerListView");
////	                break;
////	            case MotionEvent.ACTION_MASK:
////	                Log.d("touchEvent","onInterceptTouchEvent mask    @InnerListView");
////	                break;
//            default:
//                break;
//        }
//        boolean b =super.onInterceptTouchEvent(ev);
//        return true;
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        View child = getChildAt(0);
//        if (parentScrollView != null) {
//            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//                int height = child.getMeasuredHeight();
//                height = height*getCount() - getMeasuredHeight();
//                int scrollY = getScrollY();
//                int y = (int)ev.getY();
//                System.out.println("top="+getTop()+" @ InnerListView");
//                // scroll down
//                if (currentY < y) {
//                    if (getFirstVisiblePosition()==0) {
//                        setParentScrollAble(true);
////	                        overScrollBy(0, -80, 0, 0, 0, 100, 0, 0, true);
//                        setSelection(0);
//                        Log.d("touchEvent","upDead onTouchEvent  @InnerListView");
//                        return false;
//                    } else {
//                        setParentScrollAble(false);
//                    }
//                }
//                //scroll up
//                else if (currentY > y) {
//                    if (getLastVisiblePosition()==getCount()-1) {
//                        setParentScrollAble(true);
////	                        overScrollBy(0, 80, 0, 0, 0, 100, 0, 0, true);
//                        setSelection(getCount()-1);
//                        Log.d("touchEvent","downDead  onTouchEvent  @InnerListView");
//                        return false;
//                    } else {
//                        setParentScrollAble(false);
//                    }
//                }
//                currentY = y;
//            }
//        }
//        boolean b =super.onTouchEvent(ev);
//        return true;
//
//    }
    private void setParentScrollAble(boolean b) {
        parentScrollView.requestDisallowInterceptTouchEvent(!b);
    }

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }
}
