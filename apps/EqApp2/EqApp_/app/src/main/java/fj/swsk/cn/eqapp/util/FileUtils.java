package fj.swsk.cn.eqapp.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static boolean isReadableFile(File file) {
		return file != null && !file.isDirectory() && file.canRead();
	}

	public static boolean isSystemFile(File file) {
		return !(file.isFile() || file.isDirectory());
	}
	public static File newFile(String parent,String name){
		File dir = new File(parent);
		if(!dir.exists())
			dir.mkdirs();
		File file = new File(dir,name);
		return file;
	}

	// formatFileSize
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + " G";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + " M";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + " K";
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


	// mkDir
	public static int mkDir(File parent, String filename) {
		if (parent == null || !parent.exists() || !parent.isDirectory() ||
				!parent.canWrite() || TextUtils.isEmpty(filename))
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
		if (parent == null || !parent.exists() || !parent.isDirectory() ||
				!parent.canWrite() || TextUtils.isEmpty(filename))
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
			return file.delete();
		}
		return b;
	}

	public static String  getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
			return sdDir.toString();
		}
		return null;

	}

}
