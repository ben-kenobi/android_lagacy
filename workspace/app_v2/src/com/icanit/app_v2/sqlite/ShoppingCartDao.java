package com.icanit.app_v2.sqlite;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;

import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.exception.AppException;

public interface ShoppingCartDao {
	boolean deleteItemById(int id) throws AppException;
	boolean deleteItemByProdId(String id) throws AppException;
	boolean updateQuantity(CartItem item) throws AppException;
	boolean addItem(CartItem item) throws AppException;
	List<CartItem> findAllItemsByPhone(String phone) throws AppException;
	Map<String,CartItem> findAllItemsMapByPhone(String phone)throws AppException;
	Cursor findAllItemsWithCursorByPhone(String phone) throws AppException;
	CartItem findItemById(CartItem item) throws AppException;
	Set<String> prodIdSetInCart() throws AppException;
	void changeItemStatusToHistoryByPhone(String phone) throws AppException;
	boolean updateItem(CartItem item) throws AppException;
	void deleteItemByProdIdSet(Set<String> prodIdSet)throws AppException;
	
}
