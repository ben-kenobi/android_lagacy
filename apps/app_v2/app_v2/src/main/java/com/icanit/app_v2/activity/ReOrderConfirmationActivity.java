package com.icanit.app_v2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.XmlParser;

public class ReOrderConfirmationActivity extends Activity implements
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

	private double balance;

	private AppOrder order;

	private AppOrder newOrder;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		order = (AppOrder) getIntent().getSerializableExtra(
				IConstants.ORDER_INFO);
		if(order==null) {finish(); return;}
		if(order.type==0){
			setContentView(R.layout.activity_orderconfirmation_mod);
			init2();
			fillOrderInfo2();
			manageCheckBox();
		}else if(order.type==1){
			setContentView(R.layout.activity_orderconfirmation);
			init();
			fillOrderInfo();
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
				IConstants.THREAD_POOL.submit(new Runnable() {
					public void run() {
						AppOrder order = null;
						try {
							order=AppUtil.getNetUtilInstance().noticeServerAfterPay(
									payResultInfo.merchantOrderId,
									payResultInfo.merchantOrderTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
						final AppOrder ord = order;
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								AppUtil.updateUser();
								if(ord==null){
									AppUtil.toast("订单详情加载失败...");
								}else{		
									startActivity(data.setClass(ReOrderConfirmationActivity.this, OrderDetailActivity.class)
											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
									putExtra(IConstants.ORDER_INFO, ord));
								}
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
		startActivity(data.setClass(this, OrderDetailActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
		sumTx.setText("￥" + String.format("%.2f", order.sum));
		deliveryInfo.setText(order.type==0?"收货人：" + order.userName + "\n联系电话："
				+ order.contactPhone + "\n收货地址："
				+ (order.address == null ? "无" : order.address):order.type==1?"订单类型：账户充值订单":"");
	}

	private void fillOrderInfo2() {
		fillOrderInfo();
		String fsum = String.format("%.2f", order.sum);
		String fbalance = String.format("%.2f", balance);
		balanceTx.setText("当前账户余额为：￥" + fbalance);
		curAccountPay.setText("使用当前账户余额支付 ￥"
				+ (order.sum > balance ? fbalance : fsum));
		if (order.sum > balance) {
			balanceTip.setTextColor(Color.rgb(0xdd, 0x33, 0x33));
			balanceTip.setText("（余额不足）");
		} else {
			balanceTip.setTextColor(Color.rgb(0x33, 0xaa, 0x33));
			balanceTip.setText("（可以正常支付）");
		}

	}

	private void bindListeners() {
		backButton.setOnClickListener(this);
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
		if(v==backButton){
			backToPrev();
		}else if (v == confirmation) {
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
	}

	private void compareOrderNforwardToPay(final AppOrder ord) {
		newOrder = ord;
		if (order.sum != newOrder.sum) {
			CustomizedDialog dialog = CustomizedDialog.initDialog("提示",
					"商品价格发生过变化\n实际支付金额以新价格为准\n确定继续？？", null, 0, this);
			dialog.setNegativeButton("不了", null);
			dialog.setPositiveButton("继续支付",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							order = newOrder;
							fillOrderInfo2();
							startActivityForResult(new Intent(
									ReOrderConfirmationActivity.this,
									PayIndexActivity.class).putExtra(
									IConstants.ORDER_INFO, order),
									ACCOUNT_PAY_REQUESTCODE);
						}
					});
			dialog.show();
		} else {
			this.order = newOrder;
			startActivityForResult(new Intent(ReOrderConfirmationActivity.this,
					PayIndexActivity.class).putExtra(IConstants.ORDER_INFO,
					order), ACCOUNT_PAY_REQUESTCODE);
		}
	}

	private void startAccountPay() {
		if (order.sum > balance) {
			AppUtil.toast("余额不足，请充值或选择其他支付方式");
		} else {
			order.payway = 1;
			final ProgressDialog pd = DialogUtil.createProgressDialogNshow(
					"提交订单", "正在提交。。。", false, this);
			IConstants.THREAD_POOL.submit(new Runnable() {
				public void run() {
					try {
						final String respStr = AppUtil
								.getServiceFactory()
								.getDataServiceInstance(
										ReOrderConfirmationActivity.this)
								.resubmitOrder(order);
						IConstants.MAIN_HANDLER.post(new Runnable() {
							public void run() {
								AppOrder ord = null;
								if (respStr != null && respStr.startsWith("{"))
									ord = new Gson().fromJson(respStr,
											AppOrder.class);
								if (ord != null) {
									compareOrderNforwardToPay(ord);
								} else
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
		final ProgressDialog pd = DialogUtil.createProgressDialogNshow(
				"processing..", "正在提交订单..", true,
				ReOrderConfirmationActivity.this);
		order.payway = 0;
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					final String launchPay = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(
									ReOrderConfirmationActivity.this)
							.resubmitOrder(order);
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							new UpompPay()
									.start_upomp_pay(
											ReOrderConfirmationActivity.this,
											launchPay,false);
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
				pd.dismiss();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			backToPrev();
			return true;
		}else
		return super.onKeyDown(keyCode, event);
	}
	private void backToPrev() {
		if (newOrder != null) 
			setResult(2, new Intent().putExtra(IConstants.ORDER_INFO, newOrder));
		finish();
	}

}
