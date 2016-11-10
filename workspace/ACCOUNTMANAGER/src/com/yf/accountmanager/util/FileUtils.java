package com.yf.accountmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.FileInfo;
import com.yf.accountmanager.common.IConstants;

public class FileUtils {
	public static final byte SYSTEM = -1,

	SDCARD = 0,

	EX_SDCARD = 1, EX_SDCARD2 = 2;
	
	
	public static final int IMG=1,
			
			MUSIC=3,
			
			VIDEO=2,
			
			COMPRESSION=5,
			
			DOCS=0,
			
			APK=4;
	
	
	public static final String[] FILE_TYPE = new String[] { "text/*",
		"image/*", "video/*", "audio/*" };

	public static Drawable dir_empty, dir_stuffed, fileIcon, systemIcon,
			errorIcon;
	
	public static final  Map<String,int[]> extensionIconMap=new HashMap<String,int[]>();
	
	
	static{
		//------------img-------------------------------
		extensionIconMap.put("jpg",new int[]{R.drawable.icon_jpg,101});
		extensionIconMap.put("jpeg",new int[]{R.drawable.icon_jpg,102});
		extensionIconMap.put("gif",new int[]{R.drawable.icon_gif,103});
		extensionIconMap.put("png",new int[]{R.drawable.icon_png,104});
		extensionIconMap.put("bmp",new int[]{R.drawable.icon_bmp,105});
		extensionIconMap.put("wbmp",new int[]{R.drawable.icon_bmp,106});
		
		//------------music-------------------------------
				int  music = R.drawable.icon_music;
				extensionIconMap.put("mp3",new int[]{music,301});
				extensionIconMap.put("m4a",new int[]{music,302});
				extensionIconMap.put("wav",new int[]{music,303});
				extensionIconMap.put("amr",new int[]{music,304});
				extensionIconMap.put("awb",new int[]{music,305});
				extensionIconMap.put("aac",new int[]{music,306});
				extensionIconMap.put("flac",new int[]{music,307});
				extensionIconMap.put("mid",new int[]{music,308});
				extensionIconMap.put("midi",new int[]{music,309});
				extensionIconMap.put("xmf",new int[]{music,310});
				extensionIconMap.put("rttt1",new int[]{music,311});
				extensionIconMap.put("rtx",new int[]{music,312});
				extensionIconMap.put("ota",new int[]{music,313});
				extensionIconMap.put("wma",new int[]{music,314});
				extensionIconMap.put("ra",new int[]{music,315});
				extensionIconMap.put("mka",new int[]{music,316});
				extensionIconMap.put("m3u",new int[]{music,317});
				extensionIconMap.put("pls",new int[]{music,318});
				
		//------------video-------------------------------
				int video=R.drawable.icon_video;
				extensionIconMap.put("mpeg",new int[]{video,201});
				extensionIconMap.put("mp4",new int[]{video,202});
				extensionIconMap.put("mov",new int[]{video,203});
				extensionIconMap.put("m4v",new int[]{video,204});
				extensionIconMap.put("3gp",new int[]{video,205});
				extensionIconMap.put("3gpp",new int[]{video,206});
				extensionIconMap.put("3g2",new int[]{video,207});
				extensionIconMap.put("3gpp2",new int[]{video,208});
				extensionIconMap.put("avi",new int[]{video,209});
				extensionIconMap.put("divx",new int[]{video,210});
				extensionIconMap.put("wmv",new int[]{video,211});
				extensionIconMap.put("asf",new int[]{video,212});
				extensionIconMap.put("flv",new int[]{video,213});
				extensionIconMap.put("mkv",new int[]{video,214});
				extensionIconMap.put("mpg",new int[]{video,215});
				extensionIconMap.put("rmvb",new int[]{video,216});
				extensionIconMap.put("rm",new int[]{video,217});
				extensionIconMap.put("vob",new int[]{video,218});
				extensionIconMap.put("f4v",new int[]{video,219});
				
		//------------deflated-------------------------------
				extensionIconMap.put("zip",new int[]{R.drawable.icon_zip,501});
				extensionIconMap.put("gzip",new int[]{R.drawable.icon_zip,502});
				extensionIconMap.put("bzip2",new int[]{R.drawable.icon_zip,503});
				extensionIconMap.put("xzip",new int[]{R.drawable.icon_xzip,504});
				extensionIconMap.put("rar",new int[]{R.drawable.icon_rar,505});
				extensionIconMap.put("tar",new int[]{R.drawable.icon_tar,506});
				extensionIconMap.put("jar",new int[]{R.drawable.icon_jar,507});
//				extensionIconMap.put("gz",new int[]{R.drawable.icon_tar,508});
//				extensionIconMap.put("bz2",new int[]{R.drawable.icon_tar,509});
//				extensionIconMap.put("xz",new int[]{R.drawable.icon_tar,510});
				
				
		//------------docs-------------------------------
				extensionIconMap.put("xml",new int[]{R.drawable.icon_xml,1});
				extensionIconMap.put("html",new int[]{R.drawable.icon_xml,2});
				extensionIconMap.put("xhtml",new int[]{R.drawable.icon_xml,3});
				extensionIconMap.put("txt",new int[]{R.drawable.icon_txt,4});
				extensionIconMap.put("doc",new int[]{R.drawable.icon_doc,5});
				extensionIconMap.put("docx",new int[]{R.drawable.icon_doc,6});
				extensionIconMap.put("xls",new int[]{R.drawable.icon_excel,7});
				extensionIconMap.put("xlsx",new int[]{R.drawable.icon_excel,8});
				extensionIconMap.put("pdf",new int[]{R.drawable.icon_pdf,9});
				extensionIconMap.put("pptx",new int[]{R.drawable.icon_pp,10});
				extensionIconMap.put("ppt",new int[]{R.drawable.icon_pp,11});
			
				
		//------------apk-------------------------------
				extensionIconMap.put("apk", new int[]{R.drawable.icon_apk,400});
				
	}
	

