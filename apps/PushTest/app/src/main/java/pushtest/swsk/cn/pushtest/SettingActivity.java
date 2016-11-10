package pushtest.swsk.cn.pushtest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/3/30.
 */
public class SettingActivity extends InstrumentedActivity implements View.OnClickListener {

    TimePicker startTime;
    TimePicker endTime;
    CheckBox[] mCheckBoxes;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_push_time);
        init();
    }

    private void init() {
        startTime = (TimePicker) findViewById(R.id.start_time);
        endTime = (TimePicker) findViewById(R.id.end_time);
        startTime.setIs24HourView(DateFormat.is24HourFormat(this));
        endTime.setIs24HourView(DateFormat.is24HourFormat(this));
        mCheckBoxes = new CheckBox[]{
                (CheckBox) findViewById(R.id.cb_sunday),
                (CheckBox) findViewById(R.id.cb_monday),
                (CheckBox) findViewById(R.id.cb_tuesday),
                (CheckBox) findViewById(R.id.cb_wednesday),
                (CheckBox) findViewById(R.id.cb_thursday),
                (CheckBox) findViewById(R.id.cb_friday),
                (CheckBox) findViewById(R.id.cb_saturday)
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        mSettings = getSharedPreferences(IUtil.PREFS_NAME, MODE_PRIVATE);
        String days = mSettings.getString(IUtil.PREFS_DAYS, "");
        if (!TextUtils.isEmpty(days)) {
            initAllWeek(false);
            String[] sArray = days.split(",");
            for (String day : sArray) {
                setWeek(day);
            }
        } else {
            initAllWeek(true);
        }

        int startTimeStr = mSettings.getInt(IUtil.PREFS_START_TIME, 0);
        startTime.setCurrentHour(Integer.valueOf(startTimeStr));
        int endTimeStr = mSettings.getInt(IUtil.PREFS_END_TIME, 23);
        endTime.setCurrentHour(Integer.valueOf(endTimeStr));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bu_setTime) {
            v.requestFocus();
            v.requestFocusFromTouch();
            setPushTime();
        }
    }

    private void setPushTime() {
        int startime = startTime.getCurrentHour();
        int endtime = endTime.getCurrentHour();
        if (startime > endtime) {
            Toast.makeText(SettingActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer daysSB = new StringBuffer();
        Set<Integer> days = new HashSet<Integer>();
        for(int i=0;i<mCheckBoxes.length;i++){
            if(mCheckBoxes[i].isChecked()){
                days.add(i);
                daysSB.append(i + ",");
            }
        }

        //调用JPush api设置Push时间
        JPushInterface.setPushTime(getApplicationContext(), days, startime, endtime);

        mEditor = mSettings.edit();
        mEditor.putString(IUtil.PREFS_DAYS, daysSB.toString());
        mEditor.putInt(IUtil.PREFS_START_TIME, startime);
        mEditor.putInt(IUtil.PREFS_END_TIME, endtime);
        mEditor.commit();
        Toast.makeText(SettingActivity.this, R.string.setting_su, Toast.LENGTH_SHORT).show();

    }


    private void setWeek(String day) {
        int dayId = Integer.valueOf(day);
        mCheckBoxes[dayId].setChecked(true);
    }

    private void initAllWeek(boolean isChecked) {
        for (CheckBox b : mCheckBoxes) {
            b.setChecked(isChecked);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

