package com.icanit.app_v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class OuterListView extends ListView {
	private boolean requestDisallowInterceptTouchEvent=false;
	private int previousMotionEventAction;
	public OuterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OuterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public OuterListView(Context context) {
		super(context);
	}
	
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		 switch (ev.getAction()&MotionEvent.ACTION_MASK) {
         case MotionEvent.ACTION_DOWN:
             Log.i("touchEvent","onInterceptTouchEvent +_down "+requestDisallowInterceptTouchEvent+"   @outerListView");
             break;
         case MotionEvent.ACTION_MOVE:
             Log.i("touchEvent","onInterceptTouchEvent +_move "+requestDisallowInterceptTouchEvent+"  @outerListView");
             break;
         case MotionEvent.ACTION_UP:
             Log.i("touchEvent","onInterceptTouchEvent +_up    @outerListView");
             break;
         case MotionEvent.ACTION_CANCEL:
             Log.i("touchEvent","onInterceptTouchEvent +_cancel    @outerListView");
             break;
//         case MotionEvent.ACTION_HOVER_ENTER:
//             Log.d("touchEvent","onInterceptTouchEvent hover enter    @outerListView");
//             break;
//         case MotionEvent.ACTION_HOVER_EXIT:
//             Log.d("touchEvent","onInterceptTouchEvent hover exit    @outerListView");
//             break;
//         case MotionEvent.ACTION_HOVER_MOVE:
//             Log.d("touchEvent","onInterceptTouchEvent hover move    @outerListView");
//             break;
         case MotionEvent.ACTION_MASK:
             Log.i("touchEvent","onInterceptTouchEvent +_mask    @outerListView");
             break;
         default:
             break;
     }
		 if(!requestDisallowInterceptTouchEvent){
			 boolean b = super.onInterceptTouchEvent(ev);
		 }
		 int motionEventAction=ev.getAction()&MotionEvent.ACTION_MASK;
		if(motionEventAction==MotionEvent.ACTION_DOWN||motionEventAction==MotionEvent.ACTION_UP||previousMotionEventAction==MotionEvent.ACTION_DOWN) {
			previousMotionEventAction=motionEventAction;
			return false;
		}
		return !getRequestDisallowInterceptTouchEvent();
		
	}
	private boolean getRequestDisallowInterceptTouchEvent(){
		boolean b = requestDisallowInterceptTouchEvent;
		requestDisallowInterceptTouchEvent=false;
		return b;
	}
	public void setRequestDisallowInterceptTouchEvent(
			boolean requestDisallowInterceptTouchEvent) {
		this.requestDisallowInterceptTouchEvent = requestDisallowInterceptTouchEvent;
	}
	public boolean onTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//            Log.i("touchEvent","onTouchEvent _down  @outerListView");
//            break;
//        case MotionEvent.ACTION_MOVE:
//            Log.i("touchEvent","onTouchEvent _move   @outerListView");
//            break;
//        case MotionEvent.ACTION_UP:
//            Log.i("touchEvent","onTouchEvent _up    @outerListView");
//            break;
//        case MotionEvent.ACTION_CANCEL:
//            Log.i("touchEvent","onTouchEvent _cancel    @outerListView");
//            break;
////        case MotionEvent.ACTION_HOVER_ENTER:
////            Log.d("touchEvent","onInterceptTouchEvent hover enter    @outerListView");
////            break;
////        case MotionEvent.ACTION_HOVER_EXIT:
////            Log.d("touchEvent","onInterceptTouchEvent hover exit    @outerListView");
////            break;
////        case MotionEvent.ACTION_HOVER_MOVE:
////            Log.d("touchEvent","onInterceptTouchEvent hover move    @outerListView");
////            break;
//        case MotionEvent.ACTION_MASK:
//            Log.i("touchEvent","onTouchEvent _mask    @outerListView");
//            break;
//        default:
//            break;
//    }
		return super.onTouchEvent(ev);
	}

}