	public static void initIcons() {
		if (dir_empty == null) {
			Context context = CommonUtils.context;
			int size = context.getResources().getDimensionPixelSize(
					R.dimen.dp_45);
			Rect rect = new Rect(0, 0, size, size);
			dir_empty = context.getResources()
					.getDrawable(R.drawable.icon_dir_empty);
			dir_stuffed = context.getResources().getDrawable(
					R.drawable.icon_dir_stuffed);
			fileIcon = context.getResources().getDrawable(R.drawable.icon_file1);
			systemIcon = context.getResources().getDrawable(R.drawable.icon_system);
			errorIcon = context.getResources().getDrawable(R.drawable.icon_error);
			dir_empty.setBounds(rect);
			dir_stuffed.setBounds(rect);
			fileIcon.setBounds(rect);
			systemIcon.setBounds(rect);
			errorIcon.setBounds(rect);
		}
	}
	
	
	
	public static int getFileTYpe(File file){
		String name =file.getName();
		int ind = name.lastIndexOf('.');
		if(ind==-1)
			return -1;
		int [] ary = extensionIconMap.get(name.substring(ind+1).toLowerCase(Locale.CHINA));
		return ary==null||ary[1]<5/*is text*/?-1:(ary[1]/100);
	}
	
	
	
	  /*
     * 
     * appInfo.publicSourceDir = apkPath;(make sure to get the right icon ,not default icon):
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, File apk) {
        PackageManager pm = context.getPackageManager();
        String apkPath=apk.getAbsolutePath();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }
	
	public static String getLinuxFileName(File file){
		String filename=file.getName();
		return TextUtils.isEmpty(filename)?"ROOT":filename;
	}

	public static boolean isReadableFile(File file) {
		return file!=null&&!file.isDirectory() && file.canRead();
	}
	public static boolean isSystemFile(File file) {
		return !(file.isFile()||file.isDirectory());
	}
	

	// setFileIcon
	public static void setFileIconByType(String path, ImageView img, TextView tv) {
		setFileIconByType(new File(path), img, tv);
	}
	

	// setFileIconByType
	public static void setFileIconByType(File file, ImageView img, TextView tv) {
		initIcons();
		if (!file.exists()) {
			img.setImageDrawable(errorIcon);
			img.setAlpha(1f);
			if (tv != null)
				tv.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		} else if (file.isDirectory()) {
			File[] descendant = file.listFiles();
			if (descendant == null || descendant.length == 0)
				img.setImageDrawable(dir_empty);
			else
				img.setImageDrawable(dir_stuffed);
			img.setAlpha(1f);
			if (tv != null)
				tv.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		} else if (file.isFile()) {
			String name = file.getName();
			int ind=name.lastIndexOf('.');
			if(ind==-1){
				img.setImageDrawable(fileIcon);
			}else if(name.toLowerCase(Locale.CHINA).endsWith(".apk")){
				Drawable dra= getApkIcon(CommonUtils.context, file);
				if(dra==null)
					img.setImageDrawable(fileIcon);
				else 
					img.setImageDrawable(dra);
			}else{
				String extension=name.substring(ind+1).toLowerCase(Locale.CHINA);
				int[] ary=extensionIconMap.get(extension);
				if(ary==null){
					img.setImageDrawable(fileIcon);
				}else{
					img.setImageDrawable(CommonUtils.context.getResources()
						.getDrawable(ary[0]));
				}
			}
			
			img.setAlpha(1f);
			if (tv != null)
				tv.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		} else {
			img.setImageDrawable(systemIcon);
			img.setAlpha(0.5f);
			if (tv != null)
				tv.setTextColor(Color.rgb(0x88, 0x88, 0x88));
		}
		if (tv != null)
			tv.setText(file.getName());
	}

	// formatFileSize
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + " GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + " MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + " KB";
		} else if (length < 1024)
			result = Long.toString(length) + " B";
		return result;
	}

	// getFileTypeName
	public static String getFileTypeName(File file) {
		return file.isDirectory() ? "文件夹" : file.isFile() ? "普通文件" : "系统文件";
	}

	// getApplicationRootDir
	public static File getApplicationRootDir(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return context.getExternalFilesDir(null).getParentFile();
		} else {
			return context.getFilesDir().getParentFile();
		}
	}

	// getDataDir
	public static File getDataDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return new File(Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "Android" + File.separator + "data");
		} else {
			return new File(Environment.getDataDirectory().getPath()
					+ File.separator + "data");
		}
	}


	// getBackupDir
	public static File getBackupDir(Context context) {
		File backupDir = new File(getDataDir().getPath() + File.separator
				+ context.getPackageName() + File.separator + IConstants.BACKUP);
		if (!backupDir.exists() || !backupDir.isDirectory())
			backupDir.mkdirs();
		return backupDir;
	}

	// get_backupFile
	public static File get_backupFile(Context context) {
		File backup = new File(getBackupDir(context).getPath() + File.separator
				+ IConstants.ACCOUNT_BACKUP);
		return backup;
	}

	// get_backupFile
	public static File get_backupFile(File parent, int index, String platform) {
		File backup = new File(parent.getPath() + File.separator + platform
				+ "_" + IConstants.ACCOUNT_BACKUP + "_" + index);
		return backup;
	}

	// getUnexistFile
	public static File getUnexistFile(File dest) {
		File file = dest;
		for (int i = 0; file.exists(); i++) {
			file = new File(dest.getAbsolutePath() + "(copy" + i + ")");
		}
		return file;
	}

	// mkDir
	public static int mkDir(File parent, String filename) {
		if (parent == null||!parent.exists() || !parent.isDirectory()||
				!parent.canWrite()|| TextUtils.isEmpty(filename) )
			return 0;
		File file = new File(parent.getPath() + File.separator + filename);
		if (file.exists())
			return -1;
		if (file.mkdirs())
			return 1;
		return 0;
	}
	// createNewFile
		public static int createNewFile(File parent, String filename) throws IOException {
			if (parent == null||!parent.exists() || !parent.isDirectory()||
					!parent.canWrite()|| TextUtils.isEmpty(filename) )
				return 0;
			File file = new File(parent.getPath() + File.separator + filename);
			if (file.exists())
				return -1;
			if (file.createNewFile())
				return 1;
			return 0;
		}


	// deleteFileRecursively
	public static boolean deleteFileRecursively(File file, boolean retainDir) {
		if (!file.exists())
			return true;
		boolean b = true;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				b &= deleteFileRecursively(files[i], false);
			}
			if (!retainDir)
				b &= file.delete();
		} else {
			return  file.delete();
		}
		return b;
	}

	// getMultiFileBytes
	public static FileInfo getMultiFileBytes(File... files) {
		FileInfo fileInfo = new FileInfo();
		for (int i = 0; i < files.length; i++) {
			FileInfo info = getDirBytes(files[i]);
			fileInfo.bytes += info.bytes;
			fileInfo.count += info.count;
		}
		return fileInfo;
	}

	// getFileListBytes
	public static FileInfo getFileListBytes(List<File> files) {
		FileInfo fileInfo = new FileInfo();
		for (int i = 0; files != null && i < files.size(); i++) {
			FileInfo info = getDirBytes(files.get(i));
			fileInfo.bytes += info.bytes;
			fileInfo.count += info.count;
		}
		return fileInfo;
	}

	// getDirBytes
	public static FileInfo getDirBytes(File file) {
		FileInfo fileInfo = new FileInfo();
		if (!file.exists())
			return fileInfo;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; files != null && i < files.length; i++) {
				FileInfo info = getDirBytes(files[i]);
				fileInfo.bytes += info.bytes;
				fileInfo.count += info.count;
			}
		} else {
			fileInfo.bytes = file.length();
			fileInfo.count = 1;
		}
		return fileInfo;
	}

	// fileSizePattern
	public static String fileSizePattern(long bytes) {
		return formatFileSize(bytes) + "  ( "
				+ StringUtil.formatNumbers(bytes, 0) + " B ) ";
	}

	// displayFileSize
	public static void displayFileSize(final File file, final TextView tv) {
		if (file.isDirectory()) {
			IConstants.THREAD_POOL.submit(new Runnable() {
				public void run() {
					final FileInfo fileInfo = FileUtils.getDirBytes(file);
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							tv.setText(fileSizePattern(fileInfo.bytes)
									+ " \n 共 " + fileInfo.count + " 个文件");
						}
					});
				}
			});
		} else {
			tv.setText(fileSizePattern(file.length()));
		}
	}

	public static boolean contains(File src, File dest) {
		if (!dest.getAbsolutePath().contains(src.getAbsolutePath()))
			return false;
		File file = dest;
		for (; file != null; file = file.getParentFile()) {
			if (src.equals(file))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @author Administrator class: FileTransfer
	 */
	public static class FileTransfer {
		// TODO
		public static final int COVER = 1, SKIP = 2, RETAIN = 0;

