package com.truelinker.voca_mem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class LinedEditText extends EditText {
	
	private Paint mPaint = new Paint();
	
	private int mLineHeight = 35;

	public LinedEditText(Context context) {
        this(context, null);
	}

	public LinedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.editTextStyle);
	}

	public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		
		mPaint.setColor(0x40FFFFFF);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(1);
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		drawLine(canvas);
//	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		//drawLine(canvas);
	}
	
	void drawLine(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		int scrollY = getScrollY();
		
//		int y = getPaddingTop() + 35 - (scrollY % 35);
//		while (y < height + scrollY) {
//			if (y >= scrollY + getPaddingTop()) {
//				//Log.i("LinedEditText", "drawline y=" + y);
//				canvas.drawLine(0, y, width, y, mPaint);
//			}
//			y += 35;
//		}
		
		int y = getPaddingTop() + mLineHeight;
		while (y <= height + scrollY) {
			canvas.drawLine(2, y, width - 2, y, mPaint);
			y += mLineHeight;
		}
	}
	
	public void setLineColor(int lineColor) {
		//mPaint.setColor(lineColor);
		Log.i("LinedEditText", "line color=" + Integer.toHexString(lineColor));
	}

	public void setLineHeight(int lineHeight) {
		mLineHeight = lineHeight;
		Log.i("LinedEditText", "line height=" + lineHeight);
	}
	
}
