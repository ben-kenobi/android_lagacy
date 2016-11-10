package com.icanit.app.sqlite;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;

import com.icanit.app.entity.CartItem;
import com.icanit.app.exception.AppException;

public interface ShoppingCartDao {
	boolean deleteItemById(int id) throws AppException;
	boolean deleteItemByProdId(int id) throws AppException;
	boolean updateQuantity(CartItem item) throws AppException;
	boolean addItem(CartItem item) throws AppException;
	boolean addItem(Map<String,Object> item) throws AppException;
	List<CartItem> findAllItemsByPhone(String phone) throws AppException;
	Map<Integer,CartItem> findAllItemsMapByPhone(String phone)throws AppException;
	Cursor findAllItemsWithCursorByPhone(String phone) throws AppException;
	CartItem findItemById(CartItem item) throws AppException;
	Set<Integer> prodIdSetInCart() throws AppException;
	void changeItemStatusToHistoryByPhone(String phone) throws AppException;
	boolean updateItem(CartItem item) throws AppException;
	void deleteItemByProdIdSet(Set<Integer> prodIdSet)throws AppException;
	
}
