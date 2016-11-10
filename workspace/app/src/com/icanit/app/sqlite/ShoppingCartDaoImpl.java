package com.icanit.app.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.icanit.app.entity.CartItem;
import com.icanit.app.exception.AppException;

public final class ShoppingCartDaoImpl implements ShoppingCartDao {
	private AppSQLite sqlite;

	public ShoppingCartDaoImpl(Context context) {
		sqlite = AppSQLite.getInstance(context);
	}

	@Override
	public boolean deleteItemById(int id) throws AppException {
		sqlite.getWritableDatabase().execSQL(
				"DELETE FROM shopping_cart " + "WHERE _id=?",
				new Object[] { id });
		return true;
	}

	@Override
	public boolean updateQuantity(CartItem item) throws AppException {
		sqlite.getWritableDatabase().execSQL(
				"UPDATE shopping_cart " + "SET quantity=? WHERE _id=?",
				new Object[] { item.quantity, item._id });
		return true;
	}

	@Override
	public boolean addItem(CartItem item) throws AppException {
		// sqlite.getWritableDatabase().execSQL("INSERT INTO shopping_cart " +
		// "VALUES(null,?,?,?,?,?,?,?,?,?,?)",new Object[]{item.phone,
		// 1,1,item.prod_id,item.prod_name,
		// item.prod_desc,item.add_time,item.present_price,item.former_price,item.snap});
		ContentValues values = new ContentValues();
		for (Field field : item.getClass().getDeclaredFields()) {
			try {
				values.put(field.getName(), field.get(item) + "");
			} catch (Exception e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		values.remove("_id");
		item._id = (int) sqlite.getWritableDatabase().insert("shopping_cart",
				null, values);
		Log.d("cartInfo", "_id=" + item._id + "  @shoppingCartDaoImpl  addItem");
		return true;
	}

	@Override
	public boolean addItem(Map<String, Object> item) throws AppException {
		return true;
	}

	@Override
	public List<CartItem> findAllItemsByPhone(String phone) throws AppException {
		List<CartItem> items = new ArrayList<CartItem>();
		Cursor cursor = sqlite.getReadableDatabase().rawQuery(
				"SELECT * FROM " + "shopping_cart WHERE  status=1",
				new String[] {});
		if (cursor.moveToFirst()) {
			do {
				int i = 0;
				CartItem item = new CartItem();
				item._id = cursor.getInt(i++);
				item.phone = cursor.getString(i++);
				item.status = cursor.getInt(i++);
				item.quantity = cursor.getInt(i++);
				item.prod_id = cursor.getInt(i++);
				item.prod_name = cursor.getString(i++);
				item.prod_desc = cursor.getString(i++);
				item.add_time = cursor.getLong(i++);
				item.present_price = cursor.getDouble(i++);
				item.former_price = cursor.getDouble(i++);
				item.snap = cursor.getString(i++);
				items.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return items;
	}

	@Override
	public CartItem findItemById(CartItem item) throws AppException {
		Cursor cursor = sqlite.getReadableDatabase().rawQuery(
				"SELECT * FROM shopping_cart " + "WHERE _id=?",
				new String[] { item._id + "" });
		int i = 1;
		if (cursor.moveToFirst()) {
			item.phone = cursor.getString(i++);
			item.status = cursor.getInt(i++);
			item.quantity = cursor.getInt(i++);
			item.prod_id = cursor.getInt(i++);
			item.prod_name = cursor.getString(i++);
			item.prod_desc = cursor.getString(i++);
			item.add_time = cursor.getLong(i++);
			item.present_price = cursor.getDouble(i++);
			item.former_price = cursor.getDouble(i++);
			item.snap = cursor.getString(i++);
		}
		cursor.close();
		return item;
	}

	@Override
	public Cursor findAllItemsWithCursorByPhone(String phone)
			throws AppException {
		return sqlite.getReadableDatabase().rawQuery(
				"SELECT * FROM " + "shopping_cart WHERE  status=1",
				new String[] {});
	}

	@Override
	public boolean deleteItemByProdId(int id) throws AppException {
		sqlite.getWritableDatabase().execSQL(
				"DELETE FROM shopping_cart WHERE " + "prod_id=? AND status=1",
				new Object[] { id });
		return true;
	}

	@Override
	public Set<Integer> prodIdSetInCart() throws AppException {
		Cursor cursor = sqlite.getReadableDatabase().rawQuery(
				"SELECT prod_id " + "FROM shopping_cart WHERE status=1", null);
		Set<Integer> idset = new HashSet<Integer>();
		if (cursor.moveToFirst()) {
			do {
				idset.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return idset;
	}

	@Override
	public void changeItemStatusToHistoryByPhone(String phone)
			throws AppException {
		sqlite.getWritableDatabase().execSQL(
				"UPDATE shopping_cart SET status=2 " + "WHERE status=1 ",
				new Object[] {});
	}

	@Override
	public Map<Integer, CartItem> findAllItemsMapByPhone(String phone)
			throws AppException {
		Map<Integer, CartItem> map = new HashMap<Integer, CartItem>();
		for (CartItem item : findAllItemsByPhone(phone)) {
			map.put(item.prod_id, item);
		}
		return map;
	}

	@Override
	public boolean updateItem(CartItem item) throws AppException {
		sqlite.getWritableDatabase()
				.execSQL(
						"UPDATE shopping_cart SET quantity=? ,"
								+ "add_time=?,present_price=?,former_price=?,snap=? WHERE prod_id=? AND status=1",
						new Object[] { item.quantity, item.add_time,
								item.present_price, item.former_price,
								item.snap, item.prod_id });
		return true;
	}

	@Override
	public void deleteItemByProdIdSet(Set<Integer> prodIdSet)
			throws AppException {
		StringBuffer ids = new StringBuffer();
		for (Integer in : prodIdSet) {
			ids.append(in);
			ids.append(",");
		}
		if (ids.length() > 0) {
			ids.deleteCharAt(ids.length() - 1);
			sqlite.getWritableDatabase().execSQL(
					"DELETE FROM shopping_cart WHERE status=1 "
							+ "AND prod_id IN(" + ids.toString() + ")");
		}
	}

}
