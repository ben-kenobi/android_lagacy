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

import com.icanit.app.common.UriConstants;
import com.icanit.app.entity.EntityMapFactory;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;

public class RegisterActivity extends Activity {
	private ImageButton backButton, phoneDisposer, pwdDisposer, repwdDisposer;
	private Button submit;
	private EditText phone, pwd, repwd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
		bindListeners();
	}

	private void init() {
		backButton = (ImageButton) findViewById(R.id.imageButton4);
		phoneDisposer = (ImageButton) findViewById(R.id.imageButton1);
		pwdDisposer = (ImageButton) findViewById(R.id.imageButton2);
		repwdDisposer = (ImageButton) findViewById(R.id.imageButton3);
		submit = (Button) findViewById(R.id.button1);
		phone = (EditText) findViewById(R.id.editText1);
		pwd = (EditText) findViewById(R.id.editText2);
		repwd = (EditText) findViewById(R.id.editText3);

	}

	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(phone, phoneDisposer);
		AppUtil.bindEditTextNtextDisposer(pwd, pwdDisposer);
		AppUtil.bindEditTextNtextDisposer(repwd, repwdDisposer);
		submit.setOnClickListener(new OnSubmitButtonClickListener());
	}
	
	private  class OnSubmitButtonClickListener implements OnClickListener{
		private String phoneTx,passwordTx,repasswordTx;
		public void onClick(View v) {
			phoneTx=phone.getText().toString();
			passwordTx=pwd.getText().toString();
			repasswordTx=repwd.getText().toString();
			if(!AppUtil.isPhoneNum(phoneTx)){
				Toast.makeText(RegisterActivity.this, "����ĺ��벻�ǺϷ��ֻ���", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!AppUtil.isPassword(passwordTx)){
				Toast.makeText(RegisterActivity.this, "����������ʽ����", Toast.LENGTH_SHORT).show();
				return ;
			}
			if(!repasswordTx.equals(passwordTx)){
				Toast.makeText(RegisterActivity.this, "�������벻һ��,��������", Toast.LENGTH_SHORT).show();
				return;
			}
			String message = "phone="+phoneTx+"&password="+passwordTx;
			final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
			pd.setTitle("�����С���������");
			pd.setMessage("�����ύ��Ϣ����");
			AsyncTask<String, Void, String> at = new AsyncTask<String, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(String... params) {
					try {
						Log.i("infoTag",params[0]+" @registerActivity asyncTaskdoinBackground");
						return AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.USER_REGISTER, params[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					try {
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(RegisterActivity.this);
						builder.setTitle("���֪ͨ");
						if(jo.getBoolean("success")){
							AppUtil.putIntoApplication("user", EntityMapFactory.generateUser(phoneTx, passwordTx));
							builder.setMessage("��һ��\nȷ����֤��"+jo.getString("veriCode"));
							builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(RegisterActivity.this,VerifyUserAccountActivity.class);
									startActivity(intent);
								}
							});
						}else{
							builder.setMessage("ע��ʧ��\n����ԭ����");
							builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
