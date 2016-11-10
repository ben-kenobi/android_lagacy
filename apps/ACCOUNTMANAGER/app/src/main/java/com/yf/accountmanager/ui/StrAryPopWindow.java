package com.yf.accountmanager.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yf.accountmanager.R;
import com.yf.accountmanager.action.BasePopWindow;
import com.yf.accountmanager.adapter.StringAryAdapter;

public class StrAryPopWindow extends BasePopWindow implements OnItemClickListener{
	
	ListView lv;
	
	StringAryAdapter adapter;
	
	Context context;
	
	boolean updateTriggerText;
	
	OnPopListClickListener listener;
	
	
	public StrAryPopWindow(TextView trig,Context context,String[] strAry,
			boolean updateTriggerText,int width,OnPopListClickListener listener) throws Throwable {
		super(R.layout.popupwindow_options, LayoutInflater.from(context),width,-2);
		this.context=context;
		init();
		update(trig, strAry, updateTriggerText, -3, listener);
	}
	
	public StrAryPopWindow(Context context){
		super(R.layout.popupwindow_options, LayoutInflater.from(context),-2,-2);
		this.context=context;
		init();
	}

	public void update(TextView trig,String[] strAry,boolean updateTriggerText,int width,OnPopListClickListener listener){
		if(width!=-3)
			pop.setWidth(width);
		setTrigger(trig);
		this.updateTriggerText=updateTriggerText;
		this.listener=listener;
		adapter.setSary(strAry);
	}
	
	private void init(){
		lv=(ListView)contentView.findViewById(R.id.listView1);
		lv.setAdapter(adapter=new StringAryAdapter(context));
		lv.setOnItemClickListener(this);
	}
	@Override
	public void execute() throws Throwable {
		
	}

	
	
	@Override
	public void resetTrigger() {}

	@Override
	public void onItemClick(AdapterView<?> parent, View self, int position,
			long id) {
		if(updateTriggerText)
			trigger.setText(String.valueOf(adapter.getItem(position)));
		if(listener!=null)
		listener.onclick(position);
		pop.dismiss();
	}
	
	public static interface OnPopListClickListener{
		 void onclick(int position);
	}
	
}
