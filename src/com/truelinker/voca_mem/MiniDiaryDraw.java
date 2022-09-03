package com.truelinker.voca_mem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.truelinker.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Handle resource to draw view
 *
 */
public class MiniDiaryDraw {
	private static final String TAG = "MiniDiaryDraw";

	public final int SUBJECT_POSITION_BUTTON = 1;
	public final int SUBJECT_POSITION_LOCATION = 2;
	public final int SUBJECT_POSITION_FULL = 3;

	private final int[] dateNumResId = new int[] {
		//R.drawable.mdiary_date_0,
		//R.drawable.mdiary_date_1,
		//R.drawable.mdiary_date_2,
		//R.drawable.mdiary_date_3,
		//R.drawable.mdiary_date_4,
		//R.drawable.mdiary_date_5,
		//R.drawable.mdiary_date_6,
		//R.drawable.mdiary_date_7,
		//R.drawable.mdiary_date_8,
		//R.drawable.mdiary_date_9,
	};

	private final int[] viewSubjectResId = new int[] {
			/*R.id.TextView_Subject01,
			R.id.TextView_Subject02,
			R.id.TextView_Subject03*/
	};

	private final int[] dateBgResId = new int[] {
			/*R.drawable.mdiary_datebg_orange,
			R.drawable.mdiary_datebg_magenta,
			R.drawable.mdiary_datebg_blue,
			R.drawable.mdiary_datebg_green,*/
	};

	private final int[] dateListBgResId = new int[] {
			/*R.drawable.mdiary_datebg_orange,
			R.drawable.mdiary_datebg_magenta,
			R.drawable.mdiary_datebg_blue,
			R.drawable.mdiary_datebg_green,*/
	};

	private final int[] outlineResId = new int[] {
		/*	R.drawable.mdiary_outline_orange,
			R.drawable.mdiary_outline_magenta,
			R.drawable.mdiary_outline_blue,
			R.drawable.mdiary_outline_green,*/
	};
    
	static final int[] bgColor = {
		Color.rgb(220,88,24),
		Color.rgb(171,51,161),
		Color.rgb(20,144,182),
		Color.rgb(114,148,10)
	};	
	
	private final int[] photoListImageResId = new int[] {
			/*R.id.ImageView_Photo_Thumb1,
			R.id.ImageView_Photo_Thumb2,
			R.id.ImageView_Photo_Thumb3,*/
	};

	private final int[] photoListContainerResId = new int[] {
			//R.id.RelativeLayout_Photo_Container1,
			//R.id.RelativeLayout_Photo_Container2,
			//R.id.RelativeLayout_Photo_Container3,
	};

	private final int[] photoListMainThumbResId = new int[] {
			//R.id.ImageView_ListItemMainImage
	};

	Context mContext;

	public MiniDiaryDraw(Context context) {
		mContext = context;
	}

	private View setViewVisibility(View view, int resId, int visible) {
		View v  = (View) view.findViewById(resId);
		if (v != null)
			v.setVisibility(visible);
		return v;
	}
	
	private void setViewText(View view, int resId, String string) {
		TextView text = (TextView) view.findViewById(resId);
		if (text != null)
			text.setText(string);
	}
	
	private void setViewCursor(View view, int resId, Boolean cursorVisible) {
		TextView text = (TextView) view.findViewById(resId);
		if (text != null)
			text.setCursorVisible(cursorVisible);
	}
	
	private void setViewResource(View view, int resId, int resource) {
		View v = (View) view.findViewById(resId);
		if (v != null)
			v.setBackgroundResource(resource);
	}
	
	private void setViewBackgroundColor(View view, int resId, int color) {
		View v = (View) view.findViewById(resId);
		if (v != null)
			v.setBackgroundColor(color);
	}

	private void setBitmap(View view, int resId, Bitmap bm) {
		ImageView image = (ImageView) view.findViewById(resId);
		
		if (bm == null)
			image.setVisibility(View.INVISIBLE);
		else
			image.setVisibility(View.VISIBLE);

		image.setImageBitmap(bm);
	}

