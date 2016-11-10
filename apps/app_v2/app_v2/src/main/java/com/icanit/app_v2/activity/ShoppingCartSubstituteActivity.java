package com.icanit.app_v2.activity;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.adapter.CartItemListAdapterSub;
import com.icanit.app_v2.adapter.UserInfoListAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.ui.OuterListView;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DeviceInfoUtil;

public class ShoppingCartSubstituteActivity extends Activity {
	private OuterListView olv;
	private ImageButton backButton, trashButton;
	private Dialog clearCartPrompt;
	private TextView sum;
	private Button confirmation;
	private UserInfoFormDialogBuilder dialogBuilder;
	private PhoneNumFormDialogBuilder phoneNumDialogBuilder;
	private CartItemListAdapterSub oadapter;
	private int dialogResId=R.layout.dialog_user_info_form;
	private int phoneNumDialogResId=R.layout.dialog_phonenum_form;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart_substitute);
		try {
			init();
			bindListeners();
			initLv();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	private void initLv() {
		olv.setAdapter(oadapter=new CartItemListAdapterSub(this,sum,confirmation,trashButton));
	}

	private void init() throws AppException {
		olv = (OuterListView) findViewById(R.id.listView1);
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		trashButton = (ImageButton) findViewById(R.id.imageButton2);
		sum = (TextView) findViewById(R.id.textView1);
		confirmation = (Button) findViewById(R.id.button1);
	}
	protected void onResume() {
		try {
			oadapter.notifyDataSetChanged();
			if(dialogBuilder!=null)
			dialogBuilder.onLoginStatusChanged(AppUtil.getLoginUser()!=null);
		} catch (AppException e) {
			e.printStackTrace();
		}
		super.onResume();
	}
	protected void onDestroy() {
		if(dialogBuilder!=null)
			dialogBuilder.dismissDialog();
		if(phoneNumDialogBuilder!=null)
			phoneNumDialogBuilder.dismissDialog();
		super.onDestroy();
	}
	
	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		confirmation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					try {
						List<MerchantCartItems> mcList=oadapter.getDividedItemList();
						for(int i=0;i<mcList.size();i++){
							if(mcList.get(i).delivery){
								if(dialogBuilder==null)
									dialogBuilder = new UserInfoFormDialogBuilder(ShoppingCartSubstituteActivity.this);
								dialogBuilder.showDialog();return;
							}
						}
						if(phoneNumDialogBuilder==null)
							phoneNumDialogBuilder=new PhoneNumFormDialogBuilder(ShoppingCartSubstituteActivity.this);
						phoneNumDialogBuilder.showDialog();
//						AppUtil.removeFromApplication(IConstants.USER_CONTACT);
//						startupOrderConfirmationActivity();
					} catch (AppException e) {
						e.printStackTrace();
					}
			}
		});
		trashButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(clearCartPrompt==null)
				clearCartPrompt=CustomizedDialog.initDialog("提示", "确定清空购物车么？？不可恢复", null, 0, ShoppingCartSubstituteActivity.this)
				.setPositiveButton("清空", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							oadapter.clearAllCartItems();
						} catch (AppException e) {
							e.printStackTrace();
						}
					}
				}).setNegativeButton("不了", null);
				clearCartPrompt.show();
			}
		});
	}
	private void startupOrderConfirmationActivity(){
		AppUtil.setCartItemsDeliveryField();
		Intent intent = new Intent(ShoppingCartSubstituteActivity.this,
				OrderConfirmationActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("requestCode="+requestCode+",resultCode="+resultCode+",data="+
				data+"   @ShoppingCartSubstituteActivity");
	}
	
//InnerClass
	class PhoneNumFormDialogBuilder{
		private Dialog dialog;
		private EditText name, phone;
		private Button cancel, submit;
		private Context context;
		private View contentView;
		public PhoneNumFormDialogBuilder(Context context){
			this.context = context;
			dialog = new Dialog(context, R.style.dialogStyle);
			dialog.setCanceledOnTouchOutside(true);
			contentView = LayoutInflater.from(context).inflate(
					phoneNumDialogResId, null, false);
			dialog.setContentView(contentView,new LayoutParams(-1,-1));
			initDialog();
			buttonListeners();
			fillInfo();
		}
		private void fillInfo() {
			phone.setText(AppUtil.getLoginPhoneNum());
		}
		private void buttonListeners() {
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			submit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String phoneNum = phone.getText().toString();
					if (!AppUtil.isPhoneNum(phoneNum)) {
						Toast.makeText(context, "无效号码", Toast.LENGTH_SHORT).show();return;
					}
					UserContact contact = new UserContact();
					contact.phoneNum = phoneNum;
					contact.username = name.getText().toString();
					AppUtil.putIntoApplication(IConstants.USER_CONTACT, contact);
					startupOrderConfirmationActivity();
					dismissDialog();
				}
			});
		}
		private void initDialog() {
			name = (EditText) contentView.findViewById(R.id.editText1);
			phone = (EditText) contentView.findViewById(R.id.editText2);
			cancel = (Button) contentView.findViewById(R.id.button1);
			submit = (Button) contentView.findViewById(R.id.button2);
		}
		public void showDialog(){
			if(dialog!=null)
			dialog.show();
		}
		public void dismissDialog(){
			if(dialog!=null) dialog.dismiss();
		}
	}
	
	
	
	// InnerClass
	class UserInfoFormDialogBuilder {
		private Dialog dialog;
		private UserInfoListAdapter adapter02;
		private EditText name, phone, address;
		private Button cancel, submit;
		private ListView infoList;
		private Context context;
		private View contentView;
		private TextView login, register;
		private FrameLayout loginPromtContainer;
		private Boolean loginStatus;
		private CustomizedDialog promptDialog;

		public UserInfoFormDialogBuilder(Context context) throws AppException {
			this.context = context;
			dialog = new Dialog(context, R.style.dialogStyle);
			dialog.setCanceledOnTouchOutside(true);
			contentView = LayoutInflater.from(context).inflate(
					dialogResId, null, false);
			dialog.setContentView(contentView,
					new LayoutParams(DeviceInfoUtil.getScreenWidth() * 9 / 10,
							DeviceInfoUtil.getScreenHeight() * 9 / 10));
			initDialog();
			onLoginStatusChanged(AppUtil.getLoginUser() != null);
			buttonListeners();
		}

		private void fillInfo(UserContact contact) {
			if(contact==null){
				AppUser user = AppUtil.getLoginUser();
				if(user!=null){
					name.setText(user.username==null?"":user.username);
					phone.setText(user.phone);
				}
				address.setText(AppUtil.getSharedPreferencesUtilInstance().getReservedCommunityInfo()
						.commName+" ");
			}else{
				name.setText(contact.username);
				phone.setText(contact.phoneNum);
				address.setText(contact.address);
			}
		}
		private void textViewListeners(){
			login.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ShoppingCartSubstituteActivity.this,LoginActivity.class);
					startActivityForResult(intent, 1);
				}
			});
			register.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent=new Intent(ShoppingCartSubstituteActivity.this,RegisterActivity.class);
					startActivity(intent);
					AppUtil.putIntoApplication(IConstants.DESTINATION_AFTER_REGISTER, ShoppingCartSubstituteActivity.class);
				}
			});
		}
		private void buttonListeners() {
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
			submit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String phoneNum = phone.getText().toString();
					if (!AppUtil.isPhoneNum(phoneNum)) {
						Toast.makeText(context, "无效号码", Toast.LENGTH_SHORT).show();return;
					}
					if(AppUtil.getSharedPreferencesUtilInstance().getAddressPromptStatus(AppUtil.getLoginPhoneNum())){
						initPromptDialog();
						promptDialog.show();
					}else{
						submit();
					}
				}
			});
		}
		private void initPromptDialog(){
			promptDialog=CustomizedDialog.initDialog("送货提示", "超出社区范围需加收额外的费用", 
					"下次不再提示",0, context).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					AppUtil.getSharedPreferencesUtilInstance().setAddressPromptStatus(AppUtil.getLoginPhoneNum(),
							!promptDialog.getCheckedTextStatus());
					submit();
				}
			}).setNegativeButton("取消", null);
		}
		private void submit(){
			UserContact contact = new UserContact();
			contact.phoneNum = phone.getText().toString();
			contact.username = name.getText().toString();
			contact.address = address.getText().toString();
			if (loginStatus) {
				contact.phone = AppUtil.getLoginPhoneNum();
				if (adapter02.getContactList() == null
						|| !adapter02.getContactList()
								.contains(contact)) {
					try {
						AppUtil.getServiceFactory()
								.getUserContactDaoInstance(context)
								.saveUserContact(contact);
						adapter02.getContactList().add(contact);
						adapter02.notifyDataSetChanged();
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			}
			AppUtil.putIntoApplication(IConstants.USER_CONTACT, contact);
			startupOrderConfirmationActivity();
			dismissDialog();
		}
		private void initInfoList() throws AppException {
			if (adapter02 != null)
				return;
			infoList.setAdapter(adapter02 = new UserInfoListAdapter(context));
			List<UserContact> contactList = AppUtil.getServiceFactory()
					.getUserContactDaoInstance(context)
					.listUserContactsByPhone(AppUtil.getLoginPhoneNum());
			if (contactList != null && !contactList.isEmpty())
				fillInfo(contactList.get(contactList.size() - 1));
			else 
				fillInfo(null);
			adapter02.setContactList(contactList);
			infoList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					fillInfo(adapter02.getContactList().get(
							adapter02.getContactList().size() - position - 1));
				}
			});
			infoList.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> parent, View v,
						final int position, long id) {
					CustomizedDialog.Builder builder = new CustomizedDialog.Builder(
							context);
					builder.setTitle("confirmation");
					builder.setMessage("删除？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface parent,
										int which) {
									try {
										AppUtil.getServiceFactory()
												.getUserContactDaoInstance(
														context)
												.deleteContact(
														adapter02
																.getContactList()
																.get(position));
										adapter02.getContactList().remove(
												position);
										adapter02.notifyDataSetChanged();
									} catch (AppException e) {
										e.printStackTrace();
									}
								}
							});
					builder.setNegativeButton("不了", null);
					builder.create().show();
					return true;
				}
			});
		}

		private void initDialog() throws AppException {
			infoList = (ListView) contentView.findViewById(R.id.listView1);
			name = (EditText) contentView.findViewById(R.id.editText1);
			phone = (EditText) contentView.findViewById(R.id.editText2);
			address = (EditText) contentView.findViewById(R.id.editText3);
			cancel = (Button) contentView.findViewById(R.id.button1);
			submit = (Button) contentView.findViewById(R.id.button2);
			loginPromtContainer = (FrameLayout) contentView.findViewById(R.id.frameLayout1);
			login = (TextView) contentView.findViewById(R.id.textView1);
			register = (TextView) contentView.findViewById(R.id.textView2);
		}

		public void onLoginStatusChanged(boolean loginstatus)
				throws AppException {
			if (this.loginStatus!=null&&this.loginStatus == loginstatus)
				return;
			this.loginStatus = loginstatus;
			if (!loginStatus) {
				textViewListeners();
				loginPromtContainer.setVisibility(View.VISIBLE);
				infoList.setVisibility(View.GONE);
			} else {
				initInfoList();
				loginPromtContainer.setVisibility(View.GONE);
				infoList.setVisibility(View.VISIBLE);
			}
		}

		public void showDialog() throws AppException {
			if(dialog!=null)
			dialog.show();
		}
		public void dismissDialog(){
			if(dialog!=null)
			dialog.dismiss();
		}
	}
}
