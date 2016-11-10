package com.what.yunbao.history;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.util.AppUtil;
import com.what.yunbao.R;
import com.what.yunbao.collection.CollectionItemData;
import com.what.yunbao.test.TT;
import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.CollectColumns;

public class ViewPagerFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "ViewPageFragement";

	private ListView mListView;
	private CursorAdapter mAdapter;
	private ProgressBar mLoadingProgress;
	private TextView mErrorText;
	private int position;
	private View self;
	private int resId = R.layout.history_fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		new LoadingTask().execute();
	}

	private void init() {
		position = getArguments().getInt("position");
		self = LayoutInflater.from(getActivity()).inflate(resId, null, false);
		mListView = (ListView) self.findViewById(android.R.id.list);
		mLoadingProgress = (ProgressBar) self
				.findViewById(R.id.pb_history_loading);
		mErrorText = (TextView) self.findViewById(R.id.pb_history__error);
		mErrorText.setCompoundDrawablesWithIntrinsicBounds(0,
				android.R.drawable.stat_sys_warning, 0, 0);
		if(position==0) mErrorText.setText("无浏览记录,去逛逛吧~");
		else if(position==1) mErrorText.setText("无优惠记录,去逛逛吧~");
		mErrorText.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent()
						.setClassName("com.icanit.app_v2",
								"com.icanit.app_v2.activity.MainActivity")
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
						.setAction("leadToMerchantList"));
			}
		});
	}
	public void showEmptyTip(){
		mErrorText.setVisibility(View.VISIBLE);
	}

	public int getCount() {
		if (mAdapter == null)
			return 0;
		return mAdapter.getCount();
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter!=null&&mAdapter.getCount()==0){
			mErrorText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewParent vp = self.getParent();
		if (vp != null)
			((ViewGroup) vp).removeAllViews();
		return self;
	}

	/**
	 * 模拟列表
	 * 
	 */
	private final class LoadingTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
			try {
				if (position == 0)
					return getActivity().getContentResolver().query(
							Notes.BROWSING_URI,
							CollectionItemData.COLLECT_COLUMNS, "phone=?",
							new String[] { AppUtils.getLoginPhoneNum() },
							"addTime DESC");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			mLoadingProgress.setVisibility(View.GONE);
			if (cursor == null || cursor.getCount() == 0) {
				mErrorText.setVisibility(View.VISIBLE);
				if(cursor!=null)
				cursor.close();
			} else {
				if (mAdapter == null) {
					if(position==0){
						mAdapter = new BusinessAdapter(getActivity(),ViewPagerFragment.this);
						mListView.setAdapter(mAdapter);
						mListView.setOnItemClickListener(ViewPagerFragment.this);
					}else if(position==1){
						
					}
				}
				if(mAdapter!=null)
				mAdapter.changeCursor(cursor);
				getActivity().startManagingCursor(cursor);
			}
		}

		@Override
		protected void onPreExecute() {
			mErrorText.setVisibility(View.GONE);
			mLoadingProgress.setVisibility(View.VISIBLE);
		}

	}

	public void onItemClick(AdapterView<?> container, View view, int position, long id) {
		openItem(mAdapter.getCursor());
	}
	private void openItem(final Cursor cursor) {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage("稍等。。。");
		pd.setCanceledOnTouchOutside(true);pd.show();
		new AsyncTask<Void, Void, Object>(){
			@Override
			protected Object doInBackground(Void... params) {
				try {
					int merId=cursor.getInt(cursor.getColumnIndex(CollectColumns.MERID));
					if(merId==0) return null; 
					return AppUtil.newIntentForMerchandizeListActivity(merId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object obj) {
				pd.dismiss();
				if(obj==null)
					Toast.makeText(getActivity(), "尝试失败", Toast.LENGTH_SHORT).show();
				else
					startActivity((Intent)obj);
			}

		}.execute();
	}
}
