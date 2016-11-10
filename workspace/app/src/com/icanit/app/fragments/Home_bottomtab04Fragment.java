package com.icanit.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icanit.app.R;
import com.icanit.app.OrderConfirmationActivity_substitute;
import com.icanit.app.adapter.CartItemListAdapter;
import com.icanit.app.exception.AppException;
import com.icanit.app.util.AppUtil;

public class Home_bottomtab04Fragment extends AbstractRadioBindFragment{
	private View self;
	private ListView lv;
	private TextView totalCost;
	private Button orderConfirmation,clearCartButton;
	private ImageButton backButton;
	private CartItemListAdapter lvAdapter;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			init();bindListeners();initLv();
		} catch (AppException e) {
			e.printStackTrace();
		} 
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lvAdapter.notifyDataSetChanged();
		return self;
	}
	private void init() throws AppException{
		self = getActivity().getLayoutInflater().inflate(R.layout.fragment4home_04_shoppingcart,null,false);
		lv=(ListView)self.findViewById(R.id.listView1);
		orderConfirmation=(Button)self.findViewById(R.id.button1);
		totalCost=(TextView)self.findViewById(R.id.textView1);
		backButton=(ImageButton)self.findViewById(R.id.imageButton1);
		clearCartButton=(Button)self.findViewById(R.id.button2);
	}
	
	private void initLv() throws AppException {
		lv.setAdapter(lvAdapter=new CartItemListAdapter(getActivity(),totalCost,orderConfirmation,clearCartButton));
	}
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		orderConfirmation.setOnClickListener(new OnClickListener() {
			public void onClick(View self) {
				Intent intent=new Intent(getActivity(),OrderConfirmationActivity_substitute.class);
				startActivity(intent);
			}
		});
		clearCartButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					AppUtil.getServiceFactory().getShoppingCartServiceInstance(getActivity()).clearAllItems();
				} catch (AppException e) {
					e.printStackTrace();
				}
				lvAdapter.notifyDataSetChanged();
			}
		});
	}
}
