package com.icanit.app.fragments;

import android.app.Fragment;
import android.widget.RadioButton;

public class AbstractRadioBindFragment extends Fragment {
	protected RadioButton trigger;
	public void setTrigger(RadioButton radioButton){
		this.trigger=radioButton;
	}
	@Override
	public void onResume() {
		super.onResume();
		if(trigger!=null&&!trigger.isChecked())
			trigger.setChecked(true);
	}
}
