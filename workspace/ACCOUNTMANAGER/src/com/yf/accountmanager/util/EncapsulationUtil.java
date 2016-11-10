package com.yf.accountmanager.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.FileInfo;
import com.yf.accountmanager.common.IConstants;

public class EncapsulationUtil {
	public static final String BASE_STOP_RECEIVER_ACTION = "stopReceiverAction_";

	public static void deflate(File[] files, File dest, String destName,
			String charset, boolean override) throws IOException, JSONException {

		File to = null;
		if (override) {
			to = new File(dest.getAbsolutePath() + File.separator + destName);
		} else {
			to = generateUniqueFile(dest.getAbsolutePath() + File.separator
					+ destName);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(to);
			fos.write(arrange(files).toString().getBytes(charset));
			fos.write(10);
			// write a extra byte as a separator
			fos.write(10);
			fos.flush();
			writeFile(fos, files, new byte[1024 * 8]);
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public static File generateUniqueFile(String filename) {
		File file = new File(filename);
		int index = 0;
		while (file.exists()) {
			file = new File(filename + "_" + (index++));
		}
		return file;
	}

	public static void infate(File file, File dest, String charset,
			boolean override) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			String str = readLine(fis, charset);
			JSONObject jo = new JSONObject(str);
			String rootKey = (String) jo.keys().next();
			String root = null;
			if (override) {
				root = dest.getAbsolutePath() + File.separator + rootKey;
			} else {
				root = generateUniqueFile(
						dest.getAbsolutePath() + File.separator + rootKey)
						.getAbsolutePath();
			}
			distributeFiles(fis, jo, root, rootKey, new byte[1024 * 8]);
		} finally {
			if (fis != null)
				fis.close();
		}
	}

