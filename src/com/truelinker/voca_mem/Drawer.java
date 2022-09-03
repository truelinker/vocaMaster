package com.truelinker.voca_mem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
//import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.*;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

//import com.mg.android.service.VocaProvider;
import com.truelinker.R;
import com.truelinker.service.CountDownService;
import com.truelinker.service.Voca_ManageDB;
import com.truelinker.service.Voca_DB.FileItems;
import com.truelinker.service.Voca_DB.VocaItems;

public class Drawer extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener, ViewSwitcher.ViewFactory {
	
	private SlidingDrawer mDrawer;
	private FrameLayout mFrameLayout;
	private ProgressBar mProgress;
	private TextView spell = null;
	private TextView mean = null;
	private TextView wrongnum = null;
	private TextView currectnum = null;
	private TextView ElapsedMemoday = null;
	@SuppressWarnings("unused")
	private int mProgressBar = 0;
	//private Cursor toDoListCursor;
	protected String mExtra_mean = "";
	//Chronometer mChronometer;	
	
	SlidingDrawer drawer;
	
	String spell_cursor;
	String mean_cursor;
	String extramean_cursor;
	
	int mProgressStatus = 0;
		
	public static final int INVISIBLE = 4;
	public static final int VISIBLE = 0;
	public static final int GONE = 8;
	
	public static final int YES_MEMO = 1;
	public static final int NO_MEMO = 0;
	
	public static final int DICTIONARY = 3;
	public static final int IMAGE = 4;
	
	private int halt_countdown = 0;
	
	public static final int MSG_GATHER_VOCA = 1;
	public static final int MSG_TIMEOUT = 2;
	public static final int MSG_GAT_BACK_TO_STEP = 3;
	
	int mStep = 0;
	long mFileId = 0;

	int flag = 0;
	WebView web;
	
	//CountDownReceiver receiver; 
	
	Thread thr = null;
	Thread dbthr = null;
	ThreadHandle timethread = null;
	
	/* To update step list */ 
	int step_voca_num[] = new int[10];
	int step_date[] = new int[10];
	ProgressDialog pd;
	
	/* Showing voca using this index */
	int voca_index = 0;
	int initial_voca_index = 0;
	VocaDataManage mvocamanage  = null;
	
	/* Counter of wrong answer */
	int mWrongCount = 0;
	
	Button wed_imageButton;
	Button wed_dicButton;
	Button memoryButton;
	Button forgetButton;
	Button checkButton;
	
	TextView extra_mean;
	
	ImageView yourimgView;
	Bitmap ResizedImage = null;
	
	public static final String KEY_BG_IMAGE = "bg_image";
	public static final String KEY_PRE_RANDOM = "pre_random";
	//Bundle drawerdata;
	
	boolean CheckboxRepeat;
	boolean CheckboxQuiz;
	boolean CheckboxRandom;
	boolean mPreRandomSetting;
	int mRepeat;
	int mRepeatVoca;
	int mTextTime;
	
	int mSpell_Size;
	int mChar_Color;
	int mChar_face;
	
	int mCurrectNum = 0;
	int mWrongNum = 0;
	long lastMemoDate = 0;
	
	private static final int BACKIMAGE = 0;
	private static final int DIARY_EDIT = 15;
	private static final int MULTI_CHOICE = 16;
	private static final int STEPS = 17;
	private static final int SETTING = 18;
	
	private static final int FIRST_STEP = 1;
	private static final int SECOND_STEP = 2;
	
	private Handler mHandler = new Handler();
	
	boolean backToDrawer = false;
	int memobuttonState = FIRST_STEP;
	
	private TextToSpeech mTts;
	
	
	//private DrawerDraw mDrawerDraw;
    private View mChildView;
    
    protected ViewSwitcher mSwitcher;
    
	//private DoComplecatedJob mProgressbar = null;
	
    private void pickCharColor(int color)
    {
    	switch(color)
    	{
    		case 1:
    			spell.setTextColor(Color.BLACK);
    			mean.setTextColor(Color.BLACK);
    			currectnum.setTextColor(Color.BLACK);
    			wrongnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;
    		case 2:
    			spell.setTextColor(Color.RED);
    			mean.setTextColor(Color.RED);
    			currectnum.setTextColor(Color.BLACK);
    			wrongnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;    			
    		case 3:
    			spell.setTextColor(Color.YELLOW);
    			mean.setTextColor(Color.YELLOW);
    			wrongnum.setTextColor(Color.BLACK);
    			currectnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;
    		case 4:
    			spell.setTextColor(Color.GREEN);
    			mean.setTextColor(Color.GREEN);
    			currectnum.setTextColor(Color.BLACK);
    			wrongnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;    			
    		case 5:
    			spell.setTextColor(Color.BLUE);
    			mean.setTextColor(Color.BLUE);
    			currectnum.setTextColor(Color.BLACK);
    			wrongnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;
    		case 6:
    			spell.setTextColor(Color.WHITE);
    			mean.setTextColor(Color.WHITE);
    			currectnum.setTextColor(Color.BLACK);
    			wrongnum.setTextColor(Color.BLACK);
    			ElapsedMemoday.setTextColor(Color.BLACK);
    			break;    			    			
    	}
    }

