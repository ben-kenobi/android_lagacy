package com.yf.filesystem;

import android.view.View;
import android.view.ViewGroup;

public class MyAnimator {
	public boolean stop = true, stopReverse = true;
	public int fromValueX, toValueX,fromValueY,toValueY;
	public int beginValueX, endValueX,beginValueY,endValueY;
	public long endTimeMillis;
	public int duration;
	public ViewGroup.LayoutParams lp;
	public int stepMillis = 40;
	public View v;
	public forwardAnimator fa;
	public reverseAnimator ra;

	public MyAnimator(View v, int fromValueX, int toValueX,int fromValueY,int toValueY, int duration)
			throws Exception {
		this.lp = v.getLayoutParams();
		this.fromValueX = fromValueX;
		this.toValueX = toValueX;
		this.fromValueY = fromValueY;
		this.toValueY = toValueY;
		this.duration = duration;
		this.v = v;
		fa = new forwardAnimator();
		ra = new reverseAnimator();
	}

	public void start() {
		stopReverse = true;
		v.removeCallbacks(ra);
		stop = false;
		beginValueX = fromValueX;
		beginValueY=fromValueY;
		endValueX = toValueX;
		endValueY=toValueY;
		endTimeMillis = System.currentTimeMillis() + duration;
		v.post(fa);
	}

	public void startReverse() {
		stop = true;
		v.removeCallbacks(fa);
		stopReverse= false;
		beginValueX = toValueX;
		endValueX = fromValueX;
		beginValueY = toValueY;
		endValueY = fromValueY;
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
					lp.width = endValueX;
					lp.height=endValueY;
					v.setLayoutParams(lp);
				}else{
					float rate=(endTimeMillis-System.currentTimeMillis())/(float)duration;
					float deltaX=(1-rate)*(endValueX-beginValueX);
					float deltaY=(1-rate)*(endValueY-beginValueY);
					lp.width=(int)(deltaX+beginValueX);
					lp.height=(int)(deltaY+beginValueY);
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
					lp.width = endValueX;
					lp.height = endValueY;
					v.setLayoutParams(lp);
				}else{
					float rate=(endTimeMillis-System.currentTimeMillis())/(float)duration;
					float deltaX=(1-rate)*(endValueX-beginValueX);
					float deltaY=(1-rate)*(endValueY-beginValueY);
					lp.width=(int)(deltaX+beginValueX);
					lp.height=(int)(deltaY+beginValueY);
					v.setLayoutParams(lp);
					v.postDelayed(this, stepMillis);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
