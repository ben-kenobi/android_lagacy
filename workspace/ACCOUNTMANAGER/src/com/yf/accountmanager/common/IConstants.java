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
	String FILE_ALREADY_EXISTS="�ļ��Ѵ���";
	String MKDIR_SUCCESS="�����ɹ�";
	String MKDIR_FAIL="����ʧ��";
	String DELETE_FAIL="�ļ����ļ����޷�ɾ��";
	String DELETE_SUCCESS="���ɾ��";
	String RENAME_SUCCESS="���������";
	String UNCHANGED="δ�ı�";
	String RENAME_FAIL="δ��ִ�иô�������";
	String RENAME_CONFLICT="δ��ִ�иò���,�ļ�����ͻ";
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
