package com.icanit.app.entity;
// default package

import java.util.List;

/**
 * AppCategory entity. @author MyEclipse Persistence Tools
 */

public class AppCategory implements java.io.Serializable {

	// Fields

	public Integer id,parentId;
	public String cateName,pic;
	public List<AppCategory> subCateList;

}