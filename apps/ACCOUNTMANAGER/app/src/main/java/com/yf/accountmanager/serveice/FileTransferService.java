package com.yf.accountmanager.serveice;

import java.io.File;
import java.util.ArrayList;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.util.FileUtils.FileTransfer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileTransferService extends Service{
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final ArrayList<File> copyList = (ArrayList<File>)intent.getSerializableExtra(IConstants.FILE_COPY_LIST	);
		final ArrayList<File> moveList = (ArrayList<File>)intent.getSerializableExtra(IConstants.FILE_MOVE_LIST);
		final File parent = (File) intent.getSerializableExtra(IConstants.FILE_PARENT);
		final int pasteType=intent.getIntExtra(IConstants.FILE_PASTE_TYPE, 0);
		final FileTransfer ft=new FileTransfer();
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				ft.startBatchTransfer(copyList, moveList, parent, pasteType,FileTransferService.this);
			}
		});
		stopSelf();
		return  super.onStartCommand(intent, flags, startId);
	}

}