	/*
	public void drawDate(View view, Calendar cal, int position, boolean isFix) {
		RelativeLayout relativeLayout;
		RelativeLayout.LayoutParams lp;
		
		// set date layout
		relativeLayout = (RelativeLayout) view.findViewById(R.id.RelativeLayout_Date);
		lp = new RelativeLayout.LayoutParams(relativeLayout.getLayoutParams());
		if ( (position & 1) == 1 || (isFix == true))
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		else
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		relativeLayout.setLayoutParams(lp);

		setViewResource(view, R.id.RelativeLayout_Date, dateBgResId[position%4]);

		int day = cal.get(Calendar.DATE);
		setViewResource(view, R.id.ImageView_Date01, dateNumResId[day/10]);
		setViewResource(view, R.id.ImageView_Date02, dateNumResId[day%10]);
        setViewText(view, R.id.TextView_Year, DateFormat.format("yyyy", cal.getTime()).toString());
		setViewText(view, R.id.TextView_Month, DateFormat.format("MMMM", cal.getTime()).toString());
	}

	public void drawListDate(View view, Calendar cal, int position) {
		setViewResource(view, R.id.RelativeLayout_DateContainer, dateListBgResId[position%4]);

		int day = cal.get(Calendar.DATE);
		setViewResource(view, R.id.ImageView_Date01, dateNumResId[day/10]);
		setViewResource(view, R.id.ImageView_Date02, dateNumResId[day%10]);
        setViewText(view, R.id.TextView_Year, DateFormat.format("yyyy", cal.getTime()).toString());
		setViewText(view, R.id.TextView_Month, DateFormat.format("MMMM", cal.getTime()).toString());
	}

	public View getListContainer(View view, int position,boolean bPortraitOrientation) {
		if(bPortraitOrientation == true){
			setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_left, View.GONE);
			setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_right, View.GONE);

			if ((position & 1) == 1) {
				setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_left, View.GONE);
				return setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_right, View.VISIBLE);
			}
			else {
				setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_right, View.GONE);
				return setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_left, View.VISIBLE);
			}
		}
		else{
			setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_left, View.GONE);
			setViewVisibility(view, R.id.RelativeLayout_ListContainer_Port_right, View.GONE);

			if ((position & 1) == 1) {
				setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_left, View.GONE);
				return setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_right, View.VISIBLE);
			}
			else {
				setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_right, View.GONE);
				return setViewVisibility(view, R.id.RelativeLayout_ListContainer_Land_left, View.VISIBLE);
			}
		}
	}
	
	public void drawPhotoCount(View view, int count) {
		if (count > 0) {
			setViewVisibility(view, R.id.RelativeLayout_Photo_Count, View.VISIBLE);
			String countString = Integer.toString(count) + " " + mContext.getString(R.string.photos);
			setViewText(view, R.id.TextView_PhotoCount, countString);
		}
		else {
			setViewVisibility(view, R.id.RelativeLayout_Photo_Count, View.INVISIBLE);
		}
	}

	public void drawOutline(View view, int position) {
		//setViewResource(view, R.id.FrameLayout_OutlineBg, bgColor[position%4]);
		Log.e("MiniDiaryDraw","drawOutline");
		setViewBackgroundColor(view, R.id.FrameLayout_OutlineBg, bgColor[position%4]);
		setViewResource(view, R.id.LinearLayout_Outline, outlineResId[position%4]);
	}
	
	public void drawListItemMainTitleBg(View view) {
		setViewResource(view, R.id.ImageView_ListItemMainTitleBg, R.drawable.mdiary_titlebg);
	}

	public void hideListPhotoThumb(View view) {
		for (int i=0; i<3; i++)
			setViewVisibility(view, photoListContainerResId[i], View.INVISIBLE);
	}
	
	public void drawListPhotoThumb(View view, Bitmap bm, int width, int index) {
		if(index < 0){
			Log.d(TAG,"drawListPhotoThumb index < 0 index : " + index);
			return;
		}
		
		setViewVisibility(view, photoListContainerResId[index], View.VISIBLE);
		if (bm != null) {
			setBitmap(view, photoListImageResId[index], bm);
		}
		else {
			setViewResource(view, photoListContainerResId[index], R.drawable.list_no_image);
			setViewVisibility(view, photoListImageResId[index], View.INVISIBLE);
		}
	}

	public void drawListPhotoThumbMain(View view, Bitmap bm, int width, int index) {
		if (bm != null) {
			setViewVisibility(view, photoListMainThumbResId[index], View.VISIBLE);
			setBitmap(view, photoListMainThumbResId[index], bm);
		}
		else {
			setViewVisibility(view, photoListMainThumbResId[index], View.INVISIBLE);
		}
	}

	public void drawPhoto(View view, Bitmap photo) {
		if (photo == null) {
			setViewVisibility(view, R.id.RelativeLayout_Photo, View.INVISIBLE);
			setViewVisibility(view, R.id.RelativeLayout_AddPhoto, View.VISIBLE);
		}
		else {
			setViewVisibility(view, R.id.RelativeLayout_Photo, View.VISIBLE);
			setViewVisibility(view, R.id.RelativeLayout_AddPhoto, View.INVISIBLE);
		}
		setBitmap(view, R.id.ImageView_Photo, photo);
	}
*/
	public void drawViewBg(View view, int position) {
		//view.setBackgroundResource(bgColor[position%4]);
		view.setBackgroundColor(bgColor[position%4]);
	}
	/*
	public void drawTextLine(View view, int position) {
		LinedEditText text = (LinedEditText) view.findViewById(R.id.TextView_Subject);
		LinedEditText empty = (LinedEditText) view.findViewById(R.id.TextView_tapToCreate);
		
		int color = bgColor[position%4];
		
		text.setLineColor((color & 0x00FFFFFF) | 0x80000000);
		text.setLineHeight(mContext.getResources().getInteger(R.integer.diary_text_line_height));

		empty.setLineColor((color & 0x00FFFFFF) | 0x80000000);
		empty.setLineHeight(mContext.getResources().getInteger(R.integer.diary_text_line_height));
	}

	private Bitmap mPhotoFrame = null;
	public void drawPhotoFrame(View view, Bitmap bm, boolean existPhoto) {
		//setViewResource(view, R.id.ImageView_PhotoFrame, photoFrameResId[position%4]);
		if (existPhoto)
			setBitmap(view, R.id.ImageView_PhotoFrame, bm);
		else
			setBitmap(view, R.id.ImageView_AddPhoto, bm);
	}
	
	public Bitmap getPhotoFrameBitmap(int position, boolean existPhoto) {
		BitmapDrawable bd;
		if (existPhoto)
			bd = (BitmapDrawable) mContext.getResources().getDrawable(photoFrameResId[position%4]);
		else
			bd = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.mdiary_addphoto);
		mPhotoFrame = bd.getBitmap();
		return mPhotoFrame;
	}
*/
	
	
	
