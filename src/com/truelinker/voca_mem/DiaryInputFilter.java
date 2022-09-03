package com.truelinker.voca_mem;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class DiaryInputFilter implements InputFilter {
	private Context mContext;
    private int mMax;
    private int mPrevKeep;

    public DiaryInputFilter(Context context, int max) {
    	mContext = context;
        mMax = max;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
    	//Log.i("DiaryInputFilter", "prevkeep=" + mPrevKeep + "keep=" + keep + " start=" + start + " end=" + end + " dest=" + dest + " dstart=" + dstart + " dend" + dend);
    	
    	if (mPrevKeep > 0 && keep == 0) {
    		Toast.makeText(mContext, "exceed maximum limit", Toast.LENGTH_SHORT).show();
    	}
    	mPrevKeep = keep;
    	
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
        	Toast.makeText(mContext, "exceed maximum limit", Toast.LENGTH_SHORT).show();
            return source.subSequence(start, start + keep);
        }
    }
}