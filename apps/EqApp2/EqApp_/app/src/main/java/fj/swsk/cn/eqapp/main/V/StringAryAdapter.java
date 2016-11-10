package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.ResUtil;


public class StringAryAdapter extends BaseAdapter{

	private int resId= R.layout.item4lv_simpletext;

	private String[] sary;
	private int[] icons;

	private Context context;
	
	public StringAryAdapter(Context context){
		super();
		this.context=context;
	}
	
	
	public void setSary(String[] sary){
		this.sary=sary;
		notifyDataSetChanged();
	}
	public void setIcons(int[] sary){
		this.icons=sary;
	}
	
	@Override
	public int getCount() {
		if(sary==null) return 0;
		return sary.length;
	}

	@Override
	public Object getItem(int position) {
		if(sary==null) return null;
		return sary[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView=LayoutInflater.from(context).inflate(resId, parent,false);
		TextView tv = (TextView)convertView;
		((TextView)convertView).setText(sary[position]);
		if(icons!=null && icons.length>position) {
			Drawable d= ResUtil.getDrawable(icons[position]);
			d.setBounds(ResUtil.dp2Intp(13),ResUtil.dp2Intp(0),ResUtil.dp2Intp(40),ResUtil.dp2Intp(27));
			tv.setCompoundDrawables(d, null, null, null);
			tv.setCompoundDrawablePadding(ResUtil.dp2Intp(33));
			tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

		}
		return convertView;
	}
	
}
