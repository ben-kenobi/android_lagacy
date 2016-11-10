package com.what.weibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bshare.BShare;
import com.bshare.core.PlatformType;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.util.AppUtil;
import com.sina.weibo.sdk.log.Log;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class WeiboMainActivity extends Activity {

	private Weibo mWeibo;

	public Oauth2AccessToken accessToken;
	private String tecent_accessToken;

	/**
	 * SsoHandler 仅当sdk支持sso时有效，
	 */
	private SsoHandler mSsoHandler;
	// 绑定按钮
	private TextView sina_bind_tv;
	private TextView tecent_bind_tv;

	private LinearLayout sina_share_ll;
	private LinearLayout tecent_share_ll;

	private JSONObject o;
	private Context mContext;

	public static final String BSHARE_SAVE = "com.icanit.app_v2bshare_save",
			TENCENT_USERNAME = "qqmb_user_name",
			SINA_USERNAME = "sinaminiblog_user_name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_main);
		mContext = this.getApplicationContext();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		mWeibo = Weibo.getInstance(Constants.SINA_APP_KEY,
				Constants.SINA_REDIRECT_URL, Constants.SINA_SCOPE);
		// 新浪微博accessToken
		accessToken = AccessTokenKeeper.readAccessToken(this);
		// 腾讯微博accessToken
		tecent_accessToken = Util.getSharePersistent(getApplicationContext(),
				"ACCESS_TOKEN");
		setUpView();

		// 触发sso
		sina_share_ll.setOnClickListener(new MyOnClickListener());
		// 检查是否已经绑定
		checkBind();

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkBind();
	}

	private void setUpView() {
		try {
			AppUser user = AppUtil.getLoginUser();
			if (user != null) {
				((TextView) findViewById(R.id.textView4)).setText("昵称："
						+ (user.username == null ? "" : user.username));
				((TextView) findViewById(R.id.textView5)).setText("号码："
						+ (user.phone == null ? "" : user.phone));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sina_bind_tv = (TextView) findViewById(R.id.tv_sina_bind);
		tecent_bind_tv = (TextView) findViewById(R.id.tv_tecent_bind);

		MyOnClickListener myListener = new MyOnClickListener();

		sina_share_ll = (LinearLayout) findViewById(R.id.ll_sina_sso);
		sina_share_ll.setOnClickListener(myListener);
		tecent_share_ll = (LinearLayout) findViewById(R.id.ll_tecent_sso);
		tecent_share_ll.setOnClickListener(myListener);

		findViewById(R.id.ll_setting_accmana_changephone).setOnClickListener(
				myListener);
		findViewById(R.id.ll_setting_accmana_changepwd).setOnClickListener(
				myListener);
		findViewById(R.id.ib_weibo_back).setOnClickListener(myListener);

	}

	private void checkBind() {
		List<PlatformType> list = BShare.instance(WeiboMainActivity.this)
				.getBindedAccount(WeiboMainActivity.this);
		if (list.contains(PlatformType.QQMB)) {
			tecent_bind_tv.setText(getSharedPreferences(BSHARE_SAVE, 0)
					.getString(TENCENT_USERNAME, "已绑定"));
		} else {
			tecent_bind_tv.setText("未绑定");
		}
		if (list.contains(PlatformType.SINAMINIBLOG)) {
			sina_bind_tv.setText(getSharedPreferences(BSHARE_SAVE, 0)
					.getString(SINA_USERNAME, "已绑定"));
		} else {
			sina_bind_tv.setText("未绑定");
		}
		/**
		 * if (accessToken.isSessionValid()) {
		 * sina_bind_tv.setText(AccessTokenKeeper
		 * .getUserName(WeiboMainActivity.this)); } else {
		 * sina_bind_tv.setText("未绑定"); } //更新腾讯accessToken tecent_accessToken =
		 * Util.getSharePersistent(getApplicationContext(), "ACCESS_TOKEN");
		 * if("".equals(tecent_accessToken)){ tecent_bind_tv.setText("未绑定");
		 * }else{
		 * tecent_bind_tv.setText(Uri.decode(Util.getSharePersistent(this,
		 * "NAME"))); }
		 */

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.ll_sina_sso) {
				List<PlatformType> list = BShare.instance(
						WeiboMainActivity.this).getBindedAccount(
						WeiboMainActivity.this);
				if (list.contains(PlatformType.SINAMINIBLOG)) {
					try {
						Class.forName("com.icanit.app_v2.util.AppUtil")
								.getMethod(
										"removeMBCredential",
										new Class[] { String.class,
												Context.class, TextView.class,
												PlatformType.class })
								.invoke(null,
										new Object[] { "确定取消绑定新浪微博吗？",
												WeiboMainActivity.this,
												sina_bind_tv,
												PlatformType.SINAMINIBLOG });
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					BShare.instance(WeiboMainActivity.this).tryAuthorize(
							getApplicationContext(), PlatformType.SINAMINIBLOG,
							null);
				}

				/*
				 * if (accessToken.isSessionValid()) {
				 * 
				 * final WeiboAlertDialog dialog = new
				 * WeiboAlertDialog(WeiboMainActivity.this);
				 * dialog.setTitle("确定取消绑定新浪微博吗？");
				 * dialog.setNegativeButton("取消", new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { dialog.dismiss(); }
				 * }); dialog.setPositiveButton("确定", new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { //注销新浪微博
				 * AccessTokenKeeper.clear(WeiboMainActivity.this);
				 * sina_bind_tv.setText("未绑定"); accessToken =
				 * AccessTokenKeeper.readAccessToken(WeiboMainActivity.this);
				 * dialog.dismiss(); } }); } else { mSsoHandler = new
				 * SsoHandler(WeiboMainActivity.this, mWeibo);
				 * mSsoHandler.authorize(new AuthDialogListener(),null); }
				 */
			} else if (v.getId() == R.id.ll_tecent_sso) {
				List<PlatformType> list = BShare.instance(
						WeiboMainActivity.this).getBindedAccount(
						WeiboMainActivity.this);
				if (list.contains(PlatformType.QQMB)) {
					try {
						Class.forName("com.icanit.app_v2.util.AppUtil")
								.getMethod(
										"removeMBCredential",
										new Class[] { String.class,
												Context.class, TextView.class,
												PlatformType.class })
								.invoke(null,
										new Object[] { "确定取消绑定腾讯微博吗？",
												WeiboMainActivity.this,
												tecent_bind_tv,
												PlatformType.QQMB });
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					BShare.instance(WeiboMainActivity.this).tryAuthorize(
							WeiboMainActivity.this, PlatformType.QQMB, null);
				}
				/*
				 * if("".equals(tecent_accessToken)){
				 * auth(Integer.valueOf(Constants.TECENT_APP_KEY),
				 * Constants.TECENT_APP_KEY_SEC);
				 * 
				 * }else{ final WeiboAlertDialog dialog = new
				 * WeiboAlertDialog(WeiboMainActivity.this);
				 * dialog.setTitle("确定取消绑定腾讯微博吗？");
				 * dialog.setNegativeButton("取消", new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { dialog.dismiss(); }
				 * }); dialog.setPositiveButton("确定", new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { //注销腾讯微博
				 * Util.clearSharePersistent(WeiboMainActivity.this);
				 * tecent_bind_tv.setText("未绑定"); tecent_accessToken =
				 * Util.getSharePersistent(getApplicationContext(),
				 * "ACCESS_TOKEN"); dialog.dismiss(); } }); }
				 */
			} else if (v.getId() == R.id.ll_setting_accmana_changephone) {
				if (AppUtil.getLoginUser() == null) {
					AppUtil.toast("您还未登陆");
				} else {
					Intent intent = new Intent();
					intent.setClassName(WeiboMainActivity.this,
							"com.what.yunbao.setting.SettingAccManaChangeTelActivity");
					startActivity(intent);
				}
			} else if (v.getId() == R.id.ll_setting_accmana_changepwd) {
				if (AppUtil.getLoginUser() == null) {
					AppUtil.toast("您还未登陆");
				} else {
					Intent intent = new Intent();
					intent.setClassName(WeiboMainActivity.this,
							"com.what.yunbao.setting.SettingAccManaChangePwdActivity");
					startActivity(intent);
				}
			} else if (v.getId() == R.id.ib_weibo_back) {
				WeiboMainActivity.this.finish();
			}

		}

	}

	/**
	 * 新浪微博授权
	 */
	private class AuthDialogListener implements WeiboAuthListener {

		JSONObject o;
		OutputStreamWriter out = null;
		InputStream l_urlStream = null;

		@Override
		public void onComplete(Bundle values) {
			String code = values.getString("code");

			try {
				URL url = new URL("https://api.weibo.com/oauth2/access_token");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				out = new OutputStreamWriter(connection.getOutputStream(),
						"utf-8");
				out.write("client_id=" + Constants.SINA_APP_KEY
						+ "&client_secret=" + Constants.SINA_APP_KEY_SEC
						+ "&grant_type=authorization_code" + "&code=" + code
						+ "&redirect_uri=" + Constants.SINA_REDIRECT_URL);
				out.flush();
				out.close();
				String sCurrentLine;
				String sTotalString;
				sCurrentLine = "";
				sTotalString = "";

				l_urlStream = connection.getInputStream();
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(l_urlStream));
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}

				o = new JSONObject(sTotalString);
				Log.e("values:", values.toString());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.flush();
						out.close();
					}
					if (l_urlStream != null) {
						l_urlStream.close();
					}
				} catch (IOException e1) {
				}

			}

			try {
				// 更新新浪accessToken
				accessToken = new Oauth2AccessToken(
						o.getString("access_token"), o.getString("expires_in"));

				if (accessToken.isSessionValid()) {
					// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
					// .format(new java.util.Date(MainActivity.accessToken
					// .getExpiresTime()));
					// mText.setText("认证成功: \r\n access_token: " +
					// o.getString("access_token") + "\r\n"
					// + "expires_in: " + o.getString("expires_in") + "\r\n有效期："
					// + date);
					AccessTokenKeeper.keepAccessToken(WeiboMainActivity.this,
							accessToken);
					Toast.makeText(WeiboMainActivity.this, "认证成功",
							Toast.LENGTH_SHORT).show();
					sina_bind_tv.setText(AccessTokenKeeper
							.getUserName(WeiboMainActivity.this));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(WeiboMainActivity.this,
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(WeiboMainActivity.this, "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(WeiboMainActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * 腾讯微博授权
	 * 
	 * @param appid
	 * @param app_secket
	 */
	private void auth(long appid, String app_secket) {

		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			@Override
			public void onWeiBoNotInstalled() {
				// Toast.makeText(MainActivity.this, "onWeiBoNotInstalled",
				// 1000)
				// .show();
				Intent i = new Intent(mContext, Authorize.class);
				startActivity(i);
			}

			@Override
			public void onWeiboVersionMisMatch() {
				// Toast.makeText(MainActivity.this, "onWeiboVersionMisMatch",
				// 1000).show();
				Intent i = new Intent(mContext, Authorize.class);
				startActivity(i);
			}

			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(mContext, "result : " + result,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Toast.makeText(mContext, "passed", Toast.LENGTH_SHORT).show();
			}

		});
		AuthHelper.auth(this, "");

	}

	/**
	 * 新浪微博 sso 授权回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode + "," + resultCode + "," + data
				+ "   @WeiboMainActivity");
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
