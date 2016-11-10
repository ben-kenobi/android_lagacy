package com.icanit.app_v2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.XmlParser;

public class OrderConfirmationActivity extends Activity implements
		OnClickListener {

	public static final int ACCOUNT_PAY_REQUESTCODE = 1002,

	UPMOP_REQUESTCODE = 1001,

	UPMOP_RESULTCODE = 2001;

	private TextView sumTx, deliveryInfo, balanceTx, balanceTip;

	private ImageButton backButton;

	private Button confirmation;

	private RadioButton alipayClient, alipayWap, creditCard, bankCard;

	private RadioGroup radioGroup;

	private CheckedTextView curAccountPay, otherPayWay;

	private double sum, balance;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (AppUtil.getLoginUser() == null) {
			setContentView(R.layout.activity_orderconfirmation);
			init();
			fillOrderInfo();
		} else {
			setContentView(R.layout.activity_orderconfirmation_mod);
			init2();
			fillOrderInfo2();
			manageCheckBox();
		}
		bindListeners();

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	private void handleUpmopResult(final Intent data) {
		try {
			byte[] xmlData = data.getExtras().getByteArray("xml");
			final PayResultInfo payResultInfo = XmlParser
					.parsePayResultXml(xmlData);
			// TODO
			if ("0000".equals(payResultInfo.respCode)) {
				final ProgressDialog pd=DialogUtil.createProgressDialogNshow(null, "加载订单信息。。", false, this);
				IConstants.THREAD_POOL.submit(new Runnable(){
					public void run(){
						AppUtil.turnShoppingCartItemsToHistory();
						AppOrder order=null;
						try {
							order =AppUtil.getNetUtilInstance().noticeServerAfterPay(
									payResultInfo.merchantOrderId,
									payResultInfo.merchantOrderTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
						final AppOrder ord=order;
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								AppUtil.updateUser();
								if(ord==null){
									AppUtil.toast("订单详情加载失败...");
								}else				
									startActivity(data.setClass(OrderConfirmationActivity.this, OrderDetailActivity.class).
									putExtra(IConstants.BACK_CLASS,MerchandizeListActivity.class).
									putExtra(IConstants.ORDER_INFO, ord));
							}
						});
						pd.dismiss();
					}
				});
			} else {
				CustomizedDialog dialog = CustomizedDialog.initDialog("通知",
						"支付失败", null, 0, this);
				dialog.setPositiveButton("确定", null);
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleAccountPayResult(final Intent data) {
		AppUtil.turnShoppingCartItemsToHistory();
		startActivity(data.setClass(this, OrderDetailActivity.class).putExtra(IConstants.BACK_CLASS,
				MerchandizeListActivity.class));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == UPMOP_REQUESTCODE
					&& resultCode == UPMOP_RESULTCODE)
				handleUpmopResult(data);
			else if (requestCode == ACCOUNT_PAY_REQUESTCODE)
				handleAccountPayResult(data);
		} else
			Log.e("infoTag", "intent is null");
	}

	private void init() {
		sumTx = (TextView) findViewById(R.id.textView3);
		deliveryInfo = (TextView) findViewById(R.id.textView4);
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		confirmation = (Button) findViewById(R.id.button1);
		alipayClient = (RadioButton) findViewById(R.id.radio2);
		alipayWap = (RadioButton) findViewById(R.id.radio3);
		creditCard = (RadioButton) findViewById(R.id.radio0);
		bankCard = (RadioButton) findViewById(R.id.radio1);
		alipayClient.setEnabled(false);
		alipayWap.setEnabled(false);
		sum = AppUtil.calculateTotalCost(AppUtil.appContext.shoppingCartList);
	}

	private void init2() {
		init();
		curAccountPay = (CheckedTextView) findViewById(R.id.checkedTextView1);
		otherPayWay = (CheckedTextView) findViewById(R.id.checkedTextView2);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		balanceTx = (TextView) findViewById(R.id.textView1);
		balanceTip = (TextView) findViewById(R.id.textView2);
		balance = 0;
	}

	private void fillOrderInfo() {
		sumTx.setText("￥" + String.format("%.2f", sum));
		UserContact contact = AppUtil.getUserContactFromApplication();
		if (contact == null)
			deliveryInfo.setText("");
		else {
			deliveryInfo.setText("收货人：" + contact.username + "\n联系电话："
					+ contact.phoneNum + "\n收货地址："
					+ (contact.address == null ? "无" : contact.address));
		}
	}

	private void fillOrderInfo2() {
		fillOrderInfo();
		String fsum = String.format("%.2f", sum);
		String fbalance = String.format("%.2f", balance);
		balanceTx.setText("当前账户余额为：￥" + fbalance);
		curAccountPay.setText("使用当前账户余额支付 ￥"
				+ (sum > balance ? fbalance : fsum));
		if (sum > balance) {
			balanceTip.setTextColor(Color.rgb(0xdd, 0x33, 0x33));
			balanceTip.setText("（余额不足）");
		} else {
			balanceTip.setTextColor(Color.rgb(0x33, 0xaa, 0x33));
			balanceTip.setText("（可以正常支付）");
		}

	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		confirmation.setOnClickListener(this);
	}

	private void manageCheckBox() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if (v.getId() == R.id.checkedTextView1) {
					if (curAccountPay.isChecked())
						return;
					curAccountPay.setChecked(true);
					otherPayWay.setChecked(false);
					creditCard.setEnabled(false);
					bankCard.setEnabled(false);
				} else if (v.getId() == R.id.checkedTextView2) {
					if (otherPayWay.isChecked())
						return;
					curAccountPay.setChecked(false);
					otherPayWay.setChecked(true);
					creditCard.setEnabled(true);
					bankCard.setEnabled(true);
				}
			}
		};
		curAccountPay.setOnClickListener(listener);
		otherPayWay.setOnClickListener(listener);
		curAccountPay.setChecked(true);
		otherPayWay.setChecked(false);
		creditCard.setEnabled(false);
		bankCard.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		if (curAccountPay != null && curAccountPay.isChecked()) {
			startAccountPay();
			return;
		}
		if (alipayClient.isChecked()) {

		} else if (alipayWap.isChecked()) {

		} else {
			startUpmopPay();
		}
	}

	private void startAccountPay(){
		if (sum > balance) {
			AppUtil.toast("余额不足，请充值或选择其他支付方式");
		}else if(AppUtil.appContext.shoppingCartList==null||AppUtil.appContext.shoppingCartList.isEmpty()){
			AppUtil.toast("购物车内没有商品，请确认 。。");
		}else {
			final ProgressDialog pd=DialogUtil.createProgressDialogNshow("提交订单", "正在提交。。。", false, this);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final AppOrder order=AppUtil.getServiceFactory().getDataServiceInstance(OrderConfirmationActivity.this)
						.getAccountPayOrderInfo(AppUtil.appContext.shoppingCartList, AppUtil.getUserContactFromApplication());
							IConstants.MAIN_HANDLER.post(new Runnable(){
								public void run(){
									if(order!=null){
										startActivityForResult(
											new Intent(OrderConfirmationActivity.this, PayIndexActivity.class).
											putExtra(IConstants.ORDER_INFO, order),
											ACCOUNT_PAY_REQUESTCODE);
										if(AppUtil.getLoginUser()!=null)
										AppUtil.turnShoppingCartItemsToHistory();
									}
									else 
										AppUtil.toast("提交订单失败。。。");
								}
							});
					} catch (AppException e) {
						e.printStackTrace();
					}
					pd.dismiss();
				}
			});
		}
	}

	private void startUpmopPay() {
		 if(AppUtil.appContext.shoppingCartList==null||AppUtil.appContext.shoppingCartList.isEmpty()){
				AppUtil.toast("购物车内没有商品，请确认 。。");return;
		 }
		final ProgressDialog pd = DialogUtil.createProgressDialogNshow(
				"processing..", "正在提交订单..", true,
				OrderConfirmationActivity.this);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					final String launchPay = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(
									OrderConfirmationActivity.this)
							.getOrderInfoNsign(
									AppUtil.appContext.shoppingCartList,
									AppUtil.getUserContactFromApplication());
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							new UpompPay().start_upomp_pay(
									OrderConfirmationActivity.this, launchPay,AppUtil.getLoginUser()!=null);
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
				pd.dismiss();
			}
		});
	}

}
