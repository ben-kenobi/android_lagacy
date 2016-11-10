package com.icanit.app;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.service.DataService;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;

public class VerifyUserAccountActivity  extends Activity{
	private ImageButton backButton,textDisposer;
	private Button affirmative,resend;
	private EditText editText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useraccount_verify);
		init();bindListeners();
	}
	private void init(){
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		affirmative=(Button)findViewById(R.id.button1);
		textDisposer=(ImageButton)findViewById(R.id.imageButton2);
		editText=(EditText)findViewById(R.id.editText1);
		resend=(Button)findViewById(R.id.button2);
		
	}
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		resend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DataService.THREAD_POOL.submit(new Runnable(){
					public void run(){
						String phone=null;
						if(IConstants.VERIFY_AFTER_LOGIN.equals(getIntent().getAction()))
							phone=AppUtil.getLoginPhoneNum();
						else
							phone=((Map<String,Object>)AppUtil.getFromApplication("user")).get("phone").toString();
						try {
							String response=AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.RESEND_VERICODE, "phone="+phone);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);
		OnClickListener listener =null;
		if(IConstants.VERIFY_AFTER_LOGIN.equals(getIntent().getAction())){
			listener=new OnSubmitButtonClickListener1();
		}else{
			listener=new OnSubmitButtonClickListener0();
		}
		affirmative.setOnClickListener(listener);
	}
	private class OnSubmitButtonClickListener1 implements OnClickListener{
		String veriCodeTx,phoneTx,passwordTx,message;
		{
			Map<String,Object> user =AppUtil.getLoginUser();
			phoneTx=user.get("phone").toString();passwordTx=user.get("password").toString();
		}
		public void onClick(View v) {
			veriCodeTx=editText.getText().toString();
			if(!AppUtil.isVeriCode(veriCodeTx)){
				Toast.makeText(VerifyUserAccountActivity.this, "错误验证码格式", Toast.LENGTH_SHORT).show();
				return;
			}
			message = "phone="+phoneTx+"&password="+passwordTx+"&veriCode="+veriCodeTx;
			final ProgressDialog pd = new ProgressDialog(VerifyUserAccountActivity.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在验证。。");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @VerifyUserAccountActivity asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.USER_ACCOUNT_VERIFY, params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(VerifyUserAccountActivity.this);
						builder.setTitle("结果通知");
						if(jo.getBoolean("success")){
							AppUtil.removeFromApplication(IConstants.LOGIN_USER_VERIFICATION);
							builder.setMessage("验证成功 \n返回?");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(VerifyUserAccountActivity.this,HomeActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_LOGIN));
								}
							});
						}else{
							builder.setMessage("验证失败\n具体原因不明");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});
						}
						builder.create().show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.execute(message);
		}
	}
	private class OnSubmitButtonClickListener0 implements OnClickListener{
		String veriCodeTx,phoneTx,passwordTx,message;
		{
			Map<String,Object> user = (Map<String,Object>)AppUtil.getFromApplication("user");
			phoneTx=user.get("phone").toString();passwordTx=user.get("password").toString();
		}
		public void onClick(View v) {
			veriCodeTx=editText.getText().toString();
			if(!AppUtil.isVeriCode(veriCodeTx)){
				Toast.makeText(VerifyUserAccountActivity.this, "错误验证码格式", Toast.LENGTH_SHORT).show();
				return;
			}
			message = "phone="+phoneTx+"&password="+passwordTx+"&veriCode="+veriCodeTx;
			final ProgressDialog pd = new ProgressDialog(VerifyUserAccountActivity.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在验证。。");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @VerifyUserAccountActivity asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.USER_ACCOUNT_VERIFY, params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						JSONObject jo = new JSONObject(result);
						AlertDialog.Builder builder = new AlertDialog.Builder(VerifyUserAccountActivity.this);
						builder.setTitle("结果通知");
						if(jo.getBoolean("success")){
							AppUtil.removeFromApplication("user");
							builder.setMessage("验证成功 \n直接登录?");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									DataService.THREAD_POOL.submit(new Runnable(){
										public void run() {
											try {
												String response=AppUtil.loginOnServer(phoneTx, passwordTx);
												if(response!=null&&!"".equals(response)){
													boolean b = AppUtil.loginOnClient(phoneTx, passwordTx, response);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
									startActivity(new Intent(VerifyUserAccountActivity.this,HomeActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_LOGIN));
								}
							});
							builder.setNegativeButton("不了", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(VerifyUserAccountActivity.this,LoginActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
								}
							});
						}else{
							builder.setMessage("验证失败\n具体原因不明");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});
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
