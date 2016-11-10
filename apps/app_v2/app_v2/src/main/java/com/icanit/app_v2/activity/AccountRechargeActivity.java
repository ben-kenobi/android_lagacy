package com.icanit.app_v2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.XmlParser;

public class AccountRechargeActivity extends Activity implements OnClickListener{
	
	public static final int ACCOUNT_PAY_REQUESTCODE = 1002,

	UPMOP_REQUESTCODE = 1001,

	UPMOP_RESULTCODE = 2001;
	
	private EditText money;
	
	private TextView balance;
	
	private ImageButton backButton;
	
	private RadioButton alipayClient, alipayWap, creditCard, bankCard;
	
	private Button submit;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_recharge);
		init();bindListeners();
		
	}
	private void init() {
		balance=(TextView)findViewById(R.id.textView1);
		backButton = (ImageButton)findViewById(R.id.ib_account_recharge_back);
		//TODO
		balance.setText("￥"+String.format("%.2f", 0.0));
		alipayClient = (RadioButton) findViewById(R.id.radio2);
		alipayWap = (RadioButton) findViewById(R.id.radio3);
		creditCard = (RadioButton) findViewById(R.id.radio0);
		bankCard = (RadioButton) findViewById(R.id.radio1);
		submit=(Button)findViewById(R.id.button1);
		money=(EditText)findViewById(R.id.editText1);
		alipayClient.setEnabled(false);
		alipayWap.setEnabled(false);
	}
	private void bindListeners() {
		backButton.setOnClickListener(this);
		submit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v==backButton){
			finish();
		}else if(v==submit){
			submit();
		}
	}
	
	private void startUpmopPay(){
		final ProgressDialog pd=DialogUtil.createProgressDialogNshow(null, "正在提交", false,this);
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				try {
					final String launchPay = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(
									AccountRechargeActivity.this)
							.getOrderInfoNsign(money.getText().toString());
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							new UpompPay().start_upomp_pay(
									AccountRechargeActivity.this, launchPay,false);
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
				pd.dismiss();
			}
		});
	}
	
	private void submit() {
		if(AppUtil.isDecimal(money.getText().toString(),2)){
			if (alipayClient.isChecked()) {

			} else if (alipayWap.isChecked()) {

			} else {
				startUpmopPay();
			}
		}else
			AppUtil.toast("请输入正确金钱格式,小数点后最多两位");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == UPMOP_REQUESTCODE
					&& resultCode == UPMOP_RESULTCODE)
				handleUpmopResult(data);
		} else
			Log.e("infoTag", "intent is null");
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
						AppOrder order=null;
						try {
							order = AppUtil.getNetUtilInstance().noticeServerAfterPay(
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
									startActivity(data.setClass(AccountRechargeActivity.this, OrderDetailActivity.class).
									putExtra(IConstants.BACK_CLASS,AccountActivity.class).
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
	
}
