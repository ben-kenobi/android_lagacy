package fj.swsk.cn.eqapp.conf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommonService {
	public static Context context;

	protected String tableName;
	protected SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();


	public CommonService(String tableName){
		super();
		this.tableName=tableName;
	}

	public  long insert(ContentValues cv){
		cv.remove(IConstants.ID);
		return db.insert(tableName,null,cv);
	}

	public boolean batchInsert(List<ContentValues> cvs){
		for(int i = 0;i<cvs.size();i++){
			insert(cvs.get(i));
		}
		return true;
	}

	public boolean delete(long id){
		return db.delete(tableName,IConstants.ID+" = "+id,null) > 0;
	}

	public int batchDelete(Set<Integer> ids){
		return db.delete(tableName,IConstants.ID+" in ("+concatenateIds(ids)+")",null);

	}

	public boolean update(ContentValues cv,long id){
		cv.remove(IConstants.ID);
		return db.update(tableName,cv,IConstants.ID+"="+id,null)>0;
	}
	public boolean batchUpdate(List<ContentValues> cvs){
		for(ContentValues cv:cvs){
			if(cv.containsKey(IConstants.ID)){
				update(cv, cv.getAsLong(IConstants.ID));
			}
		}
		return true;
	}

	public boolean insertOrUpdate(ContentValues cv){
		if(cv.containsKey(IConstants.ID)){
			return update(cv,cv.getAsLong(IConstants.ID));
		}else{
			return insert(cv)>0;
		}

	}
	public Cursor queryBy(String where,String[] args,String order){
		return db.query(tableName,null,where,args,null,null,order);
	}





	/**
	 *
	 *
	 *
	 * method:concatenateIds
	 *
	 * @param idset
	 * @return
	 */
	public static String concatenateIds(Set<Integer> idset) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Integer> it = idset.iterator(); it.hasNext();) {
			sb.append(it.next() + ",");
		}
		if (sb.length() > 1)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
}
