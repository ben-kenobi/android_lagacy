package com.icanit.app;

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

public class RetrievePasswordActivity1  extends Activity{
	private ImageButton backButton,textDisposer;
	private Button affirmative;
	private EditText editText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_password_firststep);
		init();bindListeners();
	}
	private void init(){
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		affirmative=(Button)findViewById(R.id.button1);
		textDisposer=(ImageButton)findViewById(R.id.imageButton2);
		editText=(EditText)findViewById(R.id.editText1);
		
	}
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);
		affirmative.setOnClickListener(new OnSubmitButtonClickListener());
	}
	private class OnSubmitButtonClickListener implements OnClickListener{
		String phoneTx,message;
		public void onClick(View v) {
			phoneTx=editText.getText().toString();
			if(!AppUtil.isPhoneNum(phoneTx)){
				Toast.makeText(RetrievePasswordActivity1.this, "号码格式错误", Toast.LENGTH_SHORT).show();
				return;
			}
			message = "phone="+phoneTx;
			final ProgressDialog pd = new ProgressDialog(RetrievePasswordActivity1.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在处理。。");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @RetrievePasswordActivity1 asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.RETRIEVE_PASSWORD, params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(RetrievePasswordActivity1.this);
						builder.setTitle("notice");
						if(jo.getBoolean("success")){
							builder.setMessage("验证码为：" +jo.getString("veriCodeTemp"));
							builder.setPositiveButton("下一步", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									AppUtil.putIntoApplication(IConstants.PHONE,phoneTx);
									startActivity(new Intent(RetrievePasswordActivity1.this,RetrievePasswordActivity2.class));
								}
							});
						}else{
							builder.setMessage("该号码未注册");
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
