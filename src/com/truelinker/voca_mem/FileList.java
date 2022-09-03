package com.truelinker.voca_mem;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.AdapterView.OnItemLongClickListener;

import com.truelinker.R;
import com.truelinker.service.Voca_ManageDB;
import com.truelinker.service.Voca_DB.FileItems;
import com.truelinker.service.Voca_DB.VocaItems;


public class FileList extends Activity {

	protected Cursor mCursor;
	
	public static long selectFileId = 0;
	
	public Context mContext;
	//int step_voca_num[] = new int[10];
	//int step_date[] = new int[10];
	ProgressDialog pd;
	
	Drawable mMsgBg;
	
	ListView av;
	
	private int mLongClickedItemPosition;
	
	final static int DIALOG_DELETE_DIARY = 1103;
	final static int DIALOG_DELETE_DIARY_CONFIRM = 1104;
	final static int DIALOG_DELETE_ALL_DIARY_CONFIRM = 1105;
	
	int mDeletePosition;
    boolean mDeleteAll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showfile);
		
		mContext = this.getApplicationContext();

        LinearLayout createButton = (LinearLayout) findViewById(R.id.create);
        createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(Uri.EMPTY, "vnd.android.cursor.dir/filebrowser");
                startActivity(intent);
                finish();
			}
		});
		// Fill ListView from database
		fillFileList();

	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("FileList","onConfigurationChanged");
	}
	
	protected void onDestroy()
	{
		super.onDestroy();
		mCursor.close();
		Log.e("FileList","onDestroy");
	}
	
    @Override
    public void onPause() {
      super.onPause();
      Log.e("FileList","onPause");
            
    }
    

	public void fillFileList() {
	
		mCursor = Voca_ManageDB.show_list();
		if(mCursor == null)
		{			
			Toast.makeText(this, "No file", Toast.LENGTH_SHORT).show();
			return;
		}
		startManagingCursor(mCursor);
		
		// Use an adapter to bind the data to a ListView, where each item is
		// shown as a pet_item layout
		FileListAdapter adapter = new FileListAdapter(this, mCursor);	
		av = (ListView) findViewById(R.id.fileList);
		//av.setBackgroundResource(R.drawable.filelist_bg);
		//av.setBackgroundColor(0xFF5DB9FB);

		av.setAdapter(adapter);

		av.setOnItemLongClickListener(mOnItemLongClickListener);
		
		// Listen for clicks on our ListView
		av.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				final long SelectFileId2 = id;
				/* Get screen size */
				/*(Rect rc  = new Rect();
				int right = av.getWidth();
				int height = av.getHeight();
				rc.set(0, 0, right, height); */
			    //float sweepProgress = (float) 0.75;
				//onDrawSweepActionBar(FileList.this,sweepProgress, rc, mcanvas);
				// Use an Alert dialog to confirm delete operation
				selectFileId = SelectFileId2;
				Log.e("FileList","setOnItemClickListener  selectFileId=" + selectFileId);
				Intent intent = new Intent(mContext, Steps_list.class);
				
				Log.e("FileList_list","fileId = " + selectFileId);
				intent.putExtra("file_id", selectFileId);
				
				startActivity(intent);
				/*
				pd = ProgressDialog.show(FileList.this, null, "Searching voca...");
				
				new Thread(new Runnable()
				{
					public void run() {
						
						for(int step = 0; step < 10; step++)
						{
							step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
							step_date[step] = Voca_ManageDB.get_stepelapse(step);
						}			
						
				        handler.sendEmptyMessage(0);
					}	
				}
				).start();
				*/
				/*
				new AlertDialog.Builder(FileList.this).setMessage(
						"Choose this file?").setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								selectFileId = SelectFileId2;
								
								pd = ProgressDialog.show(FileList.this, null, "Searching voca...");
								
								new Thread(new Runnable()
								{
									public void run() {
										
										for(int step = 0; step < 10; step++)
										{
											step_voca_num[step] = Voca_ManageDB.get_stepcount(step);
											step_date[step] = Voca_ManageDB.get_stepelapse(step);
										}			
										
								        handler.sendEmptyMessage(0);
									}	
								}
								).start();
								
							}
						}).show();*/
			}
		});
	}
	
	OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view,int postion, long id)
		{
			mLongClickedItemPosition = (int)id;
			showDialog(DIALOG_DELETE_DIARY);
			return true;
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   AlertDialog.Builder builder;
	    switch (id) {
		case DIALOG_DELETE_DIARY:
			builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.select);
			builder.setItems(R.array.alert_delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case 0:
							showDialog(DIALOG_DELETE_DIARY_CONFIRM);
							break;
					}
			}
		});
		return builder.create();
		case DIALOG_DELETE_DIARY_CONFIRM:
	    		builder = new AlertDialog.Builder(this);
	    		builder.setTitle(R.string.confirmation);
	    		builder.setMessage(R.string.q_delete_diary)
	    		       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    		           public void onClick(DialogInterface dialog, int id) {
	    		        	   mDeletePosition = mLongClickedItemPosition;
	    		        	   mDeleteAll = false;
	    		        	   deleteDiary();
	    		           }
	    		       })
	    		       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    		           public void onClick(DialogInterface dialog, int id) {
	    		                dialog.cancel();
	    		           }
	    		       });
	    		return builder.create();
	    }
		return null;
	}

    private boolean deleteDiary(){
    	Cursor cursor;
		Cursor cp;
		
		//cursor = mCursor;
		//cursor.moveToFirst();
/*
		if (mCursor.move(mDeletePosition) == false) {
			Log.d("FileList", "deleteDiary move position false returned position : "
					+ mDeletePosition);
			return false;
		}
*/
		//mDeletePosition += 1;  
		Voca_ManageDB.delete(mDeletePosition);
		fillFileList();
		return true;
		/*
	cp = getDayCursor(cursor.getString(COLUMN_INDEX_DATE));

		int count = cp.getCount();
		
		if (cp != null) {
			cp.close();
		}

		if(count > 0){
	    	mProgressDeletingDiary = new ProgressDialog(this);
	    	mProgressDeletingDiary.setMax(count);
			mProgressDeletingDiary.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDeletingDiary.setMessage(getResources().getString(R.string.deleting_message));
			mProgressDeletingDiary.setProgress(0);
			mProgressDeletingDiary.setCancelable(false);
			mProgressDeletingDiary.show();

	    	mDeleteHandler.post(mDeleteRunnable);
	    	mIsDeleting = true;
	    	return true;
		}
		else{
			mIsDeleting = false;
			return false;
		}
		*/
    }
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		//	pd.dismiss();
			
			Intent intent = new Intent(mContext, Steps_list.class);
			
			Log.e("FileList_list","fileId = " + selectFileId);
			intent.putExtra("file_id", selectFileId);
			//intent.putExtra("NUMBER", step_voca_num);
			//intent.putExtra("DATE", step_date);
			
			startActivity(intent);
		//	finish();

		}
	};
	// We refresh our ListAdapter as necessary
	public void refillPetList() {
		
		mCursor.requery();
		FileListAdapter adapter = new FileListAdapter(this, mCursor);
		av = (ListView) findViewById(R.id.fileList);
		av.setAdapter(adapter);

	}

	
	// Delete a pet by id
	//public void SelectFile(Long id) {
		//Voca_ManageDB.delete(id);
	//}

	// Container class to keep track of the ListView child views
	static class FileListItemContainer  {
		
	    TextView mFileName; 
	    //TextView mPetType; 
	    //ImageView mPetPic; 
	}

	// this is a custom data adapter, with required columns
	// This adapter maps a Cursor to an array of PetRecords
	public class FileListAdapter extends BaseAdapter {

		private DataRecord[] mFiles;
		private Context mContext;
		private LayoutInflater mInflater;

		public FileListAdapter(Context context, Cursor curs) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext); 

			int iFilePets = curs.getCount();
			mFiles = new DataRecord[iFilePets];

			// Spin through cursor
			curs.moveToFirst();
			for (int i = 0; i < iFilePets; i++) {
				final String strName = curs.getString(curs.getColumnIndex(FileItems.FILE_NAME));
				/*
				final String strType = curs.getString(curs.getColumnIndex(PetType.PET_TYPE_NAME));
				final String strImageUriPath = curs.getString(curs.getColumnIndex(Pets.PET_IMAGE_URI));	
				final long petId = curs.getLong(curs.getColumnIndex(Pets._ID));
				final long petImageId = curs.getLong(curs.getColumnIndex(Pets.PET_IMAGE_ID));
*/
				final long fileId = curs.getLong(curs.getColumnIndex(FileItems._ID));
				mFiles[i] = new DataRecord(strName,fileId);
				curs.moveToNext();
			}
			curs.close();

		}
		
		public int getCount() {
			return mFiles.length;
		}

		public Object getItem(int position) {
			return mFiles[position];
		}

		public long getItemId(int position) {
			return mFiles[position].getId();
		}
	
		// Returns the View corresponding to each ListView child view
		// Each ListView item contains a custom layout: An ImageView and two TextViews
		// The method below inflates a custom layout resource called pet_item and fills it with the 
		// appropriate data. You can also generate this custom layout programmatically instead (commented out below)
		// Tip: We set the Tag property of the View to store the metadata about each Pet
		public View getView(int position, View convertView, ViewGroup parent) {

			FileListItemContainer i;
			
			if(convertView == null)
			{
				// Create a new one
				convertView = (RelativeLayout)mInflater.inflate(R.layout.file_item, null); 
				i = new FileListItemContainer();
				i.mFileName = (TextView) convertView.findViewById(R.id.TextView_FileName);
				i.mFileName.setSelected(true);
				convertView.setTag(i);
				
			} 
			else
			{
				// grab the object from the tag
				i = (FileListItemContainer) convertView.getTag();
			}
						
			i.mFileName.setText(mFiles[position].getName());
	
			// PET PICTURE
			// FYI: More robust implementations would also test to make sure the image still existed on the SD card. 
			// Here we keep it simple. 
			/*
	        if(mPets[position].getPetImageId() != DataRecord.INVALID_PET_IMAGE_ID)
	        {
	        	Uri baseUri = Uri.parse(mPets[position].getPetImageUriPath());
			    Uri imageUri = ContentUris.withAppendedId(baseUri, mPets[position].getPetImageId());
		        i.mPetPic.setImageURI(imageUri); 
	        }  
	        */
		  		
			return convertView;
			
			// FYI - A PROGRAMMATIC WAY instead of inflating from XML
			// (see PetListItemView.java for explanation)
			// PetListItemView i = new PetListItemView(mContext, mPets[position]);
			//	return i;
		}	
	}
	
}
