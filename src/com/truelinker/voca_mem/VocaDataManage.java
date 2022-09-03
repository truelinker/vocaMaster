package com.truelinker.voca_mem;

import java.util.ArrayList;
import java.util.Random;

import com.truelinker.service.Voca_DB.VocaItems;

import android.database.Cursor;
import android.util.Log;

public class VocaDataManage {

	public static final int RANDOM = 1;
	public static final int ORDER = 2;
	
	public class VocaItem{
		public long mId;
		public int mWrongNum = 0;
		public int mCurrectNum = 0;
		public int mRating = 0;		
		public int mCheckedAlready = 0;
		public int mMultiList = 0;
		public long mLastDateMemo = 0;
		public String mSpell;
		public String mMean;
		public String mExtraMean;
		
		public VocaItem(long id, int wrongnum, int currectnum, int rating, String spell, String mean, String extramean,int checked,int multilist,long lastMemoDate)
		{
			mId = id;
			mWrongNum = wrongnum;
			mCurrectNum = currectnum;
			mRating = rating;
			mSpell = spell;
			mMean = mean;
			mExtraMean = extramean;
			mCheckedAlready = checked;
			mMultiList = multilist;
			mLastDateMemo = lastMemoDate;

		}

	}
	
	public VocaDataManage(){
		//setList(cursor);
	}
	
	
	ArrayList<VocaItem> mVocaItem = new ArrayList<VocaItem>();

    private ArrayList<VocaItem> ShuffleArrayList()
    {
    	ArrayList<VocaItem> tempVocaItem = new ArrayList<VocaItem>();
    	//copyVocaItem(tempVocaItem);
        //ArrayList sortedList = new ArrayList();
        Random generator = new Random();

        int n = mVocaItem.size();
        while (n > 0)
        {
            int position = generator.nextInt(n);
            n--;
            tempVocaItem.add(mVocaItem.get(position));
            mVocaItem.remove(position);
        }

        return tempVocaItem;
    }    
	
    public void shuffleVocaItem()
    {
    	mVocaItem = ShuffleArrayList();
    }
	public boolean setList(Cursor cursor){
		
		long id = 0;
		int wrong_num = 0;
		int currect_num = 0;
		int rating = 0;
		int checked = 0;
		int multilist = 0;
		long lastMemoDate = 0;
		String spell = null;
		String mean = null;
		String extramean = null;
		
		Log.e("VocaDataManage", " setList count = " + cursor.getCount());
		cursor.moveToFirst();
		
		do{
			if (cursor != null ) {
				
				id = cursor.getInt(cursor
						.getColumnIndex(VocaItems._ID));
				wrong_num = cursor.getInt(cursor
						.getColumnIndex(VocaItems.ITEM_WRONG_NUM));
				currect_num = cursor.getInt(cursor
						.getColumnIndex(VocaItems.ITEM_CORRECT_NUM));
				rating = cursor.getInt(cursor
						.getColumnIndex(VocaItems.ITEM_RATING));
				spell = cursor.getString(cursor
						.getColumnIndex(VocaItems.ITEM_NAME));
				mean = cursor.getString(cursor
						.getColumnIndex(VocaItems.ITEM_MEAN));
				extramean = cursor.getString(cursor
						.getColumnIndex(VocaItems.ITEM_INFO));
				lastMemoDate = cursor.getInt(cursor
						.getColumnIndex(VocaItems.ITEM_MEMO_DATE));
				

				mVocaItem.add(new VocaItem(id, wrong_num, currect_num, rating,spell, mean, extramean,checked,multilist,lastMemoDate));
				//Log.e("VocaDataManage","id = " + id + " spell = " + spell + " mean = " + mean + " extramean = " + extramean);
				if(cursor.isLast() == true)
				{
					Log.e("VocaDataManage","isLast = true");
					return true;
				}
			}
			
			else{
				Log.e("VocaDataManage" , "cursor error");
				return false;
			}

		}while(cursor.moveToNext() == true);
			
		return true;
	}
	
	public boolean isEmpty()
	{
		return mVocaItem.isEmpty();
	}
	public boolean isLast(int position)
	{
		if(position == mVocaItem.size())
		{
			return true;
		}
		else
			return false;
	}
	public long getItemMemoDate(int position)
	{
		long date = mVocaItem.get(position).mLastDateMemo;
		Log.e("VocaDataManage","Memodate = " + date + " position = " + position);
		return date;
	}
	public long getItemId(int position)
	{
		long id = mVocaItem.get(position).mId;
		Log.e("VocaDataManage","id = " + id + " position = " + position);
		return mVocaItem.get(position).mId;
	}
	
	public int getItemWrongNum(int position)
	{
		int wrongnum = mVocaItem.get(position).mWrongNum;
		Log.e("VocaDataManage","wrongnum = " + wrongnum + " position = " + position);
		return mVocaItem.get(position).mWrongNum;
	}
	
	public int getItemCurrectNum(int position)
	{
		int currectnum = mVocaItem.get(position).mCurrectNum;
		Log.e("VocaDataManage","currectnum = " + currectnum + " position = " + position);
		return mVocaItem.get(position).mCurrectNum;
	}
	
	public int getItemChecked(int position)
	{
		int mchecked = mVocaItem.get(position).mCheckedAlready;
		
		return mchecked;
	}
	public void setItemChecked(int position,int value)
	{

		VocaItem setChecked = mVocaItem.get(position);
		setChecked.mCheckedAlready = value;
		mVocaItem.set(position, setChecked);
	}
	public int getMultiList(int position)
	{
		int mchecked = mVocaItem.get(position).mMultiList;
		
		return mchecked;
	}
	public void setMultiList(int position,int value)
	{

		VocaItem setChecked = mVocaItem.get(position);
		setChecked.mMultiList = value;
		mVocaItem.set(position, setChecked);
	}
	public int getItemRating(int position)
	{
		int rating = mVocaItem.get(position).mRating;
		Log.e("VocaDataManage","rating = " + rating + " position = " + position);
		return mVocaItem.get(position).mRating;
	}
	
	public void delItem(int position)
	{
		mVocaItem.remove(position);
	}
	
	public void addItem(long id,String spell, String mean)
	{
		mVocaItem.add(new VocaItem(id,0,0,0,spell,mean,"",0,0,0));
	}
	public String getItemExtraSpell(int position)
	{
		if(mVocaItem.get(position) != null)
		{
		    return mVocaItem.get(position).mExtraMean;
		}
		else
			return null;
	}
	
	public String getItemSpell(int position)
	{
		if(mVocaItem.get(position) != null)
		{
		    return mVocaItem.get(position).mSpell;
		}
		else
			return null;
	}
	
	public String getItemMean(int position)
	{
		if(mVocaItem.get(position) != null)
		{
			return mVocaItem.get(position).mMean;
		}
		else
			return null;
	}
	
	public String getItemExtraMean(int position)
	{
		if(mVocaItem.get(position) != null)
		{
		    return mVocaItem.get(position).mExtraMean;
		}
		else
			return null;
	}
}