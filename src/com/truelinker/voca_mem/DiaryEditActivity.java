package com.truelinker.voca_mem;

import com.truelinker.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity for diary insert
 *
 */
public class DiaryEditActivity extends Activity {
	private static final String TAG = "DiaryEditActivity";

	private LinedEditText mEdit;
	
	InputMethodManager mImm;
	
	long idl = 0;
	
	private Handler mHandler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Log.e(TAG,"onCreate");
        String text = intent.getStringExtra("extramean");
        int theme = intent.getIntExtra("theme", 0);
        int color = MiniDiaryDraw.bgColor[theme];
        idl = intent.getLongExtra("id", -1);
        setContentView(R.layout.diaryedit);
        
        findViewById(R.id.RelativeLayout_editMain).setBackgroundColor(color);
        
        mEdit = (LinedEditText) findViewById(R.id.diary_edit);
        
        mEdit.setLineColor((color & 0x00FFFFFF) | 0x80000000);
        mEdit.setLineHeight(getResources().getInteger(R.integer.diary_text_line_height));
        
        //mEdit.setBackgroundResource(MiniDiaryDraw.bgColor[theme]);
        mEdit.setText(text);
        if (text != null) {
        	mEdit.setSelection(text.length());
        }
       // mEdit.setFilters(new InputFilter[]{new DiaryInputFilter(this, 1000)});
		
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        /*
		TextView textViewSaveIcon = (TextView)findViewById(R.id.TextView_SaveIcon);
		textViewSaveIcon.setTextColor(color);
		textViewSaveIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.e("DiaryEditActivity","onClick  66");
				onBackPressed();				
			}
		});*/
		/*
		TextView textViewCancelIcon = (TextView)findViewById(R.id.TextView_CancelIcon);
		textViewCancelIcon.setTextColor(color);
		textViewCancelIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//mImm.hideSoftInputFromWindow(mEdit.getApplicationWindowToken(), 0);
				finish();				
			}
		});*/
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("DiaryEditActivity","onConfigurationChanged");
	}
	
    @Override
    public void onBackPressed() {
		//mImm.hideSoftInputFromWindow(mEdit.getApplicationWindowToken(), 0);
        Intent result = new Intent();
        result.putExtra("extramean", mEdit.getText().toString());
        result.putExtra("id", idl);
        setResult(RESULT_OK, result);
        Log.e(TAG,"onBackPressed id = " + idl);
        finish();
    }
    
    
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	    	    	
    	mHandler.postDelayed(new Runnable() {
			public void run() {
				Log.e(TAG,"onWindowFocusChanged  ");
				mImm.showSoftInput(mEdit, 0);
			}
		}, 400);
    }
  
}
