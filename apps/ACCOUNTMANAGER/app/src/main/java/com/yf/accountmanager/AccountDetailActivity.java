package com.yf.accountmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.yf.accountmanager.adapter.PickerAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;
import com.yf.accountmanager.sqlite.AccountService;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;

public class AccountDetailActivity extends Activity implements OnClickListener {

	private int resId = R.layout.activity_accountdetail, editButtonId = 0x11;

	private EditText mailBox, password, username, passport, sitename, phoneNum,
			website, identifyingCode, ask, answer,group;

	private Button mailBoxDisposer, passwordDisposer, usernameDisposer,
			passportDisposer, sitenameDisposer, phoneNumDisposer,
			websiteDisposer, identifyingCodeDisposer, askDisposer,
			answerDisposer, groupDisposer,save;

	private ImageButton pickMailBox, pickPassword, pickUsername, pickPassport,
			pickSite, pickPhoneNum, pickWebsite, pickIdentifyingCode, pickAsk,
			pickAnswer,pickGroup;

	private LinearLayout container;

	private ContentValues content;

	private View[] ableGroup, visibleGroup;

	private EditText[] textSequence;

	private String[] columnSequence = new String[] { AccountColumns.MAILBOX,
			AccountColumns.PASSWORD, AccountColumns.USERNAME,
			AccountColumns.PASSPORT, AccountColumns.SITENAME,
			AccountColumns.PHONENUM, AccountColumns.WEBSITE,
			AccountColumns.IDENTIFYING_CODE, AccountColumns.ASK,
			AccountColumns.ANSWER,AccountColumns.GROUP };

	private CustomizedDialog picker;

	private PickerAdapter pickerAdapter;
	
