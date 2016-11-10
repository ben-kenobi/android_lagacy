package com.yf.accountmanager.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yf.accountmanager.MyApplication;
import com.yf.accountmanager.common.IConstants;

public class CommonUtils {
	public static MyApplication context;
	
	public static void bindEditTextNtextDisposer(final EditText editText,
			final View textDisposer) {
		new ListenerUtil.TextChangeListener(editText, textDisposer);
	}
	public static void toast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	public static void toastOnMainHandler(final String message){
		IConstants.MAIN_HANDLER.post(new Runnable(){
			public void run(){
				toast(message);
			}
		});
	}
	
	public static void toggleFocusStatus(ViewGroup container,View[] ableGroup,View[] visibleGroup,TextView[] ableGroup2){
		if(container.getDescendantFocusability()==ViewGroup.FOCUS_BLOCK_DESCENDANTS)
			requetFocus(container,ableGroup,ableGroup2);
		else
			blockFocus(container, ableGroup,visibleGroup,ableGroup2);
		
	}
	public static void blockFocus(ViewGroup container,View[] ableGroup,View[] visibleGroup,TextView[] ableGroup2){
		container.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		container.clearFocus();
		for(View v:ableGroup)
			v.setVisibility(View.GONE);
		for(View v:visibleGroup)
			v.setVisibility(View.GONE);
		for(TextView v:ableGroup2)
			v.setEnabled(false);
	}
	public static void requetFocus(ViewGroup container,View[] ableGroup,TextView[] ableGroup2){
		container.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		container.requestFocus();
		for(View v:ableGroup)
			v.setVisibility(View.VISIBLE);
		for(TextView v:ableGroup2)
			v.setEnabled(true);
	}
	public static ContentValues encapsulateCursorAsContentValues(Cursor cursor){
		ContentValues cv=new ContentValues();
		for(int i=0;i<cursor.getColumnCount();i++){
			String coulumnName=cursor.getColumnName(i);
			if(IConstants.ID.equalsIgnoreCase(coulumnName))
				cv.put(coulumnName, cursor.getInt(i));
			else 
				cv.put(coulumnName, cursor.getString(i));
		}
		return cv;
	}
	
	public static List<ContentValues> encapsulateCursorAsContentValuesList(Cursor cursor){
		if(cursor==null||cursor.getCount()==0) return null;
		List<ContentValues> entries=new ArrayList<ContentValues>();
		if(cursor.moveToFirst()){
			do{
				entries.add(encapsulateCursorAsContentValues(cursor));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return entries;
	}
	
	
	public static JSONObject encapsulateCursorAsJSONObject(Cursor cursor) throws JSONException{
		JSONObject jo = new JSONObject();
		for(int i=0;i<cursor.getColumnCount();i++){
			String coulumnName=cursor.getColumnName(i);
			if(IConstants.ID.equalsIgnoreCase(coulumnName))
				jo.put(coulumnName, cursor.getInt(i));
			else 
				jo.put(coulumnName, cursor.getString(i));
		}
		return jo;
	}
	
	public static JSONArray encapsulateCursorAsJSONArray(Cursor cursor) throws JSONException {
		if(cursor==null||cursor.getCount()==0) return null;
		JSONArray ja=new JSONArray();
		if(cursor.moveToFirst()){
			do{
				ja.put(encapsulateCursorAsJSONObject(cursor));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return ja;
	}
	
	public static JSONObject convertContentValuesToJSONObject(ContentValues cv) throws JSONException{
		JSONObject jo = new JSONObject();
		for(Iterator<String> it=cv.keySet().iterator();it.hasNext();){
			String key=it.next();
			jo.put(key, cv.get(key));
		}
		return jo;
	}
	
	
	public static ContentValues convertJSONObjectToContentValues(JSONObject jo) throws JSONException{
		ContentValues cv=new ContentValues();
		for(Iterator<String> it=jo.keys();it.hasNext();){
			String key=it.next();
			if(!IConstants.ID.equalsIgnoreCase(key))
				cv.put(key, jo.getString(key));
		}
		return cv;
	}
	
	public static List<ContentValues> convertJSONArrayToContentValuesList(JSONArray ja) throws JSONException{
		if(ja==null||ja.length()==0) return null;
		List<ContentValues> list=new ArrayList<ContentValues>();
		for(int i=0;i<ja.length();i++){
			JSONObject jo=ja.getJSONObject(i);
			list.add(convertJSONObjectToContentValues(jo));
		}
		return list;
	}
	
	public static void setOnClickListenerForCheckedTextView(
			final CheckedTextView ctv) {
		ctv.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (ctv.isChecked())
					ctv.setChecked(false);
				else
					ctv.setChecked(true);
			}
		});
	}
	
	


	public static void dismissDialogs(Dialog...dialogs){
		if(dialogs!=null){
			for(int i=0;i<dialogs.length;i++){
				Dialog dialog=dialogs[i];
				if(dialog!=null)
					dialog.dismiss();
			}
		}
	}
	
	public static String getAccessKey(){
		return context.accessKey;
	}
	public static void setAccessKey(String accessKey){
		context.accessKey=accessKey;
	}

	
}
