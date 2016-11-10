package com.yf.accountmanager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.yf.accountmanager.action.BasePopWindow;
import com.yf.accountmanager.adapter.AccountListAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;
import com.yf.accountmanager.ui.StrAryPopWindow;
import com.yf.accountmanager.ui.StrAryPopWindow.OnPopListClickListener;

public class AccountListActivity extends ItemListActivity {
	
	public static final  String[]	pickerOptions=new String[]{"查看组别","站点名称","查看邮箱","查看手机","通行证号"};
	
	public static final String[] optionColumnMapping=new String[]{AccountColumns.GROUP,AccountColumns.SITENAME,
			AccountColumns.MAILBOX,AccountColumns.PHONENUM,AccountColumns.PASSPORT}; 

	private BasePopWindow pickerOptionsPop;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		platform = IConstants.ACCOUNT;
		resId=R.layout.activity_accountlist;
		super.onCreate(savedInstanceState);
		cursorAdapter.queryAll(platform);
	}

	/**
	 * base procedure
	 */

	protected void init() {
		lv = (ListView) findViewById(R.id.listView1);
		deleteButton = findViewById(R.id.relativeLayout1);
		cursorAdapter=new AccountListAdapter(this, deleteButton);
	}

	public void onSearchMenuItemClicked() {
		initPickerNshow();
	}

	
	@Override
	protected void initPicker() {
		super.initPicker();
		picker.dropDown.setVisibility(View.VISIBLE);
		picker.dropDown.setText(pickerOptions[0]);
		picker.dropDown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				initNshowGroupOption();
			}
		});
	}
	
	private void initNshowGroupOption() {
		if(pickerOptionsPop==null){
			try {
				pickerOptionsPop=new StrAryPopWindow(picker.dropDown,this,pickerOptions,true,
						(int)getResources().getDimension(R.dimen.dp_120),
						new OnPopListClickListener() {
					public void onclick(int position) {
						if(pickerAdapter!=null)
							pickerAdapter.requery(platform,optionColumnMapping[position] );
					}
				});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if(pickerOptionsPop!=null)
			pickerOptionsPop.showAsDropDown();
	}
	
	
	@Override
	protected void dismissAllDialogs() {
		super.dismissAllDialogs();
		if(pickerOptionsPop!=null)
			pickerOptionsPop.dismiss();
	}
	
	/**class GroupOptionPopupWindow extends BasePopAction implements OnItemClickListener{
		
		ListView lv;
		
		StringAryAdapter adapter;
		
		Context context;
		
		
		public GroupOptionPopupWindow(TextView trig,Context context) throws Throwable {
			super(R.layout.popupwindow_options, LayoutInflater.from(context),
					trig,(int)context.getResources().getDimension(R.dimen.dp_120),-2);
			this.context=context;
			execute();
			
		}

		@Override
		public void execute() throws Throwable {
			lv=(ListView)contentView.findViewById(R.id.listView1);
			lv.setAdapter(adapter=new StringAryAdapter(context));
			adapter.setSary(pickerOptions);
			lv.setOnItemClickListener(this);
		}

		
		
		
		@Override
		public void resetTrigger() {}

		@Override
		public void onItemClick(AdapterView<?> parent, View self, int position,
				long id) {
			trigger.setText(String.valueOf(adapter.getItem(position)));
			if(pickerAdapter!=null)
				pickerAdapter.requery(platform,optionColumnMapping[position] );
			pop.dismiss();
		}
		
	}

*/
	
	

}
