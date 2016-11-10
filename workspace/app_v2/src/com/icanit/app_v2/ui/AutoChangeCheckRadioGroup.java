package com.icanit.app_v2.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.icanit.app_v2.R;
import com.icanit.app_v2.util.DeviceInfoUtil;

public class AutoChangeCheckRadioGroup extends RadioGroup implements Runnable{
	private boolean stop=true,reset=true,started=false;
	public int delayMillis=5000;
	public int buttonResId=R.drawable.selector_radio_button;
	public AutoChangeCheckRadioGroup(Context context) {
		super(context);
	}
	@Override
	public void check(int id) { 
//		int oldId=getCheckedRadioButtonId();
//		System.out.println("newId="+id+",oldId="+oldId+"   @AutoChangeCheckRadioGroup check");
		if(id==-1) {
			super.check(id);
			return;
		}
		View view = findViewById(id);
		if(view!=null)
			((RadioButton)view).setChecked(true);
	}
	
	public  void initRadioGroup(int childCount,Context context){
		stopAd();
		clearCheck();
		removeAllViews();
		float f=DeviceInfoUtil.getScreenWidth()/320f;
		for(int i=0;i<childCount;i++){
			RadioButton rb=new RadioButton(context);
			rb.setWidth((int)(32*f));rb.setHeight((int)(32*f));
			rb.setButtonDrawable(buttonResId);
			rb.setId(i);
			addView(rb);
		}
	}
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		System.out.println("oldId="+oldId+",checkedId="+getCheckedRadioButtonId()+"  @AutoChangeCheckRadioGroup");
		reset();
//		System.out.println("checkId="+getCheckedRadioButtonId()+",childCount="+getChildCount()+",stop="+stop+
//				",reset="+reset+"started="+started+  "  @AutoChangeCheckRadioGroup  onInterceptTouchEvent");
		return super.onInterceptTouchEvent(ev);
	}
	public void run(){
//		System.out.println("checkId="+getCheckedRadioButtonId()+",childCount="+getChildCount()+",stop="
//	+stop+",reset="+reset+"started="+started+  "  @AutoChangeCheckRadioGroup  run");
		if(stop) {
			removeCallbacks(this);
			return;
		}
		if(reset){
			reset=false;
			postDelayed(this, delayMillis);
		}else{
			check((getCheckedRadioButtonId()+1)%getChildCount());
			postDelayed(this, delayMillis);
		}
	}
	public void startAd(){
		if(!stop||getChildCount()==0) return;
		if(getChildCount()==1){
//			System.out.println("childeCount ==1  @autoChangeCheckRadioGroup");
			check(0);return;
		}
		stop=false;reset=false;started=true;
		removeCallbacks(this);
		post(this);
	}
	public void stopAd(){
		removeCallbacks(this);
		stop=true;
	}
	public void restartAd(){
		if(!started) return;
		startAd();
	}
	public void reset(){
		reset=true;
	}
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == View.VISIBLE){
		}
		else
			stopAd();
	}
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.VISIBLE){
		}
		else
			stopAd();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopAd();
	}
}
