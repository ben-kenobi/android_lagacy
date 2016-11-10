package com.icanit.app_v2.adapter;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;


public class AlphabetLvAdapter extends MyBaseAdapter{
	private Context context;
	private int resId=R.layout.simple_list_item3;
	private LayoutInflater inflater;
	private List<String> alphaList;
//	{
//		alphaList=new ArrayList<String>();
//		alphaList.add("A");alphaList.add("B");alphaList.add("C");alphaList.add("D");alphaList.add("E");
//		alphaList.add("F");alphaList.add("G");alphaList.add("H");alphaList.add("I");alphaList.add("J");
//		alphaList.add("K");alphaList.add("L");alphaList.add("M");alphaList.add("N");alphaList.add("O");
//		alphaList.add("P");alphaList.add("Q");alphaList.add("R");alphaList.add("S");alphaList.add("T");
//		alphaList.add("U");alphaList.add("V");alphaList.add("W");alphaList.add("X");alphaList.add("Y");alphaList.add("Z");
//	}
	public AlphabetLvAdapter(Context context){
		super();this.context=context;inflater=LayoutInflater.from(context);
	}
	public int getCount() {
		if (alphaList==null) return 0;
		return alphaList.size();
	}

	public Object getItem(int position) {
		if(alphaList==null) return null;
		return alphaList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup container) {
		if(convertView==null){
			convertView=inflater.inflate(resId, container,false);
		}
		((TextView)convertView).setText(alphaList.get(position).toUpperCase(Locale.ENGLISH));
		return convertView;
	}
	public void setAlphaList(List<String> list){
		this.alphaList=list;
		notifyDataSetChanged();
	}
	public List<String> getAlpahList(){
		return alphaList;
	}
}
