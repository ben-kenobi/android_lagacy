package com.icanit.app_v2.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;

public class PayResultFragment extends Fragment implements OnClickListener {
	
	private View self;

	private int resId = R.layout.fragment4pay_pay_result;
	
	private Button backToOrigin;
	
	private TextView resultDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();bindListeners();describeResult();
		
	}
	
	private void describeResult() {
		String str = "订单支付成功\n将从您账户扣除 ￥"+
				String.format("%.2f", ((AppOrder)getActivity().getIntent().getSerializableExtra(IConstants.ORDER_INFO)).sum);
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(Color.rgb(0x33, 0xaa, 0x33)), 0, str.indexOf('￥'), 
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new AbsoluteSizeSpan(20, true), 0, str.indexOf('￥'), 
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.rgb(0xdd, 0x33, 0x33)), str.indexOf('￥'), str.length(), 
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new AbsoluteSizeSpan(22,true), str.indexOf('￥'), str.length(), 
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		resultDescription.setText(ss);
	}
	
	private void init() {
		self=LayoutInflater.from(getActivity()).inflate(resId, null,false);
		backToOrigin=(Button)self.findViewById(R.id.button1);
		resultDescription=(TextView)self.findViewById(R.id.textView1);
	}
	private void bindListeners() {
		backToOrigin.setOnClickListener(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewParent vp=self.getParent();
		if(vp!=null) ((ViewGroup)vp).removeAllViews();
		return self;
	}
	
	@Override
	public void onClick(View v) {
		if(v==backToOrigin){
			//TODO
			getActivity().finish();
		}
	}

}