	public static void distributeFiles(FileInputStream fis, JSONObject jo,
			String root, String key, byte[] buf) throws Exception {
		Object obj = jo.get(key);
		// System.out.println(root);
		if (obj.getClass() == JSONArray.class) {
			File file = new File(root);
			if (!file.exists())
				file.mkdir();
			JSONArray ja = (JSONArray) obj;
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jso = ja.getJSONObject(i);
				String skey = (String) jso.keys().next();
				distributeFiles(fis, jso, root + File.separator + skey, skey,
						buf);
			}

		} else {
			long length = Long.valueOf(obj.toString());
			File file = new File(root);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				int count = 0;
				while ((count = fis.read(buf, 0,
						(int) (length > buf.length ? buf.length : length))) != -1
						&& length > 0) {
					fos.write(buf, 0, count);
					fos.flush();
					length = length - count;
				}
			} finally {
				if (fos != null)
					fos.close();
			}
		}
	}

	public static String readLine(InputStream is, String charset)
			throws IOException {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			int b;
			while ((b = is.read()) != 10 && b != -1) {
				baos.write(b);
			}
			if (b == 10) {
				byte[] buf = baos.toByteArray();
				// read the next byte that was writed before as a separator
				is.read();
				return new String(buf, charset);
			} else {
				return null;
			}
		} finally {
			if (baos != null)
				baos.close();
		}
	}

	public static void writeFile(OutputStream os, File[] files, byte[] buf)
			throws IOException {
		for (int i = 0; i < files.length; i++) {
			writeFile(os, files[i], buf);
		}
	}

	public static void writeFile(OutputStream os, File file, byte[] buf)
			throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				writeFile(os, files[i], buf);
			}
		} else {
			FileInputStream fis = null;
			try {
				int count = 0;
				fis = new FileInputStream(file);
				while ((count = fis.read(buf)) != -1) {
					os.write(buf, 0, count);
				}
				os.flush();
			} finally {
				if (fis != null)
					fis.close();
			}
		}
	}

	public static JSONObject arrange(File[] files) throws JSONException {
		if (files == null)
			return null;
		if (files.length == 1)
			return arrange(files[0]);
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (int i = 0; i < files.length; i++) {
			ja.put(arrange(files[i]));
		}
		jo.put("untitled", ja);
		return jo;
	}

	public static JSONObject arrange(File file) throws JSONException {
		if (file.isDirectory()) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				ja.put(arrange(files[i]));
			}
			jo.put(file.getName(), ja);
			return jo;
		} else {
			JSONObject jo = new JSONObject();
			jo.put(file.getName(), file.length());
			return jo;
		}
	}

	public static class FileINDEflator implements Runnable {

		// TODO

		public boolean override = false;

		public File[] files;

		public File tar;

		public File dest;

		public String name;

		public Context context;

		public String charset = "utf-8";

		public FileInfo info, curInfo;

		public byte[] buf = new byte[1024 * 32];

		private int notiId;

		private String stopReceiverAction;

		private BroadcastReceiver stopReceiver;

		private Intent stopIntent;

		private NotificationManager nm;

		private Notification noti;

		private boolean stop, stopNotification;

		// deflate
		public FileINDEflator(File[] files, File dest, String name,
				Context context) {
			this.files = files;
			this.dest = dest;
			this.name = name;
			this.context = context.getApplicationContext();
			this.stop = false;
			this.curInfo = new FileInfo();
		}

		// inflate
		public FileINDEflator(File tar, File dest, Context context) {
			this.tar = tar;
			this.dest = dest;
			this.context = context.getApplicationContext();
			this.stop = false;
			this.curInfo = new FileInfo();
			this.curInfo.count = 1;
		}

		public void stop() {
			this.stop = true;
		}

		public boolean isStop() {
			return stop;
		}

		public void run() {
			onPrephase();
			startNotification();
			try {
				if (tar == null) {
					info = FileUtils.getMultiFileBytes(files);
					startCompress();
				} else {
					info = FileUtils.getMultiFileBytes(tar);
					startInflate();
				}

			} catch (Exception e) {
				stop();
				e.printStackTrace();
			}

			onAnaphase();
			anaPhaseNotification();

		}

		public void onPrephase() {
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
			noti.tickerText = "开始" + getOperationName();
			noti.contentView.setOnClickPendingIntent(R.id.imageButton1,
					PendingIntent.getBroadcast(context, 0, stopIntent, 0));
		}

		public void startNotification() {
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
						IConstants.MAIN_HANDLER.postDelayed(this, 1500);
					} else {
						IConstants.MAIN_HANDLER.removeCallbacks(this);
					}
				}
			});
		}

		public void onAnaphase() {
			context.unregisterReceiver(stopReceiver);
		}

		private String getOperationName() {
			return tar == null ? "压缩" : "解压";
		}

		public void anaPhaseNotification() {
			stopNotification = true;
			if (stop) {
				noti.flags |= Notification.FLAG_AUTO_CANCEL;
				noti.tickerText = getOperationName() + "中止";
				noti.when = System.currentTimeMillis();
				noti.setLatestEventInfo(context, getOperationName() + "中止",
						getCurInfoCount() + "\n" + getCurInfoBytes(), null);
				nm.cancel(notiId);
				nm.notify(notiId, noti);
			} else {
				noti.flags |= Notification.FLAG_AUTO_CANCEL;
				noti.tickerText = getOperationName() + "完成";
				noti.when = System.currentTimeMillis();
				noti.setLatestEventInfo(context, getOperationName() + "完成",
						getCurInfoCount() + "\n" + getCurInfoBytes(), null);
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
			return getOperationName() + "第 " + curInfo.count + " 个文件  ( 共 "
					+ info.count + " 个 )";
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
					+ FileUtils.formatFileSize(curInfo.bytes) + " / "
					+ FileUtils.formatFileSize(info.bytes) + " )";
		}

		// compress
		public void startCompress() throws Exception {
			if (dest == null || !dest.canWrite()) {
				stop();
				return;
			}
			File to = null;
			if (override) {
				to = new File(dest.getAbsolutePath() + File.separator + name);
			} else {
				to = generateUniqueFile(dest.getAbsolutePath() + File.separator
						+ name);
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(to);
				fos.write(arrange(files).toString().getBytes(charset));
				fos.write(10);
				// write a extra byte as a separator
				fos.write(10);
				fos.flush();
				writeFile(fos, files, buf);
			} finally {
				if (fos != null)
					fos.close();
			}
		}

		public void writeFile(OutputStream os, File[] files, byte[] buf)
				throws IOException {
			for (int i = 0; !stop && i < files.length; i++) {
				writeFile(os, files[i], buf);
			}
		}

		public void writeFile(OutputStream os, File file, byte[] buf)
				throws IOException {
			if (file == null || !file.exists() || !file.canRead())
				return;
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; !stop && i < files.length; i++) {
					writeFile(os, files[i], buf);
				}
			} else {
				curInfo.count++;
				FileInputStream fis = null;
				try {
					int count = 0;
					fis = new FileInputStream(file);
					while (!stop && (count = fis.read(buf)) != -1) {
						os.write(buf, 0, count);
						curInfo.bytes += count;
					}
					os.flush();
				} finally {
					if (fis != null)
						fis.close();
				}
			}

		}

		// start inflate
		private void startInflate() throws Exception {
			// TODO
			if (tar == null || !tar.exists() || !tar.isFile() || !tar.canRead()
					|| dest == null || !dest.canWrite()) {
				stop();
				return;
			}

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(tar);
				String str = readLine1(fis, charset);
				if(str==null||stop){
					stop();
					return ;
				}
				JSONObject jo = new JSONObject(str);
				String rootKey = (String) jo.keys().next();
				String root = null;
				if (override) {
					root = dest.getAbsolutePath() + File.separator + rootKey;
				} else {
					root = generateUniqueFile(
							dest.getAbsolutePath() + File.separator + rootKey)
							.getAbsolutePath();
				}
				distributeFiles(fis, jo, root, rootKey, buf);
			} finally {
				if (fis != null)
					fis.close();
			}

		}

		private String readLine1(InputStream is, String charset)
				throws IOException {
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				int b=0;
				while (!stop && (b = is.read()) != 10 && b != -1) {
					baos.write(b);
				}
				if (!stop&&b == 10) {
					byte[] buf = baos.toByteArray();
					// read the next byte that was writed before as a separator
					is.read();
					curInfo.bytes += buf.length + 2;
					return new String(buf, charset);
				} else {
					return null;
				}
			} finally {
				if (baos != null)
					baos.close();
			}
		}

		private void distributeFiles(FileInputStream fis, JSONObject jo,
				String root, String key, byte[] buf) throws Exception {
			Object obj = jo.get(key);
			// System.out.println(root);
			if (obj.getClass() == JSONArray.class) {
				File file = new File(root);
				if (!file.exists())
					file.mkdir();
				JSONArray ja = (JSONArray) obj;
				for (int i = 0; !stop && i < ja.length(); i++) {
					JSONObject jso = ja.getJSONObject(i);
					String skey = (String) jso.keys().next();
					distributeFiles(fis, jso, root + File.separator + skey,
							skey, buf);
				}

			} else {
				long length = Long.valueOf(obj.toString());
				File file = new File(root);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					int count = 0;
					while (!stop
							&& (count = fis.read(buf, 0,
									(int) (length > buf.length ? buf.length
											: length))) != -1 && length > 0) {
						fos.write(buf, 0, count);
						fos.flush();
						length = length - count;
						curInfo.bytes += count;
					}
					if (stop) {
						FileUtils.deleteFileRecursively(file, false);
					}
				} finally {
					if (fos != null)
						fos.close();
				}
			}
		}

	}

}