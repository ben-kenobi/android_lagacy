package com.icanit.app_v2.entity;
// default package

import java.util.List;

/**
 * AppMerchantType entity. @author MyEclipse Persistence Tools
 */

public class AppMerchantType implements java.io.Serializable {

	// Fields

	public int id,parentId;
	public String typeName,pic;
	public List<AppMerchantType> subTypeList;


}