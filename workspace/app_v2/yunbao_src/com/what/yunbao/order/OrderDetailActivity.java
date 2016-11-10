package com.what.yunbao.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.what.yunbao.R;
import com.what.yunbao.sync.http.AsyncHttpClient;
import com.what.yunbao.sync.http.AsyncHttpResponseHandler;
import com.what.yunbao.test.TT;
import com.what.yunbao.util.AppOrder;
import com.what.yunbao.util.AppOrderItems;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.OrderItemJSONParser;
import com.what.yunbao.util.OrderMoreJSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailActivity extends Activity{
	private ImageButton tel_ib;
	private ImageButton back_btn;
	private String phone = "10086";
	private TextView time_tv;
	private TextView addr_tv;
	private TextView tel_tv;
	private TextView status_tv;
	private TextView number_tv;
	private TextView remark_tv;
	private ListView orderItem_lv;
	
	private ProgressBar loadDetails_pb;
	private ProgressBar loadItems_pb;
	private long orderId;
	private Toast mToast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		setUpView();		
//		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.getTime()));
		MyOnclickListener listener = new MyOnclickListener();
		tel_ib.setOnClickListener(listener);
		back_btn.setOnClickListener(listener);
		
		orderId = getIntent().getLongExtra(Intent.EXTRA_UID, 0);
		
		getOrderDetails();
		getOrderItems();
		orderItem_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView goodId_tv = (TextView) arg1.findViewWithTag("GOOD_ID"+arg2);
				TextView merchantId_tv = (TextView) arg1.findViewWithTag("MERCHANT_ID"+arg2);
				final long goodId = Long.valueOf(goodId_tv.getText()+"");
				final int merchantId = Integer.valueOf(merchantId_tv.getText()+"");
				Toast.makeText(OrderDetailActivity.this, goodId_tv.getText()+"//"+merchantId_tv.getText(), 2000).show();
				List<String> list = new ArrayList<String>();
				list.add("查看商品详情");
				list.add("进入商品所属店铺");
				ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
						OrderDetailActivity.this, R.layout.dialog_list_item, R.id.tv_list_item, list);
				
				AlertDialog dialog = new AlertDialog.Builder(OrderDetailActivity.this)
					      				.setAdapter(listAdapter,
					      							new DialogInterface.OnClickListener() {
												         @Override
												         public void onClick(DialogInterface dialog,
												           int which) {
												            if (which == 0) {
																Toast.makeText(OrderDetailActivity.this,"查看商品界面//商品id"+goodId, 2000).show();
															} else if (which == 1) {
																Intent intent = new Intent(OrderDetailActivity.this,TT.class);
																intent.setAction(Intent.ACTION_VIEW);
																intent.putExtra(Intent.EXTRA_UID, Long.valueOf(merchantId));
																//merchantid是long类型？？
																startActivity(intent);
															} 
												        	 
												         }
							
												     }).show(); 			
			}
		});
	}
	private void setUpView(){
		tel_ib = (ImageButton) findViewById(R.id.ib_order_detail_tel);
		back_btn = (ImageButton) findViewById(R.id.ib_order_detail_back);
		time_tv = (TextView) findViewById(R.id.tv_order_detail_time);
		addr_tv = (TextView) findViewById(R.id.tv_order_detail_addr);
		tel_tv = (TextView) findViewById(R.id.tv_order_detail_tel);
		status_tv = (TextView) findViewById(R.id.tv_order_detail_status);
		number_tv = (TextView) findViewById(R.id.tv_order_detail_number);
		remark_tv = (TextView) findViewById(R.id.tv_order_detail_remark);
		orderItem_lv = (ListView) findViewById(R.id.lv_order_detail_item_list);
		loadDetails_pb = (ProgressBar) findViewById(R.id.pb_load_details);
		loadItems_pb = (ProgressBar) findViewById(R.id.pb_load_items);
		
	}
	
	private class MyOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.ib_order_detail_tel){
				Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone)); 
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        	startActivity(intent);
			}else{
				OrderDetailActivity.this.finish();
			}							
		}		
	}
	/**
	 * 获取订单详情
	 *
	 */
	private void getOrderDetails(){
		String url = "http://192.168.137.1:8080/fc/getOrderDetails?orderId="+orderId+"&orderItem=false";
		AsyncHttpClient client = new AsyncHttpClient();
	    client.get(url, new AsyncHttpResponseHandler(){
	
	        @Override
	        public void onStart() {
	        }
	
	        @Override
	        public void onSuccess(String result) {
	        	OrderMoreJSONParser parser = new OrderMoreJSONParser();
	        	AppOrder order;
				try {
					order = parser.parse(new JSONObject(result));
				
		        	time_tv.setText("订单时间："+order.getOrderTime());
					addr_tv.setText(order.getAddress());	
					
					phone = order.getPhone();
					tel_tv.setText(phone);
				
					if(order.getStatus()==1){
						status_tv.setText("您的订单已发送");
					}else{
						status_tv.setText("您的订单未发送");
					}
					number_tv.setText(order.getOrderNumber());
					remark_tv.setText("数据表无该字段");
				
				} catch (JSONException e) {					
					e.printStackTrace();
				}
	        }
	
	        @Override
	        public void onFailure(Throwable arg0) {
	        	CommonUtil.showToast("加载订单详情失败", mToast);
	        }
	
	        @Override
	        public void onFinish() {
	        	loadDetails_pb.setVisibility(View.GONE);
	        }
	    });
	
	}
	

	/**
	 * 获取订单条目详情
	 *
	 */
	private void getOrderItems(){
		String url = "http://192.168.137.1:8080/fc/getOrderDetails?orderId="+orderId+"&orderItem=true";
		
		AsyncHttpClient client = new AsyncHttpClient();		
		client.get(url, new AsyncHttpResponseHandler(){
			@Override
	        public void onStart() {
	        }
	
	        @Override
	        public void onSuccess(String result) {
	        	OrderItemJSONParser parser = new OrderItemJSONParser();
	        	List<AppOrderItems> list;
				try {
					list = parser.parse(new JSONObject(result));
					OrderDetailItemAdapter adapter = new OrderDetailItemAdapter(OrderDetailActivity.this, list);
					orderItem_lv.setAdapter(adapter);
				} catch (JSONException e) {					
					e.printStackTrace();
				}
	        }
	
	        @Override
	        public void onFailure(Throwable arg0) {
	        	CommonUtil.showToast("加载订单条目失败", mToast);
	        }
	
	        @Override
	        public void onFinish() {
	        	loadItems_pb.setVisibility(View.GONE);
	        }
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
