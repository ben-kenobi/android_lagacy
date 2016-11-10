package com.icanit.app_v2.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;

public class RetrievePasswordActivity2 extends Activity {
	private ImageButton backButton;
	private Button submit, resend, veriCodeDisposer, pwdDisposer,
			repwdDisposer;
	private EditText veriCode, pwd, repwd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_password_secondstep);
		init();
		bindListeners();
	}

	private void init() {
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		veriCodeDisposer = (Button) findViewById(R.id.button3);
		pwdDisposer = (Button) findViewById(R.id.button4);
		repwdDisposer = (Button) findViewById(R.id.button5);
		submit = (Button) findViewById(R.id.button1);
		veriCode = (EditText) findViewById(R.id.editText1);
		pwd = (EditText) findViewById(R.id.editText2);
		repwd = (EditText) findViewById(R.id.editText3);
		resend = (Button) findViewById(R.id.button2);

	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(veriCode, veriCodeDisposer);
		AppUtil.bindEditTextNtextDisposer(pwd, pwdDisposer);
		AppUtil.bindEditTextNtextDisposer(repwd, repwdDisposer);
		submit.setOnClickListener(new OnSubmitButtonClickListener());
		resend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IConstants.THREAD_POOL.submit(new Runnable() {
					public void run() {
						try {
							final String response = AppUtil
									.getNetUtilInstance()
									.sendMessageWithHttpPost(
											UriConstants.RESEND_VERICODE,
											"phone="
													+ AppUtil
															.getFromApplication(IConstants.PHONE)
													+ "&source=12");
							if (response != null || response.startsWith("{")) {
								IConstants.MAIN_HANDLER.post(new Runnable() {
									public void run() {
										try {
											JSONObject jo = new JSONObject(response);
											AppUtil.toast(jo.getString(IConstants.VERI_CODE));
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
			}
		});
	}

	private class OnSubmitButtonClickListener implements OnClickListener {
		private String veriCodeTx, passwordTx, repasswordTx, phoneTx;

		public void onClick(View v) {
			veriCodeTx = veriCode.getText().toString();
			passwordTx = pwd.getText().toString();
			repasswordTx = repwd.getText().toString();
			phoneTx = (String) AppUtil.getFromApplication(IConstants.PHONE);
			if (!AppUtil.isVeriCode(veriCodeTx)) {
				Toast.makeText(RetrievePasswordActivity2.this, "错误验证码格式",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!AppUtil.isPassword(passwordTx)) {
				Toast.makeText(RetrievePasswordActivity2.this, "输入的密码格式不对",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!repasswordTx.equals(passwordTx)) {
				Toast.makeText(RetrievePasswordActivity2.this, "输入密码不一致,重新输入",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String message = "phone=" + phoneTx + "&password=" + passwordTx
					+ "&veriCode=" + veriCodeTx;
			final ProgressDialog pd = new ProgressDialog(
					RetrievePasswordActivity2.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在提交信息。。");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};

				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",
								params[0]
										+ " @RetrievePasswordActivity2 asyncTaskdoinBackground");
						return AppUtil
								.getNetUtilInstance()
								.sendMessageWithHttpPost(
										UriConstants.RETRIEVE_AND_MODIFY_PASSWORD,
										params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						if (result == null)
							return;
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(
								RetrievePasswordActivity2.this);
						builder.setTitle("结果通知");
						if (jo.getBoolean("success")) {
							builder.setMessage("修改成功,返回登录界面");
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											AppUtil.removeFromApplication(IConstants.PHONE);
											Intent intent = new Intent(
													RetrievePasswordActivity2.this,
													(Class) AppUtil
															.getFromApplication(IConstants.DESTINATION_AFTER_RETRIEVEPASSWORD))
													.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
											AppUtil.removeFromApplication(IConstants.DESTINATION_AFTER_RETRIEVEPASSWORD);
										}
									});
						} else {
							builder.setMessage("修改失败\n"
									+ jo.getString("message"));
							builder.setPositiveButton("确定", null);
						}
						builder.create().show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.execute(message);
		}

	}
}