	static class StringWidth {
		public static float[] getWidthsOf(String str, TextView textView) {
			//Paint paint = new Paint();
			//paint.setTextSize(fontSize);
			Paint paint = textView.getPaint();
			
			float[] widths = new float[str.length()];
			paint.getTextWidths(str, widths);
			return widths;
		}
	
		public static float getStringWidth(String str, TextView textView) {
			float sum = 0;
			float[] strWidths = getWidthsOf(str, textView);

			for (int i=0; i<str.length(); i++)
				sum += strWidths[i];
			
			return sum;
		}
	}

	class SubjectLine {
		protected String mSubject;
		protected String[] mLine;
		protected int[] mWidth;
		protected TextView[] mTextView;
		//private final String SMARK = " \n\t";
		private final String SMARK = "\n\t ";
		//private final String NMARK = "";
		private final String EMARK = "...";
		
		public SubjectLine(String subject, int[] width, TextView[] textView, int type) {
			mSubject = subject;
			mLine = new String[3];
			mWidth = new int[3];
			mTextView = new TextView[3];

			for (int i=0; i<3; i++) {
				mLine[i] = null;
				mWidth[i] = width[i];
				mTextView[i] = textView[i];
			}

			if (type == SUBJECT_TYPE_LIST)
				initSubjectList();
			else
				initSubjectView();
		}
		
		private String getSentence(String str, int start, String cs) {
			boolean flag = false;
			int length = str.length();
			String aString;
			
			for (int i=start; i<length; i++) {
				aString = String.valueOf(str.charAt(i));
				if (aString.matches(cs) == true)
					flag = true;
				else
					if (flag == true)
						return str.substring(start, i);
			}
			return str.substring(start);
		}
		
		private boolean contain(char c, String cs) {
			for (int j=0; j<cs.length(); j++)
				if (c == cs.charAt(j))
					return true;
			return false;
		}
		
