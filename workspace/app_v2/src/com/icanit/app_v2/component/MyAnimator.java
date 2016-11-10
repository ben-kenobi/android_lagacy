package com.icanit.app_v2.component;

import android.view.View;
import android.view.ViewGroup;

public class MyAnimator {
	public boolean stop = true, stopReverse = true;
	public int fromValue, toValue;
	public int beginValue, endValue;
	public long endTimeMillis;
	public int duration;
	public ViewGroup.LayoutParams lp;
	public int stepMillis = 40;
	public View v;
	public forwardAnimator fa;
	public reverseAnimator ra;

	public MyAnimator(View v, int fromValue, int toValue, int duration)
			throws Exception {
		this.lp = v.getLayoutParams();
		this.fromValue = fromValue;
		this.toValue = toValue;
		this.duration = duration;
		this.v = v;
		fa = new forwardAnimator();
		ra = new reverseAnimator();
	}

	public void start() {
		stopReverse = true;
		v.removeCallbacks(ra);
		stop = false;
		beginValue = fromValue;
		endValue = toValue;
		endTimeMillis = System.currentTimeMillis() + duration;
		v.post(fa);
	}

	public void startReverse() {
		stop = true;
		v.removeCallbacks(fa);
		stopReverse= false;
		beginValue = toValue;
		endValue = fromValue;
		endTimeMillis = System.currentTimeMillis() + duration;
		v.post(ra);
	}

	public void stop() {
		stop = true;
		stopReverse = true;
	}

	class forwardAnimator implements Runnable {
		public void run() {
			try {
				if(stop) return;
				if (System.currentTimeMillis() >= endTimeMillis) {
					stop();
					lp.width = endValue;
					v.setLayoutParams(lp);
				}else{
					float rate=(endTimeMillis-System.currentTimeMillis())/(float)duration;
					float delta=(1-rate)*(endValue-beginValue);
					lp.width=(int)(delta+beginValue);
					v.setLayoutParams(lp);
					v.postDelayed(this, stepMillis);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class reverseAnimator implements Runnable {
		public void run() {
			try {
				if(stopReverse)return;
				if (System.currentTimeMillis() >= endTimeMillis) {
					stop();
					lp.width = endValue;
					v.setLayoutParams(lp);
				}else{
					float rate=(endTimeMillis-System.currentTimeMillis())/(float)duration;
					float delta=(1-rate)*(endValue-beginValue);
					lp.width=(int)(delta+beginValue);
					v.setLayoutParams(lp);
					v.postDelayed(this, stepMillis);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
