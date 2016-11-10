package com.icanit.app.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.icanit.app.R;
import com.icanit.app.common.FileCache;
import com.icanit.app.common.IConstants;
import com.icanit.app.common.MemoryCache;
import com.icanit.app.service.DataService;

public class ImageUtil {
	public static Point screenSize = new Point();
	static {
		((WindowManager) AppUtil.appContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getSize(screenSize);
		Log.i("bmInfo", "width=" + screenSize.x + ",height=" + screenSize.y
				+ "  @ImageUtil");
	}
	private static FileCache fileCache = FileCache.getInstance();
	private static MemoryCache memoryCache = MemoryCache.getInstance();
	private static Map<ImageView, String> map = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private static Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
		}
	};

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

	private static Bitmap getBitmap(String uri) throws Exception {
		String url = IConstants.URL_PREFIX + uri;
		Bitmap bm =null;
		if (fileCache != null) {
			bm= fileCache.getBitmap(url);
			if (bm != null) {
				Log.d("fileInfo", "findBitmapFromFileCache  @ImageUtil");
				return bm;
			}
		}
		HttpURLConnection connection = null;
		try {
			connection = AppUtil.getNetUtilInstance().getUrlConnection(uri);
			Log.d("imageInfo", "responseCode=" + connection.getResponseCode()
					+ "  @ImageUtil");
			if (connection.getResponseCode() != 200) {
				return null;
			}
			InputStream is = connection.getInputStream();
			bm = BitmapFactory.decodeStream(is);
			bm = resizeBitmap(bm);
			return bm;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static void asyncDownloadImageAndShow(final ImageView imageView,
			final String uri, final Context context, boolean busy) {
		final String url = IConstants.URL_PREFIX + uri;
		imageView.setImageBitmap(null);
		Bitmap bitmap = memoryCache.getBitmap(url);
		if (bitmap != null) {
			Log.d("imageInfo", "findBitmapFromMemoryCache  @ImageUtil");
			imageView.setImageBitmap(bitmap);
			if (imageView.getAnimation() != null)
				imageView.getAnimation().cancel();
			return;
		}
		if (busy)
			return;
		prepareLoadImage(imageView, url, context);
		DataService.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					if (posChanged(imageView, url)){
						return;
					}
						
					final Bitmap bm = getBitmap(uri);
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
											.setImageResource(R.drawable.tencentlogo);
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
								imageView.setImageResource(R.drawable.sinalogo);
							}
						}
					});
				}
			}
		});
	}

	private static Bitmap resizeBitmap(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		float fx = (screenSize.x - 40) / (float) bitmap.getWidth();
		matrix.setScale(fx, fx);
		Bitmap bm = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return bm;
	}

	private static boolean posChanged(ImageView view, String url) {
		return !url.equals(map.get(view));
	}

}
