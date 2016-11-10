package com.icanit.app_v2.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bshare.core.PlatformType;
import com.icanit.app_v2.adapter.ShareListAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.umeng.fb.FeedbackAgent;
import com.what.weibo.WeiboMainActivity;
import com.what.yunbao.R;
import com.what.yunbao.setting.CornerListView;
import com.what.yunbao.setting.SettingAboutActivity;
import com.what.yunbao.setting.SettingAdapter;
import com.what.yunbao.setting.SettingGuideActivity;
import com.what.yunbao.setting.SettingPrivacyWebViewActivity;
import com.what.yunbao.update.AppUpgradeService;
import com.what.yunbao.update.MyAlertDialog;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.DataCleanManager;

public class Main_settings_Fragment extends AbstractRadioBindFragment implements
		OnItemClickListener {
	private View self;
	private int resId = R.layout.setting;
	private static final String TAG = "SettingTabActivity";
	private ImageButton setting_back_save_bt;
	private String[] mSettingItems = { "�������", "2G/3G���Զ�����ͼƬ", "չʾ�ҵĶ�̬",
			"������", "�������", "���ְ���", "��˽����", "��������", "�������" };
	private String[] mSettingItemMethods = { "clearCache", "receivePicture",
			"displayDynamic ", "checkNewVersion", "feedBackSuggestion", "help",
			"privacy", "about", "shareApp" };
	private HashMap<String, String> mSettingItemMethodMap = new HashMap<String, String>();
	private List<List<Map<String, String>>> listDatas = new ArrayList<List<Map<String, String>>>();
	private SettingAdapter adapter_1;
	private Activity mContext;
	private CustomizedDialog shareDialog;
	private ShareListAdapter shareListAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.initBShareConfig(getActivity());
		init();
		setupLayout();
		bindListeners();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) self.getParent();
		if (vg != null)
			vg.removeAllViews();
		return self;
	}

	private void init() {
		self = LayoutInflater.from(getActivity()).inflate(resId, null, false);
		setting_back_save_bt = (ImageButton) self
				.findViewById(R.id.ib_setting_back);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(setting_back_save_bt);
		self.findViewById(R.id.tv_setting_account_manager).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),
								WeiboMainActivity.class));
					}
				});
	}

	public void setupLayout() {
		allocateDatas();
		LinearLayout cornerContainer = (LinearLayout) self
				.findViewById(R.id.setting);
		for (int i = 0; i < mSettingItems.length; i++) {
			mSettingItemMethodMap.put(mSettingItems[i], mSettingItemMethods[i]);
		}
		int size = listDatas.size();

		LayoutParams lp;
		for (int i = 0; i < size; i++) {// size = 2
			CornerListView cornerListView = new CornerListView(getActivity());
			lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			lp.setMargins(8, 4, 8, 4);
			cornerListView.setLayoutParams(lp);
			cornerListView.setDivider(getResources().getDrawable(
					R.drawable.app_divider_h_gray));
			cornerListView.setHeaderDividersEnabled(false);
			cornerListView.setFooterDividersEnabled(false);
			cornerContainer.addView(cornerListView);
			if (i == 1) {
				cornerListView.setAdapter(new SettingAdapter(getActivity(),
						listDatas.get(i)));
			} else {
				adapter_1 = new SettingAdapter(getActivity(), listDatas.get(i));
				cornerListView.setAdapter(adapter_1);
			}
			cornerListView.setOnItemClickListener(this);
			int height = listDatas.get(i).size()
					* (int) getResources().getDimension(
							R.dimen.setting_item_height)
					+ (listDatas.get(i).size() - 1)
					* cornerListView.getDividerHeight();
			cornerListView.getLayoutParams().height = height;
		}
		adapter_1.setCacheSize(getPackageStats());
		adapter_1.setSelectedIndex(getPreferences());
		adapter_1.notifyDataSetChanged();
	}

	public void allocateDatas() {
		listDatas.clear();
		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		for (int i = 0; i < mSettingItems.length; i++) {
			map = new HashMap<String, String>();
			map.put("text", mSettingItems[i]);
			listData.add(map);
			if (i == 3) {
				listDatas.add(listData);// ��ֵ��
				listData = new ArrayList<Map<String, String>>();
			}
		}
		listDatas.add(listData);// ��ֵ��
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		TextView textView = (TextView) view
				.findViewById(R.id.tv_setting_list_item_text);
		String key = textView.getText().toString();
		Class clazz = this.getClass();
		try {
			Method method = clazz.getMethod(mSettingItemMethodMap.get(key));
			method.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearCache() {
		DataCleanManager.cleanApplicationCacheData(mContext);
		adapter_1.setCacheSize("0B");
		adapter_1.notifyDataSetChanged();
	}

	public void checkNewVersion() throws Exception {
		if (!CommonUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "�Բ�����δ�������硣", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		final PackageInfo packageInfo = getActivity().getPackageManager()
				.getPackageInfo(getActivity().getPackageName(), 0);
		final ProgressDialog pd = DialogUtil.createProgressDialogNshow("",
				"��ȡ������Ϣ������", true, getActivity());
		new AsyncTask<Void, Void, String>() {
			protected String doInBackground(Void... params) {
				try {
					return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(
							UriConstants.GET_LATEST_APP_VERSION, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(String result) {
				pd.dismiss();
				if (result == null || "".equals(result))
					return;
				int serverVersion;
				String versionName;
				try {
					JSONObject jo = new JSONObject(result);
					serverVersion = jo.getInt("versionCode");
					versionName = jo.getString("versionName");
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				if (packageInfo.versionCode < serverVersion) {
					final MyAlertDialog dialog = new MyAlertDialog(
							getActivity());
					dialog.setTitle("�����°汾��������������ʹ�á�");
					dialog.setMessage("����ǰ�汾��" + packageInfo.versionName
							+ "\n���°汾��" + versionName
							+ "\n1.���� XXX����\n2.�����û�����\n3.�޸�XXXbug");
					dialog.setNegativeButton("ȡ��", new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.setPositiveButton("����", new OnClickListener() {
						public void onClick(View v) {
							Intent updateIntent = new Intent(getActivity(),
									AppUpgradeService.class);
							updateIntent
									.putExtra(
											"downloadUrl",
											IConstants.APP_DOWNLOAD_PATH);
							// TODO
							updateIntent
									.putExtra(
											"app_name",
											getResources()
													.getString(
															com.icanit.app_v2.R.string.app_name));
							getActivity().startService(updateIntent);
							dialog.dismiss();
						}
					});

				} else {
					Toast.makeText(getActivity(), "���İ汾�����°汾,����Ҫ����",
							Toast.LENGTH_SHORT).show();
				}
			}

		}.execute();

	}

	public void feedBackSuggestion() {
		Toast.makeText(getActivity(), "����", Toast.LENGTH_SHORT).show();
		// startActivity(new Intent(this,
		// SettingSuggestionActivity.class));

		FeedbackAgent agent = new FeedbackAgent(getActivity());
		agent.sync();
		agent.startFeedbackActivity();

	}

	public void help() {
		Intent intent = new Intent(getActivity(), SettingGuideActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	public void privacy() {
		Intent intent = new Intent(getActivity(),
				SettingPrivacyWebViewActivity.class);
		startActivity(intent);

	}

	public void shareApp() {
		if (shareDialog == null) {
			shareDialog = CustomizedDialog
					.initShareDialog("����", getActivity());
			shareDialog.gv.setAdapter(shareListAdapter = new ShareListAdapter(
					getActivity()));
			shareDialog.gv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if (position == 0) {
						AppUtil.shareToMB(PlatformType.SINAMINIBLOG, getActivity());
					} else if (position == 1) {
						AppUtil.shareToMB(PlatformType.QQMB, getActivity());
					} else {
						AppUtil.shareToOthers(getActivity(), shareListAdapter.activityList.get(position));
					}
					shareDialog.dismiss();
				}
			});
		}
		shareDialog.show();
	}

	public void about() {
		Intent intent = new Intent(getActivity(), SettingAboutActivity.class);
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		Map<Integer, Boolean> mSelectedIndex = adapter_1.getSelectedIndex();
		if (mSelectedIndex != null) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			sp.edit().putBoolean("receivePicture", mSelectedIndex.get(1))
					.commit();
			sp.edit().putBoolean("displayDynamic", mSelectedIndex.get(2))
					.commit();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// �������
	}

	private String getPackageStats() {
		return CommonUtil.formatFileSize(DataCleanManager.getApplicationCacheBytes(getActivity()));
	}

	private Map<Integer, Boolean> getPreferences() {
		Map<Integer, Boolean> mSelectedIndex = new HashMap<Integer, Boolean>();
		boolean status = PreferenceManager
				.getDefaultSharedPreferences(mContext).getBoolean(
						"receivePicture", true);
		mSelectedIndex.put(1, status);
		status = PreferenceManager.getDefaultSharedPreferences(mContext)
				.getBoolean("displayDynamic", false);
		mSelectedIndex.put(2, status);
		return mSelectedIndex;
	}

	

}
