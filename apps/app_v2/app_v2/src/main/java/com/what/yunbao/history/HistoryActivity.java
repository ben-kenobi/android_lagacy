package com.what.yunbao.history;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.CollectColumns;

public class HistoryActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, new ViewPagerFrags()).commit();

	}

	public static class ViewPagerFrags extends Fragment implements
			OnClickListener, DialogInterface.OnClickListener {
		private ViewPager mViewPager;
		private ViewPagerAdapter adapter;
		private ImageButton backButton;
		private ImageButton deleteButton;
		private View self;
		private int resId = R.layout.history_fragment_top;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			init();
		}

		private void init() {
			self = LayoutInflater.from(getActivity()).inflate(resId, null,
					false);
			mViewPager = (ViewPager) self.findViewById(R.id.pager);
			adapter = new ViewPagerAdapter(getFragmentManager());
			mViewPager.setAdapter(adapter);
			PagerTabStrip tabs = (PagerTabStrip) self.findViewById(R.id.tabs);
			tabs.setTabIndicatorColor(0xFF63B8FF);
			tabs.setDrawFullUnderline(false);// 不显示整条横线
			tabs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			backButton = (ImageButton) self.findViewById(R.id.ib_history_back);
			deleteButton = (ImageButton) self
					.findViewById(R.id.ib_history_edit);
			backButton.setOnClickListener(this);
			deleteButton.setOnClickListener(this);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewParent vp = self.getParent();
			if (vp != null)
				((ViewGroup)vp).removeAllViews();
			return self;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_history_back) {
				getActivity().finish();
				getActivity().overridePendingTransition(
						R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			} else if (v.getId() == R.id.ib_history_edit) {
				AlertDialog.Builder builder = new Builder(getActivity());
				if (mViewPager.getCurrentItem() == 0)
					builder.setMessage("删除浏览记录？");
				else if (mViewPager.getCurrentItem() == 1)
					builder.setMessage("删除使用记录?");
				else
					return;
				builder.setTitle("提示").setPositiveButton("确认", this)
						.setNegativeButton("取消", null).create().show();
			}
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(mViewPager.getCurrentItem()==0){
				ViewPagerFragment fragment=(ViewPagerFragment)getActivity().getSupportFragmentManager().
						findFragmentByTag("android:switcher:"+R.id.pager+":0");
				if(fragment.getCount()==0){
					Toast.makeText(getActivity(), "无浏览记录", Toast.LENGTH_SHORT).show();
					return ;
				}
				new AsyncTask<Void, Void, Boolean>(){
					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							return getActivity().getContentResolver().delete(Notes.BROWSING_URI, CollectColumns.PHONE+"=?",
										new String[]{AppUtils.getLoginPhoneNum()})>0;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						if(result)
							Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
						else 
							Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
					}
					
					
				}.execute();
				
			}else if(mViewPager.getCurrentItem()==1){
				ViewPagerFragment fragment=(ViewPagerFragment)getActivity().getSupportFragmentManager().
						findFragmentByTag("android:switcher:"+R.id.pager+":1");
				if(fragment.getCount()==0){
					Toast.makeText(getActivity(), "无使用记录", Toast.LENGTH_SHORT).show();
					return ;
				}
			}
		}

	}

	private static class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private static final int COUNT = 2;

		@Override
		public Fragment getItem(int position) {
			ViewPagerFragment fragment =new   ViewPagerFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			fragment.setArguments(bundle);
			return fragment;
		}
		@Override
		public int getCount() {
			return COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return position == 0 ? "店铺记录" : position == 1 ? "优惠记录" : "";
		}

		@Override
		public int getItemPosition(Object object) {

			return PagerAdapter.POSITION_NONE;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6,
					R.anim.anim_down_toright6);
			return true;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}
}
