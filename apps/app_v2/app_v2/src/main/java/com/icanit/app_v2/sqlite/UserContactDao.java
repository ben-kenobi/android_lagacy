package com.icanit.app_v2.sqlite;

import java.util.List;

import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;

public interface UserContactDao {
	List<UserContact> listUserContactsByPhone(String phone) throws AppException;
	boolean saveUserContact(UserContact contact) throws AppException;
	boolean deleteContact(UserContact contact)throws AppException;
	
}
