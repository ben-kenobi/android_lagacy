package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;

public class StrAryPopWindow extends BasePopWindow implements OnItemClickListener{

	ListView lv;

	public StringAryAdapter adapter;

	Context context;

	boolean updateTriggerText;

	OnPopListClickListener listener;


	public StrAryPopWindow(View trig,Context context,String[] strAry,int[] icons,
						   boolean updateTriggerText,int width,OnPopListClickListener listener) {
		super(R.layout.popupwindow_options, LayoutInflater.from(context),width,-2);
		this.context=context;
		init();
		update(trig, strAry,icons, updateTriggerText, -3, listener);
	}

	public StrAryPopWindow(Context context){
		super(R.layout.popupwindow_options, LayoutInflater.from(context),-2,-2);
		this.context=context;
		init();
	}

	public void update(View trig,String[] strAry,int[] icons,boolean updateTriggerText,int width,OnPopListClickListener listener){
		if(width!=-3)
			pop.setWidth(width);
		setTrigger(trig);
		this.updateTriggerText=updateTriggerText;
		this.listener=listener;
		adapter.setIcons(icons);
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
		if(updateTriggerText&&trigger.getClass() ==  TextView.class)
			((TextView)trigger).setText(String.valueOf(adapter.getItem(position)));
		if(listener!=null)
			listener.onclick(this,position);
		pop.dismiss();
	}

	public static interface OnPopListClickListener{
		void onclick(BasePopWindow pop,int position);
	}
}
