package com.what.yunbao.util;

import android.net.Uri;
public class Notes {
    public static final String AUTHORITY = "icanit_community";
    public static final String AUTH="com.icanit.app_v2";
    public static final String TAG = "Notes";
    public static final int TYPE_NOTE     = 0;
    public static final int TYPE_FOLDER   = 1;
    public static final int TYPE_SYSTEM   = 2;

    //存放和回收的文件夹
    public static final int ID_ROOT_FOLDER = 0;
    public static final int ID_TRASH_FOLER = -1;
    

//    public static class DataConstants {
//        public static final String NOTE = TextNote.CONTENT_ITEM_TYPE;
//    }

    /**
     * Uri to query all 
     */

    public static final Uri CONTENT_ADDRESS_URI = Uri.parse("content://" + AUTHORITY + "/address");
    
    public static final Uri USER_CONTACT_URI = Uri.parse("content://" + AUTH + "/user_contact");
    
    public static final Uri COLLECTIONS_URI = Uri.parse("content://" + AUTH + "/collection");
    
    public static final Uri BROWSING_URI = Uri.parse("content://" + AUTH + "/browsed_merchant");
    
    public static final Uri CONTENT_COLLECTION_URI = Uri.parse("content://" + AUTHORITY + "/collection");
    
    public static final Uri CONTENT_ORDER_URI = Uri.parse("content://" + AUTHORITY + "/myorder");

    public interface AddressColumns {
    	
        public static final String ID = "_id";

        public static final String USER_ID = "user_id";
        
        public static final String NOTE_ID ="what";//删除
        
        public static final String COUNT = "count";

        public static final String CREATED_DATE = "created_date";

        public static final String MODIFIED_DATE = "modified_date";

        public static final String CONTENT = "content";
        
        public static final String PHONE = "phone";
        
        public static final String ADDRESS = "address";        
        
        public static final String ZIPCODE = "zipcode";

    }
    
 public interface UserContactColumns {
    	
        String ID = "_id",

        		USERNAME= "username",
        
        		PHONENUM ="phoneNum",
        
        		ADDRESS= "address",

        		PHONE = "phone",

        		MODIFY_TIME= "modifyTime",
        		
        		CREATE_TIME = "createTime";
        

    }
 public interface CollectColumns{
	 
	 String ID="_id",
			 
			 MERID="merid",
			 
			 PHONE="phone",
			 
			 TYPE="merType",
			 
			 MERNAME="merName",
			 
			 LOCATION="location",
			 
			 MAP="map",
			 
			 MERPHONE="merPhone",
			 
			 PIC="pic",
			 
			 BOOKABLE="bookable",
			 
			 MINCOST="minCost",
			 
			 ADDTIME="addTime";
			 
 }

    
    public interface CollectionColumns{
    	public static final String ID = "_id";
    	
    	public static final String BUSINESS_ID = "business_id";
    	
    	public static final String PARENT_ID = "parent_id";
    	
    	public static final String ADDRESS = "address";
    	
    	public static final String BUSINESS = "business";
    	
    	public static final String DISTANCE = "distance";
    	
    	public static final String CREATED_DATE = "created_date";
    }
    
}
