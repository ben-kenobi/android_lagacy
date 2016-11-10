package com.icanit.app;

import java.io.OutputStream;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app.R;
import com.icanit.app.common.IConstants;
import com.icanit.app.entity.CartItem;
import com.icanit.app.entity.PayResultInfo;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.XmlParser;

public class AlipayCashierActivity extends Activity{
	
	private ImageButton backButton,accountDisposer,combinationDisposer;
	private TextView merchandise, seller,sum,registerAlipay, useBankCard, retrievePwd;
	private RelativeLayout useCreditCard;
	private EditText account,	 combination;
	private Button confirmation,abortion;
	private DataService dataService;
	private Map<String,Object> info;
	private List<CartItem> items;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_cashier_alipay);
		try {
			init();bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	private void init() throws AppException{
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		accountDisposer=(ImageButton)findViewById(R.id.imageButton2);
		combinationDisposer=(ImageButton)findViewById(R.id.imageButton3);
		merchandise=(TextView)findViewById(R.id.textView1);
		seller=(TextView)findViewById(R.id.textView2);
		sum=(TextView)findViewById(R.id.textView3);
		useCreditCard=(RelativeLayout)findViewById(R.id.relativeLayout1);
		registerAlipay=(TextView)findViewById(R.id.textView4);
		useBankCard=(TextView)findViewById(R.id.textView5);
		retrievePwd=(TextView)findViewById(R.id.textView6);
		account=(EditText)findViewById(R.id.editText1);
		combination=(EditText)findViewById(R.id.editText2);
		confirmation=(Button)findViewById(R.id.button1);
		abortion=(Button)findViewById(R.id.button2);
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(this);
	}
	private void bindListeners() throws AppException{
		fillPurchaseInfo();
		editManage();
		AppUtil.bindBackListener(backButton);
		confirmation.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Toast.makeText(AlipayCashierActivity.this, "account="+account.getText()+
						",combination="+combination.getText(),Toast.LENGTH_LONG).show();
			}
		});
		abortion.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//TODO cancleOperation
			}
		});
		registerAlipay.setOnClickListener(new OnClickListener(	) {
			public void onClick(View arg0) {
				//TODO registerAlipayProgram
			}
		});
		useCreditCard.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
					final ProgressDialog pd= new ProgressDialog(AlipayCashierActivity.this);
					pd.setTitle("processing..");
					pd.setMessage("正在提交订单..");
					pd.show();
					DataService.THREAD_POOL.submit(new Runnable(){
						public void run(){
							try {
								if(info==null)
								new UpompPay().start_upomp_pay(AlipayCashierActivity.this, 
										dataService.getOrderInfoNsign(items));
								else 
									new UpompPay().start_upomp_pay(AlipayCashierActivity.this, 
											dataService.getOrderInfoNsign(info));
							} catch (AppException e) {
								e.printStackTrace();throw new RuntimeException(e);
							}
							pd.dismiss();
						}
					});
			}
		});
		useBankCard.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO useBankCardProcedure 
			}
		});
		retrievePwd.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO retrievePwdProcedure
			}
		});		
		
	
	}
	private void editManage(){
		AppUtil.bindEditTextNtextDisposer(account, accountDisposer);
		AppUtil.bindEditTextNtextDisposer(combination, combinationDisposer);
	}
	private void fillPurchaseInfo() throws AppException{
		Object obj=getIntent().getExtras().get(IConstants.MERCHANDISE_INFO);
		if(obj instanceof Map) {
			info=(Map<String,Object>)obj;
			merchandise.setText(info.get("merchandise").toString());
			seller.setText(info.get("seller").toString());
			NumberFormat nf=NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			sum.setText(nf.format(Double.parseDouble(info.get("unitPrice").toString())*(Integer)info.get("buyingQuantity")));
		}
		if(obj instanceof List) {
			items=(List<CartItem>)obj;
			merchandise.setText("多件商品");
			seller.setText("多件商品信息");
			NumberFormat nf=NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			sum.setText(nf.format(AppUtil.calculateTotalCost(items)));
		}
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		if(data!=null){
			try {
				byte[] xmlData=data.getExtras().getByteArray("xml");
				final PayResultInfo payResultInfo=XmlParser.parsePayResultXml(xmlData);
				DataService.THREAD_POOL.submit(new Runnable(){
					public void run(){
						try {
							AppUtil.getNetUtilInstance().noticeServerAfterPay(payResultInfo.merchantOrderId);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
				Log.d("infoTag",payResultInfo.toString()+" @OrderConfirmationActivity");
				Log.d("infoTag",new String(xmlData,"utf-8"));
				if("0000".equals(payResultInfo.respCode)){
					AppUtil.turnShoppingCartItemsToHistory();
					data.setClass(this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
					.setAction(IConstants.AFTER_PAY);
					startActivity(data);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("支付失败，重试？");
					builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface parent, int position) {
							Log.d("infoTag",position+" @OrderConfirmationActivity");
						}
					});
					builder.setNegativeButton("放弃",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface parent, int position) {
							Log.d("infoTag",position+" @OrderConfirmationActivity");
							data.setClass(AlipayCashierActivity.this,HomeActivity.class).
							setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setAction(IConstants.AFTER_PAY);
							startActivity(data);
						}
					});
					builder.create().show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			Log.e("infoTag","intent is null");
	}
}
