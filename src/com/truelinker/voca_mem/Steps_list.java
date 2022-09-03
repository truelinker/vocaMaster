package com.truelinker.voca_mem;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

//import com.nemustech.tiffany.R;
//import com.nemustech.tiffany.sample.widget.CustomAnimationListViewTest.StringAndIdAndDirection;
//import com.mg.android.Drawer.CountDownReceiver;
//import com.mg.android.service.CountDownService;
import com.truelinker.R;
import com.truelinker.service.Voca_ManageDB;
import com.truelinker.service.Voca_DB.VocaItems;


public class Steps_list extends Activity {
	
	ListView mList;
	//ListView mList;
    Rect mTempRect = new Rect();
    Handler mHandler = new Handler();
    
    ProgressDialog pd;
    
    //Bundle DrawerData = null;
    
	int step_voca_num[] = new int[10];
	int step_date[] = new int[10];
    
    private View mNoItemLayout;
    
    private static final int DRAWER = 5;
    
    static final float OVERSHOT_INTERPOLATOR_TENSION = 0.8f;
    static final int STATUS_INC_LEFT_TO_RIGHT = 0;
    static final int STATUS_INC_RIGHT_TO_LEFT = 1;
    static final int ANIMATION_DURATION = 400;
    static final float DELAY_PROGRESS = 0.1f;
    static final float TEXT_ADVENT_RATE = 0.3f;
    static final float RATE_TEXT_ADVENT = 0.8f;
    static final int BUBBLE_LENGTH_PER_TEXT = 20;    
    
	static class StringAndValues {
		int mcount;
		String mStr_count;
		int melapse;
		String mStr_elapse;
	
	    StringAndValues(int index, int count, int memo_day)
	    {
	    	final Calendar cal = Calendar.getInstance();
	    	final int current_year = cal.get(Calendar.YEAR);
	    	final int current_month = cal.get(Calendar.MONTH) + 1;
	    	final int current_day = cal.get(Calendar.DAY_OF_MONTH);
	    	final int current_totaldays = (current_year * 365) + (current_month * 30) + current_day;
	    	int elapsedays = 0;
		    mcount = count;
		    mStr_count = "Step "  + index + ": " + count;
		    melapse = memo_day; 
		    elapsedays = current_totaldays - memo_day;
		    	
		    switch(index)
		    {
		        case 0:
		    	    mStr_elapse = "Not even start";
		    	    break;
		        case 1:
		        	if(memo_day > 0)
		    	        mStr_elapse = elapsedays + "/1 day";
		        	else
		        		mStr_elapse = "0/1 day";
		    	    break;
		        case 2:
		        	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + "/3 day";
		        	else
		        		mStr_elapse = "0/3 day";
		        	break;
		        case 3:
		        	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + "/7 day";
		        	else
		        		mStr_elapse = "0/7 day";
		        	break;
		        case 4:
		        	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + "/14 day";
		        	else
		        		mStr_elapse = "0/14 day";
		        	break;
		        case 5:
		        	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + "/21 day";
		        	else
		        		mStr_elapse = "0/21 day";
		        	break;
		        case 6:
		        	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + " /30 day";
		        	else
		        		mStr_elapse = "0/30 day";
		        	break;
		   	    case 7:
		   	    	if(memo_day > 0)
		   	    	    mStr_elapse = elapsedays + " /60 day";
		        	else
		        		mStr_elapse = "0/60 day";
		   	    	break;
		   	    case 8:
		   	    	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + " /120 day";
		        	else
		        		mStr_elapse = "0/120 day";
		        	break;
		        case 9:
		   	    	if(memo_day > 0)
		        	    mStr_elapse = elapsedays + " /240 day";
		        	else
		        		mStr_elapse = "0/240 day";
		        	break;
		    }
	    }
	}
	
	static class StringAndValuesAdapter extends BaseAdapter {
		
		LayoutInflater mInflater;
		
		ArrayList<StringAndValues> mValueArray = new ArrayList<StringAndValues>();
		
