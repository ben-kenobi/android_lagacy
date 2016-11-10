package com.what.yunbao.collection;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.CollectColumns;

public class CollectionActivity extends Activity {
	private static final String TAG = "CollectionActivity";
	private ListView collection_lv;
	private CollectionItemAdapter collection_adapter;
	private BackgroundQueryHandler backgroundQueryHandler;
	private ImageButton collection_edit_ib;
	private ImageButton collection_back_ib;
	private RelativeLayout delete_rl;
	private FrameLayout lvContainer;
	private TextView emptyTip;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection);
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		setUpView();
		startAsyncQuery();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(collection_adapter!=null&&collection_adapter.getCount()==0)
			showEmptyTip();
			
	}

	private void setUpView() {
		collection_lv = (ListView) findViewById(R.id.lv_collection_list);
		lvContainer = (FrameLayout) findViewById(R.id.frameLayout1);
		MyOnClickListener mClickListener = new MyOnClickListener();
		collection_edit_ib = (ImageButton) findViewById(R.id.ib_collection_edit);
		collection_edit_ib.setOnClickListener(mClickListener);
		collection_back_ib = (ImageButton) findViewById(R.id.ib_collection_back);
		collection_back_ib.setOnClickListener(mClickListener);
		delete_rl = (RelativeLayout) findViewById(R.id.what);
		delete_rl.setOnClickListener(mClickListener);

	}

	public void showEmptyTip() {
		if (emptyTip != null) {
			emptyTip.setVisibility(View.VISIBLE);
			return;
		}
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		emptyTip = new TextView(this);
		emptyTip.setText("您暂无收藏记录\n去逛逛吧～");
		emptyTip.setGravity(Gravity.CENTER);
		emptyTip.setTextColor(Color.rgb(0xff, 0xff, 0xff));
		emptyTip.setTextSize(17);
		emptyTip.setPadding(18, 8, 18, 8);
		emptyTip.setCompoundDrawablesWithIntrinsicBounds(0,
				android.R.drawable.stat_sys_warning, 0, 0);
		emptyTip.setBackgroundResource(R.drawable.address_add_positive_selector);
		emptyTip.setClickable(true);
		lvContainer.addView(emptyTip, lp);
		emptyTip.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent()
						.setClassName("com.icanit.app_v2",
								"com.icanit.app_v2.activity.MainActivity")
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
						.setAction("leadToMerchantList"));
			}
		});
	}

	public void startAsyncQuery() {
		if (backgroundQueryHandler == null)
			backgroundQueryHandler = new BackgroundQueryHandler(
					this.getContentResolver());
		backgroundQueryHandler.startQuery(1, null, Notes.COLLECTIONS_URI,
				CollectionItemData.COLLECT_COLUMNS, null, null,
				CollectColumns.ADDTIME + " DESC");
	}

	private final class BackgroundQueryHandler extends AsyncQueryHandler {
		public BackgroundQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			startManagingCursor(cursor);
			if (token == 1) {
				if (cursor == null || cursor.getCount() == 0)
					showEmptyTip();
				else {
					if (collection_adapter == null) {
						collection_lv
								.setOnItemLongClickListener(new MyOnItemLongClickListener());
						collection_lv
								.setOnItemClickListener(new MyOnListItemClickListener());
						collection_adapter = new CollectionItemAdapter(
								CollectionActivity.this);
						collection_lv.setAdapter(collection_adapter);
					}
					collection_adapter.changeCursor(cursor);
				}
			}
		}
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_collection_edit) {
				if (collection_adapter != null
						&& collection_adapter.getCount() > 0)
					collection_adapter.toggleChoiceMode(delete_rl);
			} else if (v.getId() == R.id.ib_collection_back) {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6,
						R.anim.anim_down_toright6);
			} else if (v.getId() == R.id.what) {
				if (collection_adapter == null
						|| collection_adapter.checkedIds.isEmpty()) {
					CommonUtil.showToast("未选择任何项", mToast);
					return;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CollectionActivity.this);
				builder.setTitle("删除信息");
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage(getString(
						R.string.alert_message_delete_notes,
						collection_adapter.checkedIds.size()));
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								batchDelete();
							}
						});
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.show();
			}
		}

	}

	private class MyOnItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			return !collection_adapter.toggleChoiceMode(delete_rl);
		}
	}

	private class MyOnListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (collection_adapter.isChoiceMode) {
				if (view instanceof CollectionItem) {
					CheckBox cb = ((CollectionItem) view).checkBox;
					cb.setChecked(!cb.isChecked());
				}
			} else
				openItem(collection_adapter.getCursor());
		}
	}

	private void openItem(final Cursor cursor) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("稍等。。。");
		pd.setCanceledOnTouchOutside(true);pd.show();
		new AsyncTask<Void, Void, Object>(){
			@Override
			protected Object doInBackground(Void... params) {
				try {
					return Class.forName("com.icanit.app_v2.util.AppUtil").getMethod("newIntentForMerchandizeListActivity", int.class)
					.invoke(null,cursor.getInt(cursor.getColumnIndex(CollectColumns.MERID)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object obj) {
				pd.dismiss();
				if(obj==null)
					CommonUtil.showToast("尝试失败。。。", mToast);
				else
					startActivity((Intent)obj);
			}

		}.execute();
	}

	private void batchDelete() {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				return batchMoveToFolder(getContentResolver(),
						collection_adapter.checkedIds, Notes.ID_TRASH_FOLER);
			}

			@Override
			protected void onPostExecute(Boolean b) {
				if (!b)
					CommonUtil.showToast("删除失败", mToast);
				else
					collection_adapter.toggleChoiceMode(delete_rl);
			};
		}.execute();
	}

	private boolean batchMoveToFolder(ContentResolver resolver, Set<Long> ids,
			long folderId) {
		if (ids == null || ids.isEmpty()) {
			return true;
		}
		return getContentResolver().delete(Notes.COLLECTIONS_URI,
				"_id IN(" + AppUtils.concatenateSet(ids) + ")", null) > 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (collection_adapter != null && collection_adapter.isChoiceMode) {
				collection_adapter.toggleChoiceMode(delete_rl);
				return true;
			} else {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6,
						R.anim.anim_down_toright6);
				return true;
			}
		} else {
			return super.dispatchKeyEvent(event);
		}

	}
}
