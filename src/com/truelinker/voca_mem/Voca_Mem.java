package com.truelinker.voca_mem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.truelinker.R;
import com.truelinker.filebrowser.FileBrowserActivity;
import com.truelinker.service.CountDownService;
import com.truelinker.service.Voca_ManageDB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Voca_Mem extends Activity implements View.OnClickListener,Runnable {
    /** Called when the activity is first created. */
	
	DBReceiver receiver;
	private ImageView mImageView;
	Button button;
	Context mContext;
	
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.music_library);		
              
        mContext = this;
        
        startService(new Intent(this,Voca_ManageDB.class));
        
        Cursor mCursor = Voca_ManageDB.show_list();

		final Button escbutton = (Button) findViewById(R.id.esc_button);
		escbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(Intent.ACTION_VIEW); 
				Uri u = Uri.parse("http://m.cafe.daum.net/esc4a?t__nil_cafemy=item"); 
				i.setData(u);  
				startActivity(i);
			}
		});        
        
		final Button startbutton = (Button) findViewById(R.id.start_button);
		startbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Voca_Mem.this, FileList.class);
				
				startActivity(intent);
			}
		});
        if(mCursor == null)
        {
            // DB update
        	Log.e("Voca_Mem","onCreate mCursor == null");
            pd = ProgressDialog.show(Voca_Mem.this, "Working..", "Initialization", true,
    				false);

    		Thread thread = new Thread(this);
    		thread.start();
        	
        }
        else
        {
        	Log.e("Voca_Mem","onCreate mCursor != null");
			//Intent intent = new Intent(Voca_Mem.this, FileList.class);
			
			mCursor.close();
			
			//startActivity(intent);
        }
        //mCursor.close();
    }
    
	public void run() {
		String mFileName;
		int check_db_create = 0;
		do{
			check_db_create = Voca_ManageDB.check_db_create();
		}while(check_db_create == 0);
		
		try {
			//synchronized (this) {
			//	wait(5000);
			//}
			int mcheckfile = Voca_ManageDB.check_file_list("수능");
			if(mcheckfile == 0)
			{
				Log.e("Voca_Mem","handleMessage High school Voca read right away");
			//	InputStream is = getAssets().open("HighschoolVoca.csv");
				InputStream is = getAssets().open("highschool.csv");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader((InputStream) is, "euc-kr"),
						8 * 1024);
				String str;
				// String str1[] = new String[10]; //Spelling
				Log.e("Voca_Mem", "read Highschool Voca");
				while ((str = reader.readLine()) != null) {
					String str1 = " "; // Spelling
					String str2 = " "; // Meaning
					String str3 = " "; // Extra meaning
					StringTokenizer token = new StringTokenizer(str, ",");
					str1 = token.nextToken();
					str2 = token.nextToken();
					// Log.e("Voca_Mem","read Highschool Voca Lines ");
					while (token.hasMoreTokens()) {
						str3 += token.nextToken();
					}

					Voca_ManageDB.insert("수능", str1,
							str2, 0, 0, str3);
				}
				is.close();
				reader.close();
			}
			else if(mcheckfile == -1)
			{
				Log.e("Voca_Mem","mDB not created");
			}
			else{
				Log.e("Voca_Mem","handleMessage High school Voca already read");
			}
			//while ((str = reader.readLine()) != null) {
				
		//	}
		}catch (Exception e) {
			// Should never happen!
			Log.e("Voca_Mem",Log.getStackTraceString(new Throwable()));
				
			finish();
		}
		
		pd.dismiss();

	}
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
		}
	};
	  /** Refresh data from the earthquake feed */
	  @Override 
	  public void onResume() {
	    super.onResume();
	    Log.e("Voca_Mem","onResume");

	    
	  }
	  
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			Log.e("Voca_Mem","onConfigurationChanged");
			setContentView(R.layout.music_library);
			
			final Button escbutton = (Button) findViewById(R.id.esc_button);
			escbutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					Intent i = new Intent(Intent.ACTION_VIEW); 
					Uri u = Uri.parse("http://m.cafe.daum.net/esc4a?t__nil_cafemy=item"); 
					i.setData(u);  
					startActivity(i);
				}
			});        
	        
			final Button startbutton = (Button) findViewById(R.id.start_button);
			startbutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(Voca_Mem.this, FileList.class);
					
					startActivity(intent);
				}
			});
		}
	  
	    /* Save Current UI status */
	    /* onSaveInstanceState and onPause call before the activity is terminated */
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        
	        Log.i("Voca_Mem", "onSaveInstanceState");

	        //ToDo: Save UI values
	    }
	    /* the Activity's UI state in a Bundle is passed to the onCreate and onRestoreInstanceState */
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);
	        
	        Log.i("Voca_Mem", "onRestoreInstanceState");

	    }
	    
	    @Override
	    public void onPause() {
	      super.onPause();
	      Log.e("Voca_Mem","onPause");
	            
	    }
	    
	    @Override
	    public void onDestroy(){
	    	super.onDestroy();
	    	Log.e("Voca_Mem","onDestroy");

	    }
	    
	    public class DBReceiver extends BroadcastReceiver {
	    	
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				intent = new Intent(Voca_Mem.this, Drawer.class);
				//intent = new Intent("com.commonsware.android.label/.Drawer");
				
				startActivity(intent);
			}
	      }

		
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        Intent intent;
	        switch (v.getId()) {
	                  
	        }			
		}
}