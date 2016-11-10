package com.yf.accountmanager.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class AutoSplitTextView extends TextView {
	public float textWidth, maxShowWidth;
	// public int maxLine;
	public boolean isMeasured = false;
	public boolean isBoundMeasured = false;
	public FontMetrics fm;
	public Paint paint;
	public String text;

	public static final byte NEUTRAL = 0, POSITIVE = 1, NEGATIVE = -1;

	public AutoSplitTextView(Context context) {
		super(context);
	}

	public AutoSplitTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoSplitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void doMeasure() {
		doTextMeasure();
		doBoundMeasure();
		isMeasured = true;
	}

	private void doTextMeasure() {
		if (paint == null)
			paint = getPaint();
		fm = paint.getFontMetrics();
		text = this.getText().toString();
		textWidth = paint.measureText(text);
	}

	private void doBoundMeasure() {
		maxShowWidth = getWidth() - getCompoundPaddingLeft()
				- getCompoundPaddingRight();
		// maxShowHeight = getHeight() - getCompoundPaddingBottom()
		// - getCompoundPaddingTop();
		// maxLine = (int)(maxShowHeight / textHeight);
		// maxLine += (maxShowHeight % textHeight) > (textHeight / 3) ? 1 : 0;
		isBoundMeasured = true;
	}

	@Override
	public void setTextSize(float size) {
		this.isMeasured = false;
		super.setTextSize(size);
	}

	public void setText(CharSequence text, BufferType type) {
		this.isMeasured = false;
		super.setText(text, type);
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		super.setLayoutParams(params);
		isBoundMeasured = false;
	}

	@Override
	public void setWidth(int pixels) {
		super.setWidth(pixels);
		isBoundMeasured = false;
	}

	@Override
	public void setHeight(int pixels) {
		super.setHeight(pixels);
		isBoundMeasured = false;
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		isBoundMeasured = false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void onDraw(Canvas canvas) {
		if (!isMeasured)
			doMeasure();
		else if (!isBoundMeasured)
			doBoundMeasure();
		if (TextUtils.isEmpty(text))
			return;
		paint.setColor(getTextColors().getDefaultColor());
		List<String> sary = null;
		if (maxShowWidth > textWidth) {
			sary = new ArrayList<String>();
			sary.add(text);
		} else {
			sary = autoSplit();
		}
		drawTextOnCanvas(sary, canvas, getGravity());
	}

	private byte analysisYAxisGravity(int gravity) {
		int mask = Gravity.TOP | Gravity.BOTTOM;
		int result = gravity & mask;
		if (result == Gravity.TOP)
			return NEGATIVE;
		if (result == Gravity.BOTTOM)
			return POSITIVE;
		return NEUTRAL;
	}

	private byte analysisXAxisGravity(int gravity) {
		int mask = Gravity.LEFT | Gravity.RIGHT;
		int result = gravity & mask;
		if (result == Gravity.LEFT)
			return NEGATIVE;
		if (result == Gravity.RIGHT)
			return POSITIVE;
		return NEUTRAL;

	}

	private float calculateOriginYCoordinate(byte vertical, int lines) {
		float y = 0;
		if (vertical == NEGATIVE) {
			y += getCompoundPaddingTop() - fm.top;
		} else if (vertical == POSITIVE) {
			y += (fm.bottom-fm.top)*lines- getCompoundPaddingBottom() - fm.bottom;
		} else if (vertical == NEUTRAL) {
			float delta = (getHeight() - lines * (fm.bottom - fm.top));
			if (delta > 0) {
				y += getCompoundPaddingTop() - fm.top + delta / 2;
			} else {
				y += getCompoundPaddingTop() - fm.top;
			}
		}

		return y;
	}

	private float calculateOriginXCoordinate(byte horizon) {
		float x = 0;
		if (horizon == NEGATIVE) {
			x += getCompoundPaddingLeft();
			paint.setTextAlign(Paint.Align.LEFT);
		} else if (horizon == POSITIVE) {
			x += getWidth() - getCompoundPaddingRight();
			paint.setTextAlign(Paint.Align.RIGHT);
		} else if (horizon == NEUTRAL) {
			x += getCompoundPaddingLeft() + maxShowWidth / 2;
			paint.setTextAlign(Paint.Align.CENTER);
		}
		return x;
	}

	private void drawTextOnCanvas(List<String> sary, Canvas canvas, int gravity) {
		byte vertical = analysisYAxisGravity(gravity);
		float y = calculateOriginYCoordinate(vertical, sary.size());
		if (vertical == POSITIVE) {
			for (int i = sary.size() - 1; i >= 0; i--) {
				String str = sary.get(i);
				if (i == sary.size() - 1) {
					canvas.drawText(
							str,
							calculateOriginXCoordinate(analysisXAxisGravity(gravity)),
							y - (fm.bottom - fm.top) * (sary.size() - i - 1),
							paint);
				} else {
					canvas.drawText(str, calculateOriginXCoordinate(NEGATIVE),
							y - (fm.bottom - fm.top) * (sary.size() - i - 1),
							paint);
				}
			}
		} else {
			
			for (int i = 0; i < sary.size(); i++) {
				String str = sary.get(i);
				if (i == sary.size() - 1)
					canvas.drawText(
							str,
							calculateOriginXCoordinate(analysisXAxisGravity(gravity)),
							y + (fm.bottom - fm.top) * i, paint);
				else {
					canvas.drawText(str, calculateOriginXCoordinate(NEGATIVE),
							y + (fm.bottom - fm.top) * i, paint);
				}
			}
		}

	}

	private List<String> autoSplit() {
		List<String> slist = new ArrayList<String>();
		int start = 0;
		for (int i = 2; i <= text.length(); i++) {
			if (paint.measureText(text, start, i) > maxShowWidth) {
				slist.add(text.substring(start, i - 1));
				start = i - 1;
				// if (slist.size() >= maxLine)
				// break;
			}
		}
		// if (slist.size() < maxLine || slist.isEmpty())
		slist.add(text.substring(start, text.length()));
		return slist;
	}
}
