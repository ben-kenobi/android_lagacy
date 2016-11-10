package com.yf.contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.yf.accountmanager.ItemListActivity;
import com.yf.accountmanager.R;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ContactsService;
import com.yf.accountmanager.sqlite.ISQLite.ContactColumns;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.MenuUtil;

public class ContactsActivity extends ItemListActivity {

	private EditText searchField;

	private View searchFieldDisposer,scrollUp;

	private ViewGroup searchContainer;

	private String searchColumnName;
	
	private boolean search;
	
	private ContactListAdapter mainAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		platform = IConstants.CONTACTS;
		resId = R.layout.activity_contacts;
		super.onCreate(savedInstanceState);
		mainAdapter=(ContactListAdapter)cursorAdapter;
		mainAdapter.queryAll(platform);
	}

	/**
	 * base procedure
	 */
	protected void init() {
		lv = (ListView) findViewById(R.id.listView1);
		deleteButton = findViewById(R.id.imageButton1);
		cursorAdapter = new ContactListAdapter(this, deleteButton);
		searchContainer = (ViewGroup) findViewById(R.id.linearLayout1);
		searchContainer.setVisibility(View.GONE);
		scrollUp = searchContainer.findViewById(R.id.imageButton2);
		searchFieldDisposer = searchContainer.findViewById(R.id.button1);
		searchField = (EditText) searchContainer.findViewById(R.id.editText1);
	}
	
	@Override
	protected void bindListeners() {
		super.bindListeners();
		CommonUtils.bindEditTextNtextDisposer(searchField, searchFieldDisposer);
		scrollUp.setOnClickListener(this);
		searchField.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void onTextChanged(CharSequence content, int start, int before,
					int count) {
				if(search)
					searchContacts(content.toString());
			}
		});
	}
	
	

	
	@Override
	public void onClick(View v) {
		if(v==scrollUp){
			collapseSearchContainer();
		}else
			super.onClick(v);
	}

	@Override
	protected boolean onOtherMenuItemClicked(int itemId) {
		collapseSearchContainer();
		if (itemId == R.id.byGroup) {
			initPickerNshow();
		} else if (itemId == R.id.byName) {
			setSearchColumn(ContactColumns.NAME);
		} else if (itemId == R.id.byPhone) {
			setSearchColumn(ContactColumns.PHONE);
		} else if (itemId == R.id.byTel) {
			setSearchColumn(ContactColumns.TEL);
		} else if (itemId == R.id.byChatAccount) {
			setSearchColumn(ContactColumns.CHATACCOUNT);
		} else if (itemId == R.id.byEmail) {
			setSearchColumn(ContactColumns.EMAIL);
		}
		return true;
	}
	
	
	private void setSearchColumn(String name){
		searchColumnName=name;
		searchContainer.setVisibility(View.VISIBLE);
		search=true;
		searchField.setHint(name);
		if(searchField.requestFocus())
		MenuUtil.showSoftInput(searchField, this);
	}

	
	private void searchContacts(String fragment){
		cursorAdapter.changeCursor(ContactsService.queryByColFragment(fragment,searchColumnName));
	}
	
	@Override
	protected void backOperation() {
		if (searchContainer.getVisibility() == View.VISIBLE) {
			collapseSearchContainer();
		} else
			super.backOperation();
	}
	
	private void collapseSearchContainer(){
		search=false;
		searchContainer.setVisibility(View.GONE);
		searchField.setText("");
		MenuUtil.hideSoftInput(searchField, this);
	}

	@Override
	protected void dismissAllDialogs() {
		super.dismissAllDialogs();
		if(mainAdapter!=null&&mainAdapter.numsDialog!=null)
			mainAdapter.numsDialog.dismiss();
	}
}