		public static final String BASE_STOP_RECEIVER_ACTION = "stopReceiverAction_";

		private boolean stop, stopNotification;

		public byte[] buf = new byte[1024 * 128];

		private Context context;

		private FileInfo info, curInfo;

		private File curFile;

		private int notiId;

		private String stopReceiverAction;

		private BroadcastReceiver stopReceiver;

		private Intent stopIntent;

		private NotificationManager nm;

		private Notification noti;

		public FileTransfer() {
		}

		private void onPrephase(Context con) {
			start();
			curInfo = new FileInfo();
			this.context = con.getApplicationContext();
			notiId = hashCode();

			stopReceiverAction = BASE_STOP_RECEIVER_ACTION + notiId;
			stopReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					if (stopReceiverAction.equalsIgnoreCase(intent.getAction())) {
						stop();
					}
				}
			};
			IntentFilter intentFilter = new IntentFilter(stopReceiverAction);
			context.registerReceiver(stopReceiver, intentFilter);
			stopIntent = new Intent(stopReceiverAction);
			initNotification();
		}

		private void initNotification() {
			nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			noti = new Notification();
			noti.icon = R.drawable.ic_launcher;
			noti.vibrate = new long[] { 100, 200 };
			noti.contentView = new RemoteViews(context.getPackageName(),
					R.layout.notification_progress);
			noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			noti.tickerText = "开始复制";
			noti.contentView.setOnClickPendingIntent(R.id.imageButton1,
					PendingIntent.getBroadcast(context, 0, stopIntent, 0));
		}

		private void onAnaphase(Context context) {
			context.getApplicationContext().unregisterReceiver(stopReceiver);
		}

		/***
		 * 
		 * @param src
		 * @param destParent
		 * @param removeSrcOnComplete
		 * @param removeDestOnStop
		 * @param type
		 * @param context
		 * @return
		 */
		public FileTransfer startTransfer(File src, File destParent,
				boolean removeSrcOnComplete, boolean removeDestOnStop,
				int type, Context context) {
			onPrephase(context);
			startNotification();
			info = calculateDirBytes(src);
			paste(src, destParent, type, removeSrcOnComplete, removeDestOnStop);
			onAnaphase(context);
			anaPhaseNotification();
			return this;
		}

		/***
		 * 
		 * @param copyList
		 * @param moveList
		 * @param destParent
		 * @param pasteType
		 * @param context
		 * @return
		 */
		public FileTransfer startBatchTransfer(List<File> copyList,
				List<File> moveList, File destParent, int pasteType,
				Context context) {
			onPrephase(context);
			startNotification();
			info = getTotalFileBytes(copyList, moveList);
			for (int i = 0; !stop && copyList != null && i < copyList.size(); i++) {
				paste(copyList.get(i), destParent, pasteType, false, true);
			}
			for (int i = 0; !stop && moveList != null && i < moveList.size(); i++) {
				paste(moveList.get(i), destParent, pasteType, true, true);
			}

			onAnaphase(context);
			anaPhaseNotification();
			return this;
		}

		private void startNotification() {
			stopNotification = false;
			IConstants.MAIN_HANDLER.post(new Runnable() {
				public void run() {
					if (!stopNotification) {
						noti.contentView.setTextViewText(R.id.textView1,
								getCurInfoCount());
						noti.contentView.setTextViewText(R.id.textView2,
								getCurInfoBytes());
						noti.contentView.setTextViewText(R.id.textView3,
								getCurrentTime());
						nm.notify(notiId, noti);
						IConstants.MAIN_HANDLER.postDelayed(this, 2000);
					} else {
						IConstants.MAIN_HANDLER.removeCallbacks(this);
					}
				}
			});
		}

		private void anaPhaseNotification() {
			stopNotification = true;
			if (stop) {
				noti.flags |= Notification.FLAG_AUTO_CANCEL;
				noti.tickerText = "复制中止";
				noti.when = System.currentTimeMillis();
				noti.setLatestEventInfo(context, "复制中止", getCurInfoCount()
						+ "\n" + getCurInfoBytes(), null);
				nm.cancel(notiId);
				nm.notify(notiId, noti);
			} else {
				noti.flags |= Notification.FLAG_AUTO_CANCEL;
				noti.tickerText = "复制完成";
				noti.when = System.currentTimeMillis();
				noti.setLatestEventInfo(context, "复制完成", getCurInfoCount()
						+ "\n" + getCurInfoBytes(), null);
				nm.cancel(notiId);
				nm.notify(notiId, noti);
			}
		}

		private String getCurrentTime() {
			Date date = new Date();
			return date.getHours() + ":" + date.getMinutes();
		}

		private String getCurInfoCount() {
			if (curInfo == null || info == null)
				return "计算中。。。";
			return "第 " + curInfo.count + " 个文件  ( 共 " + info.count + " 个 )";
		}

		private String getCurInfoBytes() {
			if (curInfo == null || info == null) {
				noti.contentView.setProgressBar(R.id.progressBar1, 100, 0,
						false);
				return "0.0% ( ... / ... )";
			}
			float percent = (float) curInfo.bytes * 100 / info.bytes;
			noti.contentView.setProgressBar(R.id.progressBar1, 100,
					(int) percent, false);
			return String.format("%.1f", percent) + "% ( "
					+ formatFileSize(curInfo.bytes) + " / "
					+ formatFileSize(info.bytes) + " )";
		}

		private FileInfo getTotalFileBytes(List<File> copyList,
				List<File> moveList) {
			FileInfo fileInfo = new FileInfo();
			FileInfo info1 = calculateFileListBytes(copyList);
			fileInfo.bytes += info1.bytes;
			fileInfo.count += info1.count;
			FileInfo info2 = calculateFileListBytes(moveList);
			fileInfo.bytes += info2.bytes;
			fileInfo.count += info2.count;
			return fileInfo;
		}
		

		public FileInfo calculateFileListBytes(List<File> files) {
			FileInfo fileInfo = new FileInfo();
			for (int i = 0; !stop && files != null && i < files.size(); i++) {
				FileInfo info = calculateDirBytes(files.get(i));
				fileInfo.bytes += info.bytes;
				fileInfo.count += info.count;
			}
			return fileInfo;
		}

		public FileInfo calculateDirBytes(File file) {
			FileInfo fileInfo = new FileInfo();
			if (!file.exists())
				return fileInfo;
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; !stop && files != null && i < files.length; i++) {
					FileInfo info = calculateDirBytes(files[i]);
					fileInfo.bytes += info.bytes;
					fileInfo.count += info.count;
				}
			} else {
				fileInfo.bytes = file.length();
				fileInfo.count = 1;
			}
			return fileInfo;
		}

		/**
		 * FileTransfer core Code
		 */
		public void stop() {
			stop = true;
		}

		public void start() {
			stop = false;
		}

		public boolean copyFileRecursively(File src, File dest,boolean removeDestOnStop)
				throws IOException {
			boolean b = true;
			if (src.isDirectory()) {
				if (!dest.exists() && !dest.mkdir()) {
					return false;
				}
				File[] files = src.listFiles();
				for (int i = 0; i < files.length && !stop; i++) {
					File file = files[i];
					b &= copyFileRecursively(file,
							new File(dest.getAbsolutePath() + File.separator
									+ file.getName()),removeDestOnStop);
				}
				return b;
			}
			curInfo.count++;
			InputStream is = null;
			OutputStream os = null;
			try {
				if (dest.exists())
					dest.delete();
				is = new FileInputStream(src);
				os = new FileOutputStream(dest);
				int count;
				while (!stop) {
					count = is.read(buf);
					if (count != -1) {
						os.write(buf, 0, count);
						os.flush();
						curInfo.bytes += count;
					} else
						return true;
				}
				
				
				if (removeDestOnStop)
					if (!dest.isDirectory())
						deleteFileRecursively(dest, false);
				return false;
			} finally {
				if (is != null)
					is.close();
				if (os != null) {
					os.flush();
					os.close();
				}
			}
		}

		private void paste(File src, File destParent, int pasteType,
				boolean removeSrcOnComplete, boolean removeDestOnStop) {
			curFile = src;
			if (src == null || !src.exists() || !src.canRead()
					|| destParent == null || !destParent.canWrite())
				return;

			if (contains(src, destParent)) {
				addToCurInfo(src);
				IConstants.MAIN_HANDLER.post(new Runnable() {
					public void run() {
						CommonUtils.toast("无法复制：目标文件夹与源文件夹相同或是其子文件夹");
					}
				});
				return;
			}

			File dest = new File(destParent.getAbsolutePath() + File.separator
					+ src.getName());
			if (pasteType == COVER) {
				if (dest.exists()) {
					if (src.equals(dest)) {
						addToCurInfo(src);
						return;
					}
					deleteFileRecursively(dest, false);
				}
			} else if (pasteType == SKIP) {
				if (dest.exists()) {
					addToCurInfo(src);
					return;
				}
			} else {
				if (dest.exists()) {
					if (removeSrcOnComplete && dest.equals(src)) {
						addToCurInfo(src);
						return;
					}
					dest = getUnexistFile(dest);
				}
			}
			if (removeSrcOnComplete) {
				if (src.renameTo(dest)) {
					addToCurInfo(src);
					return;
				}
			}

			boolean b = false;
			try {
				b = copyFileRecursively(src, dest,removeDestOnStop);
			} catch (IOException e) {
				e.printStackTrace();
				stop = true;
			}
			if (stop || !b) {
				if (removeDestOnStop)
					if (!dest.isDirectory())
						deleteFileRecursively(dest, false);
			} else {
				if (removeSrcOnComplete)
					deleteFileRecursively(src, false);
			}
		}

		private void addToCurInfo(File file) {
			FileInfo fi = calculateDirBytes(file);
			curInfo.bytes += fi.bytes;
			curInfo.count += fi.count;
		}
	}

	/***
	 * saveFile
	 */
	public static class FileSaver implements Runnable {
		// TODO
		public File src, dest;
		private long startOffset;
		private int modifiedLength, bufsize = 1024 * 4;
		private byte[] newContent;
		private byte[] buf = new byte[bufsize];
		private boolean stop;

		public long doneBytes = 0,sumBytes;
		
		public double getDonePercent(){
			return (doneBytes*100/(double)sumBytes);
		}

		public FileSaver(File src, long startOffset, int modifiedLength,
				byte[] newContent) {
			this.src = src;
			this.startOffset = startOffset;
			this.modifiedLength = modifiedLength;
			this.newContent = newContent;
			if (this.startOffset < 0)
				this.startOffset = 0;
			if (this.modifiedLength < 0)
				this.modifiedLength = 0;
			if (this.newContent == null)
				this.newContent = new byte[0];
			stop = false;
		}

		public FileSaver(File src, File dest, long startOffset,
				int modifiedLength, byte[] newContent) {
			this(src, startOffset, modifiedLength, newContent);
			this.dest = dest;
		}

		public void run() {
			if (src == null || !src.exists() || src.isDirectory()||!src.canRead())
				return;
			if (this.startOffset > src.length())
				this.startOffset = src.length();
			if (dest == null || src.equals(dest)) {
				if(!src.canWrite()) return ;
				RandomAccessFile raf = null;
				try {
					raf = new RandomAccessFile(src, "rw");
					if (modifiedLength > newContent.length) {
						sumBytes=src.length()-startOffset-modifiedLength+newContent.length;
						shortenSave(raf);
					} else if (modifiedLength == newContent.length) {
						sumBytes=modifiedLength;
						fixedSave(raf);
					} else {
						sumBytes=src.length()-startOffset-modifiedLength+newContent.length;
						extendedSave(raf);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (raf != null)
						try {
							raf.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			} else {
				if(dest.getParentFile()==null||!dest.getParentFile().exists()||!dest.getParentFile().canWrite())
					return ;
				sumBytes=src.length()-modifiedLength+newContent.length;
				FileInputStream fis = null;
				FileOutputStream fos = null;
				try {
					if (dest.exists())
						dest.delete();
					dest.createNewFile();
					fis = new FileInputStream(src);
					fos = new FileOutputStream(dest);
					saveas(fis, fos);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (fis != null)
							fis.close();
						if (fos != null)
							fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void shortenSave(RandomAccessFile raf) throws IOException {
			raf.seek(startOffset);
			raf.write(newContent);
			doneBytes+=newContent.length;
			long writeIndex, readIndex;
			int count = 0;
			readIndex = startOffset + modifiedLength;
			writeIndex = startOffset + newContent.length;
			raf.seek(readIndex);
			while (!stop && (count = raf.read(buf)) != -1) {
				raf.seek(writeIndex);
				raf.write(buf, 0, count);
				readIndex += count;
				writeIndex += count;
				doneBytes+=count;
				raf.seek(readIndex);
			}
			long formerlength=src.length();
			long newlength=formerlength>startOffset+modifiedLength?
					formerlength-modifiedLength+newContent.length:
						startOffset+newContent.length;
			raf.setLength(newlength);
		}

		private void extendedSave(RandomAccessFile raf) throws IOException {
			long oldlength = raf.length();
			long rest = oldlength - startOffset - modifiedLength;
			long newlength = rest > 0 ? oldlength + newContent.length
					- modifiedLength : startOffset + newContent.length;
			raf.setLength(newlength);
			long writeIndex, readIndex;
			int count = getExtendedReadCount(oldlength);
			readIndex = oldlength - count;
			writeIndex = newlength - count;
			while (!stop && count > 0) {
				raf.seek(readIndex);
				raf.read(buf, 0, count);
				raf.seek(writeIndex);
				raf.write(buf, 0, count);
				count = getExtendedReadCount(readIndex);
				readIndex -= count;
				writeIndex -= count;
				doneBytes+=count;
			}
			raf.seek(startOffset);
			raf.write(newContent);
			doneBytes+=newContent.length;
		}

		private int getExtendedReadCount(long readIndex) {
			long rest = readIndex - startOffset - modifiedLength;
			return (int) (rest > bufsize ? bufsize : rest);
		}

		private void fixedSave(RandomAccessFile raf) throws IOException {
			raf.seek(startOffset);
			raf.write(newContent);
			doneBytes+=newContent.length;
		}

		private void saveas(FileInputStream fis, FileOutputStream fos)
				throws IOException {
			int count = 0;
			doneBytes = 0;
			while (!stop && startOffset > doneBytes) {
				count = fis.read(buf, 0, getReadCount());
				fos.write(buf, 0, count);
				fos.flush();
				doneBytes += count;
			}
			if (!stop) {
				fos.write(newContent);
				fos.flush();
				doneBytes += newContent.length;
				long skiped=fis.skip(modifiedLength);
				while(!stop&&skiped<modifiedLength){
					skiped+=fis.skip(modifiedLength-skiped);
				}
			}
			while (!stop && (count = fis.read(buf)) != -1) {
				fos.write(buf, 0, count);
				fos.flush();
				doneBytes += count;
			}
		}

		
		//only be used in saveas
		private int getReadCount() {
			return (int) (startOffset - doneBytes <= 0 ? bufsize : startOffset
					- doneBytes >= bufsize ? bufsize : startOffset - doneBytes);
		}

		public void stop() {
			stop = true;
		}
		public boolean isStop(){
			return stop;
		}
	}

	
	
	//TODO
	/**
	 * File  md5sum
	 */
	
	public static class Md5sumer{
		
		public File file;
		private boolean stop;
		public long doneBytes = 0,sumBytes;
		
		public Md5sumer(File file){
			this.file=file;
			this.sumBytes=file.length();
		}
		public void stop(){
			this.stop=true;
		}
		
		
		public double getDonePercent(){
			return (doneBytes*100/(double)sumBytes);
		}
		public boolean isStop(){
			return stop;
		}
		public String start(){
			stop=false;
			if(!FileUtils.isReadableFile(file)) return "";
			FileInputStream fis=null;
			try {
				MessageDigest md = MessageDigest.getInstance("md5");
				fis = new FileInputStream(file);
				long length=fis.available();
				int count ;
				byte[] buf =new byte[length>(1024*64)?1024*64:(int)length];
				while(!stop&&(count=fis.read(buf))!=-1){
					md.update(buf, 0, count);
					doneBytes+=count;
				}
				return MD5Util.toHexStr(md.digest());
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis!=null)
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			return "";
			
		}
	}

}
