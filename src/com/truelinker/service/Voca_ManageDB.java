package com.truelinker.service;

import java.util.Timer;

import com.truelinker.voca_mem.FileList;
import com.truelinker.service.Voca_DB.FileItems;
import com.truelinker.service.Voca_DB.VocaItems;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Voca_ManageDB extends Service {

	protected static VocaDatabaseHelper mDatabase = null; 
	protected static SQLiteDatabase mDB = null;
	

	
	@Override
	public void onCreate() {
		super.onCreate();
		mDatabase = new VocaDatabaseHelper(this.getApplicationContext());
		mDB = mDatabase.getWritableDatabase();
		Log.e("Voca_ManageDB","onCreate mDB");		
		
	}
	
	public static int check_db_create(){
		if(mDB != null)
		{
			return 1;
		}
		// otherwise
		return 0;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		//handleStart(intent,startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//handleStart(intent,startId);
		return START_NOT_STICKY;
	}
	
	void handleStart(Intent intent, int startId)
	{
		/*
		mDatabase = new VocaDatabaseHelper(this.getApplicationContext());
		mDB = mDatabase.getWritableDatabase();
		*/
		Log.e("Voca_ManageDB","handleStart");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.e("Voca_ManageDB","onDestroy()");
		if(mDB != null)
		{
			mDB.close();
		}
		
		if(mDatabase != null)
		{
			mDatabase.close();
		}
	}
	public static int get_stepcount(int step)
	{
		long fileId = FileList.selectFileId;
		
		if(fileId < 1)
		{
        	Log.e("Voca_ManageDB","show  fildId < 1");
        	
        	return 0;
		}
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(VocaItems.VOCA_ITEM_TABLE_NAME);

		//Get the database and run the query
		String asColumnsToReturn[] = {
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_STEP,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_DATE,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID };
		
		String where = VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID + "=" + fileId + " AND " 
		        + VocaItems.ITEM_MEMO_STEP + "=" + step;
		
		Cursor mCursor = queryBuilder.query(mDB, asColumnsToReturn, where, null, null, null, VocaItems.DEFAULT_SORT_ORDER);
		//startManagingCursor(mCursor);
		int iNumVoca = mCursor.getCount();
		//mCursor.moveToFirst();
		if(iNumVoca == 0)
		{
			mCursor.close();
			//Log.e("Voca_ManageDB", "get_stepcount  iNumVoca = 0");
		}
		
		mCursor.close();
		
		return iNumVoca;
	}
	
	public static int get_stepelapse(int step)
	{
		long fileId = FileList.selectFileId;
		int sum_date = 0;
		int av_date = 0;
		
		if(fileId < 1)
		{
        	Log.e("Voca_ManageDB","show  fildId < 1");
        	
        	return 0;
		}
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(VocaItems.VOCA_ITEM_TABLE_NAME);

		//Get the database and run the query
		String asColumnsToReturn[] = {
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_STEP,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_DATE,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID };
		
		String where = VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID + "=" + fileId + " AND " 
		        + VocaItems.ITEM_MEMO_STEP + "=" + step;
		
		Cursor mCursor = queryBuilder.query(mDB, asColumnsToReturn, where, null, null, null, VocaItems.DEFAULT_SORT_ORDER);
		//startManagingCursor(mCursor);
		int iNumVoca = mCursor.getCount();
		//mCursor.moveToFirst();
		if(iNumVoca == 0)
		{
			mCursor.close();
			Log.e("Voca_ManageDB", "get_stepelapse  iNumVoca = 0");
			return 0;
		}
		
		if(mCursor.moveToFirst())
		{

			for(int i = 0; i < iNumVoca; i++)
			{
			    sum_date += mCursor.getInt(mCursor.getColumnIndex(VocaItems.ITEM_MEMO_DATE));	
			    mCursor.moveToNext();
			}
			av_date = sum_date/iNumVoca;
		}
		mCursor.close();
		
		return av_date;
	}
	
	public static int check_file_list(String filename)
	{		
		if(mDB == null)
		{
			Log.e("Voca_ManageDB","mDB == null");
			return -1;
		}
		int count = 0;

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(FileItems.FILE_TABLE_NAME);
		queryBuilder.appendWhere(FileItems.FILE_NAME + "='" + filename + "'");
		Cursor c = queryBuilder.query(mDB, null, null, null, null, null, null);
		count = c.getCount();
		c.close();
		return count;
		

	}
	
	public static Cursor show_list()
	{		
		if(mDB == null)
		{
			Log.e("Voca_ManageDB","mDB == null");
			return null;
		}
		Cursor mCursor = mDB.query(FileItems.FILE_TABLE_NAME, new String[] {FileItems._ID, FileItems.FILE_NAME}, null, null, null, null,FileItems.DEFAULT_SORT_ORDER);
		if(mCursor == null)
		{
			return null;
		}
		//startManagingCursor(mCursor);
		int iNumFile = mCursor.getCount();
		//mCursor.moveToFirst();
		if(iNumFile == 0)
		{
			mCursor = null;
			Log.e("Voca_ManageDB", "show  iNumFile = 0");
		}
		return mCursor;
	}

	public static Cursor show_step(int step)
	{
		long fileId = FileList.selectFileId;
		
		if(fileId < 1)
		{
        	Log.e("Voca_ManageDB","show  fildId < 1");
        	
        	return null;
		}
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(VocaItems.VOCA_ITEM_TABLE_NAME);

		//Get the database and run the query
		String asColumnsToReturn[] = {
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems._ID,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_NAME,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEAN,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_INFO,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_STEP,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_DATE,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_CORRECT_NUM,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_WRONG_NUM,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_RATING};

		
		String where = VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID + "=" + fileId + " AND " +VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_STEP + "=" + step;
		
		Log.e("Voca_ManageDB","show  asColumnsToReturn =" + asColumnsToReturn);
		//queryBuilder.query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder)
		Cursor mCursor = queryBuilder.query(mDB, asColumnsToReturn, where, null, null, null, VocaItems.DEFAULT_SORT_ORDER);
		//startManagingCursor(mCursor);
		int iNumVoca = mCursor.getCount();
		//mCursor.moveToFirst();
		if(iNumVoca == 0)
		{
			mCursor = null;
			Log.e("Voca_ManageDB", "show_step  iNumVoca = 0");
		}
		return mCursor;
	}
	
	public static Cursor show()
	{
		long fileId = FileList.selectFileId;
		
		if(fileId < 1)
		{
        	Log.e("Voca_ManageDB","show  fildId < 1");
        	
        	return null;
		}
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(VocaItems.VOCA_ITEM_TABLE_NAME + ", "
				+ FileItems.FILE_TABLE_NAME);
		// INNER JOIN
		queryBuilder.appendWhere(VocaItems.VOCA_ITEM_TABLE_NAME + "."
				+ VocaItems.ITEM_FILE_ID + "=" + FileItems.FILE_TABLE_NAME + "."
				+ FileItems._ID);
		//Get the database and run the query
		String asColumnsToReturn[] = {
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_NAME,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems._ID,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEAN,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_STEP,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_MEMO_DATE,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_FILE_ID,
				VocaItems.VOCA_ITEM_TABLE_NAME + "." + VocaItems.ITEM_INFO,
				FileItems.FILE_TABLE_NAME + "." + FileItems.FILE_NAME,
				FileItems.FILE_TABLE_NAME + "." + FileItems._ID};
		
		String where = FileItems.FILE_TABLE_NAME + "." + FileItems._ID + "=" + fileId;
		
		Log.e("Voca_ManageDB","show  asColumnsToReturn =" + asColumnsToReturn);
		//queryBuilder.query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder)
		Cursor mCursor = queryBuilder.query(mDB, asColumnsToReturn, where, null, null, null, VocaItems.DEFAULT_SORT_ORDER);
		//startManagingCursor(mCursor);
		int iNumVoca = mCursor.getCount();
		//mCursor.moveToFirst();
		if(iNumVoca == 0)
		{
			mCursor = null;
			Log.e("Voca_ManageDB", "show  iNumVoca = 0");
		}
		return mCursor;
	}
	
	public static void update(String where, ContentValues contentvalues)
	{
       int result_row = 0;	
	   result_row = mDB.update(VocaItems.VOCA_ITEM_TABLE_NAME, contentvalues, where, null);	
	   
	   
	}
	
	public static long add(ContentValues contentvalues)
	{
		
		return mDB.insert(VocaItems.VOCA_ITEM_TABLE_NAME, VocaItems.ITEM_NAME, contentvalues);
	}
	
	public static void delete(int id)
	{
		//String astrArgs[] = { id.toString() };
		//mDB.delete(FileItems.FILE_TABLE_NAME, FileItems._ID + "=?", astrArgs);
		mDB.delete(FileItems.FILE_TABLE_NAME, FileItems._ID + "=" + id, null);
		mDB.delete(VocaItems.VOCA_ITEM_TABLE_NAME ,VocaItems.ITEM_FILE_ID + "=" + id, null );
		    
	}
	public static void delete_voca(long id)
	{
		mDB.delete(VocaItems.VOCA_ITEM_TABLE_NAME ,VocaItems._ID + "=" + id, null );
		/* Todo : Consider when every voca deleted in a file */
	}
	public static int checkFile(String FileName)
	{
		mDB.beginTransaction();
		int result = 0;
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(FileItems.FILE_TABLE_NAME);
			queryBuilder.appendWhere(FileItems.FILE_NAME + "='" + FileName + "'");
			Cursor c = queryBuilder.query(mDB, null, null, null, null, null, null);
		    if(c.getCount() != 0)
		    {
		    	result = -1; // Already the File involved.
		    }
		    else
		    {
		    	result = 0;
		    }
		    c.close();
			mDB.setTransactionSuccessful();
		}finally{
			mDB.endTransaction();
			return result;
		}
	}
	public static void insert(String FileName, String spell, String mean,int memo_step,long date,String extra_mean)
	{
		long rowFileItemId = 0;
		//boolean test = true;
		mDB.beginTransaction();
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(FileItems.FILE_TABLE_NAME);
			queryBuilder.appendWhere(FileItems.FILE_NAME + "='" + FileName + "'");
			Cursor c = queryBuilder.query(mDB, null, null, null, null, null, null);
			if(c.getCount() == 0)
			{
				ContentValues DataRecordToAdd = new ContentValues();
				DataRecordToAdd.put(FileItems.FILE_NAME, FileName);
				rowFileItemId = mDB.insert(FileItems.FILE_TABLE_NAME,FileItems.FILE_NAME,DataRecordToAdd);
			    //test = mDB.equals(DataRecordToAdd);
			   // Log.e("Voca_ManageDB","same :" + test);
			} else {
				c.moveToFirst();
				rowFileItemId = c.getLong(c.getColumnIndex(FileItems._ID));

			}
		
			c.close();
		
			ContentValues vocaRecordToAdd = new ContentValues();
			vocaRecordToAdd.put(VocaItems.ITEM_NAME, spell);
			vocaRecordToAdd.put(VocaItems.ITEM_MEAN, mean);
			vocaRecordToAdd.put(VocaItems.ITEM_FILE_ID, rowFileItemId);
			vocaRecordToAdd.put(VocaItems.ITEM_MEMO_STEP, memo_step);
			vocaRecordToAdd.put(VocaItems.ITEM_MEMO_DATE, date);
			vocaRecordToAdd.put(VocaItems.ITEM_INFO, extra_mean);
			vocaRecordToAdd.put(VocaItems.ITEM_CORRECT_NUM, 0);
			vocaRecordToAdd.put(VocaItems.ITEM_WRONG_NUM, 0);
			vocaRecordToAdd.put(VocaItems.ITEM_RATING, 0);
			mDB.insert(VocaItems.VOCA_ITEM_TABLE_NAME, VocaItems.ITEM_NAME, vocaRecordToAdd);
		
			mDB.setTransactionSuccessful();
		}finally{
			mDB.endTransaction();
		}
		//ContentValues vocaRecordToAdd = new ContentValues();
		//vocaRecordToAdd.put(VocaItems., value)
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
