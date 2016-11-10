package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.ResUtil;


public class StringAryGvAdapter extends BaseAdapter{

	private int resId= R.layout.item4lv_simpletext2;

	private String[] sary;
	private int[] icons;

	private Context context;
	private Set<Integer> seletedList = new HashSet<>();


	public void onPosSelected(int pos){
		if(seletedList.contains(pos)){
			seletedList.remove(pos);
		}else{
			seletedList.add(pos);
		}
		notifyDataSetChanged();
	}
	public Set<Integer> getSeletedList(){
		return seletedList;
	}


	public StringAryGvAdapter(Context context){
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
			d.setBounds(ResUtil.dp2Intp(0),ResUtil.dp2Intp(20),ResUtil.dp2Intp(42),ResUtil.dp2Intp(62));
			tv.setCompoundDrawables(null, d, null, null);
			tv.setCompoundDrawablePadding(ResUtil.dp2Intp(15));
			tv.setGravity(Gravity.CENTER);

		}
		tv.setActivated(seletedList.contains(position));
		return convertView;
	}
	
}
