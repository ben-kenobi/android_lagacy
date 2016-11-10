package com.what.yunbao.collection;

import android.database.Cursor;
import android.util.Log;

import com.what.yunbao.util.Notes.CollectColumns;
import com.what.yunbao.util.Notes.CollectionColumns;

public class CollectionItemData {
	public static final String [] COLLECTION_COLUMNS = new String[]{
		CollectionColumns.ID,
		CollectionColumns.BUSINESS_ID,
		CollectionColumns.BUSINESS,
		CollectionColumns.ADDRESS,
		CollectionColumns.DISTANCE
	};
	public static final String[] COLLECT_COLUMNS=new String[]{
		CollectColumns.ID,
		CollectColumns.LOCATION,
		CollectColumns.BOOKABLE,
		CollectColumns.MAP,
		CollectColumns.MERID,
		CollectColumns.MERNAME,
		CollectColumns.MERPHONE,
		CollectColumns.MINCOST,
		CollectColumns.PHONE,
		CollectColumns.PIC,
		CollectColumns.TYPE
	};
	private long id;
	private long businessId;
	private String name;
	private String address;
	private double distance;
	
	public CollectionItemData(Cursor cursor){
		id = cursor.getLong(0);
		businessId = cursor.getLong(1);
		name = cursor.getString(2); 
		address = cursor.getString(3);
		distance = cursor.getDouble(4);
		Log.e("查询数据1", ""+id);
		Log.e("查询数据2", ""+businessId);
		Log.e("查询数据3", ""+name);
		Log.e("查询数据4", ""+address);
	}

	public long getId() {
		return id;
	}

	public long getBusinessId() {
		return businessId;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public double getDistance() {
		return distance;
	}
	
}
