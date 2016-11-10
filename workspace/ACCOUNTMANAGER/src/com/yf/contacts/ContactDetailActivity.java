package com.yf.contacts;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.yf.accountmanager.R;
import com.yf.accountmanager.adapter.PickerAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ContactsService;
import com.yf.accountmanager.sqlite.ISQLite.ContactColumns;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.MenuUtil;
import com.yf.accountmanager.util.SystemUtil;

public class ContactDetailActivity extends Activity implements View.OnClickListener{
	private int resId = R.layout.activity_contactdetail_1, editButtonId = 0x11;
	private EditText name, group, phone, phone2, tel, chatAccount,
			email, postscript;

	private Button nameDisposer, groupDisposer, phoneDisposer,
			phone2Disposer, telDisposer, chatAccountDisposer,
			emailDisposer, postscriptDisposer,save;

	private ImageButton pickName, pickGroup, pickPhone, pickPhone2,
			pickTel, pickChatAccount, pickEmail, pickPostscript,
			phoneMes,phoneCall,phone2Mes,phone2Call,telMes,telCall;

	private LinearLayout container;

	private ContentValues content;

	private View[] ableGroup, visibleGroup,callNmesGroup;

	private EditText[] textSequence;
	
	private String[] columnSequence = new String[] { ContactColumns.NAME,
			ContactColumns.GROUP,ContactColumns.PHONE,
			ContactColumns.PHONE2,ContactColumns.TEL,
			ContactColumns.CHATACCOUNT, ContactColumns.EMAIL,
			ContactColumns.PS};

	private CustomizedDialog picker;

	private PickerAdapter pickerAdapter;
	
