package com.icanit.app_v2.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.ui.CustomizedDialog;

public class DialogUtil {
	public static ProgressDialog createProgressDialogNshow(String title,
			String message,boolean cancelOnTouchOutside,Context context) {
		ProgressDialog pd = new ProgressDialog(context,
				ProgressDialog.STYLE_SPINNER);
		pd.setTitle(title);
		pd.setMessage(message);
		pd.setCanceledOnTouchOutside(cancelOnTouchOutside);
		pd.show();
		return pd;
	}

	public static void loginFlow(final Context context, final String phone,
			final String password, final boolean autoLogin,
			final Runnable runnable) {
		final ProgressDialog pd = createProgressDialogNshow("用户登录", "正在登录。。。",
				true,context);
		IConstants.THREAD_POOL.submit(new Runnable() {
			boolean b = false;
			public void run() {
				try {
					 b = AppUtil.loginOnServer(phone, password);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							pd.dismiss();
							if (b) {
								AppUtil.getSharedPreferencesUtilInstance()
										.saveLoginUserInfo(phone, password,
												autoLogin);
								AppUtil.appContext.loginUser.password=password;
								runnable.run();
							} else {
								CustomizedDialog dialog =CustomizedDialog.initDialog("通知","登录失败\n", null, 0, context);
								dialog.setPositiveButton("确定", null);
								dialog.show();
							}
						}
					});
				}
			}
		});

	}
}
