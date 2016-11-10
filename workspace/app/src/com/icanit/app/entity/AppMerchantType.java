package com.icanit.app.entity;
// default package

import java.util.List;

/**
 * AppMerchantType entity. @author MyEclipse Persistence Tools
 */

public class AppMerchantType implements java.io.Serializable {

	// Fields

	public Integer id,parentId;
	public String typeName,pic;
	public List<AppMerchantType> subTypeList;


}