		private int getIndexOf(String str, String cs, int start, int viewWidth, TextView textView) {
			int lastIndex = 0;
			int length = str.length();
			float sum = 0;
			float[] strWidths = StringWidth.getWidthsOf(str, textView);
			
			for (int i=start; i<length; i++) {
				sum += strWidths[i];
				if (sum > viewWidth) {
					if (lastIndex <= 0)
						lastIndex = i-1;
					break;
				}
				
				if (contain(str.charAt(i), cs) == true)
					lastIndex = i;
			}
			
			int index = (sum <= viewWidth) ? length : lastIndex;
			return index;
		}
		
		/*
		private int checkMax(String str, int start, int end, int max) {
			int index = end - start;
			char c;
			//index = (index > max) ? max : index;
			//Log.d(TAG, "start:" + start + ", end:" + end + ", max:" + max);
			if (index > max) {
				for (int i=max; i>=0; i--) {
					index = i;
					c = str.charAt(i+start);
					//Log.d(TAG, "index:" + index + ", i:" + i + ", c:" + c);
					if (contain(c, " ") == true) {
						break;
					}
				}
				if (index == 0)
					index = max;
			}
			return (start + index);
		}
		*/
		
		private int checkSpace(String str, int index) {
			if (str.indexOf(' ', index) == index)
				return (++index);
			else
				return index;
		}
		
		public void initSubjectList() {
			int start=0, end=0;

			String firstSentence = getSentence(mSubject, 0, "[.?!\\n]");
	 		//Log.d("SubjectLine", "firstSentence=" + firstSentence + " length=" + firstSentence.length());

	 		// handle 1st line
	 		start = 0;
			end = getIndexOf(firstSentence, SMARK, start, mWidth[0], mTextView[0]);
			mLine[0] = firstSentence.substring(0, end);
			//Log.d("SubjectLine", "line0 " + ((mLine[0]!=null) ? mLine[0] : "null") + " start=" + start + " end=" + end);

			// handle 2nd line
			start = checkSpace(firstSentence, end);
			end = getIndexOf(firstSentence, SMARK, start, mWidth[1], mTextView[1]);

			//int strLen = firstSentence.length() - start;
            int strLen = firstSentence.length();
			if (strLen > end) {
				end = end - 3;
				mLine[1] = firstSentence.substring(start, end) + EMARK;
			}
			else
				mLine[1] = firstSentence.substring(start, end);
			//Log.d("SubjectLine", "line1" + ((mLine[1]!=null) ? mLine[1] : "null"));
		}
		
		public void initSubjectView() {
			int start=0, end=0;

			String firstSentence = getSentence(mSubject, 0, "[.?!\\n]");
	 		Log.d("SubjectLine@", firstSentence);

	 		// handle 1st line
	 		start = 0;
			end = getIndexOf(firstSentence, SMARK, start, mWidth[0], mTextView[0]);
			//end = checkMax(firstSentence, start, end, 10);
			mLine[0] = firstSentence.substring(0, end);
			Log.d("SubjectLine@ line0", (mLine[0]!=null) ? mLine[0] : "null");

			// handle 2nd line
			//start = checkSpace(firstSentence, end);
			//end = getIndexOf(firstSentence, SMARK, start, mWidth[1]);
			//end = checkMax(firstSentence, start, end, 0);
			//mLine[1] = firstSentence.substring(start, end);
			//Log.d("SubjectLine@ line1", (mLine[1]!=null) ? mLine[1] : "null");
		
			// handle 3rd line
			//start = checkSpace(mSubject, end);
			//mLine[2] = mSubject.substring(start);
			//Log.d("SubjectLine@ line2", (mLine[2]!=null) ? mLine[2] : "null");
		}
	}
	
	public final int SUBJECT_TYPE_LIST = 1;
	public final int SUBJECT_TYPE_VIEW = 2;
	
