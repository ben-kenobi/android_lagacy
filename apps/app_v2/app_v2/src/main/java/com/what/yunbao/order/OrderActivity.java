package com.what.yunbao.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.sync.http.AsyncHttpClient;
import com.what.yunbao.sync.http.AsyncHttpResponseHandler;
import com.what.yunbao.test.TestActivity;
import com.what.yunbao.util.AppOrder;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.OrderJSONParser;


public class OrderActivity extends Activity implements OnScrollListener{
	private ListView order_lv;
	private RelativeLayout order_ll;
	private ImageButton order_home_ib;
	private ImageButton order_back_ib;
	private Toast mToast;
	private ProgressBar load_pb;
	private static String TAG = "OrderActivity";
	
	private OrderItemAdapter orderWeb_adapter;	
	private List<AppOrder> parse_orderList  = new ArrayList<AppOrder>();
	private int orderListCount = 0;
	
	//FootView
	private final LayoutParams mProgressBarLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private final LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private TextView mTipContent;
    private LinearLayout mLoadLayout;
    private int size = 6;
    private int page = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.order);
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		setUpView();
		LoadOrder(page, size, 0); 
		
	}
	
	private void setUpView() {
		load_pb = (ProgressBar) findViewById(R.id.pb_load_order);
		
		
		order_lv = (ListView) findViewById(R.id.lv_order_list);
		order_lv.setOnItemClickListener(new MyOnListItemClickListener());
		order_lv.setOnScrollListener(this);
		
		addFootView();
		
		MyOnClickListener mClickListener = new MyOnClickListener();
		order_home_ib = (ImageButton) findViewById(R.id.ib_order_home);
		order_home_ib.setOnClickListener(mClickListener);
		order_back_ib = (ImageButton) findViewById(R.id.ib_order_back);
		order_back_ib.setOnClickListener(mClickListener);	
	}
	
	/**
	 * when there is no orders
	 */
	private void notice(){
		order_ll = (RelativeLayout) findViewById(R.id.rl_order);			
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);    
	    lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
		    
		TextView tv = new TextView(this);
		tv.setId(1);
		tv.setText("您暂无订单记录\n去逛逛吧～");
		tv.setTextAppearance(this, android.R.attr.textAppearanceMedium);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(getResources().getColor(android.R.color.black));
		order_ll.addView(tv,lp1);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams  
            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);			
		lp2.addRule(RelativeLayout.ABOVE,1);
		lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		ImageView iv = new ImageView(this);
		iv.setImageResource(android.R.drawable.ic_menu_report_image);
		order_ll.addView(iv,lp2); 
		
		CommonUtil.showToast("您暂无订单记录，可通过测试列表模拟记录", mToast);
	}

	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_order_home) {
				CommonUtil.showToast("跳转到主页", mToast);
				startActivity(new Intent(OrderActivity.this,TestActivity.class));
			} else if (v.getId() == R.id.ib_order_back) {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}		
		}		
	}
	
	
	private class MyOnListItemClickListener implements OnItemClickListener{
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			TextView order_tv = (TextView) view.findViewWithTag(position);
			long orderId = Long.valueOf(order_tv.getText()+"");
			openItem(orderId);
        }
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return true;
		}else{
			return super.dispatchKeyEvent(event);
		}
	}
	
	private void openItem(long orderId){
		Intent intent = new Intent(OrderActivity.this,OrderDetailActivity.class);
		intent.putExtra(Intent.EXTRA_UID, orderId);
		startActivity(intent);
	}
	
	
	private void LoadOrder(int page,int size,int userId){
		String url = "http://192.168.137.1:8080/fc/getOrderList?page="+page+"&size="+size+"&userId="+userId;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
			}
			@Override
			public void onSuccess(String content) {
				OrderJSONParser parser = new OrderJSONParser();
				List<AppOrder> list;
				try {
					list = parser.parse(new JSONObject(content));
					for(int i=0; i<list.size(); i++){
						parse_orderList.add(list.get(i));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(parse_orderList.size() == 0){
					CommonUtil.showToast("您目前暂无订单记录", mToast);
					notice(); 
					return;
				}				
				if(parse_orderList.size() == orderListCount){
					CommonUtil.showToast("所有订单已加载完毕", mToast);
					order_lv.removeFooterView(mLoadLayout);
					return;
				}
				orderListCount = parse_orderList.size();
				if(orderWeb_adapter!=null){
					orderWeb_adapter.notifyDataSetChanged();
					return; 
				}
				orderWeb_adapter = new OrderItemAdapter(OrderActivity.this, parse_orderList);
				order_lv.setAdapter(orderWeb_adapter);
			}
			@Override
			public void onFailure(Throwable error) {
				CommonUtil.showToast("加载订单失败...", mToast);
			}
			@Override
			public void onFinish() {
				load_pb.setVisibility(View.GONE);
			}
		});
	}

    private void addFootView(){
        
    	//此布局被添加到ListView的Footer中
    	mLoadLayout = new LinearLayout(this);
        mLoadLayout.setMinimumHeight(60);
        mLoadLayout.setGravity(Gravity.CENTER);
        mLoadLayout.setOrientation(LinearLayout.HORIZONTAL);
        //布局中添加一个圆型进度条
        ProgressBar mProgressBar = new ProgressBar(this);
        mProgressBar.setPadding(0, 0, 15, 0);
        mLoadLayout.addView(mProgressBar, mProgressBarLayoutParams);
        //布局中添加提示信息。
        mTipContent = new TextView(this);
        mTipContent.setText("加载中...");
        mLoadLayout.addView(mTipContent, mTipContentLayoutParams);
        //获取ListView组件，并将"加载项"布局添加到ListView组件的Footer中 
//        mListView = getListView();
        order_lv.addFooterView(mLoadLayout);
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
		} else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if(view.getLastVisiblePosition() == view.getCount()-1){
				Log.i(TAG, "开始加载下一页...");
				LoadOrder(page++, size, 0);
			}
		} else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
		} 
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
