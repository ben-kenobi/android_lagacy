package com.what.yunbao.setting;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.common.VeriCodeSender;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.what.yunbao.R;

public class SettingAccManaChangeTelActivity extends Activity implements OnClickListener{
	
	private ImageButton backButton;
	
	private Button sendVeriCodeToFormerPhone,sendVeriCodeToNewPhone,submit,contactCustomService;
	
	private EditText formerPhone,formerPhoneVeriCode,newPhone,newPhoneVeriCode;
	
	private int resId=R.layout.setting_account_change_phonenum;
	
	private VeriCodeSender formerPhoneVeriCodeSender,newPhoneVeriCodeSender;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		init();bindListeners();
		
	}
	private void init() {
		backButton=(ImageButton)findViewById(R.id.ib_setting_accmana_changetel_back);
		sendVeriCodeToFormerPhone=(Button)findViewById(R.id.button3);
		sendVeriCodeToNewPhone=(Button)findViewById(R.id.button4);
		submit=(Button)findViewById(R.id.button1);
		contactCustomService=(Button)findViewById(R.id.button2);
		formerPhone=(EditText)findViewById(R.id.editText1);
		formerPhoneVeriCode=(EditText)findViewById(R.id.editText2);
		newPhone=(EditText)findViewById(R.id.editText3);
		newPhoneVeriCode=(EditText)findViewById(R.id.editText4);
	}
	private void bindListeners() {
		backButton.setOnClickListener(this);
		sendVeriCodeToFormerPhone.setOnClickListener(this);
		sendVeriCodeToNewPhone.setOnClickListener(this);
		submit.setOnClickListener(this);
		contactCustomService.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v==backButton){
			finish();
		}else if(v==sendVeriCodeToFormerPhone){
			if(checkFormerPhoneNum()){
				if(formerPhoneVeriCodeSender==null)
					formerPhoneVeriCodeSender=new VeriCodeSender(UriConstants.RESEND_VERICODE,
							IConstants.VERI_CODE, sendVeriCodeToFormerPhone);
				formerPhoneVeriCodeSender.setMessage("phone="+formerPhone.getText().
						toString()+"&source=14");
				IConstants.THREAD_POOL.submit(formerPhoneVeriCodeSender);
			}
		}else if(v==sendVeriCodeToNewPhone){
			if(checkNewPhoneNum()){
				if(newPhoneVeriCodeSender==null)
					newPhoneVeriCodeSender=new VeriCodeSender(UriConstants.SEND_VERICODE_TEMP_TONEWPHONE,
							IConstants.VERI_CODE,sendVeriCodeToNewPhone);
				newPhoneVeriCodeSender.setMessage("phone="+AppUtil.getLoginPhoneNum()+
						"&password="+AppUtil.getLoginUser().password+
						"&newPhone="+newPhone.getText().toString());
				IConstants.THREAD_POOL.submit(newPhoneVeriCodeSender);
			}
		}else if(v==submit){
			if(submitCheck()){
				submitInfo();
			}
		}else if(v==contactCustomService){
			startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+IConstants.CUSTOM_SERVICE))
			.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
		}
	}
	private boolean  checkFormerPhoneNum(){
		boolean b= false;
		String phoneNum=formerPhone.getText().toString();
		if(phoneNum.equals(AppUtil.getLoginPhoneNum()))
			b=true;
		else {
			AppUtil.toast("原号码错误");
			formerPhone.requestFocus();
			formerPhone.setSelection(formerPhone.length());
		}
		return b;
	}
	private boolean checkNewPhoneNum(){
		boolean b = false;
		String phoneNum=newPhone.getText().toString();
		if(AppUtil.isPhoneNum(phoneNum))
			b=true;
		else {
			AppUtil.toast("新号码格式错误");
			newPhone.requestFocus();
			newPhone.setSelection(newPhone.length());
		}
		return b;
	}
	private boolean submitCheck(){
		boolean b = false;
		if(checkFormerPhoneNum()&&checkNewPhoneNum()){
			if(!AppUtil.isVeriCode(formerPhoneVeriCode.getText().toString())){
				AppUtil.toast("原号码的验证码格式错误");
				formerPhoneVeriCode.requestFocus();
				formerPhoneVeriCode.setSelection(formerPhoneVeriCode.length());
			}else if(!AppUtil.isVeriCode(newPhoneVeriCode.getText().toString())){
				AppUtil.toast("新号码的验证码格式错误");
				newPhoneVeriCode.requestFocus();
				newPhoneVeriCode.setSelection(newPhoneVeriCode.length());
			}else
				b=true;
		}
		return b;
	}
	
	public void submitInfo(){
		final ProgressDialog pd= DialogUtil.createProgressDialogNshow(null,"正在提交信息。。", false, this);
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("newPhone",newPhone.getText().toString()));
				params.add(new BasicNameValuePair("phone",AppUtil.getLoginUser().phone));
				params.add(new BasicNameValuePair("veriCode",formerPhoneVeriCode.getText().toString()));
				params.add(new BasicNameValuePair("password",AppUtil.getLoginUser().password));
				params.add(new BasicNameValuePair("newVericode",newPhoneVeriCode.getText().toString()));
				String jsonResp=null;
				try {
					 jsonResp=AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.CHANGE_ACCOUNT_PHONENUM, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				final String resp=jsonResp;
				IConstants.MAIN_HANDLER.post(new Runnable(){
					public void run(){
						pd.dismiss();
						String message=null,newPhone=null;
						boolean success=false;
						if(resp==null)
							message="连接失败，检查网络";
						else
						try {
							JSONObject jo=new JSONObject(resp);
							if(success=jo.getBoolean(IConstants.RESPONSE_SUCCESS)){
								newPhone=jo.getString("newPhone");
								AppUtil.afterModifyUserInfoSuccess(newPhone, null);
							}
							else
								message=jo.getString(IConstants.RESPONSE_DESC);
						} catch (JSONException e) {
							e.printStackTrace();
							message="数据解析异常";
						}
						CustomizedDialog dialog=CustomizedDialog.initDialog("结果通知",success?"修改成功":"修改失败\n"+message,
								null,0,SettingAccManaChangeTelActivity.this);
						final boolean result=success;
						dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if(result)
									finish();
							}
						});
						dialog.show();
					}
				});
				
			}
		});
		
	}
}