	public void setNoteLines(View view, String string, int type) {
		if (string == null) {
			for (int i=0; i<3; i++) {
				setViewText(view, viewSubjectResId[i], null);
			}
			return;
		}
		
		int[] width = new int[3];
		TextView[] textView = new TextView[3];

		if (type == SUBJECT_TYPE_LIST) {
			width[0] = getIntResValue(mContext, R.integer.subject_list_text_width_1);
			width[1] = getIntResValue(mContext, R.integer.subject_list_text_width_2);
			width[2] = getIntResValue(mContext, R.integer.subject_list_text_width_3);
		}
		else {
			width[0] = getIntResValue(mContext, R.integer.subject_view_text_width_1);
			width[1] = getIntResValue(mContext, R.integer.subject_view_text_width_2);
			width[2] = getIntResValue(mContext, R.integer.subject_view_text_width_3);
		}

		textView[0] = (TextView) view.findViewById(viewSubjectResId[0]);
		textView[1] = (TextView) view.findViewById(viewSubjectResId[1]);
		textView[2] = (TextView) view.findViewById(viewSubjectResId[2]);

		SubjectLine line = new SubjectLine(string, width, textView, type);

		if (type == SUBJECT_TYPE_LIST) {
			setViewText(view, viewSubjectResId[0], line.mLine[0]);
			setViewText(view, viewSubjectResId[1], line.mLine[1]);

		}
	}
	
	public int getIntResValue(Context context, int resId) {
		return context.getResources().getInteger(resId);
	}
	
	public void setNoteLinesGravity(View view, int type, int position) {
		TextView text;
		int gravity = Gravity.LEFT;
		
		if (type == SUBJECT_TYPE_LIST)
			if ((position & 1) == 1)
				gravity = Gravity.RIGHT;

		for (int i=0; i<3; i++) {
			text = (TextView) view.findViewById(viewSubjectResId[i]);
			if (text != null)
				text.setGravity(gravity);
		}
	}
	/*
	public void setLocation(View view, Calendar cal, String location) {
		if (location != null && location.length() > 0) {
			setViewVisibility(view, R.id.RelativeLayout_Location, View.VISIBLE);

			SimpleDateFormat format = new SimpleDateFormat("hh:mm");
			String str = format.format(cal.getTime()) + " " + location;
	
			setViewText(view, R.id.TextView_Location, str.substring(0, str.length()));
		}		
		else {
			setViewVisibility(view, R.id.RelativeLayout_Location, View.GONE);
		}
	}

	public void moveSubject(View view, int position) {
		switch (position) {
		case SUBJECT_POSITION_BUTTON:
			setViewVisibility(view, R.id.RelativeLayout_Location, View.GONE);
			setViewVisibility(view, R.id.RelativeLayout_ButtonContainer, View.VISIBLE);
			break;
		case SUBJECT_POSITION_FULL:
			setViewVisibility(view, R.id.RelativeLayout_Location, View.GONE);
			setViewVisibility(view, R.id.RelativeLayout_ButtonContainer, View.VISIBLE);
			break;
		default: // case SUBJECT_POSITION_LOCATION:
			setViewVisibility(view, R.id.RelativeLayout_Location, View.VISIBLE);
			setViewVisibility(view, R.id.RelativeLayout_ButtonContainer, View.VISIBLE);
			break;
		}
	}
	
	
	public void setButtons(Context contex, View view, int position) {
		if (position >= 0) {
			setViewVisibility(view, R.id.RelativeLayout_ButtonContainer, View.VISIBLE);
			setViewVisibility(view, R.id.RelativeLayout_SaveIcon, View.VISIBLE);
			setViewVisibility(view, R.id.RelativeLayout_CancelIcon, View.VISIBLE);

			int color = bgColor[position%4];

			TextView textViewSaveIcon = (TextView) view.findViewById(R.id.TextView_SaveIcon);
			textViewSaveIcon.setTextColor(color);
			
			TextView textViewCancelIcon = (TextView) view.findViewById(R.id.TextView_CancelIcon);
			textViewCancelIcon.setTextColor(color);
		}
		else {
			setViewVisibility(view, R.id.RelativeLayout_ButtonContainer, View.INVISIBLE);
		}
	}
	
	public boolean drawWeather(View view, int weatherId) {
		int resid;
		Weather item = new Weather(mContext); 
		resid = item.getSmallResById(weatherId);

		Log.d(TAG, "drawWeather: weatherId: " + weatherId);
		
		if (resid == -1){
		    resid = R.drawable.weather_nodata;
		}
        setViewResource(view, R.id.ImageView_Weather, resid);
		
		return true;
	}
*/
	public static String getResString(Context context, int resId) {
		return context.getResources().getString(resId);
	}
}
