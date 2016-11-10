package com.icanit.app_v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class InnerListView2 extends ListView{
	private int currentY;
	private OuterListView outerLv;
	public InnerListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public InnerListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public InnerListView2(Context context) {
		super(context);
	}
	
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	        switch (ev.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	            	 currentY = (int)ev.getY();
	            	 outerLv.setRequestDisallowInterceptTouchEvent(true);
	                Log.d("touchEvent","onInterceptTouchEvent +_down  @InnerListView2");
	                break;
	            case MotionEvent.ACTION_MOVE:
	                Log.d("touchEvent","onInterceptTouchEvent +_move   @InnerListView2");
	                break;
	            case MotionEvent.ACTION_UP:
	                Log.d("touchEvent","onInterceptTouchEvent +_up    @InnerListView2");
	                break;
	            case MotionEvent.ACTION_CANCEL:
	                Log.d("touchEvent","onInterceptTouchEvent +_cancel    @InnerListView2");
	                break;
//	            case MotionEvent.ACTION_HOVER_ENTER:
//	                Log.d("touchEvent","onInterceptTouchEvent hover enter    @InnerListView2");
//	                break;
//	            case MotionEvent.ACTION_HOVER_EXIT:
//	                Log.d("touchEvent","onInterceptTouchEvent hover exit    @InnerListView2");
//	                break;
//	            case MotionEvent.ACTION_HOVER_MOVE:
//	                Log.d("touchEvent","onInterceptTouchEvent hover move    @InnerListView2");
//	                break;
	            case MotionEvent.ACTION_MASK:
	                Log.d("touchEvent","onTouchEvent +_mask    @InnerListView2");
	                break;
	            default:
	                break;
	        }
//	        boolean b =super.onInterceptTouchEvent(ev);
	        return false;
	    }
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//            Log.d("touchEvent","onTouchEvent _down  @InnerListView2");
//            break;
//        case MotionEvent.ACTION_MOVE:
//            Log.d("touchEvent","onTouchEvent _move   @InnerListView2");
//            break;
//        case MotionEvent.ACTION_UP:
//            Log.d("touchEvent","onTouchEvent _up   @InnerListView2");
//            break;
//        case MotionEvent.ACTION_CANCEL:
//            Log.d("touchEvent","onTouchEvent _cancel  @InnerListView2");
//            break;
////        case MotionEvent.ACTION_HOVER_ENTER:
////            Log.d("touchEvent","onInterceptTouchEvent hover enter    @InnerListView2");
////            break;
////        case MotionEvent.ACTION_HOVER_EXIT:
////            Log.d("touchEvent","onInterceptTouchEvent hover exit    @InnerListView2");
////            break;
////        case MotionEvent.ACTION_HOVER_MOVE:
////            Log.d("touchEvent","onInterceptTouchEvent hover move    @InnerListView2");
////            break;
//        case MotionEvent.ACTION_MASK:
//            Log.d("touchEvent","onTouchEvent _mask   @InnerListView2");
//            break;
//        default:
//            break;
//    }
	        if (outerLv != null) {
	            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	                int y = (int)ev.getY();
	               
	                if(currentY==y){
	                	outerLv.setRequestDisallowInterceptTouchEvent(true);
	                	return true;
	                }
	                // scroll down
	                else if (currentY < y) {
	                    if (getFirstVisiblePosition()==0) {
	                        setSelection(0);
	                        return false;
	                    }else{
	                    	 outerLv.setRequestDisallowInterceptTouchEvent(true);
	                    }
	                }
	                //scroll up
	                else if (currentY > y) {
	                    if (getLastVisiblePosition()==getCount()-1) {
	                        setSelection(getCount()-1);
	                        return false;
	                    }else{
	                    	outerLv.setRequestDisallowInterceptTouchEvent(true);
	                    }
	                }
	                currentY = y;
	            }
	        }
	    return super.onTouchEvent(ev);
	}
	public OuterListView getOuterLv() {
		return outerLv;
	}
	public void setOuterLv(OuterListView outerLv) {
		this.outerLv = outerLv;
	}
	
	
	
}
