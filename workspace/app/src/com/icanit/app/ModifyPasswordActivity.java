package com.icanit.app;

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

import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;

public class ModifyPasswordActivity extends Activity {
	private ImageButton backButton, oldPwdDisposer, newpwdDisposer, repwdDisposer;
	private Button submit;
	private EditText oldPwd, newpwd, repwd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		init();
		bindListeners();
	}

	private void init() {
		backButton = (ImageButton) findViewById(R.id.imageButton4);
		oldPwdDisposer = (ImageButton) findViewById(R.id.imageButton1);
		newpwdDisposer = (ImageButton) findViewById(R.id.imageButton2);
		repwdDisposer = (ImageButton) findViewById(R.id.imageButton3);
		submit = (Button) findViewById(R.id.button1);
		oldPwd = (EditText) findViewById(R.id.editText1);
		newpwd = (EditText) findViewById(R.id.editText2);
		repwd = (EditText) findViewById(R.id.editText3);

	}

	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(oldPwd,oldPwdDisposer);
		AppUtil.bindEditTextNtextDisposer(newpwd, newpwdDisposer);
		AppUtil.bindEditTextNtextDisposer(repwd, repwdDisposer);
		submit.setOnClickListener(new OnSubmitButtonClickListener());
	}
	
	private  class OnSubmitButtonClickListener implements OnClickListener{
		private String oldpasswordTx,newpasswordTx,repasswordTx,phoneTx;
		public void onClick(View v) {
			oldpasswordTx=oldPwd.getText().toString();
			newpasswordTx=newpwd.getText().toString();
			repasswordTx=repwd.getText().toString();
			phoneTx=AppUtil.getLoginPhoneNum();
					
			if(!AppUtil.isPassword(oldpasswordTx)){
				Toast.makeText(ModifyPasswordActivity.this, "输入的原密码格式不对", Toast.LENGTH_SHORT).show();
				return ;
			}
			if(!AppUtil.isPassword(newpasswordTx)){
				Toast.makeText(ModifyPasswordActivity.this, "输入的新密码格式不对", Toast.LENGTH_SHORT).show();
				return ;
			}
			if(!repasswordTx.equals(newpasswordTx)){
				Toast.makeText(ModifyPasswordActivity.this, "输入密码不一致,重新输入", Toast.LENGTH_SHORT).show();
				return;
			}
			String message = "phone="+phoneTx+"&newpassword="+newpasswordTx+"&oldpassword="+oldpasswordTx;
			final ProgressDialog pd = new ProgressDialog(ModifyPasswordActivity.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在提交信息。。");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @modifyPasswordActivity2 asyncTask doinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.MODIFY_PASSWORD, params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(ModifyPasswordActivity.this);
						builder.setTitle("结果通知");
						if(jo.getBoolean("success")){
							Map<String,Object> loginUser = AppUtil.getLoginUser();
							loginUser.put(IConstants.PASSWORD,newpasswordTx);
							builder.setMessage("修改成功");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(ModifyPasswordActivity.this,HomeActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_LOGIN);
									startActivity(intent);
								}
							});
						}else{
							builder.setMessage("修改失败\n 原密码错误");
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
