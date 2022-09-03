package com.truelinker.service;

import java.util.Timer;
import java.util.TimerTask;

import com.truelinker.voca_mem.Drawer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class CountDownService extends Service {

	public static final String TIME_UPDATE = "New_Time_Update";
	private Timer updateTimer;
	
	boolean CheckboxRepeat;
	boolean CheckboxQuiz;
	int mReapeat;
	int mTextTime;
	
	@Override
	public void onCreate() {
		
	}
	
    public boolean isNumber(String number){  
    	if (number == "") { 
    	      return false; 
    	} 
    	for (int i = 0; i < number.length(); i++) { 
    	if (!Character.isDigit(number.charAt(i))) {
    		
    			return false; 
    	    } 
    	} 
    	
    	return true; 
    	} 
    
    private void getPrefs() {
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String temp;
    	CheckboxRepeat = prefs.getBoolean("repeat", true);
    	
    	temp = prefs.getString("repeat_count","7");
    	
    	if(isNumber(temp)  == true)
    	{
    		mReapeat = Integer.parseInt(temp);
    	}
    	CheckboxQuiz = prefs.getBoolean("quiz", true);
    	
    	temp = prefs.getString("time_count","5");
    	
    	if(isNumber(temp)  == true)
    	{
    		mTextTime = Integer.parseInt(temp);
    	}
    	
    }
    
	@Override
	public void onStart(Intent intent, int startId) {
		getPrefs();
		handleStart(intent,startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		handleStart(intent,startId);
		return START_NOT_STICKY;
	}
	
	void handleStart(Intent intent, int startId)
	{
		
		Log.e("CountDownService","onStart");
		updateTimer = new Timer("CountDownService");
		updateTimer.schedule(doRefresh,(mTextTime*1000));	
	}
	
	protected void onStop()
	{
		   Log.e("CountDownService","onStop");
		   updateTimer.cancel();
	}
	
	public void onPause()
	{
	   Log.e("CountDownService","pause");
	   updateTimer.cancel();	
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		Log.e("CountDownService","onDestroy");
		updateTimer.cancel();
		
	}
	
	private TimerTask doRefresh = new TimerTask() {
		  public void run() {
		    refreshTimer();
		  }
		};
		
	private void refreshTimer(){
		Intent intent = new Intent(TIME_UPDATE);
		long start = System.currentTimeMillis();
		sendBroadcast(intent);
		
	}
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}