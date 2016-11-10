package com.icanit.app_v2.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.icanit.app_v2.ui.AutoChangeCheckRadioGroup;

public class FeaturedViewCreator {
	public static AutoChangeCheckRadioGroup createAdIndicatingRG(Context context){
		AutoChangeCheckRadioGroup radioGroup = new AutoChangeCheckRadioGroup(context);
		FrameLayout.LayoutParams  lp=new FrameLayout.LayoutParams(-2, -2);
		lp.gravity=Gravity.RIGHT|Gravity.BOTTOM;
		radioGroup.setLayoutParams(lp);
		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		return radioGroup;
	}
}
