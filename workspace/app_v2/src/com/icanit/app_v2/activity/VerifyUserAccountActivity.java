package com.icanit.app_v2.activity;

import java.util.Map;

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
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;

public class VerifyUserAccountActivity  extends Activity{
	private ImageButton backButton;
	private Button affirmative,resend,textDisposer;
	private EditText editText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useraccount_verify);
		init();bindListeners();
	}
	private void init(){
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		affirmative=(Button)findViewById(R.id.button1);
		textDisposer=(Button)findViewById(R.id.button3);
		editText=(EditText)findViewById(R.id.editText1);
		resend=(Button)findViewById(R.id.button2);
		
	}
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);
		resend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IConstants.THREAD_POOL.submit(new Runnable(){
					public void run(){
						String phone=null;
						if(IConstants.VERIFY_AFTER_LOGIN.equals(getIntent().getAction()))
							phone=AppUtil.getLoginPhoneNum();
						else
							phone=((Map<String,Object>)AppUtil.getFromApplication("user")).get("phone").toString();
						try {
							String response=AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.SEND_VERICODE, "phone="+phone);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
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
			AppUser user =AppUtil.getLoginUser();
			phoneTx=user.phone;passwordTx=user.password;
		}
		public void onClick(View v) {
			veriCodeTx=editText.getText().toString();
			if(!AppUtil.isVeriCode(veriCodeTx)){
				Toast.makeText(VerifyUserAccountActivity.this, "错误验证码格式", Toast.LENGTH_SHORT).show();
				return;
			}
			message = "phone="+phoneTx+"&password="+passwordTx+"&veriCode="+veriCodeTx;
			final ProgressDialog pd = DialogUtil.createProgressDialogNshow("处理中。。。。。",
					"正在验证。。",true,VerifyUserAccountActivity.this);
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @VerifyUserAccountActivity asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.USER_ACCOUNT_VERIFY, params[0]);
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
//							AppUtil.getLoginUser().isverified=true;
							builder.setMessage("验证成功 \n返回?");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(VerifyUserAccountActivity.this,MainActivity.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_LOGIN));
								}
							});
						}else{
							builder.setMessage("验证失败\n具体原因不明");
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
			final ProgressDialog pd = DialogUtil.createProgressDialogNshow("处理中。。。。。",
					"正在验证。。",true,VerifyUserAccountActivity.this);
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @VerifyUserAccountActivity asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.USER_ACCOUNT_VERIFY, params[0]);
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
							AppUtil.getSharedPreferencesUtilInstance().saveLoginUserInfo(phoneTx, passwordTx, false);
							AppUtil.removeFromApplication("user");
							builder.setMessage("验证成功 \n直接登录?");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									DialogUtil.loginFlow(VerifyUserAccountActivity.this,
											phoneTx, passwordTx, false, new Runnable(){
										public void run(){
											startActivity(new Intent(VerifyUserAccountActivity.this,(Class)AppUtil.getFromApplication
													(IConstants.DESTINATION_AFTER_REGISTER))
											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_LOGIN));
											AppUtil.removeFromApplication(IConstants.DESTINATION_AFTER_REGISTER);
										}
									});
								}
							});
							builder.setNegativeButton("不了", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(VerifyUserAccountActivity.this,(Class)AppUtil.getFromApplication
											(IConstants.DESTINATION_AFTER_REGISTER))
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
									AppUtil.removeFromApplication(IConstants.DESTINATION_AFTER_REGISTER);
								}
							});
						}else{
							builder.setMessage("验证失败\n具体原因不明");
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