	private boolean contentChanged=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		MenuUtil.commonActionBarDisplayOption(this);
		init();
		content = (ContentValues) getIntent().getParcelableExtra(
				IConstants.CONTACTS);
		if (content == null) {
			if (savedInstanceState == null)
				requestFocus();
			save.setText("保存信息");
			save.setBackgroundResource(R.drawable.selector_blue_radius_button);
		} else {
			fillInfos();
			if (savedInstanceState == null)
				blockFocus();
			save.setText("更新信息");
			save.setBackgroundResource(R.drawable.selector_blue_button);
		}
		bindListeners();
	}

	

	
	/***
	 * base procedure
	 */
	private void init() {
		container = (LinearLayout) findViewById(R.id.linearLayout1);
		nameDisposer = (Button) findViewById(R.id.button1);
		groupDisposer = (Button) findViewById(R.id.button2);
		phoneDisposer = (Button) findViewById(R.id.button3);
		phone2Disposer = (Button) findViewById(R.id.button4);
		telDisposer = (Button) findViewById(R.id.button5);
		chatAccountDisposer = (Button) findViewById(R.id.button6);
		emailDisposer = (Button) findViewById(R.id.button7);
		postscriptDisposer = (Button) findViewById(R.id.button8);
		save = (Button) findViewById(R.id.button16);
		pickName = (ImageButton) findViewById(R.id.imageButton1);
		pickGroup = (ImageButton) findViewById(R.id.imageButton2);
		pickPhone = (ImageButton) findViewById(R.id.imageButton3);
		phoneMes = (ImageButton) findViewById(R.id.imageButton23);
		phoneCall = (ImageButton) findViewById(R.id.imageButton13);
		pickPhone2 = (ImageButton) findViewById(R.id.imageButton4);
		phone2Mes = (ImageButton) findViewById(R.id.imageButton24);
		phone2Call = (ImageButton) findViewById(R.id.imageButton14);
		pickTel = (ImageButton) findViewById(R.id.imageButton5);
		telMes = (ImageButton) findViewById(R.id.imageButton25);
		telCall = (ImageButton) findViewById(R.id.imageButton15);
		pickChatAccount = (ImageButton) findViewById(R.id.imageButton6);
		pickEmail = (ImageButton) findViewById(R.id.imageButton7);
		pickPostscript = (ImageButton) findViewById(R.id.imageButton8);
		name = (EditText) findViewById(R.id.editText1);
		group = (EditText) findViewById(R.id.editText2);
		phone = (EditText) findViewById(R.id.editText3);
		phone2 = (EditText) findViewById(R.id.editText4);
		tel = (EditText) findViewById(R.id.editText5);
		chatAccount = (EditText) findViewById(R.id.editText6);
		email = (EditText) findViewById(R.id.editText7);
		postscript = (EditText) findViewById(R.id.editText8);
		
		ableGroup = new View[] { pickName, pickGroup, pickPhone,
				pickPhone2, pickTel, pickChatAccount, pickEmail,
				pickPostscript,  save };
		visibleGroup = new View[] { nameDisposer, groupDisposer,
				phoneDisposer, phone2Disposer, telDisposer,
				chatAccountDisposer, emailDisposer, postscriptDisposer };

		textSequence = new EditText[] { name, group, phone, phone2,
				tel, chatAccount, email, postscript};
		
		callNmesGroup=new View[]{phoneMes,phone2Mes,telMes,phoneCall,phone2Call,telCall};
	}

	

	private void initNshowPicker(View v) {
		int index = indexOfViewInGroup(ableGroup,v);
		if(index==-1||index>columnSequence.length-1)  return;
		if (picker == null) {
			picker = CustomizedDialog.initOptionDialog(this);
			picker.lv.setAdapter(pickerAdapter=new PickerAdapter(this));
			picker.lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					textSequence[indexOfColumn(pickerAdapter.getCursor().getColumnName(0))].setText
					(pickerAdapter.getCursor().getString(0));
					picker.dismiss();
				}
			});
		}
		Cursor cursor=ContactsService.queryDistinctColumnWithId(columnSequence[index]);
		pickerAdapter.changeCursor(cursor);
		picker.title.setText(columnSequence[index]);
		picker.show();
	}
	private int indexOfColumn(String str){
		for(int i=0;i<columnSequence.length;i++){
			if(str.equalsIgnoreCase(columnSequence[i]))
				return i;
		}
		return -1;
	}
	
	private int indexOfViewInGroup(View[] group ,View v) {
		for(int i=0;i<group.length;i++){
			if(v==group[i])
				return i;
		}
		return -1;
	}

	private void saveAccount() {
		ContentValues cv = encapsulateAccountInfo();
		if (ContactsService.insert(cv) > 0) {
			CommonUtils.toast("保存成功");
			blockFocus();
			contentChanged=true;
		} else {
			CommonUtils.toast("保存失败");
		}
	}

	private void updateAccount() {
		ContentValues cv = encapsulateAccountInfo();
		if (ContactsService.updateContacts(cv,
				content.getAsInteger(ContactColumns.ID))) {
			CommonUtils.toast("更新成功");
			blockFocus();
			contentChanged=true;
		} else {
			CommonUtils.toast("更新失败");
		}
	}

	private ContentValues encapsulateAccountInfo() {
		ContentValues cv = new ContentValues();
		cv.put(columnSequence[0], name.getText().toString());
		cv.put(columnSequence[1], group.getText().toString());
		cv.put(columnSequence[2], phone.getText().toString());
		cv.put(columnSequence[3], phone2.getText().toString());
		cv.put(columnSequence[4], tel.getText().toString());
		cv.put(columnSequence[5], chatAccount.getText().toString());
		cv.put(columnSequence[6], email.getText().toString());
		cv.put(columnSequence[7], postscript.getText()
				.toString());
		return cv;
	}

	private void bindListeners() {
		for (View v : ableGroup)
			v.setOnClickListener(this);
		
		if(callNmesGroup!=null)
			for(int i=0;i<callNmesGroup.length;i++)
				callNmesGroup[i].setOnClickListener(this);
			
		CommonUtils.bindEditTextNtextDisposer(name, nameDisposer);
		CommonUtils.bindEditTextNtextDisposer(group, groupDisposer);
		CommonUtils.bindEditTextNtextDisposer(phone, phoneDisposer);
		CommonUtils.bindEditTextNtextDisposer(phone2, phone2Disposer);
		CommonUtils.bindEditTextNtextDisposer(tel, telDisposer);
		CommonUtils.bindEditTextNtextDisposer(chatAccount, chatAccountDisposer);
		CommonUtils.bindEditTextNtextDisposer(email, emailDisposer);
		CommonUtils.bindEditTextNtextDisposer(postscript,
				postscriptDisposer);
	}

	private void fillInfos() {
		name.setText(content.getAsString(columnSequence[0]));
		group.setText(content.getAsString(columnSequence[1]));
		phone.setText(content.getAsString(columnSequence[2]));
		phone2.setText(content.getAsString(columnSequence[3]));
		tel.setText(content.getAsString(columnSequence[4]));
		chatAccount.setText(content.getAsString(columnSequence[5]));
		email.setText(content.getAsString(columnSequence[6]));
		postscript.setText(content
				.getAsString(columnSequence[7]));
	}

	
	
	
	
	/***
	 * 
	 * override
	 */
	
	public void onClick(View v) {
		if (v == save) {
			String na = name.getText().toString();
			if (TextUtils.isEmpty(na)) {
				CommonUtils.toast("名字必须填写");
				name.requestFocus();
				return;
			}
			if (content == null)
				saveAccount();
			else
				updateAccount();

		} else if(contain(ableGroup, v)) {
			initNshowPicker(v);
		}else if(contain(callNmesGroup,v)){
			handleMesOrCall(v);
		}
	}
	
	private void handleMesOrCall(View v){
		int index = indexOfViewInGroup(callNmesGroup, v);
		int editTextIndex=index%3+2;
		int action=index/3;
		if(action==0){
			SystemUtil.intentToMessageType2(textSequence[editTextIndex].getText().toString(), this,"");
		}else if(action==1){
			SystemUtil.intentToDial(textSequence[editTextIndex].getText().toString(), this);
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			backOperation();
			return true;
		} else if (id == editButtonId) {
			toggleFocusState();
			return true;
		} else
			return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, editButtonId, 0, R.string.edit)
				.setIcon(android.R.drawable.ic_menu_edit)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("block", save.getVisibility()==View.GONE);
		if(picker!=null)
			picker.dismiss();
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean("block")) {
			blockFocus();
		} else {
			requestFocus();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(pickerAdapter!=null)
			pickerAdapter.changeCursor(null);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			backOperation();
			return true;
		}else	return super.onKeyDown(keyCode, event);
	}
	
	private void backOperation() {
		if(contentChanged)
			setResult(1);
		finish();
	}
	
	private void toggleFocusState(){
		if(save.getVisibility()==View.GONE)
			requestFocus();
		else 
			blockFocus();
		
	}
	
	private void blockFocus(){
		CommonUtils.blockFocus(container, ableGroup, visibleGroup,textSequence);
		if(callNmesGroup!=null)
			for(int i=0;i<callNmesGroup.length;i++){
				callNmesGroup[i].setVisibility(TextUtils.isEmpty(textSequence[i%3+2]
						.getText().toString())?View.GONE:View.VISIBLE);
			}
			
		
	}
	
	private void requestFocus(){
		CommonUtils.requetFocus(container, ableGroup,textSequence);
		if(callNmesGroup!=null)
			for(int i=0;i<callNmesGroup.length;i++)
				callNmesGroup[i].setVisibility(View.GONE);
			
	}
	
	private boolean contain(View[] group,View tar){
		if(group!=null)
			for(int i=0;i<group.length;i++)
				if(group[i]==tar)
					return true;
		return false;
	}
}
