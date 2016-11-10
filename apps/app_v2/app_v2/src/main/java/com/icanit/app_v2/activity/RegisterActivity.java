package com.icanit.app_v2.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.action.BasePopAction;
import com.icanit.app_v2.adapter.AlphabetLvAdapter;
import com.icanit.app_v2.adapter.CommunityDistrictLvAdapter;
import com.icanit.app_v2.adapter.CommunityLvAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.entity.AppArea;
import com.icanit.app_v2.entity.AppCommunity;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.PinyinUtil;

public class RegisterActivity extends Activity{
	private ImageButton backButton;
	private Button phoneDisposer, veriCodeDisposer,nicknameDisposer,pwdDisposer, repwdDisposer,obtainVericode,submit;
	private CheckedTextView complyServiceContract;
	private TextView contract,communityChoose;
	private EditText phone,veriCode,nickname, pwd, repwd;
	private CustomizedDialog dialog;
	private AppCommunity community;
	private Pop1Action commPop;
	private View popParent;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		try {
			init();
			bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	public void setCommunity(AppCommunity community) {
		this.community = community;
		if(community!=null){
			StringBuffer sb = new StringBuffer();
			sb.append(commPop.adapter.getSelectedAreaName()+"/");
			sb.append(commPop.subAdapter.getSelectedAreaName()+"/");
			sb.append(community.commName);
			communityChoose.setText(sb.toString());
		}
	}
	private void init() throws AppException {
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		phoneDisposer = (Button) findViewById(R.id.button1);
		veriCodeDisposer = (Button) findViewById(R.id.button2);
		nicknameDisposer = (Button) findViewById(R.id.button3);
		pwdDisposer = (Button) findViewById(R.id.button4);
		repwdDisposer = (Button) findViewById(R.id.button5);
		obtainVericode= (Button) findViewById(R.id.button6);
		submit = (Button) findViewById(R.id.button7);
		phone = (EditText) findViewById(R.id.editText1);
		veriCode = (EditText) findViewById(R.id.editText2);
		nickname = (EditText) findViewById(R.id.editText3);
		pwd = (EditText) findViewById(R.id.editText4);
		repwd = (EditText) findViewById(R.id.editText5);
		contract=(TextView)findViewById(R.id.textView8);
		communityChoose=(TextView)findViewById(R.id.textView7);
		complyServiceContract=(CheckedTextView)findViewById(R.id.checkedTextView1);
		popParent=findViewById(R.id.frameLayout1);
		commPop=new Pop1Action(communityChoose, RegisterActivity.this);
	}

	private void bindListeners(){
		
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(phone, phoneDisposer);
		AppUtil.bindEditTextNtextDisposer(pwd, pwdDisposer);
		AppUtil.bindEditTextNtextDisposer(repwd, repwdDisposer);
		AppUtil.bindEditTextNtextDisposer(veriCode, veriCodeDisposer);
		AppUtil.bindEditTextNtextDisposer(nickname, nicknameDisposer);
		AppUtil.setOnClickListenerForCheckedTextView(complyServiceContract);
		submit.setOnClickListener(new OnSubmitButtonClickListener());
		contract.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(dialog==null){
					CustomizedDialog.Builder builder = new CustomizedDialog.Builder(RegisterActivity.this);
					builder.setTitle("用户协议");
					builder.setMessage(getResources().getString(R.string.serviceContract));
					builder.setContentFontSize(14);
					builder.setPositiveButton("确定", null);
					dialog=builder.create();
				}
				dialog.show();
			}
		});
		obtainVericode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String phoneTx=phone.getText().toString();
				if(!AppUtil.isPhoneNum(phoneTx)){
					Toast.makeText(RegisterActivity.this, "输入的号码不是合法手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				IConstants.THREAD_POOL.submit(new Runnable(){
					public void run(){
						try {
							String response=AppUtil.getNetUtilInstance().sendMessageWithHttpPost
									(UriConstants.SEND_VERICODE, "phone="+phoneTx);
							JSONObject jo = new JSONObject(response);
							final boolean success=jo.getBoolean(IConstants.RESPONSE_SUCCESS);
							final String veriCode=jo.getString(IConstants.VERI_CODE);
							runOnUiThread(new Runnable(){
								public void run(){
									if(success)
										Toast.makeText(RegisterActivity.this,"验证码："+veriCode, Toast.LENGTH_LONG).show();
									else
										Toast.makeText(RegisterActivity.this, "号码不可用或已经被注册，" +
												"重新输入一个手机号",Toast.LENGTH_LONG).show();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	
	private  class OnSubmitButtonClickListener implements OnClickListener{
		private String phoneTx,passwordTx,repasswordTx,veriCodeTx,nicknameTx;
		public void onClick(View v) {
			if(!complyServiceContract.isChecked()){
				Toast.makeText(RegisterActivity.this, "请阅读服务条款并确认", Toast.LENGTH_SHORT).show();
				return;
			}
			phoneTx=phone.getText().toString();
			passwordTx=pwd.getText().toString();
			repasswordTx=repwd.getText().toString();
			veriCodeTx=veriCode.getText().toString();
			nicknameTx=nickname.getText().toString();
			if(!AppUtil.isPhoneNum(phoneTx)){
				Toast.makeText(RegisterActivity.this, "输入的号码不是合法手机号", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!AppUtil.isVeriCode(veriCodeTx)){
				Toast.makeText(RegisterActivity.this, "验证码格式不对", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!AppUtil.isPassword(passwordTx)){
				Toast.makeText(RegisterActivity.this, "输入的密码格式不对", Toast.LENGTH_SHORT).show();
				return ;
			}
			if(!repasswordTx.equals(passwordTx)){
				Toast.makeText(RegisterActivity.this, "输入密码不一致,重新输入", Toast.LENGTH_SHORT).show();
				return;
			}
			final List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone", phoneTx));
			params.add(new BasicNameValuePair("password", passwordTx));
			params.add(new BasicNameValuePair("veriCode", veriCodeTx));
			params.add(new BasicNameValuePair("username",nicknameTx));
			params.add(new BasicNameValuePair("commId", community==null?0+"":community.id+""));
			final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
			pd.setTitle("处理中。。。。。");
			pd.setMessage("正在提交信息。。");
			AsyncTask<List<NameValuePair>, Void, String> at =
					new AsyncTask<List<NameValuePair>, Void, String>() {
				protected void onPreExecute() {
					pd.show();
				};
				protected String doInBackground(List<NameValuePair>...lists) {
					try {
						return AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.USER_REGISTER,lists[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				protected void onPostExecute(String result) {
					pd.dismiss();
					if(result==null){
						CustomizedDialog dialog = CustomizedDialog.initDialog("提示", "请检查网络，无法获取服务器数据",
								null, 0,RegisterActivity.this);
						dialog.setPositiveButton("确认", null);
						dialog.show();
						return ;
					}
					try {
						JSONObject jo = new JSONObject(result);
						CustomizedDialog.Builder builder = new CustomizedDialog.Builder(RegisterActivity.this);
						builder.setTitle("结果通知");
						if(jo.getBoolean(IConstants.RESPONSE_SUCCESS)){
							AppUtil.getSharedPreferencesUtilInstance().saveLoginUserInfo(phoneTx, passwordTx, false);
							AppUtil.getSharedPreferencesUtilInstance().reserveCommunityInfo(community);
//							AppUtil.putIntoApplication("user", EntityMapFactory.generateUser(phoneTx, passwordTx));
							/*builder.setMessage("注册成功，直接登录?");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {*/
									DialogUtil.loginFlow(RegisterActivity.this, phoneTx, passwordTx,false, new Runnable(){
										public void run(){
											startActivity(new Intent(RegisterActivity.this,(Class)AppUtil.getFromApplication
													(IConstants.DESTINATION_AFTER_REGISTER))
											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
											AppUtil.removeFromApplication(IConstants.DESTINATION_AFTER_REGISTER);
										}
									});
								/*}
							});
							builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(RegisterActivity.this,(Class)AppUtil.getFromApplication
											(IConstants.DESTINATION_AFTER_REGISTER))
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
									AppUtil.removeFromApplication(IConstants.DESTINATION_AFTER_REGISTER);
								}
							});*/
						}else{
							builder.setMessage("注册失败\n具体原因不明");
							builder.setPositiveButton("确定", null);
							builder.create().show();
						}
//						builder.create().show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}.execute(params);
		}
		
	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	
	private class Pop1Action extends BasePopAction{
		private CommunityDistrictLvAdapter adapter,subAdapter;
		public CommunityLvAdapter sub2Adapter;
		private ListView lv,sublv,sub2Lv,alphabetLv;
		private AlphabetLvAdapter alphabetLvAdapter;
		private Context context;
		public Pop1Action(TextView trig,Context context) throws AppException {
			super(R.layout.pop1_community,LayoutInflater.from(context), trig);
			this.context=context;
			execute();
		}
		public void showAsDropDown() {
			pop.showAsDropDown(popParent, 0, 5);
		}
		
		public void execute() throws AppException {
			 lv=(ListView)contentView.findViewById(R.id.listView1);
			sublv=(ListView)contentView.findViewById(R.id.listView2);
			sub2Lv=(ListView)contentView.findViewById(R.id.listView3);
			alphabetLv=(ListView)contentView.findViewById(R.id.listView4);
			lv.setAdapter(adapter=new CommunityDistrictLvAdapter(context,CommunityDistrictLvAdapter.TYPE_AREA));
			sublv.setAdapter(subAdapter=new CommunityDistrictLvAdapter(context,CommunityDistrictLvAdapter.TYPE_COMM));
			subAdapter.setSelectedColor(Color.rgb(0xdd, 0xdd, 0xdd));
			sub2Lv.setAdapter(sub2Adapter=new CommunityLvAdapter(context));
			alphabetLv.setAdapter(alphabetLvAdapter=new AlphabetLvAdapter(context));
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppArea> areaList=AppUtil.getServiceFactory().getDataServiceInstance(context).
								listAreasByParentId(IConstants.ROOTAREAID);
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								adapter.setAreaList(areaList);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if(adapter.selected==position) return;
					adapter.setSelected(position);
					refreshSubLvAdapter(position);
				}
			});
			sublv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if(subAdapter.selected==position) return;
					subAdapter.setSelected(position);
					refreshSub2LvAdapter(position);
				}
			});
			sub2Lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					pop.dismiss();
					setCommunity(sub2Adapter.getCommunities().get(position));
//					AppUtil.getSharedPreferencesUtilInstance().reserveCommunityInfo(sub2Adapter.getCommunities().get(position));
				}
			});
			alphabetLv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					String alpha=alphabetLvAdapter.getAlpahList().get(position);
					sub2Lv.setSelection(sub2Adapter.getPositionByAlpha(alpha));
				}
			});
			
		}
		private void refreshSubLvAdapter(final int position){
			subAdapter.setCommList(null);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppCommunity> commList=AppUtil.getServiceFactory().getDataServiceInstance(context)
								.findCommunitiesByAreaId(adapter.getAreaList().get(position).id);
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								subAdapter.setSelected(-1);
								subAdapter.setCommList(commList);
								sub2Adapter.setCommunities(null);
								alphabetLvAdapter.setAlphaList(null);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
		}
		private void refreshSub2LvAdapter(final int position){
			sub2Adapter.setCommunities(null);
			alphabetLvAdapter.setAlphaList(null);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppCommunity> communities=AppUtil.getServiceFactory().getDataServiceInstance(context)
								.findCommunitiesByAreaId(subAdapter.getCommList().get(position).id);
						Collections.sort(communities, new Comparator<AppCommunity>() {
							public int compare(AppCommunity comm0, AppCommunity comm1) {
								return PinyinUtil.toPinyin(comm0.commName).
										compareTo(PinyinUtil.toPinyin(comm1.commName));
							}
						});
						final List<String> alphaList=new ArrayList<String>();
						for(int i=0;i<communities.size();i++){
							AppCommunity comm = communities.get(i);
							String alpha=PinyinUtil.toPinyin(comm.commName.charAt(0)).substring(0, 1);
							if(!alphaList.contains(alpha)) alphaList.add(alpha);
						}
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								alphabetLvAdapter.setAlphaList(alphaList);
								sub2Adapter.setCommunities(communities);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
		}
		public void resetTrigger() {
		}
	}
	
}
