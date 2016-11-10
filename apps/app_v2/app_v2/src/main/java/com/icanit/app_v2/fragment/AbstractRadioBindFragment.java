package com.icanit.app_v2.fragment;

import android.support.v4.app.Fragment;
import android.widget.RadioButton;

public class AbstractRadioBindFragment extends Fragment {
	protected RadioButton trigger;
	public void setTrigger(RadioButton radioButton){
		this.trigger=radioButton;
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onResume() {
		super.onResume();
		if(trigger!=null&&!trigger.isChecked())
			trigger.setChecked(true);
	}
	public void onPause(){
		super.onPause();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onDetach() {
		super.onDetach();
	}
}
