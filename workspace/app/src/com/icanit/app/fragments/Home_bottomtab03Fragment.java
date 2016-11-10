package com.icanit.app.fragments;

import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app.LoginActivity;
import com.icanit.app.ModifyPasswordActivity;
import com.icanit.app.R;
import com.icanit.app.VerifyUserAccountActivity;
import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.service.DataService;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;

public class Home_bottomtab03Fragment extends AbstractRadioBindFragment{
	private LayoutInflater inflater;
	private View self;
	private ListView listView;
	private Button loginButton,modifyPwdButton;
	private TextView collectionCount;
	private RelativeLayout userInfoContainer;
	private TextView userInfoSection;
	private ImageButton backButton;
	
	public Home_bottomtab03Fragment(){};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();initlv();bindListeners();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return self;
	}
	@Override
	public void onResume() {
		super.onResume();
		updateUserInfoContainer();
	}
	
	
	public void updateUserInfoContainer(){
		Object obj = AppUtil.getFromApplication(IConstants.LOGIN_USER);
		if(obj!=null){
			modifyPwdButton.setEnabled(true);
			Map<String,Object> loginUser = (Map<String,Object>)obj;
			final String phoneTx = (String)loginUser.get(IConstants.PHONE);
			loginButton.setText("换号登录");
			String userInfo=phoneTx+" 用户欢迎回来...";
			SpannableString spannableUserInfo=new SpannableString(userInfo);
			spannableUserInfo.setSpan(new ForegroundColorSpan(Color.rgb(0xff,0x7f,0x00)), 0,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableUserInfo.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableUserInfo.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC),12,userInfo.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			userInfoSection.setText(spannableUserInfo);
			if(AppUtil.getFromApplication(IConstants.LOGIN_USER_VERIFICATION)!=null){
				CustomizedDialog.Builder builder = new CustomizedDialog.Builder(getActivity());
				builder.setTitle("notice");builder.setMessage("您的手机号码还未验证\n现在验证？");
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							DataService.THREAD_POOL.submit(new Runnable(){
								public void run(){
									String response;
									try {
										response = AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.RESEND_VERICODE, "phone="+phoneTx);
										Log.d("infoTag","@json responseText:"+response+" @home_bottomtab03Fragment");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							startActivity(new Intent(getActivity(),VerifyUserAccountActivity.class).setAction(IConstants.VERIFY_AFTER_LOGIN));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int which){
					}
				});
				builder.create().show();
			}
		}else{
			modifyPwdButton.setEnabled(false);
			loginButton.setText(getResources().getString(R.string.loginnow));
			userInfoSection.setText(getResources().getString(R.string.notLoginYet));
		}
	}
	
	
	private void bindListeners(){
		if(loginButton!=null)
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);
			}
		});
		modifyPwdButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				getActivity().startActivity(new Intent(getActivity(),ModifyPasswordActivity.class));
			}
		});
		AppUtil.bindBackListener(backButton);
		
	}
	
	private void  init(){
		inflater=getActivity().getLayoutInflater();
		self=inflater.inflate(R.layout.fragment4home_03_user, null,false);
		listView=(ListView)self.findViewById(R.id.listView1);
		loginButton=(Button)self.findViewById(R.id.button1);
		collectionCount=(TextView)self.findViewById(R.id.textView1);
		userInfoContainer=(RelativeLayout)self.findViewById(R.id.relativeLayout1);
		userInfoSection=(TextView)self.findViewById(R.id.textView2);
		modifyPwdButton=(Button)self.findViewById(R.id.button2);
		backButton=(ImageButton)self.findViewById(R.id.imageButton1);
		modifyPwdButton.setEnabled(false);
	}
	
	private void initlv(){
		final String[] sary;
		listView.setAdapter(new ArrayAdapter(getActivity(),R.layout.item4lv_fragment4home_03,
				R.id.textView1,sary=new String[]{"未消费订单","已消费订单","待付款订单","退款订单"}));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self, int position,
					long id) {
				Toast.makeText(getActivity(), "$$"+sary[position], Toast.LENGTH_SHORT).show();
			}
		});
	}

}
