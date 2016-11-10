package com.icanit.app_v2.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AnimationUtil {
	public static  void startBuyingAnimation(final ImageView src,View container, final ImageView base,View dest,Context context) throws Exception{
		int compensation=15;
		int[] basePos=new int[2];
		int[] parentPos=new int[2];
		int[] destPos=new int[2];
		dest.getLocationOnScreen(destPos);
		base.getLocationOnScreen(basePos);
		container.getLocationOnScreen(parentPos);
		LayoutParams lps=src.getLayoutParams();
//		System.out.println("basePos="+Arrays.toString(basePos)+",parentPos="+Arrays.toString(parentPos)+
//				",destPos="+Arrays.toString(destPos)+"    @AnimationUtil");
		lps.getClass().getField("topMargin").set(lps, basePos[1]-parentPos[1]);
		lps.getClass().getField("leftMargin").set(lps, basePos[0]-parentPos[0]-container.getPaddingLeft());
//		int[] srcPos=new int[2];
//		src.getLocationOnScreen(srcPos);
//		System.out.println("srcPos="+Arrays.toString(srcPos)+"    @AnimationUtil");
		AnimationSet as = new AnimationSet(context,null);
		ScaleAnimation sa=new ScaleAnimation(1,0.25f,1,0.25f,Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
//		sa.setInterpolator(new AccelerateInterpolator());
//		AlphaAnimation aa = new AlphaAnimation(1,0.8f);
		TranslateAnimation ta=new TranslateAnimation(0,destPos[0]-basePos[0]-compensation,
				0, destPos[1]-basePos[1]-compensation);
		ta.setInterpolator(new DecelerateInterpolator(2));
		as.addAnimation(sa);
//		as.addAnimation(aa);
		as.addAnimation(ta);
		as.setDuration(600);
		as.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {
				src.setImageDrawable(base.getDrawable());
			}
			public void onAnimationRepeat(Animation arg0) {
			}
			public void onAnimationEnd(Animation arg0) {
				src.setImageDrawable(null);
				src.setAnimation(null);
			}
		});
		src.startAnimation(as);
	}
}