    private void pickCharTypeface(int type)
    {
    	switch(type)
    	{
    		case 1:
    			spell.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
    			mean.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
    			currectnum.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
    			wrongnum.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
    			ElapsedMemoday.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);   
    			break;
    		case 2:
    			spell.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    			mean.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    			currectnum.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    			wrongnum.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    			ElapsedMemoday.setTypeface(Typeface.DEFAULT,Typeface.BOLD);  
    			break;    			
    		case 3:
    			spell.setTypeface(Typeface.DEFAULT_BOLD,Typeface.NORMAL);
    			mean.setTypeface(Typeface.DEFAULT_BOLD,Typeface.NORMAL);
    			currectnum.setTypeface(Typeface.DEFAULT_BOLD,Typeface.NORMAL);
    			wrongnum.setTypeface(Typeface.DEFAULT_BOLD,Typeface.NORMAL);
    			ElapsedMemoday.setTypeface(Typeface.DEFAULT_BOLD,Typeface.NORMAL);  
    			break;
    		case 4:
    			spell.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
    			mean.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
    			currectnum.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
    			wrongnum.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
    			ElapsedMemoday.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);  
    			break;    			
    		case 5:
    			spell.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);
    			mean.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);
    			currectnum.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);
    			wrongnum.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);
    			ElapsedMemoday.setTypeface(Typeface.DEFAULT,Typeface.BOLD_ITALIC);  
    			break;
    		case 6:
    			spell.setTypeface(Typeface.SERIF,Typeface.NORMAL);
    			mean.setTypeface(Typeface.SERIF,Typeface.NORMAL);
    			currectnum.setTypeface(Typeface.SERIF,Typeface.NORMAL);
    			wrongnum.setTypeface(Typeface.SERIF,Typeface.NORMAL);
    			ElapsedMemoday.setTypeface(Typeface.SERIF,Typeface.NORMAL);  
    			break;
    		case 7:
    			spell.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL);
    			mean.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL);
    			currectnum.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL);
    			wrongnum.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL);
    			ElapsedMemoday.setTypeface(Typeface.SANS_SERIF,Typeface.NORMAL);  
    			break;    			    			    			
    	}
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Get savedInstanceState */
        /*
        Bundle b = savedInstanceState;
        if(b != null)
        {
            voca_index = b.getInt("voca_index", -1);
            initial_voca_index = b.getInt("iniital_voca_index", -1);
            mStep = b.getInt("step_index", -1);
            ResizedImage = (Bitmap) b.getParcelable("bitmap");
        } */
        
        /* Get Preferences */
    	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
    	String image_url = prefs.getString(KEY_BG_IMAGE, null);
    	mPreRandomSetting = prefs.getBoolean(KEY_PRE_RANDOM, false);
    	
        getPrefs();	
        
        /* Compose Display */
        setContentView(R.layout.drawer);
        mFrameLayout = (FrameLayout) findViewById(R.id.drawer_layer);
       
        mSwitcher = (ViewSwitcher) findViewById(R.id.ViewSwitcher_DiaryView);
        mSwitcher.setFactory(this);
        
        View view = mSwitcher.getCurrentView();
        yourimgView = (ImageView)view.findViewById(R.id.ImageView_Bg);
    	
    	Bitmap yourSelectedImage = BitmapFactory.decodeFile(image_url);
        
    	if(yourSelectedImage != null)
    	{
    		ComposeImage(yourSelectedImage);
		}

        spell = (TextView) view.findViewById(R.id.spell);
        spell.setSelected(true);

        //spell.setTextColor(Color.BLUE);
        //spell.setTypeface(face);
        //spell.setBackgroundColor(color)
        //spell.setHeight(50);
        mean = (TextView) view.findViewById(R.id.mean);
        currectnum = (TextView)view.findViewById(R.id.currect);
        wrongnum = (TextView)view.findViewById(R.id.wrong);
        ElapsedMemoday = (TextView)view.findViewById(R.id.memodate);
        //mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
        
        spell.setTextSize(mSpell_Size*10);
        
        pickCharColor(mChar_Color);
        pickCharTypeface(mChar_face);
        
        /* Button */
        extra_mean = (TextView)view.findViewById(R.id.extra_mean);
		memoryButton = (Button)view.findViewById(R.id.memory);     
		forgetButton = (Button)view.findViewById(R.id.forget);
		wed_dicButton = (Button)view.findViewById(R.id.wed_dic);
		wed_imageButton = (Button)view.findViewById(R.id.wed_image);
		checkButton = (Button)view.findViewById(R.id.check_mean);
		
		final FrameLayout frameLayer = mFrameLayout;
		mDrawer = (SlidingDrawer) frameLayer.findViewById(R.id.slide_drawer);
		
		web = (WebView)findViewById(R.id.webview); 
		
		drawer = mDrawer;
		halt_countdown = 0;	
		
		
		
		/* Initialize TTS */
        mTts = new TextToSpeech(this,
                this  // TextToSpeech.OnInitListener
                );

	    Intent intent = getIntent();
	    
	    mStep = intent.getIntExtra("step_id", 0);  // Steps_list로 부터 step 단계 읽어들임
	    mFileId = intent.getLongExtra("file_id", 0);
	    
	    /* Get Bundle Data From another activity
	    drawerdata = intent.getBundleExtra("extra");
	    if(drawerdata != null)
 {
			voca_index = drawerdata.getInt("voca_index", 0);
			ResizedImage = (Bitmap) drawerdata.getParcelable("bitmap");
		}
		*/
	    Log.e("Drawer","mFileId = " + mFileId);
	    
	    initial_voca_index = voca_index; // 암기중인 단어의 번호

	    timethread = new ThreadHandle("timemanage");
		
		loadVocaFromDatabase();		
		
		Log.e("Drawer","onCreate");   
		// Use an Alert dialog to confirm delete operation 
		new AlertDialog.Builder(Drawer.this).setMessage(
				"   Start    ").setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						//thr = new Thread(null, mTask, "AlarmService_Service");
						
						//thr.start();
						timethread.stop();
						timethread.start();
						//mChronometer.start();
						
					}
				}).show();

		mean.setVisibility(INVISIBLE); // invisible
		extra_mean.setVisibility(INVISIBLE);
		memoryButton.setVisibility(GONE); 
		forgetButton.setVisibility(GONE);
		wed_dicButton.setVisibility(GONE);
		wed_dicButton.setVisibility(GONE);
		wed_imageButton.setVisibility(GONE);
		
    }
    
    public void ComposeImage(Bitmap yourSelectedImage)
    {
    	Matrix matrix = new Matrix();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		int width = display.getWidth(); 
		int height = display.getHeight();
		yourSelectedImage = Bitmap.createScaledBitmap(yourSelectedImage, width, height, true);
		//Bitmap.createBitmap(yourSelectedImage, 0, 0,
          //      width, height, matrix, true);
		
		yourimgView.setImageBitmap(yourSelectedImage);
		yourimgView.setScaleType(ImageView.ScaleType.CENTER);
		return;
		
    }
	
	@Override
	public View makeView() {
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View v = inflater.inflate(R.layout.drawer_item, null); 
    	v.setLayoutParams(new ViewSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	/* Back ground Color change */
    	//int color = MiniDiaryDraw.bgColor[0];
    	
    	/* Resize Background ImageSize */

    	v.findViewById(R.id.ImageView_Bg);
    	
    	/* Compose of View */
    	//v.findViewById(R.id.ImageView_Bg);
    	v.findViewById(R.id.spell);
    	v.findViewById(R.id.mean);
    	v.findViewById(R.id.extra_mean).setOnClickListener(this);
    	//v.findViewById(R.id.chronometer);
    	v.findViewById(R.id.progress_bar);  
    	v.findViewById(R.id.check_mean).setOnClickListener(this);    	
    	v.findViewById(R.id.memory).setOnClickListener(this);
    	v.findViewById(R.id.forget).setOnClickListener(this);
    	v.findViewById(R.id.wed_dic).setOnClickListener(this);
    	v.findViewById(R.id.wed_image).setOnClickListener(this);


    	return v;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		
        setContentView(R.layout.drawer);
        mFrameLayout = (FrameLayout) findViewById(R.id.drawer_layer);
        
        mSwitcher = (ViewSwitcher) findViewById(R.id.ViewSwitcher_DiaryView);
        mSwitcher.setFactory(this);
        
        View view = mSwitcher.getCurrentView();        
        
        yourimgView = (ImageView)view.findViewById(R.id.ImageView_Bg);
        
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
    	String image_url = prefs.getString(KEY_BG_IMAGE, null);
    	
    	Bitmap yourSelectedImage = BitmapFactory.decodeFile(image_url);
        
    	if(yourSelectedImage != null)
    	{
    		ComposeImage(yourSelectedImage);
		}
    	else{
    		yourimgView.setImageDrawable(null);
    	}
		
        spell = (TextView) view.findViewById(R.id.spell);
        spell.setSelected(true);
        mean = (TextView) view.findViewById(R.id.mean);
        currectnum = (TextView)view.findViewById(R.id.currect);
        wrongnum = (TextView)view.findViewById(R.id.wrong);
        ElapsedMemoday = (TextView)view.findViewById(R.id.memodate);
		currectnum.setText("맞춘 횟수 : " + mCurrectNum);
		wrongnum.setText("틀린 횟수 : " + mWrongNum);
		ElapsedMemoday.setText("경과 날짜 : " + lastMemoDate +" 일" );
        //mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgress.setProgress(mProgressStatus);
        
        spell.setTextSize(mSpell_Size*10);        
        pickCharColor(mChar_Color);
        pickCharTypeface(mChar_face);
        
        
        /* Button */
        extra_mean = (TextView)view.findViewById(R.id.extra_mean);
		memoryButton = (Button)view.findViewById(R.id.memory);     
		forgetButton = (Button)view.findViewById(R.id.forget);
		wed_dicButton = (Button)view.findViewById(R.id.wed_dic);
		wed_imageButton = (Button)view.findViewById(R.id.wed_image);
		checkButton = (Button)view.findViewById(R.id.check_mean);
		
		final FrameLayout frameLayer = mFrameLayout;
		mDrawer = (SlidingDrawer) frameLayer.findViewById(R.id.slide_drawer);
		web = (WebView)frameLayer.findViewById(R.id.webview);
		
		drawer = mDrawer;
		
		spell.setText(spell_cursor);
		mean.setText(mean_cursor);
		extra_mean.clearComposingText();
		extra_mean.setText("");
		extra_mean.setText(extramean_cursor);

		if(memobuttonState == FIRST_STEP)
		{
			Log.e("TEST_LMG","onConfigurationChanged Hiden meaning");
			mean.setVisibility(INVISIBLE); // invisible
			extra_mean.setVisibility(INVISIBLE);
			memoryButton.setVisibility(GONE); 
			forgetButton.setVisibility(GONE);
			wed_dicButton.setVisibility(GONE);
			wed_dicButton.setVisibility(GONE);
			wed_imageButton.setVisibility(GONE);
		}else if(memobuttonState == SECOND_STEP){
			Log.e("TEST_LMG","onConfigurationChanged showing meaning");
			mean.setVisibility(VISIBLE);
			extra_mean.setVisibility(VISIBLE);
			extra_mean.setVisibility(VISIBLE);
			checkButton.setVisibility(GONE);//Gone
			forgetButton.setVisibility(VISIBLE);
			memoryButton.setVisibility(VISIBLE);
			wed_dicButton.setVisibility(VISIBLE);
			wed_imageButton.setVisibility(VISIBLE);	
		}
		
		Log.e("Drawer","onConfigurationChanged");
	}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	long idl = 0;
    	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
    	switch (requestCode) {
    	
    	case BACKIMAGE:
            
            if(resultCode == RESULT_OK){ 

                Uri selectedImage1 = data.getData();
                String[] filePathColumn1 = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage1, filePathColumn1, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                SharedPreferences prefs_preData = getSharedPreferences("PrefName", MODE_PRIVATE);
                String filePath = cursor.getString(columnIndex); // file path of selected image
                
                SharedPreferences.Editor editor = prefs_preData.edit();
                editor.putString(KEY_BG_IMAGE, filePath);
                editor.commit();
                cursor.close();
                      //  Convert file path into bitmap image using below line.
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                if(yourSelectedImage != null)
                {
                    ComposeImage(yourSelectedImage);
                }
            	else{
            		yourimgView.setImageDrawable(null);
            	}
            }
            break;           
            
    		case DIARY_EDIT:
    			Log.e("Drawer","onActivityResult");
    			//idl = mcurrent_cursor.getLong(current_cursor.getColumnIndex(VocaItems._ID));
        		mExtra_mean = data.getStringExtra("extramean");
        		idl = data.getLongExtra("id", -1);
        		Log.e("Drawer","onActivityResult idl = " + idl);
        		//final int memo_step = current_cursor.getInt(current_cursor.getColumnIndex(VocaItems.ITEM_MEMO_STEP));
        		String where = VocaItems._ID + "=" + idl;
        		ContentValues contentValues = new ContentValues();
        		contentValues.put(VocaItems.ITEM_INFO, mExtra_mean);
        		Voca_ManageDB.update(where, contentValues);

        		extra_mean.setText(mExtra_mean);
    			break;
    		case MULTI_CHOICE:
    			Log.e("Drawer","onActivityResult  MULTI_CHOICE");
				mean.setVisibility(GONE);
				extra_mean.setVisibility(GONE);
				
				//mProgressStatus = 0;
				String count = Integer.toString(mTextTime*1000);
				Log.e("Drawer","moveToNextVoca  count = " + count);
				//mProgressbar.cancel(true);
				//mProgressbar.execute(count);
				
    			//thr = new Thread(null, mTask, "AlarmService_Service");
    			//thr.start();
				timethread.stop();
    			timethread.start();
				//mChronometer.start();
    			break;
    	}
    }
    
    private void moveToNextVoca(int memoStat){
    	    	
        final Calendar cal = Calendar.getInstance();
        final int mYear = cal.get(Calendar.YEAR);
        final int mMonth = cal.get(Calendar.MONTH) + 1;
        final int mDay = cal.get(Calendar.DAY_OF_MONTH);
    	final long memo_day = mYear*365 + mMonth * 30 + mDay;
    	
    	
    	int mcheckedalready = 0;
    	int mMultiList = 0;
    	int mCheck_repeat_first_voca = 0;
    	long idl = 0;
        
        //stopService(new Intent(this,CountDownService.class));
		checkButton.setVisibility(VISIBLE);
		forgetButton.setVisibility(GONE);//Gone
		memoryButton.setVisibility(GONE);
		wed_dicButton.setVisibility(GONE);
		wed_imageButton.setVisibility(GONE);
		extra_mean.setVisibility(GONE);
    	
		idl = (long)mvocamanage.getItemId(voca_index);
		mCurrectNum = mvocamanage.getItemCurrectNum(voca_index);
		mWrongNum = mvocamanage.getItemWrongNum(voca_index);
		lastMemoDate = mvocamanage.getItemMemoDate(voca_index);
		//lastMemoDate -= memo_day;
		if(lastMemoDate != 0)
		{
			lastMemoDate = memo_day - (long)lastMemoDate;
		}
		if(lastMemoDate < 0)
		{
		    	lastMemoDate = 0;
		}
		
		//mProgressStatus = 0;
		
    	if(memoStat == YES_MEMO)
    	{    		
    		
    		Log.e("Drawer","moveToNextVoca idl = " + idl + " position(voca_index) = " + voca_index + " mStep = " + mStep);
    		String where = VocaItems._ID + "=" + idl;
    		ContentValues contentValues = new ContentValues();
    		mcheckedalready = mvocamanage.getItemChecked(voca_index);
            if(mcheckedalready != 1)
            {
        		contentValues.put(VocaItems.ITEM_MEMO_STEP, (mStep + 1));
        		contentValues.put(VocaItems.ITEM_MEMO_DATE, memo_day);
        		contentValues.put(VocaItems.ITEM_CORRECT_NUM, (mCurrectNum+1));
        		Voca_ManageDB.update(where, contentValues);
        		mvocamanage.delItem(voca_index);
        		timethread.stop();
        		timethread.start();            	
        		
            }
            else
            {
            	voca_index++;
            	mRepeatVoca++;
            		
            }

    	}
    	else if(memoStat == NO_MEMO)
    	{

    		String where = VocaItems._ID + "=" + idl;
    		ContentValues contentValues = new ContentValues();
    		contentValues.put(VocaItems.ITEM_MEMO_STEP, 0);

    		contentValues.put(VocaItems.ITEM_WRONG_NUM, (mWrongNum+1));
    		Voca_ManageDB.update(where, contentValues);
    		mvocamanage.setItemChecked(voca_index,1);
    		
    		mWrongCount++;
    		voca_index++;

    		if(CheckboxRepeat == true )
			{
				if ((mWrongCount % mRepeat) == 0) 
				{
					// Quiz activity
					int index = 0;

					ArrayList<MultiChoiceData> list = new ArrayList<MultiChoiceData>();

					Intent intent = new Intent(Drawer.this, Multichoice.class);
					int multilist_index = 0;	
					
					//multilist_index = (voca_index - mRepeat);
					mWrongCount = 0;
					//voca_index = multilist_index;
					voca_index -= mRepeat;
		    		if(CheckboxQuiz == true )
		    		{
					while (multilist_index < mRepeat) {

						mean_cursor = mvocamanage.getItemSpell(voca_index + multilist_index);
						spell_cursor = mvocamanage.getItemMean(voca_index + multilist_index);
						list.add(new MultiChoiceData(mean_cursor,spell_cursor));
						mvocamanage.setMultiList(voca_index + multilist_index, 0);
						multilist_index++;

					}
				}
					
	    	    	mCurrectNum = mvocamanage.getItemCurrectNum(voca_index);
	    			mWrongNum = mvocamanage.getItemWrongNum(voca_index);
					spell_cursor = mvocamanage.getItemSpell(voca_index);
					mean_cursor = mvocamanage.getItemMean(voca_index);
					extramean_cursor = mvocamanage.getItemExtraMean(voca_index);
					spell.setText(spell_cursor);
					mean.setText(mean_cursor);
					extra_mean.clearComposingText();
					extra_mean.setText("");
					extra_mean.setText(extramean_cursor);
					mean.setVisibility(GONE);
					extra_mean.setVisibility(GONE);
	    			//thr.interrupt();;
					timethread.stop();
					//mChronometer.stop();
														
					intent.putExtra("number", mRepeat);
					intent.putParcelableArrayListExtra("element", list);
					startActivityForResult(intent, MULTI_CHOICE);
					return;
				}
			}
    		//voca_index++;
    		timethread.stop();
    		timethread.start();
    	}
		
		/* check the last element */
		if((mvocamanage.isEmpty() == true) || (mvocamanage.isLast(voca_index) == true))
		{
			pd = ProgressDialog.show(Drawer.this, "No data",
			"Move to the step list");
			dbthr = new Thread(null, mDBthread, "Database update");
			
			if(dbthr.isAlive()  == false)
				dbthr.start();
			else
			{
				dbthr.stop();
				dbthr.start();
			}
			finish();
			return;
		}	

		spell_cursor = mvocamanage.getItemSpell(voca_index);
		mean_cursor = mvocamanage.getItemMean(voca_index);
    	mCurrectNum = mvocamanage.getItemCurrectNum(voca_index);
		mWrongNum = mvocamanage.getItemWrongNum(voca_index);
		extramean_cursor = null;
		extramean_cursor = mvocamanage.getItemExtraMean(voca_index);
		
		spell.setText(spell_cursor);
		mean.setText(mean_cursor);
		extra_mean.clearComposingText();
		extra_mean.setText("");
		extra_mean.setText(extramean_cursor);
		
		currectnum.setText("맞춘 횟수 : " + mCurrectNum);
		wrongnum.setText("틀린 횟수 : " + mWrongNum);
		ElapsedMemoday.setText("경과 날짜 : " + lastMemoDate +" 일" );
		mean.setVisibility(GONE);
		extra_mean.setVisibility(GONE);
		String count = Integer.toString(mTextTime*100);
		Log.e("Drawer","moveToNextVoca  count = " + count);
		
		return;    	
    }   

	private boolean loadVocaFromDatabase() {
		
    	int mCurrectNum = 0;
    	int mWrongNum = 0;
    	
		Cursor mcurrentCursor = Voca_ManageDB.show_step(mStep);
		
		if(mcurrentCursor == null)
		{
			pd = ProgressDialog.show(Drawer.this, "No data",
			"Move to the step list");
			dbthr = new Thread(null, mDBthread, "Database update");
			
			if(dbthr.isAlive()  == false)
				dbthr.start();
			else
			{
				dbthr.stop();
				dbthr.start();
			}
			return false;
		}
		
		mvocamanage = new VocaDataManage();
		Log.e("Drawer", " loadVocaFromDatabase count = " + mcurrentCursor.getCount());

		String count = Integer.toString(mTextTime*100);
		Log.e("Drawer","moveToNextVoca  count = " + count);

		/*Checking Available Voca Database */
		//mcurrentCursor.moveToFirst();
		//int id = mcurrentCursor.getInt(mcurrentCursor.getColumnIndex(VocaItems._ID));
		//Log.e("Drawer","loadVocaFromDatabase  id = " + id);
		if(mvocamanage.setList(mcurrentCursor) == true)
		{
			if(CheckboxRandom == true)
			{
				mvocamanage.shuffleVocaItem();
			}
			if(voca_index >= 0)
			{
				spell_cursor = mvocamanage.getItemSpell(voca_index);
				mean_cursor = mvocamanage.getItemMean(voca_index);
				extramean_cursor = mvocamanage.getItemExtraMean(voca_index);
				mCurrectNum = mvocamanage.getItemCurrectNum(voca_index);
				mWrongNum = mvocamanage.getItemWrongNum(voca_index);
			}
			else
			{
				Log.e("Drawer","File voca_index value < 0");
				return false;
			}
		}
		else
		{
			Log.e("Drawer",Log.getStackTraceString(new Throwable()));
			return false;
		}
		
		spell.setText(spell_cursor);
		mean.setText(mean_cursor);
		extra_mean.setText(extramean_cursor);
		//
		currectnum.setText("맞춘 횟수 : " + mCurrectNum);
		wrongnum.setText("틀린 횟수 : " + mWrongNum);
		ElapsedMemoday.setText("경과 날짜 : " + lastMemoDate +" 일" );
		
		mcurrentCursor.close();
		//mcurrentCursor.close();
	    return true;
	}    
	
	
	private Cursor moveToNextWed(int showitem){
	     
	     
	     String Word=null;
	     
	     Word = mvocamanage.getItemSpell(voca_index);
	     
	     if(showitem == 3)
	     {
	      String URL_dic=null;
	      
	      String URL_dic_front="http://www.google.co.kr/dictionary?langpair=en%7Cko&q=";
	      String URL_dic_back="&hl=ko&aq=f";
	      
	      drawer.animateOpen();
	      
	      URL_dic = URL_dic_front+Word+URL_dic_back;
	      
	      web.getSettings().setJavaScriptEnabled(true);
	         web.loadUrl(URL_dic);
	         
	        }
	     if(showitem == 4)
	     {
	         
	         String URL_image;
	         String Test_image ="http://www.google.com/m/search?site=images&source=mog&gl=kr&client=ms-android-google&q=";//이미지 주소
	         URL_image = Test_image + Word;
	         
	         drawer.animateOpen();
	      
	      web.loadUrl(URL_image);
	         web.getSettings().setJavaScriptEnabled(true);    	        
	      
	     }
	    return null;  
	         
	  } 
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Drawer","onResume");  
        IntentFilter filter = new IntentFilter(CountDownService.TIME_UPDATE);

		long start = System.currentTimeMillis();  //not necessary

		getPrefs();		
		
        spell.setTextSize(mSpell_Size*10);
        
        pickCharColor(mChar_Color);
        pickCharTypeface(mChar_face);
		
		//loadVocaFromDatabase();

    }

    @Override
    public void onPause() {
      
      super.onPause();

      Log.e("Drawer","onPause");
      mProgressStatus = 0;
      timethread.stop();
  
            
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	mTts.shutdown();
    	Log.e("Drawer","onDestroy"); 

    }

    
    /* Save Current UI status */
    /* onSaveInstanceState and onPause call before the activity is terminated */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        halt_countdown = 1;
        
        outState.putInt("voca_index", voca_index);
        outState.putInt("initial_voca_index", initial_voca_index);
        outState.putInt("step_index", mStep);
        outState.putParcelable("bitmap",ResizedImage);
        
        Log.e("Drawer", "onSaveInstanceState");

        //ToDo: Save UI values
    }
    /* the Activity's UI state in a Bundle is passed to the onCreate and onRestoreInstanceState */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Bundle b = savedInstanceState;
        
        voca_index = b.getInt("voca_index", -1);
        initial_voca_index = b.getInt("iniital_voca_index", -1);
        mStep = b.getInt("step_index", -1);
        ResizedImage = (Bitmap) b.getParcelable("bitmap");
        Log.e("Drawer", "onRestoreInstanceState  voca_index = " + voca_index + " initial_voca_index = " + initial_voca_index
        		+ " mStep = " + mStep);

    }	
    
    public void onBackPressed() {
    	Log.e("Drawer","onBackPressed");
    	
    	if(backToDrawer == false)
    	{
			// mImm.hideSoftInputFromWindow(mEdit.getApplicationWindowToken(),
			// 0);
			for (int step = 0; step < 10; step++) {
				step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
				step_date[step] = Voca_ManageDB.get_stepelapse(step);
			}
			//Intent intent = new Intent(Drawer.this, Steps_list.class);

			//intent.putExtra("NUMBER", step_voca_num);
			//intent.putExtra("DATE", step_date);

			//startActivityForResult(intent,STEPS);
			//startActivity(intent);
			
			/* Sending Bundle data to another activity 
			drawerdata = new Bundle();
			drawerdata.putInt("voca_index", voca_index);
			drawerdata.putInt("initial_voca_index", initial_voca_index);
			drawerdata.putInt("step_index", mStep);
			drawerdata.putParcelable("bitmap",ResizedImage);
	        
			Intent result = new Intent();
			result.putExtra("drawer", drawerdata);
			*/
			setResult(RESULT_OK, null);
			
			finish();
		}
    	else
    	{
    		drawer.animateClose();
    	    backToDrawer = false;
    	}
    }
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("Drawer", "onClick ");
		switch (v.getId()) {
		case R.id.extra_mean:
			Log.e("MiniDiaryViewActivity", Log.getStackTraceString(new Throwable()));
			Intent intent = new Intent(Drawer.this, DiaryEditActivity.class);
			long idl = (long) mvocamanage.getItemId(voca_index);
			String extramean = extra_mean.getText().toString();
			//String extramean = mvocamanage.getItemExtraMean(voca_index);
			intent.putExtra("id", idl);
			intent.putExtra("extramean", extramean);
			startActivityForResult(intent, DIARY_EDIT);
			break;
		case R.id.memory:	
			mean.setVisibility(INVISIBLE); // Invisible
			moveToNextVoca(YES_MEMO);
			break;
		case R.id.forget:		
			mean.setVisibility(INVISIBLE);// Invisible
			moveToNextVoca(NO_MEMO);
			break;
		case R.id.wed_dic:	
			backToDrawer = true;
			moveToNextWed(DICTIONARY);
			break;
		case R.id.wed_image:	
			backToDrawer = true;
			moveToNextWed(IMAGE);
			break;
		case R.id.check_mean:		
			//Log.e("Drawer", Log.getStackTraceString(new Throwable()));
			Log.e("Drawer","click check_mean");
			memobuttonState = SECOND_STEP;
			mean.setVisibility(VISIBLE);
			extra_mean.setVisibility(VISIBLE);
			extra_mean.setVisibility(VISIBLE);
			checkButton.setVisibility(GONE);//Gone
			forgetButton.setVisibility(VISIBLE);
			memoryButton.setVisibility(VISIBLE);
			wed_dicButton.setVisibility(VISIBLE);
			wed_imageButton.setVisibility(VISIBLE);	
			mProgressStatus = 100;
			
			break;
		}

	}

    /* Getting Step Voca Number */
	/* When no data */
	Runnable mDBthread = new Thread(new Runnable()
	{
		public void run() {
			
			for(int step = 0; step < 10; step++)
			{
				step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
				step_date[step] = Voca_ManageDB.get_stepelapse(step);
			}			
	        handler.sendEmptyMessage(MSG_GATHER_VOCA);
		}	
	}
	);
	
    /* Getting Step Voca Number */
	Runnable mDBthread1 = new Thread(new Runnable()
	{
		public void run() {
			
			for(int step = 0; step < 10; step++)
			{
				step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
				step_date[step] = Voca_ManageDB.get_stepelapse(step);
			}			
	        handler.sendEmptyMessage(MSG_GAT_BACK_TO_STEP);
		}	
	}
	);
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{
			case MSG_GATHER_VOCA:
				try{
				    pd.dismiss();
				}catch(Exception e)
				{
					
				}
				
				finish();
				break;
			case MSG_GAT_BACK_TO_STEP:
			
				break;	
			case MSG_TIMEOUT:
				memobuttonState = SECOND_STEP;
				mean.setVisibility(VISIBLE);
				extra_mean.setVisibility(VISIBLE);
				checkButton.setVisibility(GONE);//Gone
				forgetButton.setVisibility(VISIBLE);
				memoryButton.setVisibility(VISIBLE);
				wed_dicButton.setVisibility(VISIBLE);
				wed_imageButton.setVisibility(VISIBLE);
				//mChronometer.stop();
				Log.e("Drawer Test", "spell,toString()" + spell.getText().toString());
				mTts.speak(spell.getText().toString(),TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		                null);
				break;
			}
			

		}
	};

	

	
	Runnable mTask = new Thread(new Runnable() {	
		
		private boolean threadDone = false;
		
	    public void done() {
	        threadDone = true;
	    }
	    
		public void run() {

			int waittime = mTextTime * 10;
			mProgressStatus = 0;
			synchronized (this) {
				// final int mProgressBar = mProgressStatus;
				// mProgressBar = mProgressStatus;
				Log.e("Drawer", "mTextTime = " + mTextTime);
				while (!threadDone) {
					while (mProgressStatus < 100) {
						try {
							mProgressStatus++;
							wait(waittime);
						} catch (InterruptedException e) {
							Log.e("LMG", "Thread interrupt");
							threadDone = true;
						}

						// mProgressStatus++;

						// Update the progress bar
						// UI Thread에 위임
						mHandler.post(new Runnable() {
							public void run() {
								mProgress.setProgress(mProgressStatus);
							}
						});
					}
				}
			}
			
			mProgressStatus = 0;
			handler.sendEmptyMessage(MSG_TIMEOUT);
			Log.e("TEST_LMG","mTask  EndThread   mProgressStatus = " + mProgressStatus);
		}
	});
    
    private void getPrefs() {
    	
    	//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences prefs_preData = getSharedPreferences("PrefName", MODE_PRIVATE);
    	String bgImage_Url = prefs_preData.getString(KEY_BG_IMAGE, null);
    	
    	
    	String temp;
    	CheckboxRepeat = prefs.getBoolean("repeat", true);
    	
    	//mRepeat =prefs.getInt("repeat_count",-1);
    	//mRepeatVoca = mRepeat;
    	
    	try{
    		temp = prefs.getString("repeat_count","5");
    	}catch(Exception e){
    		temp = Integer.toString(prefs.getInt("repeat_count", 5));
    	}
    	
    	/*
    	temp = prefs.getString("repeat_count","7");
    	*/
    	if(isNumber(temp)  == true)
    	{
    		mRepeat = Integer.parseInt(temp);
    		if(mRepeat < 10)
    		{
    			mRepeat++;
    		}
    		mRepeatVoca = mRepeat;
    	}
    	
    	CheckboxQuiz = prefs.getBoolean("quiz", true);
    	
    	temp = prefs.getString("time_count","1");
    	
    	if(isNumber(temp)  == true)
    	{
    		mTextTime = Integer.parseInt(temp);
    		if(mTextTime < 10)
    		{
    			mTextTime++;
    		}
    	}
    	
    	/*
    	try{
    		temp = prefs.getString("repeat_count","7");
    	}catch(Exception e){
    		temp = Integer.toString(prefs.getInt("repeat_count", 7));
    	}
    	*/
    	temp = prefs.getString("spell_size","4");
    	if(isNumber(temp)  == true)
    	{
    		mSpell_Size = Integer.parseInt(temp);
    	}
    	
    	temp = prefs.getString("char_color","1");
    	if(isNumber(temp)  == true)
    	{
    		mChar_Color = Integer.parseInt(temp);
    	}
    	
    	temp = prefs.getString("charface","1");
    	if(isNumber(temp)  == true)
    	{
    		mChar_face = Integer.parseInt(temp);
    	}
    	
    	
    	CheckboxRandom = prefs.getBoolean("random_setting", false);   
    	if(mPreRandomSetting != CheckboxRandom )
    	{
    		mPreRandomSetting = CheckboxRandom;
    		Toast.makeText(Drawer.this, "Random기능은 Step 창으로 나간 이후에 적용됩니다.", Toast.LENGTH_LONG).show();
    		
            SharedPreferences.Editor editor = prefs_preData.edit();
            editor.putBoolean(KEY_PRE_RANDOM, mPreRandomSetting);
            editor.commit();
            finish();
    	}
    	
    }
    
    public boolean isNumber(String number){  
    	if (number == "") { 
    		Toast.makeText(Drawer.this, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
    	      return false; 
    	} 
    	for (int i = 0; i < number.length(); i++) { 
    	if (!Character.isDigit(number.charAt(i))) {
    		
    		     Toast.makeText(Drawer.this, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
    			return false; 
    	    } 
    	} 
    	
    	return true; 
    	} 
    
    private void launchSettingsList() {
		Intent intent = new Intent(Drawer.this,
                                 SettingsListActivity.class);
		startActivity(intent);
    }
    
    private Dialog launchAddDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
    	final EditText mVoca = (EditText) textEntryView.findViewById(R.id.voca_edit);
    	final EditText mMean = (EditText) textEntryView.findViewById(R.id.mean_edit);
    	//final EditText mMean = (LinedEditText) findViewById(R.id.mean_edit); 
        return new AlertDialog.Builder(Drawer.this)
        	.setIcon(R.drawable.alert_dialog_icon)
            .setTitle("Enter New Vocabulary")
            .setView(textEntryView)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                	
                	Log.e("Drawer","mFileId = " + mFileId);
                	ContentValues contentValues = new ContentValues();
                	contentValues.put(VocaItems.ITEM_NAME, mVoca.getText().toString());
                	contentValues.put(VocaItems.ITEM_MEAN, mMean.getText().toString());
                	contentValues.put(VocaItems.ITEM_FILE_ID, mFileId);
                	contentValues.put(VocaItems.ITEM_MEMO_STEP, 0);
                	contentValues.put(VocaItems.ITEM_MEMO_DATE, 0);
                	contentValues.put(VocaItems.ITEM_INFO, "");
                	contentValues.put(VocaItems.ITEM_CORRECT_NUM, 0);
                	contentValues.put(VocaItems.ITEM_WRONG_NUM, 0);
                	contentValues.put(VocaItems.ITEM_RATING, 0);
                	long id = Voca_ManageDB.add( contentValues);
                	mvocamanage.addItem(id,mVoca.getText().toString(),mMean.getText().toString());
                	
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                	Log.e("Drawer","voca_index" + voca_index);
                	Log.e("Drawer","voca_index" + voca_index);
    
                }
            })
            .show();
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	AlertDialog.Builder builder;
		
  		builder = new AlertDialog.Builder(this);
		builder.setTitle("Change Background Image");
		builder.setItems(R.array.background, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item) {
		    	case 0:
	                String filePath = null; // file path of selected image
	                SharedPreferences prefs_preData = getSharedPreferences("PrefName", MODE_PRIVATE);
	                SharedPreferences.Editor editor = prefs_preData.edit();
	                editor.putString(KEY_BG_IMAGE, filePath);
	                editor.commit();
	                      //  Convert file path into bitmap image using below line.
	                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	                if(yourSelectedImage != null)
	                {
	                    ComposeImage(yourSelectedImage);
	                }
	            	else{
	            		yourimgView.setImageDrawable(null);
	            	}
		    		break;
		    	case 1:
		    		  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		    		  intent.setType("image/*");
		    		  startActivityForResult(intent, BACKIMAGE);
		    		break;
		    	}
		    }
		});
		return builder.create();
    		
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_draw, menu);
    	
    	Log.i("Drawer", "onCreateOptionsMenu");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("Drawer", "onOptionsItemSelected");
    	switch (item.getItemId()) {
    	case R.id.MENU_ID_SETTINGS:
    		Log.i("Drawer", "menu_item_weather_settings...");
    		//launchWeatherList();
    		launchSettingsList();
    		return true;
    	case R.id.MENU_ID_BKIMAGE:

    		showDialog(R.id.MENU_ID_BKIMAGE);
    		return true;    		
    	case R.id.MENU_ID_ADDVOCA:
    		launchAddDialog();
    		return true;
    	case R.id.MENU_ID_DELVOCA:
			Log.e("Drawer", "voca_index" + voca_index);
			Log.e("Drawer", "voca_index" + voca_index);
			
			
			long idl = (long)mvocamanage.getItemId(voca_index);
			Voca_ManageDB.delete_voca(idl);
			mvocamanage.delItem(voca_index);
			Log.e("Drawer","onOptionsItemSelected idl = " + idl + " voca_index = " + voca_index);
			timethread.stop();
			timethread.start();
			/* check the last element */
			if ((mvocamanage.isEmpty() == true)
					|| (mvocamanage.isLast(voca_index) == true)) {
				pd = ProgressDialog.show(Drawer.this, "No data",
						"Move to the step list");
				finish();
				return super.onOptionsItemSelected(item);
				}
			}
			spell_cursor = mvocamanage.getItemSpell(voca_index);
			mean_cursor = mvocamanage.getItemMean(voca_index);
			mCurrectNum = mvocamanage.getItemCurrectNum(voca_index);
			mWrongNum = mvocamanage.getItemWrongNum(voca_index);
			extramean_cursor = null;
			extramean_cursor = mvocamanage.getItemExtraMean(voca_index);

			spell.setText(spell_cursor);
			mean.setText(mean_cursor);
			extra_mean.clearComposingText();
			extra_mean.setText("");
			extra_mean.setText(extramean_cursor);

			currectnum.setText("맞춘 횟수 : " + mCurrectNum);
			wrongnum.setText("틀린 횟수 : " + mWrongNum);
			ElapsedMemoday.setText("경과 날짜 : " + lastMemoDate +" 일" );
			mean.setVisibility(GONE);
			extra_mean.setVisibility(GONE);
			String count = Integer.toString(mTextTime * 100);
			Log.e("Drawer", "moveToNextVoca  count = " + count);
			
		return super.onOptionsItemSelected(item);
	}
    
    public class ThreadHandle implements Runnable {
    	
        final private static int STATE_INIT = 0x1;
        final private static int STATE_STARTED = 0x1 << 1;
        final private static int STATE_SUSPENDED = 0x1 << 2;
        final private static int STATE_STOPPED = 0x1 << 3;
        private int stateCode = STATE_INIT;
    	
        private Thread thisThread ;
        private String threadName ;
        public ThreadHandle(){}
        public ThreadHandle(String threadName){
            this.threadName = threadName;
        }
        public void start(){
			synchronized (this) {
				if (stateCode == STATE_STARTED) // INIT 상태가 아니면 이미 실행되었다.
					stop();
					//throw new IllegalStateException("already started");

				thisThread = new Thread(this);
				if (threadName != null)
					thisThread.setName(threadName);
				thisThread.start();
				stateCode = STATE_STARTED;
			}
        }
        
        public void stop() {
			synchronized (this) {
				//if (stateCode == STATE_STOPPED) // 이미 멈췄다면 또 호출할 필요가 없음.
					//throw new IllegalStateException("already stopped");
				if(thisThread != null)
				    thisThread.interrupt();
				stateCode = STATE_STOPPED;
			}
        }
        
        public void suspend() {
            synchronized ( this ){
                if ( stateCode == STATE_SUSPENDED) return;
                if ( stateCode == STATE_INIT )
                    throw new IllegalStateException("not started yet");
                if ( stateCode == STATE_STOPPED)
                    throw new IllegalStateException("already stopped");
                stateCode = STATE_SUSPENDED;
            }    
        }
        
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int waittime = mTextTime * 10;
			mProgressStatus = 0;
			synchronized (this) {
				// final int mProgressBar = mProgressStatus;
				// mProgressBar = mProgressStatus;
				Log.e("Drawer", "mTextTime = " + mTextTime);
				while (mProgressStatus < 100) {
		            if ( stateCode == STATE_STOPPED)
		                break; //종료하라는 신호이므로 루프를 끝낸다.
					try {
						mProgressStatus++;
						wait(waittime);
					} catch (InterruptedException e) {
						//mProgressStatus = 100;
						Log.e("LMG", "Thread interrupt");
					}

					// mProgressStatus++;

					// Update the progress bar
					// UI Thread에 위임
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(mProgressStatus);
						}
					});
				}
			}
			//mProgressStatus = 0;
			if(mProgressStatus >= 100)
			{
			    handler.sendEmptyMessage(MSG_TIMEOUT);
			    Log.e("TEST_LMG", "ThreadHandle EndThread   mProgressStatus = "
					    + mProgressStatus);
			}
		}
    }

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e("Drawer", "Language is not available.");
            } else {
            	// The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
            	 Log.e("Drawer", "TTS engine successfully initialized.");
            }
        } else {
            // Initialization failed.
            Log.e("Drawer", "Could not initialize TextToSpeech.");
        }
	}

}