package cn.swsk.rgyxtqapp.adapter;

import android.content.Context;
import android.graphics.Color;

import cn.swsk.rgyxtqapp.R;

public abstract  class CascadeLvAdapter<T> extends CommonAdapter<T> {

	public int selected = -1;
	public int selectedColor = Color.rgb(0xee, 0xee, 0xee);

	public CascadeLvAdapter(Context context) {
		super(context, null, R.layout.simple_list_item3);
	}

	public void setSelected(int selected) {
		this.selected = selected;
		notifyDataSetInvalidated();
	}
	public T selectedItem() {
		if (selected < 0)
			return null;
		return getDatas().get(selected);
	}




}
