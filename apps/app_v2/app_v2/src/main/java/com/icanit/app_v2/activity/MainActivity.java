package com.icanit.app_v2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.fragment.AbstractRadioBindFragment;
import com.icanit.app_v2.fragment.Main_community_Fragment;
import com.icanit.app_v2.fragment.Main_settings_Fragment;
import com.icanit.app_v2.fragment.Main_usercentral_Fragment;
import com.icanit.app_v2.fragment.Main_userlogin_Fragment;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;

public class MainActivity extends FragmentActivity {
	private int containerId=R.id.frameLayout1;
	private FrameLayout container;
	private RadioGroup radioGroup;
	private RadioButton rb0, rb1, rb2, rb3, rb4;
	private AbstractRadioBindFragment home_bottomtab00Fragment,
			home_bottomtab01Fragment, home_bottomtab02Fragment,
			home_bottomtab03Fragment, home_bottomtab04Fragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			AppUtil.loadShoppingCartInfo();
			init();
			initRadios();
			home_bottomtab02Fragment = new Main_community_Fragment();
			home_bottomtab02Fragment.setTrigger(rb2);
			getSupportFragmentManager().beginTransaction()
					.add(containerId, home_bottomtab02Fragment).commit();
			AppUtil.checkAutoLoginIf();
			shortcutPrompt();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	private void shortcutPrompt() {
		if (AppUtil.getSharedPreferencesUtilInstance().getShortcutPromptStatus()) {
			final CustomizedDialog dialog=CustomizedDialog.initDialog("提示", "是否添加到桌面快捷方式?",
					"下次不再提醒", 0,this).setPositiveButton("添加", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface idialog, int which) {
					AppUtil.addShortcutToDesktop(MainActivity.this);
					AppUtil.getSharedPreferencesUtilInstance().setShortcutPromptStatus(false);
				}
			});
			dialog.setNegativeButton("不了",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface idialog, int which) {
					AppUtil.getSharedPreferencesUtilInstance().setShortcutPromptStatus(!dialog.getCheckedTextStatus());
				}
			});
			dialog.show();
		}
	}
	protected void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if(IConstants.LEAD_TO_MERCHANT_LIST.equals(action)){
			if (home_bottomtab02Fragment == null)
				createFragment(R.id.radio2);
			if (!home_bottomtab02Fragment.isResumed())
				getSupportFragmentManager()
						.beginTransaction()
						.replace(containerId,
								home_bottomtab02Fragment, null)
						.addToBackStack(null).commitAllowingStateLoss();
		}
	}

	/*private String getAuthorityThroughPermission(Context context, String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packs =context.getPackageManager().getInstalledPackages(
				PackageManager.GET_PERMISSIONS|PackageManager.GET_PROVIDERS|
				PackageManager.GET_URI_PERMISSION_PATTERNS|PackageManager.GET_ACTIVITIES|PackageManager.GET_GIDS);
		if (packs == null) {
			return null;
		}
		for (PackageInfo pack : packs) {
			if(!pack.packageName.contains("launcher")&&!pack.packageName.contains("app_v2")) continue;
			System.out.println("packName="+pack.packageName+",permission="+Arrays.toString(pack.permissions)+
					"|"+Arrays.toString(pack.requestedPermissions)+"   @MainActivity");
			System.out.println("appPermission="+pack.applicationInfo.permission+",uid="+Arrays.toString(pack.gids)+"  @MainActivity");
			ProviderInfo[] providers = pack.providers;
			if (providers != null) {
				for (ProviderInfo provider : providers) {
					provider.exported=true;provider.grantUriPermissions=true;
					System.out.println("providerName="+provider.name+",permission="+provider.readPermission+"|"+provider.writePermission+
							",authority="+provider.authority+",uriPermission="+provider.grantUriPermissions+
							"|"+provider.exported+"|"+Arrays.toString(provider.pathPermissions)+",pattern="+provider.uriPermissionPatterns+"   -------------@MainActivity");
					if (permission.equals(provider.readPermission)
							|| permission.equals(provider.writePermission)) {
						return provider.authority;
					}
				}
			}
		}
		return null;
	}

	private boolean hasInstallShortcut() {
		System.out.println("authority="+getAuthorityThroughPermission(getApplicationContext(), "com.android.launcher.permission.READ_SETTINGS")+"  @MainActivity");
		boolean isInstallShortcut = false;
		final ContentResolver cr = getContentResolver();
		final Uri CONTENT_URI = Uri
				.parse("content://com.sonyericsson.tvlauncher.provider1/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI, null, null, null, null);
		System.out.println(c + "  @MainActivity");
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;

			System.out.println("已创建");
			c.moveToFirst();
			do {
				for (int i = 0; i < c.getColumnCount(); i++)
					System.out.print(c.getColumnName(i) + "=" + c.getString(i)
							+ ",  ");
				System.out.println();
			} while (c.moveToNext());

		}
		System.out.println(isInstallShortcut + "  @MainActivity");
		return isInstallShortcut;
		// boolean hasInstall = false;
		// Uri CONTENT_URI =
		// Uri.parse("content://com.android.launcher.settings/favorites?notify=true");
		// Cursor cursor = this.getContentResolver().query(CONTENT_URI,new
		// String[] { "title", "iconResource"},
		// "title=?",new String[] { this.getString(R.string.app_name) }, null);
		// if (cursor != null && cursor.getCount() > 0) {
		// hasInstall = true;
		// }
		// System.out.println(hasInstall+"  @MainActivity");
		// return hasInstall;
	}*/
	public void toHome_bottomtab03Fragment(AbstractRadioBindFragment fragment){
		createFragment(R.id.radio3);
		if (!home_bottomtab03Fragment.isResumed())
			getSupportFragmentManager().beginTransaction()
					.replace(containerId,home_bottomtab03Fragment,null).addToBackStack(null).commit();
	}

	public void setHome_bottomtab03Fragment(AbstractRadioBindFragment fragment) {
		getSupportFragmentManager().beginTransaction().remove(fragment).commit();
		createFragment(R.id.radio3);
		if (!home_bottomtab03Fragment.isResumed())
			getSupportFragmentManager().beginTransaction()
					.add(containerId,home_bottomtab03Fragment,null).addToBackStack(null).commit();
		
	}

	private void createFragment(int id) {
		switch (id) {
		case R.id.radio0:
			home_bottomtab00Fragment = new Main_community_Fragment();
			home_bottomtab00Fragment.setTrigger(rb0);
			break;
		case R.id.radio1:
			home_bottomtab01Fragment = new Main_community_Fragment();
			home_bottomtab01Fragment.setTrigger(rb1);
			break;
		case R.id.radio2:
			home_bottomtab02Fragment = new Main_community_Fragment();
			home_bottomtab02Fragment.setTrigger(rb2);
			break;
		case R.id.radio3:
			AppUser user = AppUtil.getLoginUser();
			if (user == null) {
				if (home_bottomtab03Fragment == null
						|| home_bottomtab03Fragment.getClass() != Main_userlogin_Fragment.class)
					home_bottomtab03Fragment = new Main_userlogin_Fragment();
			} else {
				if (home_bottomtab03Fragment == null
						|| home_bottomtab03Fragment.getClass() != Main_usercentral_Fragment.class)
					home_bottomtab03Fragment = new Main_usercentral_Fragment();
			}
			home_bottomtab03Fragment.setTrigger(rb3);
			break;
		case R.id.radio4:
			home_bottomtab04Fragment = new Main_settings_Fragment();
			home_bottomtab04Fragment.setTrigger(rb4);
			break;

		default:
			break;
		}
	}

	private void initRadios() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.radio0:
					if (home_bottomtab00Fragment == null) {
						createFragment(R.id.radio0);
					}
					if (!home_bottomtab00Fragment.isResumed())
						getSupportFragmentManager()
								.beginTransaction()
								.replace(containerId,
										home_bottomtab00Fragment, null)
								.addToBackStack(null).commit();
					break;
				case R.id.radio1:
					if (home_bottomtab01Fragment == null)
						createFragment(R.id.radio1);
					if (!home_bottomtab01Fragment.isResumed())
						getSupportFragmentManager()
								.beginTransaction()
								.replace(containerId,
										home_bottomtab01Fragment, null)
								.addToBackStack(null).commit();
					break;
				case R.id.radio2:
					if (home_bottomtab02Fragment == null)
						createFragment(R.id.radio2);
					if (!home_bottomtab02Fragment.isResumed())
						getSupportFragmentManager()
								.beginTransaction()
								.replace(containerId,
										home_bottomtab02Fragment, null)
								.addToBackStack(null).commit();
					break;
				case R.id.radio3:
					createFragment(R.id.radio3);
					if (!home_bottomtab03Fragment.isResumed()
							|| (home_bottomtab00Fragment != null && home_bottomtab00Fragment
									.isResumed())
							|| (home_bottomtab01Fragment != null && home_bottomtab01Fragment
									.isResumed())
							|| (home_bottomtab02Fragment != null && home_bottomtab02Fragment
									.isResumed())
							|| (home_bottomtab04Fragment != null && home_bottomtab04Fragment
									.isResumed()))
						getSupportFragmentManager()
								.beginTransaction()
								.replace(containerId,
										home_bottomtab03Fragment, null)
								.addToBackStack(null).commit();
					System.out.println("fragment02IsResume="+home_bottomtab02Fragment.isResumed()
							+ ",Fragment03IsResume=" + home_bottomtab03Fragment.isResumed()
							+ "  @MainActivity");
					break;
				case R.id.radio4:
					if (home_bottomtab04Fragment == null) {
						createFragment(R.id.radio4);
					}
					if (!home_bottomtab04Fragment.isResumed())
						getSupportFragmentManager()
								.beginTransaction()
								.replace(containerId,
										home_bottomtab04Fragment, null)
								.addToBackStack(null).commit();
					break;
				}
			}
		};
		rb0 = (RadioButton) findViewById(R.id.radio0);
		rb1 = (RadioButton) findViewById(R.id.radio1);
		rb2 = (RadioButton) findViewById(R.id.radio2);
		rb3 = (RadioButton) findViewById(R.id.radio3);
		rb4 = (RadioButton) findViewById(R.id.radio4);
		rb0.setOnClickListener(listener);
		rb1.setOnClickListener(listener);
		rb2.setOnClickListener(listener);
		rb3.setOnClickListener(listener);
		rb4.setOnClickListener(listener);
	}

	private void init() throws AppException {
		container = (FrameLayout) findViewById(containerId);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	}

	@Override
	protected void onDestroy() {
		Log.d("infoTag", "@HomePageActivity  onDestroy");
		AppUtil.updateShoppingCartDB();
		// AppUtil.deleteFileFromSDCard(newVersionName+IConstants.APP_NEWVERSION_FILE);
		AppUtil.clearAllCaches();
		super.onDestroy();
	}

}
