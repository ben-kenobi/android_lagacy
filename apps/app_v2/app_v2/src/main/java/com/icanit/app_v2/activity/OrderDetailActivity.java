package com.icanit.app_v2.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.adapter.OrderDetailLvAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.MerchantOrderItems;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.XmlParser;

public class OrderDetailActivity extends Activity implements OnClickListener{
	private TextView sum, orderTime, deliveryInfo, address, phone,
			orderStatus, orderId, orderPS;
	private ImageButton backButton, callButton;
	private Button orderOperationButton;
	private ListView lv;
	private View lvHeader;
	private int headerResId = R.layout.header4lv_orderdetail;
	private OrderDetailLvAdapter adapter;
	private PayResultInfo payResultInfo;
	private FrameLayout bottomFrame;
	private AppOrder order;
	private boolean contentChnaged=false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		Object obj= getIntent().getSerializableExtra(IConstants.ORDER_INFO);
		if(obj!=null) order=(AppOrder)obj;
		else{
			finish();return;
		}
		init();
		initLv();
		bindListeners();
		try {
			fillOrderResultInfo2();
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	private void initLv() {
		lv.setAdapter(adapter = new OrderDetailLvAdapter(this, sum));
	}

	private void fillOrderResultInfo2() throws AppException {
		orderTime.setText("����ʱ�䣺"
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
						.format(order.orderTime));
		orderStatus.setText(AppUtil.getOrderStatusDescByOrderStatus(order.status));
		orderId.setText("������ţ�" + order.orderNumber);
		orderPS.setText("������ע��" + "����");
		deliveryInfo.setText((order.type==0? "�ջ�����Ϣ��"+order.userName:order.type==1?"�������ͣ��˻���ֵ����":""));
		address.setText(order.address == null ? "��" : order.address);
		phone.setText(order.contactPhone==null?"��":order.contactPhone);
		sum.setText("�ܽ��:��"+String.format("%.2f",order.sum));
		if(order.status==0){
			orderOperationButton.setText("����ȥ֧��");
			bottomFrame.setVisibility(View.VISIBLE);
		}else if(order.status==1){
			orderOperationButton.setText("ȡ������");
			bottomFrame.setVisibility(View.VISIBLE);
		}else{
			orderOperationButton.setText("...");
			bottomFrame.setVisibility(View.VISIBLE);
			orderOperationButton.setEnabled(false);
		}
		
		if(order.type==0)
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					final List<MerchantOrderItems> merOrderItemsList = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(OrderDetailActivity.this)
							.getMerchantOrderItemsListByOrderId(order.id);
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							adapter.setMerOrderItemsList(merOrderItemsList);
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void fillOrderResultInfo() throws Exception {
		bottomFrame.setVisibility(View.VISIBLE);
		orderOperationButton.setText("ȡ������");
		byte[] xmlData =getIntent().getExtras().getByteArray("xml");
		payResultInfo = XmlParser.parsePayResultXml(xmlData);
		orderTime.setText("����ʱ�䣺"
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
						.format(new SimpleDateFormat("yyyyMMddHHmmss")
								.parse(payResultInfo.merchantOrderTime)));
		orderStatus.setText(payResultInfo.respDesc == null ? "δ֧��"
				: payResultInfo.respDesc);
		orderId.setText("������ţ�" + payResultInfo.merchantOrderId);
		orderPS.setText("������ע��" + "����");
		UserContact contact = AppUtil.getUserContactFromApplication();
		if (contact == null)
			deliveryInfo.setText("");
		else {
			deliveryInfo.setText("�ջ�����Ϣ��" + contact.username);
			address.setText(contact.address == null ? "��" : contact.address);
			phone.setText(contact.phoneNum);
		}
		sum.setText("�ܽ��:��"
				+ String.format("%.2f", getIntent().getExtras().getDouble("sum")));

		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					final List<MerchantOrderItems> merOrderItemsList = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(OrderDetailActivity.this)
							.getMerchantOrderItemsListByOrderNumNTime(
									payResultInfo.merchantOrderId,
									payResultInfo.merchantOrderTime);
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							adapter.setMerOrderItemsList(merOrderItemsList);
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void init() {
		lvHeader = LayoutInflater.from(this).inflate(headerResId, null, false);
		sum = (TextView) findViewById(R.id.textView1);
		orderTime = (TextView) lvHeader.findViewById(R.id.textView2);
		deliveryInfo = (TextView) lvHeader.findViewById(R.id.textView3);
		address = (TextView) lvHeader.findViewById(R.id.textView4);
		phone = (TextView) lvHeader.findViewById(R.id.textView5);
		orderStatus = (TextView) lvHeader.findViewById(R.id.textView7);
		orderId = (TextView) lvHeader.findViewById(R.id.textView8);
		orderPS = (TextView) lvHeader.findViewById(R.id.textView9);
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		callButton = (ImageButton) findViewById(R.id.imageButton2);
		orderOperationButton = (Button) findViewById(R.id.button1);
		lv = (ListView) findViewById(R.id.listView1);
		lv.addHeaderView(lvHeader, null, false);
		bottomFrame = (FrameLayout) findViewById(R.id.frameLayout3);
	}

	private void bindListeners() {
		callButton.setOnClickListener(this);
		orderOperationButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1&&resultCode==2&&data!=null){
			updateOrder(data);
		}else
		Log.e("infoTag","intent is null");
		
	}
	public void onClick(View v) {
		if(v==orderOperationButton)
			orderOperation();
		else if(v==backButton)
			backOperation();
		else if(v==callButton){
			AppUtil.intentToDial(IConstants.CUSTOM_SERVICE, this);
		}
		
	}
	private void orderOperation(){
		if(order==null||order.status==1){
			final ProgressDialog pd= DialogUtil.createProgressDialogNshow("processing..", "�����ύ����..", true, this);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final JSONObject jo=AppUtil.getServiceFactory().
							getDataServiceInstance(OrderDetailActivity.this).cancelOrder(order,payResultInfo);
						IConstants.MAIN_HANDLER.post(new Runnable(){
							public void run(){
								try {
									if(jo!=null&&jo.getBoolean(IConstants.RESPONSE_SUCCESS)){
										int status=jo.getInt("status");
										if(status==4)
										AppUtil.toast("�Ѿ��ύ��������\n����Ⱥ���֪ͨ�������ظ��ύ");
										else if(status==5) AppUtil.toast("���������ɹ��������ظ��ύ");
										orderOperationButton.setEnabled(false);
										orderOperationButton.setText("...");
										orderStatus.setText(AppUtil.getOrderStatusDescByOrderStatus(status));
										contentChnaged=true;
										order.status=status;
										
										AppUtil.updateUser();
									
									}else{
										AppUtil.toast("ȡ������ʧ��,���Ժ���");
									}
								} catch (JSONException e) {
									e.printStackTrace();
									AppUtil.toast("ȡ������ʧ��,���Ժ���");
								}
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
					pd.dismiss();
				}
			});
		}else if(order.status==0){
			startActivityForResult(new Intent(this,ReOrderConfirmationActivity.class).putExtra(IConstants.ORDER_INFO, order),1);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			backOperation();
			return true;
		}else 
		return super.onKeyDown(keyCode, event);
	}
	
	private void backOperation(){
		Serializable seri=getIntent().getSerializableExtra(IConstants.BACK_CLASS);
		if(seri==null){
			if(contentChnaged)
				setResult(2, new Intent().putExtra(IConstants.ORDER_INFO, order));
			this.finish();
		}else{
			startActivity(new Intent(this,(Class)seri).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
	}
	

	protected void onNewIntent(Intent intent) {
		updateOrder(intent);
	}
	
	private void updateOrder(Intent intent){
		Serializable seri = intent.getSerializableExtra(IConstants.ORDER_INFO);
		if(seri!=null){
			order=(AppOrder)seri;
			contentChnaged=true;
			try {
				fillOrderResultInfo2();
			} catch (AppException e) {
				e.printStackTrace();
			}
		}
	}
	
}
