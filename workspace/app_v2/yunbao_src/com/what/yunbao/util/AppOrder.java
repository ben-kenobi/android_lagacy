package com.what.yunbao.util;

import java.util.Date;


/**
 * AppOrder entity. @author MyEclipse Persistence Tools
 */

public class AppOrder  implements java.io.Serializable {


    // Fields    

     private long id;
     private String orderNumber;
     private String userName;
     private String phone;
     private String address;
     private String orderTime;//Date 类型改为String
     private Integer userId;
     private double sum;
     private Integer status;
     private String deleted;
     //服务器端无该字段 自己添加的..
     private String count;


    // Constructors

    /** default constructor */
    public AppOrder() {
    }

    
    /** full constructor */
    public AppOrder(String orderNumber, String userName, String phone, String address, String orderTime, Integer userId, double sum, Integer status, String deleted) {
        this.orderNumber = orderNumber;
        this.userName = userName;
        this.phone = phone;
        this.address = address;
        this.orderTime = orderTime;
        this.userId = userId;
        this.sum = sum;
        this.status = status;
        this.deleted = deleted;
    }

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderTime() {
		return orderTime;
	}


	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}


	public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public double getSum() {
        return this.sum;
    }
    
    public void setSum(double sum) {
        this.sum = sum;
    }

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }


	public String getCount() {
		return count;
	}


	public void setCount(String count) {
		this.count = count;
	}


}