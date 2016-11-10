package com.what.yunbao.push;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import  com.what.yunbao.R;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.SharedPrefrencesUtil;

public class PushSettingActivity extends Activity implements OnClickListener {
	private TimePicker startTime;
	private TimePicker endTime;
	private CheckBox mMonday ;
	private CheckBox mTuesday ;
	private CheckBox mWednesday;
	private CheckBox mThursday;
	private CheckBox mFriday ;
	private CheckBox mSaturday;
	private CheckBox mSunday ;
	private Button mSetTime;
	private SharedPreferences mSettings;
	private Editor mEditor;
	
	private Toast mToast;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.setting_push_time);
		init();
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		initListener();
	}
	
   @Override
    public void onStart() {
	    super.onStart();
	    initData();
   }
   
	private void init(){
		startTime = (TimePicker) findViewById(R.id.start_time);
		endTime = (TimePicker) findViewById(R.id.end_time);
		startTime.setIs24HourView(DateFormat.is24HourFormat(this));
		endTime.setIs24HourView(DateFormat.is24HourFormat(this));
		mSetTime = (Button)findViewById(R.id.bu_setTime);
		mMonday = (CheckBox)findViewById(R.id.cb_monday);
		mTuesday = (CheckBox)findViewById(R.id.cb_tuesday);
		mWednesday = (CheckBox)findViewById(R.id.cb_wednesday);
	    mThursday = (CheckBox)findViewById(R.id.cb_thursday);
		mFriday = (CheckBox)findViewById(R.id.cb_friday);
		mSaturday = (CheckBox)findViewById(R.id.cb_saturday);
		mSunday = (CheckBox)findViewById(R.id.cb_sunday);	
			
	}
  
    private void initListener(){
	   mSetTime.setOnClickListener(this); 
    }
   
    private void initData(){
	  mSettings = getSharedPreferences(SharedPrefrencesUtil.PUSH_SHAREDPREFERENCE_NAME, MODE_PRIVATE);
	  String days = mSettings.getString(SharedPrefrencesUtil.PUSH_PREFS_DAYS, "");
		if (!TextUtils.isEmpty(days)) {
			initAllWeek(false);
			String[] sArray = days.split(",");
			for (String day : sArray) {
				setWeek(day);
			}
		} else {
			initAllWeek(true);
		}
		
	  int startTimeStr = mSettings.getInt(SharedPrefrencesUtil.PUSH_PREFS_START_TIME, 0);
	  startTime.setCurrentHour(Integer.valueOf(startTimeStr));
	  int endTimeStr = mSettings.getInt(SharedPrefrencesUtil.PUSH_PREFS_END_TIME, 23);
	  endTime.setCurrentHour(Integer.valueOf(endTimeStr));
   }

	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.bu_setTime){
			v.requestFocus();
			v.requestFocusFromTouch();
			setPushTime();
			finish();
		}
	}
	
	/**
	 *设置允许接收通知时间
	 */
	private void setPushTime(){
		int startime = startTime.getCurrentHour();
		int endtime = endTime.getCurrentHour();
		if (startime > endtime) {
			Toast.makeText(PushSettingActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
			return;
		}
		StringBuffer daysSB = new StringBuffer();
		Set<Integer> days = new HashSet<Integer>();
		if (mSunday.isChecked()) {
			days.add(0);
			daysSB.append("0,");
		}
		if (mMonday.isChecked()) {
			days.add(1);
			daysSB.append("1,");
		}
		if (mTuesday.isChecked()) {
			days.add(2);
			daysSB.append("2,");
		}
		if (mWednesday.isChecked()) {
			days.add(3);
			daysSB.append("3,");
		}
		if (mThursday.isChecked()) {
			days.add(4);
			daysSB.append("4,");
		}
		if (mFriday.isChecked()) {
			days.add(5);
			daysSB.append("5,");
		}
		if (mSaturday.isChecked()) {
			days.add(6);
			daysSB.append("6,");
		}
		
	
		//调用JPush api设置Push时间
		JPushInterface.setPushTime(getApplicationContext(), days, startime, endtime);
		
		mEditor = mSettings.edit();
		mEditor.putString(SharedPrefrencesUtil.PUSH_PREFS_DAYS, daysSB.toString());
		mEditor.putInt(SharedPrefrencesUtil.PUSH_PREFS_START_TIME, startime);
		mEditor.putInt(SharedPrefrencesUtil.PUSH_PREFS_END_TIME, endtime);
		mEditor.commit();
		CommonUtil.showToast("设置成功", mToast);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setWeek(String day){
		int dayId = Integer.valueOf(day);
		if (dayId == 0) {
			mSunday.setChecked(true);
		} else if (dayId == 1) {
			mMonday.setChecked(true);
		} else if (dayId == 2) {
			mTuesday.setChecked(true);
		} else if (dayId == 3) {
			mWednesday.setChecked(true);
		} else if (dayId == 4) {
			mThursday.setChecked(true);
		} else if (dayId == 5) {
			mFriday.setChecked(true);
		} else if (dayId == 6) {
			mSaturday.setChecked(true);
		}
	}
	
	private void initAllWeek(boolean isChecked) {
		mSunday.setChecked(isChecked);
		mMonday.setChecked(isChecked);
		mTuesday.setChecked(isChecked);
		mWednesday.setChecked(isChecked);
		mThursday.setChecked(isChecked);
		mFriday.setChecked(isChecked);
		mSaturday.setChecked(isChecked);
	}
}