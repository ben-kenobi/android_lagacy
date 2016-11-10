package com.icanit.app_v2.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.FileCache;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.MemoryCache;

public class ImageUtil {
	public static DisplayMetrics dm ;
	static {
		dm=DeviceInfoUtil.getDisplayMetrics();
	}
	private static FileCache fileCache = FileCache.getInstance();
	private static MemoryCache memoryCache = MemoryCache.getInstance();
	private static Map<ImageView, String> map = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private  static Handler handler =IConstants.MAIN_HANDLER;

	private static void prepareLoadImage(ImageView imageView, String url,
			Context context) {
		map.put(imageView, url);
		Animation anim = imageView.getAnimation();
		if (anim == null) {
			anim = AnimationUtils.loadAnimation(context,
					R.anim.loading_pic_rotation);
			imageView.setAnimation(anim);
			imageView.setBackgroundResource(R.drawable.shape_loading_pic);
		}
		// imageView.setImageBitmap(null);
		anim.reset();
		anim.start();
	}

	private static Bitmap getBitmap(String url,ImageView container) throws Exception {
		Bitmap bm =null;
		if (fileCache != null) {
			bm= fileCache.getBitmap(url);
			if (bm != null) {
				return bm;
			}
		}
		HttpURLConnection connection = null;
		try {
			connection = AppUtil.getNetUtilInstance().getHttpUrlConnection(url);
			if (connection.getResponseCode() != 200) {
				return null;
			}
			InputStream is = connection.getInputStream();
			bm = BitmapFactory.decodeStream(is);
			bm = resizeBitmap(bm,container);
			return bm;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static void asyncDownloadImageAndShow(final ImageView imageView,
			final String uri, final Context context, boolean busy) {
		System.out.println(uri+"  @ImageUtil");
		if(TextUtils.isEmpty(uri)) {
			handler.post(new Runnable() {
				public void run() {
					if (imageView != null) {
						if (imageView.getAnimation() != null)
							imageView.getAnimation().cancel();
						imageView
								.setImageResource(R.drawable.app_logo);
					}
				}
			});
			return;
		}
		//TODO
		final String url = IConstants.IMAGE_PREFIX + uri;
		imageView.setImageBitmap(null);
		Bitmap bitmap = memoryCache.getBitmap(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			if (imageView.getAnimation() != null)
				imageView.getAnimation().cancel();
			return;
		}
		if (busy)
			return;
		prepareLoadImage(imageView, url, context);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					if (posChanged(imageView, url)){
						return;
					}
						
					final Bitmap bm = getBitmap(url,imageView);
					if (bm != null)
						memoryCache.cacheBitmap(url, bm);
					if (posChanged(imageView, url)){
						return;
					}
					if (bm == null) {
						// failure to load image
						handler.post(new Runnable() {
							public void run() {
								if (imageView != null) {
									if (imageView.getAnimation() != null)
										imageView.getAnimation().cancel();
									imageView
											.setImageResource(R.drawable.app_logo);
								}
							}
						});
						return;
					}
					handler.post(new Runnable() {
						public void run() {
							if (imageView != null) {
								if (imageView.getAnimation() != null)
									imageView.getAnimation().cancel();
								imageView.setImageBitmap(bm);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					// load image exception
					handler.post(new Runnable() {
						public void run() {
							if (imageView != null) {
								if (imageView.getAnimation() != null)
									imageView.getAnimation().cancel();
								imageView.setImageResource(R.drawable.chrome);
							}
						}
					});
				}
			}
		});
	}

	private static Bitmap resizeBitmap(Bitmap bitmap,ImageView container) {
		Matrix matrix = new Matrix();
		float fx = container.getWidth()/ (float) bitmap.getWidth();
		matrix.setScale(fx, fx);
		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		if(bitmap!=bm)
		bitmap.recycle();
		return bm;
	}

	private static boolean posChanged(ImageView view, String url) {
		return !url.equals(map.get(view));
	}

}
