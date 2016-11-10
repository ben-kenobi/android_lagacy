package com.yf.contacts;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.adapter.BaseUnstableCursorAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ContactsService;
import com.yf.accountmanager.sqlite.ISQLite.ContactColumns;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.SystemUtil;

public class ContactListAdapter extends BaseUnstableCursorAdapter implements OnClickListener{
	
	private int resId=R.layout.item4lv_contact;
	
	public CustomizedDialog numsDialog;
	
	public NumsAdapter numsAdapter;

	public static final int MESSAGE=1,
			
			CALL=0;
	
	public ContactListAdapter(Context context,View deleteButton) {
		super(context, deleteButton);
	}

	
	
	@Override
	public void bindView(View convertView, Context context, Cursor c) {
		boolean b = selectedIds.contains(c.getInt(c.getColumnIndex(IConstants.ID)));
		convertView.setActivated(b);
		
		ViewHolder holder=(ViewHolder)convertView.getTag();
		holder.clearNums();
		holder.title.setText("NAME : ");
		String group=c.getString(c.getColumnIndex(ContactColumns.GROUP));
		holder.name.setText(c.getString(c.getColumnIndex(ContactColumns.NAME))+
				"     ( "+(TextUtils.isEmpty(group)?"未分组":group)+" ) ");
		String phone =c.getString(c.getColumnIndex(ContactColumns.PHONE));
		String phone2=c.getString(c.getColumnIndex(ContactColumns.PHONE2));
		String tel =c.getString(c.getColumnIndex(ContactColumns.TEL));
		StringBuffer info = new StringBuffer();
		if(!TextUtils.isEmpty(phone)){
			info.append("手机  : "+phone+"\n");
			holder.numsDetail.add("手机  : "+phone);
			holder.nums.add(phone);
		}
		if(!TextUtils.isEmpty(phone2)){
			info.append("手机2 : "+phone2+"\n");
			holder.numsDetail.add("手机2 : "+phone2);
			holder.nums.add(phone2);
		}
		if(!TextUtils.isEmpty(tel)){
			info.append("电话  : "+tel+"\n");
			holder.numsDetail.add("电话  : "+tel);
			holder.nums.add(tel);
		}
		if(info.length()>2)
			info.deleteCharAt(info.length()-1);
		holder.info.setText(info.toString());
		showCallNmes(!isMultiChoiceMode&&info.length()>0, holder);
	}
	
	private void showCallNmes(boolean show,ViewHolder holder){
		holder.call.setVisibility(show?View.VISIBLE:View.GONE);
		holder.message.setVisibility(show?View.VISIBLE:View.GONE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		View v=LayoutInflater.from(context).inflate(resId,parent,false);
		ViewHolder holder=new ViewHolder(v);
		holder.call.setOnClickListener(this);
		holder.message.setOnClickListener(this);
		return v;
	}
	
	class ViewHolder{
		public ViewHolder(View v){
			title=(TextView)v.findViewById(R.id.textView1);
			name=(TextView)v.findViewById(R.id.textView2);
			info=(TextView)v.findViewById(R.id.textView3);
			call=(ImageButton)v.findViewById(R.id.imageButton1);
			message=(ImageButton)v.findViewById(R.id.imageButton2);
			v.setTag(this);
			nums=new ArrayList<String>();
			numsDetail=new ArrayList<String>();
		}
		
		public void clearNums(){
			numsDetail.clear();
			nums.clear();
		}
		
		public TextView title,name,info;
		public ImageButton call,message;
		public List<String> numsDetail,nums;
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.imageButton1){
			handleCallButtonClickEvent(v);
		}else if(id==R.id.imageButton2){
			handleMesButtonClickEvent(v);
		}
	}
	
	private void handleCallButtonClickEvent(View v){
		ViewGroup parent = (ViewGroup)v.getParent().getParent();
		if(parent==null) return ;
		Object obj =parent.getTag();
		if(obj!=null){
			ViewHolder holder = (ViewHolder)obj;
			if(holder.nums.isEmpty()) return ;
			if(holder.nums.size()==1){
				SystemUtil.intentToDial(holder.nums.get(0),context );
			}else{
				initOptionDialogNshow(holder.numsDetail,holder.nums,context,CALL);
			}
		}
	}
	private void handleMesButtonClickEvent(View v) {
		ViewGroup parent = (ViewGroup)v.getParent().getParent();
		if(parent==null) return ;
		Object obj =parent.getTag();
		if(obj!=null){
			ViewHolder holder = (ViewHolder)obj;
			if(holder.nums.isEmpty()) return ;
			if(holder.nums.size()==1){
				SystemUtil.intentToMessageType2(holder.nums.get(0),context,"");
			}else{
				initOptionDialogNshow(holder.numsDetail,holder.nums,context,MESSAGE);
			}
		}
	}
	
	private void initOptionDialogNshow(List<String> numsDetail,List<String> nums,final Context context,int type){
		if(numsDialog==null){
			numsDialog=CustomizedDialog.initOptionDialog(context);
			numsDialog.title.setText("请选择号码");
			numsDialog.lv.setAdapter(numsAdapter=new NumsAdapter(context));
			numsDialog.lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					String num=numsAdapter.getNum(position);
					if(numsAdapter.type==CALL){
						SystemUtil.intentToDial(num, context);
					}else if(numsAdapter.type==MESSAGE){
						SystemUtil.intentToMessageType2(num, context, "");
					}
					numsDialog.dismiss();
				}
			});
		}
		numsAdapter.setNums(numsDetail,nums,type);
		numsDialog.show();
	}
	
}
