package cn.swsk.rgyxtqapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//
//public class MarqueeTextView extends TextView implements Runnable {
//	public int currentScrollX;// 当前滚动的位置
//	public boolean isStop = true;
//	public int textWidth;
//	public int visibleTextWidth;
//	public boolean isMeasure = false;
//	public int velocity = 3;
//
//	public MarqueeTextView(Context context) {
//		super(context);
//	}
//
//	public MarqueeTextView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		if (!isMeasure) {// 文字宽度只需获取一次就可以了
//			getTextWidth();
//			visibleTextWidth=getWidth()-getCompoundPaddingLeft()-getCompoundPaddingRight()+2;
//			isMeasure = true;
//		}
//		startScrollDelay(2000);
////		System.out.println("textWidth="+textWidth+",visibleTxW="+visibleTextWidth+"  onDraw   @MarqueeTextView");
//	}
//
//	/**
//	 * 获取文字宽度
//	 */
//	private void getTextWidth() {
//		Paint paint = this.getPaint();
//		String str = this.getText().toString();
//		textWidth = (int) paint.measureText(str);
//	}
//
//	// 重写setText 在setText的时候重新计算text的宽度
//	@Override
//	public void setText(CharSequence text, BufferType type) {
//		this.isMeasure = false;
//		super.setText(text, type);
//	}
//
//	@Override
//     public void run() {
//	    	 if (isStop) {
//	             return;
//	    	 }
//             currentScrollX += velocity;// 滚动速度
//             scrollTo(currentScrollX, 0);
//
//             if (getScrollX()+visibleTextWidth>textWidth+60) {
//                     currentScrollX = -velocity;
//                     postDelayed(this, 250);
//             }else if(getScrollX()==0){
//            	 postDelayed(this,2500);
//             }else{
//            	 postDelayed(this, 40);
//             }
////             System.out.println("curX="+currentScrollX+",scrollX:Y="+getScrollX()+":"+getScrollY()+
////            		 ",width="+getWidth()+",textWidth="+textWidth+"   @MarqueeTextView");
//
//     }
//
//	protected void onVisibilityChanged(View changedView, int visibility) {
////		System.out.println("visibility=" + visibility
////				+ "  @MarqueeTextView  onVisibilityChanged");
//		super.onVisibilityChanged(changedView, visibility);
//		if (visibility == View.VISIBLE){
//			startScrollDelay(2000);
//		}
//		else
//			stopScroll();
//	}
//
//	@Override
//	protected void onWindowVisibilityChanged(int visibility) {
////		System.out.println("visibility=" + visibility
////				+ "  @MarqueeTextView  onWindowVisibilityChanged");
////		System.out.println("drawablePad="+getCompoundDrawablePadding()+",draPadLeft:right="+
////				getCompoundPaddingLeft()+":"+getCompoundPaddingRight()+",padLeft:right="+
////				getPaddingLeft()+":"+getPaddingRight()+"   @MarqueeTextView  oN");
//		super.onWindowVisibilityChanged(visibility);
//		if (visibility == View.VISIBLE){
//			startScrollDelay(2000);
//		}
//		else
//			stopScroll();
//	}
//
//	// 开始滚动
//	public void startScroll() {
//		if(textWidth<=visibleTextWidth||!isStop) return;
//		isStop = false;
//		this.removeCallbacks(this);
//		post(this);
//	}
//
//	public void startScrollDelay(int millis) {
//		if(textWidth<=visibleTextWidth||!isStop) return;
//		isStop = false;
//		this.removeCallbacks(this);
//		postDelayed(this, millis);
//	}
//
//	// 停止滚动
//	public void stopScroll() {
//		removeCallbacks(this);
//		isStop = true;
//	}
//
//	// 从头开始滚动
//	public void restartFrom0() {
//		currentScrollX = 0;
//		startScroll();
//	}
//	@Override
//	protected void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
//		stopScroll();
//	}
//}
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context con) {
		super(con);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
//		if(getEditableText().equals(TextUtils.TruncateAt.MARQUEE)){
//			return true;
//		}
//		return super.isFocused();
	}
}