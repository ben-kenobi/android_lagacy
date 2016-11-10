package com.icanit.app_v2.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.activity.MainActivity;
import com.icanit.app_v2.activity.RegisterActivity;
import com.icanit.app_v2.activity.RetrievePasswordActivity1;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.SharedPreferencesUtil;

public class Main_userlogin_Fragment extends AbstractRadioBindFragment
		implements OnClickListener {
	private View self;
	private int resId = R.layout.activity_login;
	private MainActivity context;
	private Button phoneNumDisposer, passwordDisposer, register, login;
	private ImageButton backButton;
	private EditText phoneNum, password;
	private TextView retrievePassword;
	private CheckedTextView autoLogin;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		bindListeners();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = (MainActivity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppUtil.getLoginUser() != null)
			context.setHome_bottomtab03Fragment(this);
		else
			restoreSetup();
	}

	private void init() {
		self = LayoutInflater.from(context).inflate(resId, null, false);
		phoneNumDisposer = (Button) self.findViewById(R.id.button1);
		passwordDisposer = (Button) self.findViewById(R.id.button2);
		register = (Button) self.findViewById(R.id.button3);
		login = (Button) self.findViewById(R.id.button4);
		backButton = (ImageButton) self.findViewById(R.id.imageButton1);
		phoneNum = (EditText) self.findViewById(R.id.editText1);
		password = (EditText) self.findViewById(R.id.editText2);
		retrievePassword = (TextView) self.findViewById(R.id.textView1);
		autoLogin = (CheckedTextView) self.findViewById(R.id.checkedTextView1);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(phoneNum, phoneNumDisposer);
		AppUtil.bindEditTextNtextDisposer(password, passwordDisposer);
		retrievePassword.setOnClickListener(this);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		AppUtil.setOnClickListenerForCheckedTextView(autoLogin);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) self.getParent();
		if (vg != null)
			vg.removeAllViews();
		return self;
	}

	private void restoreSetup() {
		phoneNum.setText(null);
		SharedPreferencesUtil share = AppUtil
				.getSharedPreferencesUtilInstance();
		String phone = share.getReservedUserInfo(IConstants.PHONE);
		String password = share.getReservedUserInfo(IConstants.PASSWORD);
		String autoLogin = share.getReservedUserInfo(IConstants.AUTO_LOGIN);
		if (phone != null)
			this.phoneNum.setText(phone);
		if (password != null && autoLogin != null && "true".equals(autoLogin)) {
			this.password.setText(password);
			this.autoLogin.setChecked(true);
		}
	}

	private void saveSetup(String phone, String pwd, boolean autoLogin) {
		AppUtil.getSharedPreferencesUtilInstance().saveLoginUserInfo(phone,
				pwd, autoLogin);
	}

	@Override
	public void onClick(View v) {
		if(v==retrievePassword){
			startActivity(new Intent(getActivity(),
					RetrievePasswordActivity1.class));
			AppUtil.putIntoApplication(
					IConstants.DESTINATION_AFTER_RETRIEVEPASSWORD,
					MainActivity.class);
		}else if(v==register){
			Intent intent = new Intent(getActivity(),
					RegisterActivity.class);
			getActivity().startActivity(intent);
			AppUtil.putIntoApplication(
					IConstants.DESTINATION_AFTER_REGISTER,
					MainActivity.class);
			
		}else if (v == login) {
			final String phoneTx = phoneNum.getText().toString();
			final String passwordTx = password.getText().toString();
			if (!AppUtil.isPhoneNum(phoneTx)) {
				Toast.makeText(getActivity(), "无效格式号码", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (!AppUtil.isPassword(passwordTx)) {
				Toast.makeText(getActivity(), "密码格式错误", Toast.LENGTH_LONG)
						.show();
				return;
			}
			DialogUtil.loginFlow(context, phoneTx, passwordTx,
					autoLogin.isChecked(), new Runnable() {
						public void run() {
							saveSetup(phoneTx, passwordTx,
									autoLogin.isChecked());
							context.setHome_bottomtab03Fragment(Main_userlogin_Fragment.this);
						}
					});
		}
	}

}
