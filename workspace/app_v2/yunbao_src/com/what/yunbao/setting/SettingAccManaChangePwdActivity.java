package com.what.yunbao.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.util.AppUtil;
import com.what.yunbao.R;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.HttpUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingAccManaChangePwdActivity extends Activity{
	private TextView temporary_tv;
	
	private EditText oldPwd_et;
	private EditText newPwd_et;
	private EditText newPwd2_et;
	private Button confirm_btn;
	private Toast mToast = null;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_account_change_password);
		setUpViews();
		findViewById(R.id.ib_setting_accmana_changepwd_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingAccManaChangePwdActivity.this.finish();
				
			}
		});
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		if(tm.getLine1Number() != null){
			temporary_tv.setText("未获取到本机手机号(使用下边测试帐号)\n+(号码imsi:"+tm.getSubscriberId()+")");
		}
		confirm_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//检测网络状态
			    if (!CommonUtil.isNetworkAvailable(SettingAccManaChangePwdActivity.this)) {
			        CommonUtil.showToast("对不起，您未连接网络", mToast);
			        return;
			    }
				String oldPwd = oldPwd_et.getText().toString();
				String newPwd = newPwd_et.getText().toString();
				String newPwd2 = newPwd2_et.getText().toString();
				
				if(TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)){
					CommonUtil.showToast("抱歉！密码不能为空", mToast);
					return;
				}
				if(!TextUtils.equals(newPwd, newPwd2)){
					CommonUtil.showToast("抱歉！两次新密码不相同", mToast);
					return;
				}
				else{
					try {
						AppUser user = AppUtil.getLoginUser();
						if(user==null) {
							CommonUtil.showToast("未登录", mToast);
							return;
						}
						String phone =user.phone;
						String[] arr = new String[]{phone,oldPwd, newPwd};
						new changePwdTask().execute(arr);
						showDialog();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
			}
		});

	}
	private void setUpViews(){
		oldPwd_et = (EditText) findViewById(R.id.et_old_pwd);
		newPwd_et = (EditText) findViewById(R.id.et_new_pwd);
		newPwd2_et = (EditText) findViewById(R.id.et_confirm_pwd);
		confirm_btn = (Button) findViewById(R.id.btn_confirm_modify);
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		
		temporary_tv = (TextView) findViewById(R.id.tv_temporary_username);
	}
	private void showDialog(){
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("正在修改密码...");
		pd.setCancelable(true);
		pd.show();
	}
   private class changePwdTask extends AsyncTask<String[], Void,Boolean>{
        @Override
        protected Boolean doInBackground(String[]... params) {
        	
        	String data = "";
        	boolean status = false;
			try{
				data = HttpUtil.changePwd(params[0][0], params[0][1], params[0][2]);
			}catch(Exception e){
				Log.e("Exception","修改密码异常");
				return status;
			}
			
			//解析				           
			JSONObject jObject;
			try {
				jObject = new JSONObject(data);
				status = jObject.getBoolean("success"); 	
			} catch (JSONException e) {
				e.printStackTrace();
			}
						
			return status;
        }
 
        @Override
        protected void onPostExecute(Boolean result) {
        	pd.dismiss();
        	if(result == false){
				CommonUtil.showToast("抱歉！输入密码错误", mToast);
				return;
			}else{	
				AppUtil.afterModifyUserInfoSuccess(null,newPwd_et.getText().toString());
				CommonUtil.showToast("修改密码成功", mToast);
				finish();
			}
        }
    }
}
