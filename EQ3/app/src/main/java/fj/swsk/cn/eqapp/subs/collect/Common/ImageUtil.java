package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DeviceInfoUtil;

public class ImageUtil {
	public static DisplayMetrics dm ;
	static {
		dm= DeviceInfoUtil.getDisplayMetrics();
	}
	private static FileCache fileCache = FileCache.getInstance();
	private static MemoryCache memoryCache = MemoryCache.getInstance();
	private static Map<ImageView, String> map = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private  static Handler handler = IConstants.MAIN_HANDLER;

	private static boolean prepareLoadImage(ImageView imageView, String url,
			Context context) {
		if(!posChanged(imageView,url))
			return true;
		map.put(imageView, url);
//		Animation anim = imageView.getAnimation();
//		if (anim == null) {
//			anim = AnimationUtils.loadAnimation(context,
//					R.anim.loading_pic_rotation);
//			imageView.setAnimation(anim);
//			imageView.setBackgroundResource(R.drawable.shape_loading_pic);
//		}
		 imageView.setImageBitmap(null);
//		anim.reset();
//		anim.start();
		return false;
	}

//	private static Bitmap getBitmap(String url,ImageView container) throws Exception {
//		Bitmap bm =null;
//		if (fileCache != null) {
//			bm= fileCache.getBitmap(url);
//			if (bm != null) {
//				return bm;
//			}
//		}
//		HttpURLConnection connection = null;
//		try {
//			connection = AppUtil.getNetUtilInstance().getHttpUrlConnection(url);
//			if (connection.getResponseCode() != 200) {
//				return null;
//			}
//			InputStream is = connection.getInputStream();
//			bm = BitmapFactory.decodeStream(is);
//			bm = resizeBitmap(bm,container);
//			return bm;
//		} finally {
//			if (connection != null)
//				connection.disconnect();
//		}
//	}
//
//	public static void asyncDownloadImageAndShow(final ImageView imageView,
//			final String uri, final Context context, boolean busy) {
//		System.out.println(uri+"  @ImageUtil");
//		if(TextUtils.isEmpty(uri)) {
//			handler.post(new Runnable() {
//				public void run() {
//					if (imageView != null) {
//						if (imageView.getAnimation() != null)
//							imageView.getAnimation().cancel();
//						imageView
//								.setImageResource(R.drawable.app_logo);
//					}
//				}
//			});
//			return;
//		}
//		//TODO
//		final String url = IConstants.IMAGE_PREFIX + uri;
//		imageView.setImageBitmap(null);
//		Bitmap bitmap = memoryCache.getBitmap(url);
//		if (bitmap != null) {
//			imageView.setImageBitmap(bitmap);
//			if (imageView.getAnimation() != null)
//				imageView.getAnimation().cancel();
//			return;
//		}
//		if (busy)
//			return;
//		prepareLoadImage(imageView, url, context);
//		IConstants.THREAD_POOL.submit(new Runnable() {
//			public void run() {
//				try {
//					if (posChanged(imageView, url)){
//						return;
//					}
//
//					final Bitmap bm = getBitmap(url,imageView);
//					if (bm != null)
//						memoryCache.cacheBitmap(url, bm);
//					if (posChanged(imageView, url)){
//						return;
//					}
//					if (bm == null) {
//						// failure to load image
//						handler.post(new Runnable() {
//							public void run() {
//								if (imageView != null) {
//									if (imageView.getAnimation() != null)
//										imageView.getAnimation().cancel();
//									imageView
//											.setImageResource(R.drawable.app_logo);
//								}
//							}
//						});
//						return;
//					}
//					handler.post(new Runnable() {
//						public void run() {
//							if (imageView != null) {
//								if (imageView.getAnimation() != null)
//									imageView.getAnimation().cancel();
//								imageView.setImageBitmap(bm);
//							}
//						}
//					});
//				} catch (Exception e) {
//					e.printStackTrace();
//					// load image exception
//					handler.post(new Runnable() {
//						public void run() {
//							if (imageView != null) {
//								if (imageView.getAnimation() != null)
//									imageView.getAnimation().cancel();
//								imageView.setImageResource(R.drawable.chrome);
//							}
//						}
//					});
//				}
//			}
//		});
//	}

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


	private static Bitmap getBitmap(String path,ImageView container) throws Exception {
		Bitmap bm =null;
		if (fileCache != null) {
			bm= fileCache.getBitmap(path);
			if (bm != null) {
				return bm;
			}
		}
		HttpURLConnection connection = null;
		try {

			if (path.endsWith(".mp4")) {
				bm = ThumbnailUtils.createVideoThumbnail(path,
						MediaStore.Images.Thumbnails.MINI_KIND);

			} else {
				bm = CommonUtils.getImage(path);

			}

			bm = resizeBitmap(bm, container);
			return bm;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static void asyncDecodeImageAndShow(final ImageView imageView,
												 final String path, final Context context, boolean busy) {
		if(TextUtils.isEmpty(path)) {
			handler.post(new Runnable() {
				public void run() {
					if (imageView != null) {
						if (imageView.getAnimation() != null)
							imageView.getAnimation().cancel();
						imageView
								.setImageResource(R.mipmap.loading_icon);
					}
				}
			});
			return;
		}
		//TODO
		imageView.setImageBitmap(null);
		Bitmap bitmap = memoryCache.getBitmap(path);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			if (imageView.getAnimation() != null)
				imageView.getAnimation().cancel();
			return;
		}
		if (busy)
			return;

		if(prepareLoadImage(imageView, path, context))
			return;
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					if (posChanged(imageView, path)){
						return;
					}

					final Bitmap bm = getBitmap(path,imageView);
					if (bm != null)
						memoryCache.cacheBitmap(path, bm);
					if (posChanged(imageView, path)){
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
											.setImageResource(R.mipmap.loading_icon);
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
								imageView.setImageResource(R.mipmap.loading_icon);
							}
						}
					});
				}
			}
		});
	}
}
