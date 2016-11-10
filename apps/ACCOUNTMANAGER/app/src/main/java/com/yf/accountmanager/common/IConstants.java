package com.yf.accountmanager.common;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public interface IConstants {
	public static final ExecutorService THREAD_POOL=Executors.newFixedThreadPool(3);
	public static final Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());
	String NAME="name";
	String ACCOUNT="account";
	String CONTACTS="contacts";
	String FILESYSTEM="filesystem";
	String MATCH_ALL="*";
	String BACKUP="backup";
	String ACCOUNT_BACKUP="_backup";
	String ID="_id";
	String FILE_ALREADY_EXISTS="文件已存在";
	String MKDIR_SUCCESS="创建成功";
	String MKDIR_FAIL="创建失败";
	String DELETE_FAIL="文件或文件夹无法删除";
	String DELETE_SUCCESS="完成删除";
	String RENAME_SUCCESS="重命名完成";
	String UNCHANGED="未改变";
	String RENAME_FAIL="未能执行该次重命名";
	String RENAME_CONFLICT="未能执行该操作,文件名冲突";
	String LOCAL_SERVICE="7378423";
	String PLATFORM_ENCODING="UTF-8";
	String FILE="file";
	String DIRECTORY="directory";
	String TIME_PATTERN="yyyy-MM-dd  HH:mm:ss";
	String FILE_COPY_LIST="fileCopyList";
	String FILE_MOVE_LIST="fileMoveList";
	String FILE_PARENT="fileParent";
	String FILE_PASTE_TYPE="filePasteType";
	int minGestureInterval=250;
			
	
	
}