	private boolean contentChanged=false;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		init();
		if (content == null) {
			if (savedInstanceState == null)
				CommonUtils.requetFocus(container, ableGroup,textSequence);
			save.setText("保存信息");
			save.setBackgroundResource(R.drawable.selector_blue_radius_button);
		} else {
			if (savedInstanceState == null)
				CommonUtils.blockFocus(container, ableGroup, visibleGroup,textSequence);
			fillInfos();
			save.setText("更新信息");
			save.setBackgroundResource(R.drawable.selector_blue_button);
		}
		bindListeners();
	}

	private void init() {
		container = (LinearLayout) findViewById(R.id.linearLayout1);
		mailBoxDisposer = (Button) findViewById(R.id.button1);
		passwordDisposer = (Button) findViewById(R.id.button2);
		usernameDisposer = (Button) findViewById(R.id.button3);
		passportDisposer = (Button) findViewById(R.id.button4);
		sitenameDisposer = (Button) findViewById(R.id.button5);
		phoneNumDisposer = (Button) findViewById(R.id.button6);
		websiteDisposer = (Button) findViewById(R.id.button7);
		identifyingCodeDisposer = (Button) findViewById(R.id.button8);
		askDisposer = (Button) findViewById(R.id.button9);
		answerDisposer = (Button) findViewById(R.id.button10);
		groupDisposer = (Button) findViewById(R.id.button11);
		save = (Button) findViewById(R.id.button16);
		pickMailBox = (ImageButton) findViewById(R.id.imageButton1);
		pickPassword = (ImageButton) findViewById(R.id.imageButton2);
		pickUsername = (ImageButton) findViewById(R.id.imageButton3);
		pickPassport = (ImageButton) findViewById(R.id.imageButton4);
		pickSite = (ImageButton) findViewById(R.id.imageButton5);
		pickPhoneNum = (ImageButton) findViewById(R.id.imageButton6);
		pickWebsite = (ImageButton) findViewById(R.id.imageButton7);
		pickIdentifyingCode = (ImageButton) findViewById(R.id.imageButton8);
		pickAsk = (ImageButton) findViewById(R.id.imageButton9);
		pickAnswer = (ImageButton) findViewById(R.id.imageButton10);
		pickGroup = (ImageButton) findViewById(R.id.imageButton11);
		mailBox = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		username = (EditText) findViewById(R.id.editText3);
		passport = (EditText) findViewById(R.id.editText4);
		sitename = (EditText) findViewById(R.id.editText5);
		phoneNum = (EditText) findViewById(R.id.editText6);
		website = (EditText) findViewById(R.id.editText7);
		identifyingCode = (EditText) findViewById(R.id.editText8);
		ask = (EditText) findViewById(R.id.editText9);
		answer = (EditText) findViewById(R.id.editText10);
		group = (EditText) findViewById(R.id.editText11);
		content = (ContentValues) getIntent().getParcelableExtra(
				IConstants.ACCOUNT);
		ableGroup = new View[] { pickMailBox, pickPassword, pickUsername,
				pickPassport, pickSite, pickPhoneNum, pickWebsite,
				pickIdentifyingCode, pickAsk, pickAnswer,pickGroup, save };
		visibleGroup = new View[] { mailBoxDisposer, passwordDisposer,
				usernameDisposer, passportDisposer, sitenameDisposer,
				phoneNumDisposer, websiteDisposer, identifyingCodeDisposer,
				askDisposer, answerDisposer,groupDisposer };

		textSequence = new EditText[] { mailBox, password, username, passport,
				sitename, phoneNum, website, identifyingCode, ask, answer,group };
	}

	public void onClick(View v) {
		if (v == save) {
			String site = sitename.getText().toString();
			if (TextUtils.isEmpty(site)) {
				CommonUtils.toast("站点名称必须填写");
				sitename.requestFocus();
				return;
			}
			if (content == null)
				saveAccount();
			else
				updateAccount();

		} else {
			showPicker(v);
		}
	}

	private void showPicker(View v) {
		int index = indexOfViewInAbleGroup(v);
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
		Cursor cursor=AccountService.queryDistinctColumnWithId(columnSequence[index]);
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
	
	private int indexOfViewInAbleGroup(View v) {
		for(int i=0;i<ableGroup.length;i++){
			if(v==ableGroup[i])
				return i;
		}
		return -1;
	}

	private void saveAccount() {
		ContentValues cv = encapsulateAccountInfo();
		if (AccountService.addAccount(cv) > 0) {
			CommonUtils.toast("保存成功");
			CommonUtils.blockFocus(container, ableGroup, visibleGroup,textSequence);
			contentChanged=true;
		} else {
			CommonUtils.toast("保存失败");
		}
	}

	private void updateAccount() {
		ContentValues cv = encapsulateAccountInfo();
		if (AccountService.updateAccount(cv,
				content.getAsInteger(AccountColumns.ID))) {
			CommonUtils.toast("更新成功");
			CommonUtils.blockFocus(container, ableGroup, visibleGroup,textSequence);
			contentChanged=true;
		} else {
			CommonUtils.toast("更新失败");
		}
	}

	private ContentValues encapsulateAccountInfo() {
		ContentValues cv = new ContentValues();
		cv.put(AccountColumns.MAILBOX, mailBox.getText().toString());
		cv.put(AccountColumns.PASSPORT, passport.getText().toString());
		cv.put(AccountColumns.PASSWORD, password.getText().toString());
		cv.put(AccountColumns.PHONENUM, phoneNum.getText().toString());
		cv.put(AccountColumns.SITENAME, sitename.getText().toString());
		cv.put(AccountColumns.USERNAME, username.getText().toString());
		cv.put(AccountColumns.WEBSITE, website.getText().toString());
		cv.put(AccountColumns.IDENTIFYING_CODE, identifyingCode.getText()
				.toString());
		cv.put(AccountColumns.ASK, ask.getText().toString());
		cv.put(AccountColumns.ANSWER, answer.getText().toString());
		cv.put(AccountColumns.GROUP, group.getText().toString());
		return cv;
	}

	private void bindListeners() {
		for (View v : ableGroup)
			v.setOnClickListener(this);
		CommonUtils.bindEditTextNtextDisposer(mailBox, mailBoxDisposer);
		CommonUtils.bindEditTextNtextDisposer(password, passwordDisposer);
		CommonUtils.bindEditTextNtextDisposer(username, usernameDisposer);
		CommonUtils.bindEditTextNtextDisposer(passport, passportDisposer);
		CommonUtils.bindEditTextNtextDisposer(sitename, sitenameDisposer);
		CommonUtils.bindEditTextNtextDisposer(phoneNum, phoneNumDisposer);
		CommonUtils.bindEditTextNtextDisposer(website, websiteDisposer);
		CommonUtils.bindEditTextNtextDisposer(identifyingCode,
				identifyingCodeDisposer);
		CommonUtils.bindEditTextNtextDisposer(ask, askDisposer);
		CommonUtils.bindEditTextNtextDisposer(answer, answerDisposer);
		CommonUtils.bindEditTextNtextDisposer(group, groupDisposer);
	}

	private void fillInfos() {
		mailBox.setText(content.getAsString(AccountColumns.MAILBOX));
		password.setText(content.getAsString(AccountColumns.PASSWORD));
		username.setText(content.getAsString(AccountColumns.USERNAME));
		passport.setText(content.getAsString(AccountColumns.PASSPORT));
		sitename.setText(content.getAsString(AccountColumns.SITENAME));
		phoneNum.setText(content.getAsString(AccountColumns.PHONENUM));
		website.setText(content.getAsString(AccountColumns.WEBSITE));
		identifyingCode.setText(content
				.getAsString(AccountColumns.IDENTIFYING_CODE));
		ask.setText(content.getAsString(AccountColumns.ASK));
		answer.setText(content.getAsString(AccountColumns.ANSWER));
		group.setText(content.getAsString(AccountColumns.GROUP));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			backOperation();
			return true;
		} else if (id == editButtonId) {
			CommonUtils.toggleFocusStatus(container, ableGroup, visibleGroup,textSequence);
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
			CommonUtils.blockFocus(container, ableGroup, visibleGroup,textSequence);
		} else {
			CommonUtils.requetFocus(container, ableGroup,textSequence);
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
}
