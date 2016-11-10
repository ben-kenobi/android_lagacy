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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.adapter.CartItemListAdapter;
import com.icanit.app_v2.adapter.UserInfoListAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DeviceInfoUtil;

public class ShoppingCartActivity extends Activity {
	private ImageButton backButton, trashButton;
	private TextView sum;
	private Button confirmation;
	private UserInfoFormDialogBuilder dialogBuilder;
	private ListView lv;
	private CartItemListAdapter adapter;
	private RadioButton deliveryOption;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart);
		try {
			init();
			bindListeners();
			initLv();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	private void initLv() throws AppException {
		lv.setAdapter(adapter = new CartItemListAdapter(this, sum, confirmation));
		AppUtil.getListenerUtilInstance().setOnScrollListener(lv, adapter);
	}

	private void init() throws AppException {
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		trashButton = (ImageButton) findViewById(R.id.imageButton2);
		sum = (TextView) findViewById(R.id.textView1);
		confirmation = (Button) findViewById(R.id.button1);
		dialogBuilder = new UserInfoFormDialogBuilder(this);
		lv = (ListView) findViewById(R.id.listView1);
		deliveryOption = (RadioButton) findViewById(R.id.radio0);
	}

	@Override
	protected void onResume() {
		try {
			dialogBuilder.onLoginStatusChanged(AppUtil.getLoginUser() != null);
		} catch (AppException e) {
			e.printStackTrace();
		}
		super.onResume();
	}
	protected void onDestroy() {
		if(dialogBuilder!=null)
		dialogBuilder.dismissDialog();
		super.onDestroy();
	}
	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		confirmation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					if (deliveryOption.isChecked())
						dialogBuilder.showDialog();
					else {
						AppUtil.removeFromApplication(IConstants.USER_CONTACT);
						Intent intent = new Intent(ShoppingCartActivity.this,
								OrderConfirmationActivity.class);
						startActivity(intent);
					}
			}
		});
		trashButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});

	}

	/***
	 * 
	 * @author Administrator
	 * 
	 */
	class UserInfoFormDialogBuilder {
		private Dialog dialog;
		private UserInfoListAdapter adapter02;
		private EditText name, phone, address;
		private Button cancel, submit;
		private ListView infoList;
		private Context context;
		private View contentView;
		private TextView login, register;
		private FrameLayout fl;
		private Boolean loginStatus;

		public UserInfoFormDialogBuilder(Context context) throws AppException {
			this.context = context;
			dialog = new Dialog(context, R.style.dialogStyle);
			dialog.setCanceledOnTouchOutside(true);
			contentView = LayoutInflater.from(context).inflate(
					R.layout.dialog_user_info_form, null, false);
			dialog.setContentView(contentView,
					new LayoutParams(DeviceInfoUtil.getScreenWidth() * 9 / 10,
							DeviceInfoUtil.getScreenHeight() * 9 / 10));
			initDialog();
			onLoginStatusChanged(AppUtil.getLoginUser() != null);
			buttonListeners();
		}

		private void fillInfo(UserContact contact) {
			name.setText(contact.username);
			phone.setText(contact.phoneNum);
			address.setText(contact.address);
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
						Toast.makeText(context, "无效号码", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					UserContact contact = new UserContact();
					contact.phoneNum = phoneNum;
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
					Intent intent = new Intent(ShoppingCartActivity.this,
							OrderConfirmationActivity.class);
					startActivity(intent);
				}
			});
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
			fl = (FrameLayout) contentView.findViewById(R.id.frameLayout1);
			login = (TextView) contentView.findViewById(R.id.textView1);
			register = (TextView) contentView.findViewById(R.id.textView2);
		}

		public void onLoginStatusChanged(boolean loginstatus)
				throws AppException {
			if (this.loginStatus!=null&&this.loginStatus == loginstatus)
				return;
			this.loginStatus = loginstatus;
			if (!loginStatus) {
				fl.setVisibility(View.VISIBLE);
				infoList.setVisibility(View.GONE);
			} else {
				initInfoList();
				fl.setVisibility(View.GONE);
				infoList.setVisibility(View.VISIBLE);
			}
		}

		public void showDialog()  {
			dialog.show();
		}
		public void dismissDialog(){
			dialog.dismiss();
		}
	}

}
