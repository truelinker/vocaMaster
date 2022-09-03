package com.truelinker.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.truelinker.service.Voca_DB.FileItems;
import com.truelinker.service.Voca_DB.VocaItems;

// FYI: This is the same setup as PetTracker, although we have added two new columns to the Pets table to store the image URI information
class VocaDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "voca_memo.db";
    private static final int DATABASE_VERSION = 1; // if you make changes to the schema, just increment the version and all the tables are dropped for you

        
    VocaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.e("VocaDatabaseHelper","onCreate");
		db.execSQL("CREATE TABLE " + VocaItems.VOCA_ITEM_TABLE_NAME + " ("
				+ VocaItems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ VocaItems.ITEM_NAME + " text not null, " + VocaItems.ITEM_MEAN + " VARCHAR, " 
				+ VocaItems.ITEM_INFO + " VARCHAR, "
				+ VocaItems.ITEM_MEMO_STEP + " INTEGER, " + VocaItems.ITEM_MEMO_DATE + " LONG, " 
				+ VocaItems.ITEM_FILE_ID + " INTEGER, " + VocaItems.ITEM_CORRECT_NUM + " INTEGER, "
				+ VocaItems.ITEM_WRONG_NUM + " INTEGER, " + VocaItems.ITEM_RATING 
				+ " INTEGER" + ");");
		
		db.execSQL("CREATE TABLE " + FileItems.FILE_TABLE_NAME + " ("
				+ FileItems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ FileItems.FILE_NAME  + " text not null"
				+ ");");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Housekeeping here.
		// Implement how "move" your application data during an upgrade of schema versions		
		// ALTER Tables as necessary, or move data, or delete data. Your call.
        Log.i("MediaPetTrackerDatabaseHelper", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", dropping all tables");
        //db.execSQL("DROP TABLE IF EXISTS "+ Pets.PETS_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS "+ PetType.PETTYPE_TABLE_NAME);
        onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
