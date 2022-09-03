package com.truelinker.voca_mem;

import java.util.ArrayList;
import java.util.Random;

import com.truelinker.R;
import com.truelinker.voca_mem.FileList.FileListAdapter;
import com.truelinker.voca_mem.FileList.FileListItemContainer;
import com.truelinker.voca_mem.Steps_list.StringAndValues;
import com.truelinker.service.Voca_ManageDB;
import com.truelinker.service.Voca_DB.VocaItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Multichoice extends Activity implements View.OnClickListener {

	int right_answer = 0;
	
	public static final int INVISIBLE = 4;
	public static final int VISIBLE = 0;
	public static final int GONE = 8;
	//Cursor mCursor = null;
//	private TextView spell = null;
//	private TextView mean = null;
	private String sample_mean[] = {"����ϴ�","����","�и���","�Ŀ�","��ȿȭ��Ű��","�ߴܽ���","������","���Ǳ���","�쿬��","�ʱ׷���",
			"�ٲٴ�","������","������","����","����","����ϴ�","��ȭ��Ű��","�����","������","�����ϴ�",
			"�ƴ���","���س���","�׸��δ�","������","���ϴ�","����","�ļ�","����","�����ϴ�","�����ϴ�",
			"����","����","����","�ø���","���̴�","������","��ȭ","ȣ��","������","��ٸ�",
			"�䵿ġ��","����","�ð� �帣��","���۽���","������","�ɷ�","���","������","�����","�����ϴ�",
			"��û��","�����ϴ�","�����ϴ�","������","ģ��","���� ����","������","�����ϴ�","����","������",
			"��������","���۽���","�ż���","�и��ϴ�","�ݱ�","������","ä���ϴ�","�����ϴ�","�ɷ�","�����ϴ�",
			"�����","������","������","��������","������Ű��","��������","��������","������","�и��� �ϴ�","��Ȥ��",
			"�κ�����","������","�ָŸ�ȣ��","��û��","��Ѹ���","�����ϴ�","����","����","����������","�����ϴ�",
			"ǳ����","�����ϴ�","������","���","�����ϴ�","�и���","Ź����","�����ϴ�","��������","����"};
	//ListView av;
	
	//MeanListAdapter mAdapter;
	
	TextView mSpell;
	TextView mMean1;
	TextView mMean2;
	TextView mMean3;
	TextView mMean4;
	TextView mRealMean;
	
	ImageView mRightAnswer;
	ImageView mWrongAnswer;
	Button NextButton1;
	Button NextButton2;
	int mWordIndex;
	int mTotalNo;
	
	private DataRecord mReal_Means = null;
	
	private ArrayList<MultiChoiceData> list2 = new ArrayList<MultiChoiceData>();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        //setContentView(R.layout.multichoice);
        
        Log.e("Multichoice","onCreate");
        
	    list2 = getIntent().getParcelableArrayListExtra("element");
	    mTotalNo = getIntent().getIntExtra("number", -1);
	    
	    setContentView(R.layout.multichoice);
	    
	    mWordIndex = 0;
	    	    
	    mRightAnswer = (ImageView)findViewById(R.id.rightanswer);
	    mWrongAnswer = (ImageView)findViewById(R.id.wronganswer);
	    
	    mWrongAnswer.setVisibility(GONE);
	    mRightAnswer.setVisibility(GONE);
	    
	    mSpell = (TextView)findViewById(R.id.spell);
	    
	    mRealMean = (TextView)findViewById(R.id.realmean);
	    
	    NextButton1 = (Button) findViewById(R.id.next1);
	    NextButton1.setOnClickListener(this);
	    
	    NextButton2 = (Button) findViewById(R.id.next2);
	    NextButton2.setOnClickListener(this);

	    NextButton1.setVisibility(GONE);
	    NextButton2.setVisibility(GONE);
	    mMean1 = (TextView)findViewById(R.id.mean1);
	    mMean1.setOnClickListener(this);

	    mMean2 = (TextView)findViewById(R.id.mean2);
	    mMean2.setOnClickListener(this);

	    mMean3 = (TextView)findViewById(R.id.mean3);
	    mMean3.setOnClickListener(this);

	    mMean4 = (TextView)findViewById(R.id.mean4);
	    mMean4.setOnClickListener(this);

	    MoveToNextQuiz(mWordIndex);	    	    
    }
    
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("Multichoice","onConfigurationChanged");
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("Drawer", "onClick ");
		switch (v.getId()) {
			case R.id.next1:
				   MoveToNextQuiz(mWordIndex);
				   break;
				case R.id.next2:
				    MoveToNextQuiz(mWordIndex);
				    break;
				case R.id.mean1:
				     if((right_answer+1) == 1)
					 {
					     mRightAnswer.setVisibility(VISIBLE);
					     mWrongAnswer.setVisibility(GONE);
						 mRealMean.setVisibility(GONE);
						 NextButton1.setVisibility(VISIBLE);
						 NextButton2.setVisibility(GONE);
				     }
					 else
					 {
						 mRightAnswer.setVisibility(GONE);
						 mWrongAnswer.setVisibility(VISIBLE);		
						 mRealMean.setVisibility(VISIBLE);
						 NextButton1.setVisibility(GONE);
						 NextButton2.setVisibility(VISIBLE);
				     }
					 mWordIndex++;
				     break;
				case R.id.mean2:
					if((right_answer+1) == 2)
					{
					    mRightAnswer.setVisibility(VISIBLE);
						mWrongAnswer.setVisibility(GONE);
						mRealMean.setVisibility(GONE);
						NextButton1.setVisibility(VISIBLE);
						NextButton2.setVisibility(GONE);
				    }
					else
					{
						mRightAnswer.setVisibility(GONE);
						mWrongAnswer.setVisibility(VISIBLE);			
						mRealMean.setVisibility(VISIBLE);
						NextButton1.setVisibility(GONE);
						NextButton2.setVisibility(VISIBLE);
					}
					mWordIndex++;
					break;
				case R.id.mean3:
					if((right_answer+1) == 3)
					{
						mRightAnswer.setVisibility(VISIBLE);
						mWrongAnswer.setVisibility(GONE);
						mRealMean.setVisibility(GONE);
						NextButton1.setVisibility(VISIBLE);
						NextButton2.setVisibility(GONE);
					}
					else
					{
						mRightAnswer.setVisibility(GONE);
						mWrongAnswer.setVisibility(VISIBLE);	
						mRealMean.setVisibility(VISIBLE);
						NextButton1.setVisibility(GONE);
						NextButton2.setVisibility(VISIBLE);
					}
					mWordIndex++;
					break;
				case R.id.mean4:
					if((right_answer+1) == 4)
					{
						mRightAnswer.setVisibility(VISIBLE);
						mWrongAnswer.setVisibility(GONE);
						mRealMean.setVisibility(GONE);
						NextButton1.setVisibility(VISIBLE);
						NextButton2.setVisibility(GONE);
					}
					else
					{
						mRightAnswer.setVisibility(GONE);
						mWrongAnswer.setVisibility(VISIBLE);		
						mRealMean.setVisibility(VISIBLE);
						NextButton1.setVisibility(GONE);
						NextButton2.setVisibility(VISIBLE);
					}
					mWordIndex++;
					break;						
		}
	}
    
    
    protected void onResume() {
        super.onResume();
        
    }
    
    @Override
    public void onPause() {
      
      super.onPause();
    }
    
    /* Save Current UI status */
    /* onSaveInstanceState and onPause call before the activity is terminated */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Multichoice", "onSaveInstanceState");

        //ToDo: Save UI values
    }
    /* the Activity's UI state in a Bundle is passed to the onCreate and onRestoreInstanceState */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        Log.e("Multichoice", "onRestoreInstanceState");

    }            

    public void MoveToNextQuiz(int id)
    {
    	 MultiChoiceData data = null;
	    Random oRandom_Mean = new Random();
	    Random oRandom = new Random();
	    right_answer = oRandom.nextInt(4);
	    
	    mWrongAnswer.setVisibility(GONE);
	    mRightAnswer.setVisibility(GONE);
	    mRealMean.setVisibility(GONE);
	    NextButton1.setVisibility(GONE);
	    NextButton2.setVisibility(GONE);
	    //int right_answer = 0;
	
	   // right_answer = oRandom.nextInt(5);
	    if(mTotalNo == -1)
	    {
	    	Log.e("Multichoice","MoveToNextQuiz  mTotalNo wrong value");
	    	mTotalNo = 1;
	    }
	    try{
	    	if(id < mTotalNo)
	    	{
	    	 data = list2.get(id);
	    	}
	    	else
	    	{
	    		finish();
	    		return;
	    	}
	    }catch(Exception e)
	    {
	    	finish();
	    	return;
	    }

	    mSpell.setText(data.mSpell);
	    mRealMean.setText(data.mMean);
	    
	    for(int index=0; index<4; ++index)
	    {
	    	//MultiChoiceData data = list2.get(index);
	    	
	        if(index != right_answer)
	        {
	        	int mean_index = oRandom_Mean.nextInt(100);
	        	if(index == 0)
	        	{
	        		mMean1.setText(sample_mean[mean_index]);
	        	}
	        	else if(index == 1)
	        	{
	        		mMean2.setText(sample_mean[mean_index]);
	        	}
	        	else if(index == 2)
	        	{
	        		mMean3.setText(sample_mean[mean_index]);
	        	}	      
	        	else if(index == 3)
	        	{
	        		mMean4.setText(sample_mean[mean_index]);
	        	}	      	        	
	        }
	        else if(index == right_answer)
	        {
	        	if(index == 0)
	        	{
	        		mMean1.setText(data.mMean);
	        	}
	        	else if(index == 1)
	        	{
	        		mMean2.setText(data.mMean);
	        	}
	        	else if(index == 2)
	        	{
	        		mMean3.setText(data.mMean);
	        	}	      
	        	else if(index == 3)
	        	{
	        		mMean4.setText(data.mMean);
	        	}	      	        	
	        }
	    }
    }
    /*
	static class MeanListValues {
		String mMean;
		String mSpell;
	
		MeanListValues(String mean)
		{
			mMean = mean;
		}
	    MeanListValues(String mean, String spell)
	    {
	        mMean = mean;
	        mSpell = spell;
	    }
	}
	*/
	// this is a custom data adapter, with required columns
	// This adapter maps a Cursor to an array of PetRecords
    /*
	public class MeanListAdapter extends BaseAdapter {

		private DataRecord[] mMeans;
		private Context mContext;
		private LayoutInflater mInflater;
		
		ArrayList<String> mValueArray = new ArrayList<String>();
		
		public void addItem(String mean)
		{			
                mValueArray.add(mean);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(mInflater==null) {
				Log.e("Steps_list","getView   mInflater == null");	
				mInflater = (LayoutInflater)parent.getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			}
			
			LinearLayout layout = null;
			if(convertView == null) {
				layout = (LinearLayout) mInflater.inflate(R.layout.multichoice_items, parent, false);
			} else {
				layout = (LinearLayout)convertView;
			}
									
			assert(layout instanceof LinearLayout);
		  		
			TextView tv = (TextView) layout.findViewById(R.id.mean_item);
			
			//tv.setWidth((int)(mValueArray.get(position).mStr_count.length()*25));
			//tv.setWidth((int)(mValueArray.get(position).mStr_count.length())*10);
			tv.setText(mValueArray.get(position).toString());
			
			return layout;

		}	

		public MeanListAdapter(Context context, Cursor curs) {
			int i[] = {0,0,0,0};
			mContext = context;
			mInflater = LayoutInflater.from(mContext); 

			int iMeans = curs.getCount();
			int count = 0;
			int position = 0;
			long id = 0;
			int loopban_count = 0;
			String strMean = null;
			mMeans = new DataRecord[4];

			Random oRandom = new Random();
			
			// Spin through cursor
			curs.moveToFirst();
			
			
			position = oRandom.nextInt(4);
			while(count < 4)
			{
				i[count] = oRandom.nextInt(iMeans) + 1;
				
				for(int index = 0; index < 4; index++)
				{
					while((i[index] == i[count]) && (index != count))
					{
						i[count] = oRandom.nextInt(iMeans) + 1;
						loopban_count++;
						Log.e("Multichoice", "loopban_count = " + loopban_count);
						if(loopban_count == 10)
						{
							loopban_count = 0;
							break;
						}
					}
				}
				
				Log.e("Multichoice ", "MeanListAdapter i = " + i + " count = " + count);
				if(count == position)
				{
				    mMeans[count] = mReal_Means;
				    count++;
				    
				}
				else if(curs.moveToPosition(i[count]))
			    {
			       strMean = curs.getString(curs.getColumnIndex(VocaItems.ITEM_MEAN));
			       //id = curs.getColumnIndex(VocaItems._ID);
			       id = curs.getLong(curs.getColumnIndex(VocaItems._ID));
			       
			       mMeans[count] = new DataRecord(strMean,id);
			       Log.e("Multichoice ", " MeanListAdapter mMeans[" + count + "] id = " + id);
			       Log.e("Multichoice ", " mReal_Means id = " + mReal_Means.mId);
			       if(mMeans[count].compareTo(mReal_Means) == 0)
			       {
			    	   mMeans[count] = null;
			       }
			       else
			       {
			    	   count++;
			       }			       			       
			    }
			}
		}
		
		public MeanListAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mValueArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mValueArray.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	

	}
	*/
}
