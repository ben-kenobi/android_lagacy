package fj.swsk.cn.eqapp.util;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

import fj.swsk.cn.eqapp.R;


public class SystemUtil {
	public static void intentToCall(String phoneNum, Context context) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//		context.startActivity(intent);
	}

	public static void intentToDial(String phoneNum, Context context) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		context.startActivity(intent);
	}

	public static void intentToMessage(String phoneNum, Context context,
			String content) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"
				+ phoneNum)).putExtra("sms_body", content).setFlags(
				Intent.FLAG_ACTIVITY_NO_HISTORY);
		context.startActivity(intent);
	}

	public static void intentToMessageType2(String phoneNum, Context context,
			String content) {
		Intent intent = new Intent(Intent.ACTION_VIEW)
				.setType("vnd.android-dir/mms-sms")
				.putExtra("sms_body", content).putExtra("address", phoneNum)
				.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		context.startActivity(intent);
	}

	public static void intentTosendMessage(String phoneNum, Context context,
			String content) {
		Intent it = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto",
				phoneNum, null));
		it.putExtra("sms_body", content);
		context.startActivity(it);
	}

	/**
	 * 
	 * File OpenAs
	 */
	public static void openAsVideo(File file, Context context) {
		openAsByType(file, context, "video/*", true);
	}

	public static void openAsAudio(File file, Context context) {
		openAsByType(file, context, "audio/*", true);
	}

	public static void openAsImage(File file, Context context) {
		openAsByType(file, context, "image/*", true);
	}

	public static void openAsText(File file, Context context) {
		openAsByType(file, context, "text/*", false);
	}

	public static void openAsInstallation(File file, Context context) {
		openAsByType(file, context, "application/vnd.android.package-archive",
				false);
	}

	public static void openAsByType(File file, Context context, String type,
			boolean chooser) {
		Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK).setDataAndType(
				Uri.fromFile(file), type);
		if (chooser)
			context.startActivity(Intent.createChooser(intent,
					"choose application"));
		else
			context.startActivity(intent);
	}

	public static void openAs(File file, int type, Context context) {
		switch (type) {
		case 0:
			openAsText(file, context);
			break;
		case 1:
			openAsImage(file, context);
			break;
		case 2:
			openAsVideo(file, context);
			break;
		case 3:
			openAsAudio(file, context);
			break;
		case 4:
			openAsInstallation(file, context);
			break;
		default:
			break;
		}
	}

	public static void viewFileByType(File file, ResolveInfo ri, String type,
			Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW).addFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK).setDataAndType(
				Uri.fromFile(file), type);
		if (ri != null) {
			intent.setComponent(new ComponentName(
					ri.activityInfo.applicationInfo.packageName,
					ri.activityInfo.name));
		}
		context.startActivity(intent);
	}


	// shareFile
	public static void shareFile(File file, Context context) {
		context.startActivity(Intent.createChooser(
				new Intent(Intent.ACTION_SEND).setType("*/*").putExtra(
						Intent.EXTRA_STREAM, Uri.fromFile(file)),
				context.getString(R.string.shareTo)));
	}

	// shareFile
	public static void shareFile(File file, ResolveInfo ri, Context context) {
		if (ri != null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("*/*");
			// intent.putExtra(Intent.EXTRA_SUBJECT,"");
			// intent.putExtra(Intent.EXTRA_TEXT,"");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			intent.setComponent(new ComponentName(
					ri.activityInfo.applicationInfo.packageName,
					ri.activityInfo.name));
			context.startActivity(intent);
		}
	}
	
	public static void intent2Cam(Activity con,int result,Uri uri){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 2);
		intent.putExtra("autofocus", true);
		intent.putExtra("fullScreen", false);
		intent.putExtra("showActionIcons", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		con.startActivityForResult(intent, result);
	}
	public static void intent2pick(Activity con,int result){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		con.startActivityForResult(intent, result);
	}
	public static void intent2Img(Activity con,int result){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		con.startActivityForResult(intent, result);
	}

	/**
	 * 判断 SDCard是否可用
	 *
	 * @return
	 */
	public static boolean checkSDCardIsAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}


	

	public static void intentToEmail(String mailbox, Context context,
			String content) {
		// 1
		// Uri uri = Uri.parse("mailto:"+mailbox);
		// Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		// context.startActivity(it);

		// 2
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_EMAIL, mailbox);
		it.putExtra(Intent.EXTRA_TEXT, content);
		it.setType("text/plain");
		context.startActivity(Intent.createChooser(it, "Choose Email Client"));

		// //3
		// String[] tos={mailbox};
		// String[] ccs={mailbox};
		// it.putExtra(Intent.EXTRA_EMAIL, tos);
		// it.putExtra(Intent.EXTRA_CC, ccs);
		// it.putExtra(Intent.EXTRA_TEXT, content);
		// it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
		// it.setType("message/rfc822");
		// context.startActivity(Intent.createChooser(it,
		// "Choose Email Client"));
		//
		//
		// //4
		// it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
		// it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3");
		// it.setType("audio/mp3");
		// context.startActivity(Intent.createChooser(it,
		// "Choose Email Client"));

	}

}
