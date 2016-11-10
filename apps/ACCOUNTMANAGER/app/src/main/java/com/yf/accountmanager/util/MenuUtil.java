package com.yf.accountmanager.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.filesystem.MyAnimator;

public class MenuUtil {
	public static final  int modify = 0x11, toggleAccessKey = 0x12;
	
	
	
	
	
	
	public static  void showSoftInput(final View v,final Context context){
		IConstants.MAIN_HANDLER.postDelayed(new Runnable(){
			public void run(){
				InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(v, 0, null);
			}
		}, 300);
	}
	
	public static void hideSoftInput(View v,Context context){
		InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0, null);
	}
	
	
	
	
	
	public static  int getToggleAccessKeyMenuItemTitle(String name) {
		return CommonService.isAccessKeyEnable(name) ? R.string.disableAccessKey
				: R.string.enableAccessKey;
	}
	
	public static void createAccessKeyMenu(String name,Menu menu){
		if (!TextUtils.isEmpty(CommonUtils.getAccessKey()))
			menu.add(0, modify, 0, R.string.modifyAccessKey).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(0, toggleAccessKey, 0, getToggleAccessKeyMenuItemTitle(name))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}
	
	public static void commonActionBarDisplayOption(Activity context){
		context.getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
	}
	
	public static void bindFloatMenuTrigger(View trigger,View v,Context context) {
		try {
			int toValue=context.getResources().getDimensionPixelSize(R.dimen.dp_120);
			final MyAnimator anim = new MyAnimator(v, 0,toValue,0,toValue, 200);
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (anim.toValueX == anim.endValueX) {
						anim.startReverse();
					} else {
						anim.start();
					}
				}
			};
			if (trigger != null)
				trigger.setOnClickListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
