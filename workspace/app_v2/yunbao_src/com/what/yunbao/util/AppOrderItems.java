package com.what.yunbao.util;


/**
 * AppOrderItems entity. @author MyEclipse Persistence Tools
 */

public class AppOrderItems  implements java.io.Serializable {


    // Fields    

     private long id;
     private long orderId;
     private long goodId;
     private Integer quantity;
     private double sum;
     private String goodName;
     private double curPrice;
     private double duePrice;
     private Integer status;
     private Integer merId;
     private String postscript;
     private String delivery;


    // Constructors

    /** default constructor */
    public AppOrderItems() {
    }

    
    /** full constructor */
    public AppOrderItems(long orderId, long goodId, Integer quantity, double sum, String goodName, double curPrice, double duePrice, Integer status, Integer merId, String postscript, String delivery) {
        this.orderId = orderId;
        this.goodId = goodId;
        this.quantity = quantity;
        this.sum = sum;
        this.goodName = goodName;
        this.curPrice = curPrice;
        this.duePrice = duePrice;
        this.status = status;
        this.merId = merId;
        this.postscript = postscript;
        this.delivery = delivery;
    }

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return this.orderId;
    }
    
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getGoodId() {
        return this.goodId;
    }
    
    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return this.sum;
    }
    
    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getGoodName() {
        return this.goodName;
    }
    
    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public double getCurPrice() {
        return this.curPrice;
    }
    
    public void setCurPrice(double curPrice) {
        this.curPrice = curPrice;
    }

    public double getDuePrice() {
        return this.duePrice;
    }
    
    public void setDuePrice(double duePrice) {
        this.duePrice = duePrice;
    }

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMerId() {
        return this.merId;
    }
    
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    public String getPostscript() {
        return this.postscript;
    }
    
    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getDelivery() {
        return this.delivery;
    }
    
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
   








}