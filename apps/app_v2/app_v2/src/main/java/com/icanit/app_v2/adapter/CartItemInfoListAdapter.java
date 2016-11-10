package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.CartItem;

public final class CartItemInfoListAdapter extends MyBaseAdapter {
	private int selected=-1;
	private List<CartItem> items	;
	private Context context;private LayoutInflater inflater;
	private int resId=R.layout.simple_list_marquee_item;
	StyleSpan bold = new StyleSpan(Typeface.BOLD);
	ForegroundColorSpan colorspan = new ForegroundColorSpan(Color.rgb(0xee, 76, 00));
	public CartItemInfoListAdapter(Context context,List<CartItem> items){
		this.context=context;inflater=LayoutInflater.from(context);
		this.items=items;
	}

	public View getView(int position, View convertView, ViewGroup root) {
		if(convertView==null){
			convertView=inflater.inflate(resId, root,false);
		}
		TextView tv=(TextView)convertView;
		CartItem item = items.get(position);
		Drawable drawableLeft=context.getResources().getDrawable(android.R.drawable.radiobutton_on_background);
		tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
		String str="��Ʒ:��ֵ"+item.former_price+"Ԫ����ȯ1��  "+"�۸�:"+item.present_price+" X "+item.quantity;
		SpannableString ss=new SpannableString(str);
//		ss.setSpan(bold, str.indexOf("ֵ")+1, str.indexOf("Ԫ"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ss.setSpan(bold, str.indexOf("ȯ")+1, str.indexOf("��"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(colorspan, str.lastIndexOf(":")+1, str.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(bold, str.indexOf("�۸�:"), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ss);
		return convertView;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}
	private static class ViewHolder{
		
	}
	
	
}
