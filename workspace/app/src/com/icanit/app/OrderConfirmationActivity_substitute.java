package com.icanit.app;

import java.io.OutputStream;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icanit.app.R;
import com.icanit.app.adapter.CartItemInfoListAdapter;
import com.icanit.app.common.IConstants;
import com.icanit.app.entity.CartItem;
import com.icanit.app.entity.PayResultInfo;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.XmlParser;

public class OrderConfirmationActivity_substitute extends Activity {
	private TextView finalPay,tip,alipay,creditCard,bankCard;
	private ImageButton backButton;
	private Intent intent;
	private ArrayList<CartItem> items;
	private DataService dataService;
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirmation_substitute);
		try {
			init();fillTextInfo();initLv();bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	private void init() throws AppException{
		finalPay = (TextView)findViewById(R.id.textView1);
		tip = (TextView)findViewById(R.id.textView2);
		alipay = (TextView)findViewById(R.id.textView6);
		creditCard = (TextView)findViewById(R.id.textView7);
		bankCard= (TextView)findViewById(R.id.textView8);
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		lv=(ListView)findViewById(R.id.listView1);
		intent=getIntent();
		items=(ArrayList)AppUtil.appContext.shoppingCartList;
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(this);
	}
	private void fillTextInfo(){
		StyleSpan bold = new StyleSpan(Typeface.BOLD);
		String s;SpannableString ss;
		NumberFormat nf=NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		s="需支付   ￥"+nf.format(AppUtil.calculateTotalCost(items));
		ss=new SpannableString(s);
		ss.setSpan(bold, s.indexOf("￥")+1,s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		finalPay.setText(ss);
		s="团购成功后，团购券序列号将发送到"+AppUtil.getLoginPhoneNum();
		ss=new SpannableString(s);
		if(s.indexOf("1")!=-1){
		ss.setSpan(bold, s.indexOf("1"),s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.rgb(0x43, 0x6e,0xee)),
				s.indexOf("1"),s.length()	, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);}
		tip.setText(ss);
	}
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		alipay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(OrderConfirmationActivity_substitute.this, AlipayCashierActivity.class)
				.putExtra(IConstants.MERCHANDISE_INFO, items);
				startActivity(intent);
			}
		});
		creditCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog pd= new ProgressDialog(OrderConfirmationActivity_substitute.this);
				pd.setTitle("processing..");
				pd.setMessage("正在提交订单..");
				pd.show();
				DataService.THREAD_POOL.submit(new Runnable(){
					public void run(){
						try {
							new UpompPay().start_upomp_pay(OrderConfirmationActivity_substitute.this, 
									dataService.getOrderInfoNsign(items));
						} catch (AppException e) {
							e.printStackTrace();throw new RuntimeException(e);
						}
						pd.dismiss();
					}
				});
			}
		});
		bankCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO bankCardOperation
			}
		});
	}
	private void initLv(){
		lv.setAdapter(new CartItemInfoListAdapter(this, items));
	}
	
	protected void onActivityResult(int requestCode, int resultCode,final  Intent data) {
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
							data.setClass(OrderConfirmationActivity_substitute.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
							.setAction(IConstants.AFTER_PAY);
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
