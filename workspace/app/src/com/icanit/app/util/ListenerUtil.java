package com.icanit.app.util;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.icanit.app.adapter.MyBaseAdapter;
import com.icanit.app.adapter.MyBasePagerAdapter;

public class ListenerUtil {
	private static ListenerUtil self;
	private ListenerUtil(){}
	public static ListenerUtil getInstance(){
		if(self==null) self= new ListenerUtil();
		return self;
	}
	
	public static class MyOnPageChangeListener implements OnPageChangeListener{
		private MyBasePagerAdapter  adapter;
		public MyOnPageChangeListener(MyBasePagerAdapter adapter){
			this.adapter=adapter;
		}
		public void onPageScrollStateChanged(int scrollState) {
			switch(scrollState){
			case ViewPager.SCROLL_STATE_DRAGGING:
				adapter.setBusy(false);
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				adapter.setBusy(false);
				adapter.notifyDataSetChanged();
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				adapter.setBusy(true);break;
			default :break;
			}
		}
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		public void onPageSelected(int arg0) {
			
		}
	}
	
	
	public static class MyOnScrollListener implements OnScrollListener{
		private MyBaseAdapter adapter;
		public MyOnScrollListener(MyBaseAdapter adapter){
			this.adapter=adapter;
		}
		
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch(scrollState){
			case SCROLL_STATE_FLING:
				adapter.setBusy(true);
				break;
			case SCROLL_STATE_IDLE:
				adapter.setBusy(false);
				adapter.notifyDataSetChanged();
				break;
			case SCROLL_STATE_TOUCH_SCROLL:
				adapter.setBusy(false);break;
			default :break;
			}
		}
	}
	
	public static class  TextChangeListener implements TextWatcher,OnClickListener{
		private EditText editText;
		private ImageButton textDisposer;
		private Button searchConfirm;
		
		public TextChangeListener(EditText editText,ImageButton textDisposer,Button searchConfirm){
			this.editText=editText;this.textDisposer=textDisposer;this.searchConfirm=searchConfirm;
			this.textDisposer.setOnClickListener(this);
			editText.addTextChangedListener(this);
		}
		public TextChangeListener(EditText editText,ImageButton textDisposer){
			this.editText=editText;this.textDisposer=textDisposer;
			this.textDisposer.setOnClickListener(this);
			editText.addTextChangedListener(this);
		}
		public void afterTextChanged(Editable s) {
			
		}
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		public void onTextChanged(CharSequence content, int start, int before,
				int count) {
			if(content.length()==0){
				if(textDisposer!=null)
				textDisposer.setVisibility(View.GONE);
				if(searchConfirm!=null)
				searchConfirm.setVisibility(View.GONE);
			}else{
				if(textDisposer!=null)
				textDisposer.setVisibility(View.VISIBLE);
				if(searchConfirm!=null)
				searchConfirm.setVisibility(View.VISIBLE);
			}
		}
		public void onClick(View v) {
			if(v==textDisposer)
			editText.setText("");
		}
	}
}
