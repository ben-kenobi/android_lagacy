package com.what.yunbao.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.what.yunbao.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class SettingGuideActivity extends Activity{
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	
	private Handler handler=new Handler();
	
	private Runnable run=new Runnable(){

		@Override
		public void run() { 
			if (isContinue) {
//				currentPositon=what.get();
				viewHandler.sendEmptyMessage(what.get());
				whatOption();				
			} 	
			handler.postDelayed(run, 2000);
		}		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_guide);
		initViewPager();
		run.run();
	}

	/**
	 * 设置广告栏的图片及切换效果
	 */
	private void initViewPager() {
		advPager = (ViewPager) findViewById(R.id.adv_pager);
		// 图片列表
		List<View> advPics = new ArrayList<View>();
		// 图片1
		ImageView img1 = new ImageView(this);
		img1.setBackgroundResource(R.drawable.guide_0);
		advPics.add(img1);
		// 图片2
		ImageView img2 = new ImageView(this);
		img2.setBackgroundResource(R.drawable.guide_1);
		advPics.add(img2);
		// 图片3
		ImageView img3 = new ImageView(this);
		img3.setBackgroundResource(R.drawable.nexus);
		advPics.add(img3);
		// 图片4
		ImageView img4 = new ImageView(this);
		img4.setBackgroundResource(R.drawable.googol);
		advPics.add(img4);
  
		// group是R.layou.mainview中的负责包裹小圆点的LinearLayout.
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		imageViews = new ImageView[advPics.size()];

		for (int i = 0; i < advPics.size(); i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_blur);
			}
			group.addView(imageViews[i]);
		}
		
		advPager.setAdapter(new AdvAdapter(advPics));
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		//按下时不继续定时滑动,弹起时继续定时滑动
		advPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_MOVE) {
					isContinue = false;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					isContinue = true;
				} else {
					isContinue = true;
				}
				Log.e("touch", v.getId()+"");
				return false;
			}
		});
	}

	/**
	 * 操作圆点轮换变背景
	 */
	private void whatOption() {
		if (what.incrementAndGet() > imageViews.length - 1) {
			what.set(0);
		}
	}

	/**
	 * 处理定时切换图片的句柄
	 */
	private  Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			advPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	/** 指引页面改监听器 */
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for(int i=0;i<imageViews.length;i++){
				imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
			}
			imageViews[arg0].setBackgroundResource(R.drawable.banner_dian_focus);			
			what.set(arg0);

		}

	}

	/**
	 * PaperView 图片适配器
	 * 
	 */
	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		} 

		@Override 
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		isContinue = false;
		viewHandler.removeMessages(what.get());		
		handler.removeCallbacks(run);
//		run = null;	//????
		
	}
}