		public void addItem(int position, StringAndValues item)
		{
			if(position < 0 || position >= mValueArray.size()) {
                mValueArray.add(item);
            } else {
                mValueArray.add(position, item);
            }

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e("Steps_list","getView");	
			if(mInflater==null) {
				Log.e("Steps_list","getView   mInflater == null");	
				mInflater = (LayoutInflater)parent.getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			}
        
			LinearLayout layout = null;
			if(convertView == null) {
				layout = (LinearLayout) mInflater.inflate(R.layout.step_list_item_image, parent, false);
			} else {
				layout = (LinearLayout)convertView;
			}

			assert(layout instanceof LinearLayout);
			
			TextView tv = (TextView) layout.findViewById(R.id.step);
			TextView tv2 = (TextView) layout.findViewById(R.id.elapse);

        /*
        if(mDataArray.get(position).mDir == STATUS_INC_LEFT_TO_RIGHT) {
            layout.setBackgroundResource(R.drawable.bubbleleft);
        } else {
            layout.setBackgroundResource(R.drawable.bubbleright);
        }*/
			// layout.setBackgroundResource(R.drawable.bubbleleft);

			tv.setWidth((int)(mValueArray.get(position).mStr_count.length()*25));
			//tv.setWidth((int)(mValueArray.get(position).mStr_count.length())*10);
			tv.setText(mValueArray.get(position).mStr_count);

			tv2.setWidth((int)(mValueArray.get(position).mStr_elapse.length()*20));
			//tv2.setWidth((int)(mValueArray.get(position).mStr_elapse.length()*10));
			tv2.setText(mValueArray.get(position).mStr_elapse);

        /*
        ImageView img = (ImageView) layout.findViewById(R.id.list_item_icon);
        img.setImageDrawable(img.getDrawable().mutate());
        */
			
			return layout;
		}
		
        public boolean hasStableIds() {
            return true;
        }

		public int getCount() {
			// TODO Auto-generated method stub
			return mValueArray.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mValueArray.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

	StringAndValuesAdapter mAdapter;
	long fileId = 0;
	
	public void composelist(int number_of_voca[],int elapse_days[] )
	{
		mAdapter = new StringAndValuesAdapter();
        for(int i=0; i<10; ++i) {
            mAdapter.addItem(-1, new StringAndValues(i,number_of_voca[i],elapse_days[i]));
        }
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //mAdapter = new StringAndValuesAdapter();

        Intent intent = getIntent();
        fileId = intent.getLongExtra("file_id", 0);
       // int number_of_voca[] = intent.getIntArrayExtra("NUMBER");
       // int elapse_days[] = intent.getIntArrayExtra("DATE");
        Log.e("Steps_list","fileId = " + fileId);
        
       // composelist(number_of_voca,elapse_days);
        setContentView(R.layout.steplist);
        
        Log.e("Steps_list","onCreate()");

        mList = (ListView) findViewById(R.id.stepslist);
        mList.setAdapter(mAdapter);
             
     
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){

				Intent intent = new Intent(Steps_list.this, Drawer.class);
				
				Log.e("Steps_list","Step id = " + id + " position = " + position);
				
				intent.putExtra("step_id", position);
				intent.putExtra("file_id", fileId);
				//intent.putExtra("extra", DrawerData);
				Cursor mcheckCursor = Voca_ManageDB.show_step(position);
				if(mcheckCursor != null)
				{
				
					mcheckCursor.close();
					startActivityForResult(intent, DRAWER);

				}
				else
				{
					Toast.makeText(Steps_list.this, "No voca", Toast.LENGTH_SHORT).show();	
				}
				
			}
		});
		
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	long idl = 0;

		
    	switch (requestCode) {
    	
    	case DRAWER:

			break;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();

			composelist(step_voca_num, step_date);
			mList.setAdapter(mAdapter);
		}
	};
    
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("Steps_List","onConfigurationChanged");
	}
	
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(0.8f);

    @Override
	protected void onResume() {
		super.onResume();
		pd = ProgressDialog.show(Steps_list.this, null, "Searching voca...");

		// DrawerData = new Bundle();
		// DrawerData = data.getExtras();

		new Thread(new Runnable() {
			public void run() {

				for (int step = 0; step < 10; step++) {
					step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
					step_date[step] = Voca_ManageDB.get_stepelapse(step);
				}

				handler.sendEmptyMessage(0);
			}
		}).start();
//		composelist(step_voca_num, step_date);
//		mList.setAdapter(mAdapter);

	}
    
    @Override
    public void onPause() {
      
      super.onPause();
      Log.e("Steps_list","onPause");
            
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	Log.e("Steps_list","onDestroy"); 
    }

    /* Save Current UI status */
    /* onSaveInstanceState and onPause call before the activity is terminated */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Steps_list", "onSaveInstanceState");

        //ToDo: Save UI values
    }
    /* the Activity's UI state in a Bundle is passed to the onCreate and onRestoreInstanceState */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        Log.e("Steps_list", "onRestoreInstanceState");

    }
}