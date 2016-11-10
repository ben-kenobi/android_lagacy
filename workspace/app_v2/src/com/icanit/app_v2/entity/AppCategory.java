package com.icanit.app_v2.entity;
// default package

import java.util.List;

/**
 * AppCategory entity. @author MyEclipse Persistence Tools
 */

public class AppCategory implements java.io.Serializable {

	// Fields
	public AppCategory(int id,String cateName,int parentId,int turn,int count){
		this.id=id;this.cateName=cateName;this.parentId=parentId;this.turn =turn;
		this.count=count;
	}
	public AppCategory(){}
	public int id,parentId,turn,count;
	public String cateName;

}